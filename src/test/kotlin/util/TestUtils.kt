package util

import java.lang.IllegalArgumentException

object TestUtils {
    fun getFileContent(path: String): String{
        return this.javaClass.getResource(path)?.readText()?: throw IllegalArgumentException("The requested file $path not found")
    }
}