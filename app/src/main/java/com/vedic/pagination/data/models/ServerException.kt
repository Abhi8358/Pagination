package com.vedic.pagination.data.models

data class ServerException(val msg: String = "Server Error Exception"): Exception(msg)
