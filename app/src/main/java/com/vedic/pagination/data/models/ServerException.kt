package com.vedic.pagination.data.models

data class ServerException(val msg: String = "Server Error Exception"): Exception(msg)
data class NetworkException(val msg: String = "Server Error Exception"): Exception(msg)
data class OtherException(val msg: String = "Server Error Exception"): Exception(msg)
