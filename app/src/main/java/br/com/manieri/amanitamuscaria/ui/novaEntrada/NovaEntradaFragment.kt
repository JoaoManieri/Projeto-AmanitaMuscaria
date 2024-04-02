package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.manieri.amanitamuscaria.databinding.FragmentHomeBinding
import br.com.manieri.amanitamuscaria.util.filtros.PlacaVeiculoTextWatcher
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel

class NovaEntradaFragment : Fragment(), KoinComponent {

    private var _binding: FragmentHomeBinding? = null
    private val novaEntradaViewModel : NovaEntradaViewModel by viewModel()
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
        //acesso a view
        binding.tfPlaca.addTextChangedListener(PlacaVeiculoTextWatcher())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}