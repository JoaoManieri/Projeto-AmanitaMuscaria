package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.manieri.amanitamuscaria.R
import br.com.manieri.amanitamuscaria.databinding.FragmentHomeBinding
import br.com.manieri.amanitamuscaria.error.ErrorAction
import br.com.manieri.amanitamuscaria.error.ErrorResult
import br.com.manieri.amanitamuscaria.error.ErrorUIController
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class NovaEntradaFragment : Fragment(), KoinComponent {

    private var _binding: FragmentHomeBinding? = null
    private val novaEntradaViewModel: NovaEntradaViewModel by viewModel()
    private val errorHandler: ErrorHandler by inject()
    private lateinit var errorUIController: ErrorUIController
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        novaEntradaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorUIController = ErrorUIController(errorHandler)
        errorUIController.observeErrors(viewLifecycleOwner, binding.errorAnchor) { handleErrorAction(it) }

        binding.buttonSave.setOnClickListener {
            novaEntradaViewModel.saveData(simulateFailure = true)
        }

        binding.buttonForceLogout.setOnClickListener {
            novaEntradaViewModel.simulateUnauthorized()
        }
    }

    private fun handleErrorAction(errorResult: ErrorResult) {
        when (errorResult.action) {
            ErrorAction.RETRY -> novaEntradaViewModel.saveData(simulateFailure = false)
            ErrorAction.LOGOUT, ErrorAction.GO_HOME -> findNavController().navigate(R.id.navigation_home)
            ErrorAction.COME_BACK -> findNavController().popBackStack()
            ErrorAction.NONE -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
