package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController

import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.ui.theme.AmanitaTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.io.File
import java.time.Instant

class NovaEntradaFragment : Fragment(), KoinComponent {

    private val viewModel: NovaEntradaViewModel by viewModel()
    private val errorHandler: ErrorHandler by inject()
    private lateinit var errorUIController: ErrorUIController
    private var pendingPhotoUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingPhotoUri
            if (success && uri != null) {
                viewModel.addPhoto(uri.toString())
            } else if (uri != null) {
                requireContext().contentResolver.delete(uri, null, null)
            }
            pendingPhotoUri = null
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmanitaTheme {
                    val state by viewModel.state.collectAsState()
                    NovaEntradaScreen(
                        state = state,
                        onFieldChange = viewModel::onFieldChange,
                        onSaveClick = viewModel::save,
                        onAddPhotoClick = { launchCamera() },
                        onRemovePhoto = viewModel::removePhoto
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorUIController = ErrorUIController(errorHandler)
        errorUIController.observeErrors(viewLifecycleOwner, view) { handleErrorAction(it) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        UiEvent.Saved -> findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun launchCamera() {
        val uri = createImageUri() ?: return
        pendingPhotoUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun createImageUri(): Uri? {
        val file = File.createTempFile(
            "vehicle_entry_${Instant.now().toEpochMilli()}",
            ".jpg",
            requireContext().cacheDir
        )
        return FileProvider.getUriForFile(
            requireContext(),
            "br.com.manieri.amanitamuscaria.fileprovider",
            file
        )
    }

    private fun handleErrorAction(errorResult: ErrorResult) {
        when (errorResult.action) {
            ErrorAction.RETRY -> viewModel.save()
            ErrorAction.LOGOUT, ErrorAction.GO_HOME -> findNavController().navigateUp()
            ErrorAction.COME_BACK -> findNavController().popBackStack()
            ErrorAction.NONE -> Unit
        }
    }
}
