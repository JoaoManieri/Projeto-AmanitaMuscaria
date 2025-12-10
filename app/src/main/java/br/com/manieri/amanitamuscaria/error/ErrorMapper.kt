package br.com.manieri.amanitamuscaria.error

import android.database.sqlite.SQLiteException
import java.io.FileNotFoundException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ErrorMapper {

    fun map(throwable: Throwable): ErrorType = when (throwable) {
        is ConnectException, is UnknownHostException -> ErrorType.NetworkError(throwable)
        is SocketTimeoutException, is TimeoutException -> ErrorType.TimeoutError(throwable)
        is SecurityException -> ErrorType.CameraPermissionError(throwable)
        is SQLiteException -> ErrorType.DatabaseError(throwable)
        is IllegalArgumentException -> ErrorType.ValidationError(cause = throwable)
        is FileNotFoundException -> ErrorType.FileSaveError(throwable)
        is UnauthorizedException -> ErrorType.UnauthorizedError(throwable)
        is IOException -> ErrorType.NetworkError(throwable)
        else -> ErrorType.UnexpectedError(throwable)
    }
}

class UnauthorizedException(message: String? = null) : Exception(message)
