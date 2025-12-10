package br.com.manieri.amanitamuscaria.error

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ErrorHandler(
    private val errorMapper: ErrorMapper
) {

    private val _errors =
        MutableSharedFlow<ErrorResult>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val errors: SharedFlow<ErrorResult> = _errors.asSharedFlow()

    fun handle(throwable: Throwable, userMessage: String? = null) {
        if (!isTreatable(throwable)) return
        val type = errorMapper.map(throwable)
        val message = userMessage ?: defaultMessage(type)
        val action = defaultAction(type)
        _errors.tryEmit(ErrorResult(type, message, action))
    }

    fun handleType(type: ErrorType, message: String? = null, action: ErrorAction? = null) {
        val resolvedMessage = message ?: defaultMessage(type)
        val resolvedAction = action ?: defaultAction(type)
        _errors.tryEmit(ErrorResult(type, resolvedMessage, resolvedAction))
    }

    private fun isTreatable(throwable: Throwable): Boolean =
        throwable !is CancellationException

    private fun defaultAction(type: ErrorType): ErrorAction = when (type) {
        is ErrorType.UnauthorizedError -> ErrorAction.LOGOUT
        is ErrorType.TimeoutError -> ErrorAction.RETRY
        is ErrorType.ValidationError -> ErrorAction.NONE
        is ErrorType.UnexpectedError -> ErrorAction.GO_HOME
        is ErrorType.NetworkError -> ErrorAction.RETRY
        is ErrorType.DatabaseError -> ErrorAction.RETRY
        is ErrorType.CameraPermissionError -> ErrorAction.COME_BACK
        is ErrorType.FileSaveError -> ErrorAction.RETRY
    }

    private fun defaultMessage(type: ErrorType): String = when (type) {
        is ErrorType.NetworkError -> "Não foi possível conectar. Verifique sua conexão e tente novamente."
        is ErrorType.TimeoutError -> "A requisição demorou demais. Tente novamente em instantes."
        is ErrorType.UnauthorizedError -> "Sua sessão expirou. Faça login novamente."
        is ErrorType.ValidationError -> type.issues.joinToString("\n").ifBlank { "Dados inválidos. Revise as informações." }
        is ErrorType.UnexpectedError -> "Algo inesperado aconteceu. Vamos te levar para casa."
        is ErrorType.DatabaseError -> "Não foi possível acessar os dados. Tentaremos novamente."
        is ErrorType.CameraPermissionError -> "Precisamos da permissão da câmera para continuar."
        is ErrorType.FileSaveError -> "Não conseguimos salvar o arquivo agora. Tente novamente."
    }
}
