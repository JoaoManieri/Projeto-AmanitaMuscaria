package br.com.manieri.amanitamuscaria.error

sealed class ErrorType(open val cause: Throwable? = null) {
    data class NetworkError(override val cause: Throwable? = null) : ErrorType(cause)
    data class TimeoutError(override val cause: Throwable? = null) : ErrorType(cause)
    data class UnauthorizedError(override val cause: Throwable? = null) : ErrorType(cause)
    data class ValidationError(
        val issues: List<String> = emptyList(),
        override val cause: Throwable? = null
    ) : ErrorType(cause)

    data class UnexpectedError(override val cause: Throwable? = null) : ErrorType(cause)
    data class DatabaseError(override val cause: Throwable? = null) : ErrorType(cause)
    data class CameraPermissionError(override val cause: Throwable? = null) : ErrorType(cause)
    data class FileSaveError(override val cause: Throwable? = null) : ErrorType(cause)
}

data class ErrorResult(
    val type: ErrorType,
    val message: String,
    val action: ErrorAction
)
