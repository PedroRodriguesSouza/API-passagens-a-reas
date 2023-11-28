package com.github.ata.shared.file

import java.lang.IllegalArgumentException

object fileUtils {

    fun loadResource(path: String): String{
        return this.javaClass.getResource(path)?.readText()?: throw IllegalArgumentException("The requested resource $path not found")
    }
}