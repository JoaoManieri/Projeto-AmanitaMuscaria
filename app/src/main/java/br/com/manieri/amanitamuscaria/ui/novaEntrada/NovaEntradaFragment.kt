package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.manieri.amanitamuscaria.databinding.FragmentHomeBinding
import br.com.manieri.amanitamuscaria.util.formatadoresTxt.DataTextWatcher
import br.com.manieri.amanitamuscaria.util.formatadoresTxt.PercentLabelFormatter
import br.com.manieri.amanitamuscaria.util.formatadoresTxt.PlacaVeiculoTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.amanitamuscaria.image.CameraManager
import br.com.manieri.amanitamuscaria.model.Avaria
import br.com.manieri.amanitamuscaria.ui.novaEntrada.adapter.FotoAdapter
import java.util.Date

class NovaEntradaFragment : Fragment(), KoinComponent {


    private lateinit var cameraManager: CameraManager

    private var _binding: FragmentHomeBinding? = null
    private val novaEntradaViewModel: NovaEntradaViewModel by viewModel()
    private val binding get() = _binding!!

    private lateinit var adapter : FotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraManager = CameraManager(this)

        binding.tfPlaca.addTextChangedListener(PlacaVeiculoTextWatcher())
        binding.sliderCombustivel.setLabelFormatter(PercentLabelFormatter())

        val formatoData = SimpleDateFormat("dd/MM/yyyy")
        binding.tfDataEntrada.setText(formatoData.format(Date()))
        binding.tfDataEntrada.addTextChangedListener(DataTextWatcher(binding.tfDataEntrada))

        binding.btnAvariaVeiculo.setOnClickListener {
            cameraManager.dispatchTakePictureIntent()
        }



        val recyclerView: RecyclerView = binding.recyclerViewImagens
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        val photos: ArrayList<Avaria> = novaEntradaViewModel.getImages()
        adapter = FotoAdapter(photos)
        recyclerView.adapter = adapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraManager.onActivityResult(requestCode, resultCode, data, adapter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}