package com.example.weathertracker.core.result

/**
 * 데이터 계층 결과 래퍼. 성공/실패를 명시적으로 표현해
 * 호출부에서 예외를 직접 try/catch 하지 않도록 한다.
 */
sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val throwable: Throwable, val message: String? = throwable.message) :
        Resource<Nothing>
}

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> this
}
