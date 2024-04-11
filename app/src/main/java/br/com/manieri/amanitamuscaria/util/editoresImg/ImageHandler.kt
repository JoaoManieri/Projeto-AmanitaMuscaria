package br.com.manieri.amanitamuscaria.util.editoresImg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import br.com.manieri.amanitamuscaria.model.Foto

/**
 * Classe utilitária para manipulação de imagens.
 *
 * Essa classe oferece funcionalidades para decodificar imagens a partir de um caminho de arquivo,
 * rotacionar imagens e liberar recursos associados a um objeto Bitmap.
 */
class ImageHandler(private val context: Context) {



    private fun proporcionaBitmap(filePath: String, proporcao : Int) : Bitmap{
        val bitmap = BitmapFactory.decodeFile(filePath)
        val width = bitmap.width / proporcao
        val height = bitmap.height / proporcao
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }


    /**
     * Decodifica um arquivo de imagem para um objeto Bitmap.
     *
     * O método utiliza a função `BitmapFactory.decodeFile` para decodificar o arquivo de imagem
     * especificado pelo caminho `filePath`. A imagem decodificada é então redimensionada para metade
     * do seu tamanho original usando o método `Bitmap.createScaledBitmap`.
     *
     * @param filePath Caminho do arquivo de imagem a ser decodificado.
     * @return Um objeto Bitmap representando a imagem decodificada.
     */
    fun decodeBitmap(foto: Foto): Bitmap {
        val bitmap = proporcionaBitmap(foto.getFile().absolutePath, 1)
        return rotateBitmap(bitmap,foto.rotacao)
    }

    /**
     * Rotaciona um objeto Bitmap de acordo com o ângulo de rotação especificado.
     *
     * O método utiliza uma instância de `Matrix` para definir a rotação desejada e então
     * cria um novo objeto Bitmap rotacionado usando o método `Bitmap.createBitmap`.
     *
     * @param bitmap O Bitmap a ser rotacionado.
     * @param rotation Ângulo de rotação em graus.
     * @return Um novo objeto Bitmap rotacionado.
     */
    fun rotateBitmap(bitmap: Bitmap, rotation: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotation)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Libera os recursos associados a um objeto Bitmap.
     *
     * O método chama o método `recycle` do objeto Bitmap para liberar a memória utilizada
     * pela imagem. É importante chamar este método quando um Bitmap não é mais necessário
     * para evitar vazamentos de memória.
     *
     * @param bitmap O Bitmap a ser liberado.
     */
    fun cleanupBitmap(bitmap: Bitmap) {
        bitmap.recycle()
    }
}
