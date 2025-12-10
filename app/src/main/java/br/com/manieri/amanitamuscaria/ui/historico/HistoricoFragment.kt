package br.com.manieri.amanitamuscaria.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.ui.theme.AmanitaTheme
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class HistoricoFragment : Fragment(), KoinComponent {

    private val viewModel: HistoricoViewModel by viewModel()
    private val errorHandler: ErrorHandler by inject()
    private lateinit var errorUIController: ErrorUIController

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
                    HistoricoScreen(
                        state = state,
                        onSearchChange = viewModel::onSearch,
                        onDelete = viewModel::onDelete,
                        onItemClick = viewModel::onItemClick
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
                        is HistoricoEvent.NavigateToDetail -> {
                            findNavController().navigate(
                                br.com.manieri.amanitamuscaria.R.id.navigation_vehicle_detail,
                                Bundle().apply { putString("entryId", event.id) }
                            )
                        }
                        is HistoricoEvent.ShowMessage -> {
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun handleErrorAction(errorResult: ErrorResult) {
        when (errorResult.action) {
            ErrorAction.RETRY -> viewModel.refresh()
            ErrorAction.COME_BACK -> findNavController().popBackStack()
            ErrorAction.GO_HOME, ErrorAction.LOGOUT -> findNavController().navigateUp()
            ErrorAction.NONE -> Unit
        }
    }
}
