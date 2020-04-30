package com.luciofm

import java.io.File

class FileReader(private val filename: String) {
    fun readFile(): List<String> = File(filename).bufferedReader().readLines()
}