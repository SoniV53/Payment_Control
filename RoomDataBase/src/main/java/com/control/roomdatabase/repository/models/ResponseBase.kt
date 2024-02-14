package com.control.roomdatabase.repository.models

data class ResponseBase(
    var status:String,
    var code:String,
    var message:String = ""
)
