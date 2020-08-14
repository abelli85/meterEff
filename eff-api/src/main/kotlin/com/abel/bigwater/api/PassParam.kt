package com.abel.bigwater.api

import java.io.Serializable

data class PassParam(
        var userId: String,
        var oldHash: String,
        var newHash: String) : Serializable {
}