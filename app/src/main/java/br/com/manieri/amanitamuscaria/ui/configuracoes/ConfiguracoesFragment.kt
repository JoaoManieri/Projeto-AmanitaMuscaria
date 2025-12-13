package br.com.manieri.amanitamuscaria.ui.configuracoes

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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.ui.theme.AmanitaTheme
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException

class ConfiguracoesFragment : Fragment() {

    private val viewModel: ConfiguracoesViewModel by viewModel()
    private val errorHandler: ErrorHandler by inject()
    private lateinit var errorUIController: ErrorUIController

    private val pickLogoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { copyLogoToAppStorage(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AmanitaTheme {
                val state by viewModel.state.collectAsState()
                ConfiguracoesScreen(
                    state = state,
                    events = viewModel.events,
                    onFieldChange = viewModel::onFieldChange,
                    onLogoClick = ::pickLogo,
                    onCropChange = viewModel::onCropChanged,
                    onSaveClick = viewModel::save
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorUIController = ErrorUIController(errorHandler).also {
            it.observeErrors(viewLifecycleOwner, view, ::handleErrorAction)
        }
    }

    private fun pickLogo() {
        pickLogoLauncher.launch("image/*")
    }

    private fun copyLogoToAppStorage(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            val destination = withContext(Dispatchers.IO) {
                try {
                    val directory = File(requireContext().filesDir, "logos").apply { mkdirs() }
                    val file = File(directory, "logo_${System.currentTimeMillis()}.jpg")
                    requireContext().contentResolver.openInputStream(uri)?.use { input ->
                        file.outputStream().use { output -> input.copyTo(output) }
                    } ?: return@withContext null
                    file.toUri()
                } catch (e: IOException) {
                    errorHandler.handle(e, "Não foi possível salvar a imagem.")
                    null
                }
            }

            if (destination == null) {
                Snackbar
                    .make(requireView(), "Não foi possível carregar a imagem.", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                viewModel.onLogoSelected(destination.toString())
            }
        }
    }

    private fun handleErrorAction(errorResult: ErrorResult) {
        when (errorResult.action) {
            ErrorAction.RETRY -> viewModel.retryLast()
            ErrorAction.LOGOUT, ErrorAction.GO_HOME -> requireActivity().onBackPressedDispatcher.onBackPressed()
            ErrorAction.COME_BACK -> requireActivity().onBackPressedDispatcher.onBackPressed()
            ErrorAction.NONE -> Unit
        }
    }
}
