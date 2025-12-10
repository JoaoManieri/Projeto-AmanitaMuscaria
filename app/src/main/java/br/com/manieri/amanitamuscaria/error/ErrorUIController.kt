package br.com.manieri.amanitamuscaria.error

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ErrorUIController(private val errorHandler: ErrorHandler) : DefaultLifecycleObserver {

    private var currentDialog: AlertDialog? = null
    private var currentSnackbar: Snackbar? = null

    fun observeErrors(
        lifecycleOwner: LifecycleOwner,
        anchorView: View,
        onAction: (ErrorResult) -> Unit = {}
    ) {
        lifecycleOwner.lifecycle.addObserver(this)
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                errorHandler.errors.collect { result ->
                    show(anchorView, result, onAction)
                }
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        dismissCurrent()
    }

    private fun show(anchorView: View, result: ErrorResult, onAction: (ErrorResult) -> Unit) {
        dismissCurrent()
        val actionLabel = labelFor(result.action)
        if (actionLabel != null && result.action != ErrorAction.NONE) {
            currentSnackbar = Snackbar.make(anchorView, result.message, Snackbar.LENGTH_LONG).apply {
                setAction(actionLabel) { onAction(result) }
                addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        currentSnackbar = null
                    }
                })
            }
            currentSnackbar?.show()
        } else {
            currentDialog = MaterialAlertDialogBuilder(anchorView.context)
                .setTitle(titleFor(result.type))
                .setMessage(result.message)
                .setPositiveButton(actionLabel ?: "OK") { _, _ ->
                    if (result.action != ErrorAction.NONE) {
                        onAction(result)
                    }
                }
                .setNegativeButton("Fechar", null)
                .create()
            currentDialog?.setOnDismissListener { currentDialog = null }
            currentDialog?.show()
        }
    }

    private fun labelFor(action: ErrorAction): String? = when (action) {
        ErrorAction.RETRY -> "Tentar novamente"
        ErrorAction.LOGOUT -> "Sair"
        ErrorAction.COME_BACK -> "Voltar"
        ErrorAction.GO_HOME -> "Ir para início"
        ErrorAction.NONE -> null
    }

    private fun titleFor(type: ErrorType): String = when (type) {
        is ErrorType.ValidationError -> "Validação"
        is ErrorType.UnauthorizedError -> "Sessão expirada"
        is ErrorType.NetworkError -> "Conexão"
        is ErrorType.TimeoutError -> "Tempo esgotado"
        is ErrorType.CameraPermissionError -> "Permissão"
        is ErrorType.FileSaveError -> "Arquivo"
        is ErrorType.DatabaseError -> "Dados"
        is ErrorType.UnexpectedError -> "Ops!"
    }

    private fun dismissCurrent() {
        currentDialog?.dismiss()
        currentDialog = null
        currentSnackbar?.dismiss()
        currentSnackbar = null
    }
}
