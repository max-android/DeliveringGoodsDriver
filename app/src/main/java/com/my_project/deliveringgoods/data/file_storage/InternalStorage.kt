package com.my_project.deliveringgoods.data.file_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import timber.log.Timber
import java.io.*


class InternalStorage(private val context: Context) {

    companion object {
        const val IMAGE_PROFILE = "image_profile"
        const val EMPTY_PROFILE = "empty_profile"
        private const val FORMAT_TXT = ".txt"
    }

    fun createFile(filename: String): File {
        return File(context.filesDir, filename)
    }

    fun writeFile(filename: String, fileContents: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    fun readFile(filename: String): String? {
        try {
            context.openFileInput(filename)?.let { fileInput ->
                val bufferedReader = BufferedReader(InputStreamReader(fileInput))
                val stringBuilder = StringBuilder()
                bufferedReader.useLines {
                    it.forEach { str -> stringBuilder.append(str) }
                }
                fileInput.close()
                return stringBuilder.toString()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }

    fun deleteFile(filename: String) {
        context.deleteFile(filename)
    }

    fun checkExistFile(): Boolean = context.fileList().isNotEmpty()

    fun writeBitmap(filename: String, bitmap: Bitmap) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            it.write(bos.toByteArray())
        }
    }

    fun readBitmap(filename: String): Bitmap? {
        try {
            context.openFileInput(filename)?.let { fileInput: FileInputStream ->
                val bitmapFactory = BitmapFactory.Options()
                return BitmapFactory.decodeStream(fileInput, null, bitmapFactory)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }

    fun writeCache(filename: String, content: String) {
        val file = File.createTempFile(filename, FORMAT_TXT, context.cacheDir)
        file.writeText(content)
    }

    fun readCache(): String? {
        var receiveString = ""
        try {
            val file = context.cacheDir
            val iterator = file.listFiles().iterator()
            while (iterator.hasNext()) {
            }
            val inputStreamReader = file.listFiles()[0].reader()
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            bufferedReader.useLines {
                it.forEach { str -> stringBuilder.append(str) }
            }
            receiveString = stringBuilder.toString()
            inputStreamReader.close()
        } catch (e: Exception) {
            Timber.e(e)
        }
        return receiveString
    }

    private fun getTempFile(url: String): File? =
        Uri.parse(url)?.lastPathSegment?.let { filename ->
            File.createTempFile(filename, null, context.cacheDir)
        }

}