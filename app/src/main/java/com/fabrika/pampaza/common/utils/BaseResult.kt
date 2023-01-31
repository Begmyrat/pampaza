package com.fabrika.pampaza.common.utils

sealed class BaseResult <out T : Any> {
    data class Success <T: Any>(val data : T) : BaseResult<T>()
    data class Error<T: Any>(val code: DomainError) : BaseResult<T>()
}

enum class DomainError{
    NEEDS_AUTH,
    FORCE_AUTH,
    COMMON_ERROR
}