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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.ui.theme.AmanitaTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.Instant

class NovaEntradaFragment : Fragment() {

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
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmanitaTheme {
                    val state by viewModel.state.collectAsState()
                    NovaEntradaScreen(
                        state = state,
                        onFieldChange = viewModel::onFieldChange,
                        onSaveClick = viewModel::save,
                        onAddPhotoClick = ::launchCamera,
                        onRemovePhoto = viewModel::removePhoto
                    )
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorUIController = ErrorUIController(errorHandler).also {
            it.observeErrors(viewLifecycleOwner, view, ::handleErrorAction)
        }
        collectEvents()
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect(::handleUiEvent)
            }
        }
    }

    private fun launchCamera() {
        pendingPhotoUri = createImageUri() ?: run {
            Snackbar
                .make(requireView(), "Não foi possível abrir a câmera.", Snackbar.LENGTH_LONG)
                .show()
            return
        }
        takePictureLauncher.launch(pendingPhotoUri)
    }

    private fun createImageUri(): Uri? =
        runCatching {
            val directory = File(requireContext().cacheDir, "entries").apply { mkdirs() }
            val file = File.createTempFile(
                "vehicle_entry_${Instant.now().toEpochMilli()}",
                ".jpg",
                directory
            )
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
        }.getOrNull()

    private fun handleUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Saved -> Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
        }
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
