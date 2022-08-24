package fr.jhelp.kotlinToSwift.protocol

data class Generic(val index: Int,
                   val originalName: String,
                   var replaceName: String = originalName,
                   val extended : String,
                   var fromProtocol: String? = null,
                   var indexInProtocol: Int = -1)
