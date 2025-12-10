package br.com.manieri.amanitamuscaria.ui.detalhesEntrada

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.ui.theme.AmanitaTheme
import androidx.lifecycle.Lifecycle
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.io.File
import java.time.Instant
import java.util.UUID

class DetalhesEntradaFragment : Fragment(), KoinComponent {

    private val viewModel: DetalhesEntradaViewModel by viewModel()
    private val errorHandler: ErrorHandler by inject()
    private lateinit var errorUIController: ErrorUIController
    private var pendingPhotoUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingPhotoUri
            if (success && uri != null) {
                viewModel.onEvent(DetalhesEntradaEvent.AddPhoto(uri.toString()))
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
        val entryId = arguments?.getString("entryId").orEmpty()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AmanitaTheme {
                    val state by viewModel.state.collectAsState()
                    DetalhesEntradaScreen(
                        state = state,
                        uiMessages = viewModel.messages,
                        onEvent = viewModel::onEvent,
                        onAddPhotoClick = { launchCamera() }
                    )
                }
            }
        }.also {
            if (entryId.isNotBlank()) {
                runCatching { UUID.fromString(entryId) }
                    .onSuccess { viewModel.onEvent(DetalhesEntradaEvent.Load(it)) }
                    .onFailure { errorHandler.handleType(br.com.manieri.amanitamuscaria.error.ErrorType.ValidationError(listOf("ID inválido"))) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorUIController = ErrorUIController(errorHandler)
        errorUIController.observeErrors(viewLifecycleOwner, view) { handleErrorAction(it) }

        addMenu()
    }

    private fun launchCamera() {
        val uri = createImageUri() ?: return
        pendingPhotoUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun createImageUri(): Uri? {
        return runCatching {
            val imagesDir = File(requireContext().filesDir, "images").apply { mkdirs() }
            val file = File.createTempFile(
                "vehicle_detail_${Instant.now().toEpochMilli()}",
                ".jpg",
                imagesDir
            )
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
        }.getOrElse {
            errorHandler.handleType(br.com.manieri.amanitamuscaria.error.ErrorType.FileSaveError(it), "Não foi possível criar o arquivo para a foto.")
            null
        }
    }

    private fun shareDraft(state: DetalhesEntradaUiState) {
        // Recebe o estado atual para futura implementação de compartilhamento.
        val entry = state.entry
        val photos = state.photos
        if (entry == null) return
        // TODO: Implementar compartilhamento usando entry e photos
    }

    private fun addMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val shareItem = menu.add(0, MENU_SHARE_ID, 0, "Compartilhar")
                shareItem.setIcon(android.R.drawable.ic_menu_share)
                shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    MENU_SHARE_ID -> {
                        shareDraft(viewModel.state.value)
                        true
                    }
                    android.R.id.home -> {
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private companion object {
        const val MENU_SHARE_ID = 1001
    }

    private fun handleErrorAction(errorResult: ErrorResult) {
        when (errorResult.action) {
            ErrorAction.RETRY -> viewModel.state.value.entry?.id?.let { id ->
                runCatching { UUID.fromString(id) }.getOrNull()?.let { viewModel.onEvent(DetalhesEntradaEvent.Load(it)) }
            }
            ErrorAction.LOGOUT, ErrorAction.GO_HOME -> findNavController().navigateUp()
            ErrorAction.COME_BACK -> findNavController().popBackStack()
            ErrorAction.NONE -> Unit
        }
    }
}
