package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.manieri.amanitamuscaria.databinding.FragmentHomeBinding
import br.com.manieri.amanitamuscaria.util.filtros.DataTextWatcher
import br.com.manieri.amanitamuscaria.util.filtros.PercentLabelFormatter
import br.com.manieri.amanitamuscaria.util.filtros.PlacaVeiculoTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Calendar

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.Date

class NovaEntradaFragment : Fragment(), KoinComponent {

    private val REQUEST_TAKE_PHOTO = 1
    private var currentPhotoPath: String = ""

    private var _binding: FragmentHomeBinding? = null
    private val novaEntradaViewModel: NovaEntradaViewModel by viewModel()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        novaEntradaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //acesso a view
        binding.tfPlaca.addTextChangedListener(PlacaVeiculoTextWatcher())
        binding.sliderCombustivel.setLabelFormatter(PercentLabelFormatter())


        val calendar = Calendar.getInstance()
        val formatoData = SimpleDateFormat("dd/MM/yyyy")
        val dataAtual = formatoData.format(calendar.time)
        binding.tfDataEntrada.setText(dataAtual)


        binding.tfDataEntrada.addTextChangedListener(DataTextWatcher(binding.tfDataEntrada))

        binding.btnAvariaVeiculo.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }



    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "seu.package.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            Log.d("CameraFragment", "Caminho da foto: $currentPhotoPath")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // A foto foi tirada e salva no caminho especificado em currentPhotoPath
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}