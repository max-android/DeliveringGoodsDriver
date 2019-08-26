package com.my_project.deliveringgoods.data.file_storage

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.*


class ExternalStorage(private val context: Context) {

    companion object {
        private const val FORMAT_JPG = ".jpg"
        private const val FORMAT_TXT = ".txt"
        private const val NAME_DIRECTORY = "/MY_FILE_MANAGER"
    }

    private fun isExternalStorageWritable(): Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun isExternalStorageReadable(): Boolean = Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)

    fun getPublicAlbumStorageDir(albumName: String): File? {
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Timber.d("Directory not created")
        }
        return file
    }

    fun getPrivateAlbumStorageDir(albumName: String): File? {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Timber.d("Directory not created")
        }
        return file
    }

    fun writeTextFile(filename: String, fileContent: String) {
        if (!isExternalStorageWritable()) {
            return
        }
        val file: File? = File(getPath(filename, FORMAT_TXT))
        //Если нет директорий в пути, то они будут созданы:
        if (!file?.parentFile?.exists()!!) {
            file.parentFile.mkdirs()
            Timber.d("нет директорий в пути, то они будут созданы%s", file.absolutePath)
        }
        try {
            val bufferedWriter = BufferedWriter(FileWriter(file))
            bufferedWriter.use {
                it.write(fileContent)
            }
            Timber.d("Файл записан на SD: %s", file.absolutePath)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun readTextFile(filename: String): String? {
        if (!isExternalStorageReadable()) {
            return null
        }
        var receiveString = ""
        val file = File(getPath(filename, FORMAT_TXT))
        try {
            val bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines {
                it.forEach { str -> stringBuilder.append(str) }
            }
            receiveString = stringBuilder.toString()
        } catch (e: Exception) {
            Timber.e(e)
        }
        return receiveString
    }

    fun writeImage(filename: String, bitmap: Bitmap) {
        if (!isExternalStorageWritable()) {
            return
        }
        val file: File? = File(getPath(filename, FORMAT_JPG))

        file?.parentFile?.let {
            if (!it.exists()) {
                val mkdirs = it.mkdirs()
                Timber.d("нет директорий в пути, то они будут созданы%s", file.absolutePath)
            }
        }
        file?.let {
            try {
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                Timber.d("Файл записан на SD: %s", file.absolutePath)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun readImage(filename: String): Bitmap? {
        if (!isExternalStorageReadable()) {
            return null
        }
        var bitmap: Bitmap? = null
        val file = File(getPath(filename, FORMAT_JPG))
        try {
            val fileInputStream = FileInputStream(file)
            fileInputStream.use {
                val bitmapFactory = BitmapFactory.Options()
                bitmap = BitmapFactory.decodeStream(it, null, bitmapFactory)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return bitmap
    }

    fun readImageFile(filename: String) = File(getPath(filename, FORMAT_JPG))

    fun writeImageToPicturesDirectory(filename: String, bitmap: Bitmap) {
        if (isExternalStorageWritable()) {
            val location = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                filename + FORMAT_JPG,
                "$filename$FORMAT_JPG"
            )
            if (location != null) Timber.d("запись произошла") else
                Timber.d("запись  не произошла")
        } else {
            Timber.d("директория недоступна")
        }
    }

    fun readImageFromPicturesDirectory(filename: String): Bitmap? {
        var bitmap: Bitmap? = null
        if (isExternalStorageReadable()) {
            val cursor: Cursor = MediaStore.Images.Media.query(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATE_TAKEN),
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1"
            )
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    val id = cursor.getInt(0)
                    bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                        context.contentResolver,
                        id.toLong(), MediaStore.Images.Thumbnails.MINI_KIND, null
                    )
                }
            }
        }
        return bitmap
    }

    fun writeInPicture(filename: String, bitmap: Bitmap) {
        if (!isExternalStorageWritable()) {
            return
        }
        val file: File? = File(getPicturePath(filename, FORMAT_JPG))

        file?.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
                Timber.d("нет директорий в пути, то они будут созданы%s", file.absolutePath)
            }
        }

        file?.let {
            try {
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                Timber.d("Файл записан в Pictures: %s", file.absolutePath)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun readFromPicture(filename: String): Bitmap? {
        if (!isExternalStorageReadable()) {
            return null
        }
        var bitmap: Bitmap? = null
        val file = File(getPicturePath(filename, FORMAT_JPG))
        try {
            val fileInputStream = FileInputStream(file)
            fileInputStream.use {
                val bitmapFactory = BitmapFactory.Options()
                bitmap = BitmapFactory.decodeStream(it, null, bitmapFactory)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return bitmap
    }


    private fun createPictureDirectory(): File {
        val absolutePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        val dir = File(absolutePath + NAME_DIRECTORY)
        dir.mkdir()
        return dir
    }

    private fun createDirectory(): File {
        val absolutePath = Environment.getExternalStorageDirectory().absolutePath
        val dir = File(absolutePath + NAME_DIRECTORY)
        dir.mkdir()
        return dir
    }

    private fun getPicturePath(filename: String, format: String): String {
        if (!createPictureDirectory().exists()) {
            createPictureDirectory().mkdir()
        }
        val rootPath = createPictureDirectory().absolutePath
        return "$rootPath/$filename$format"
    }

    private fun getPath(filename: String, format: String): String {
        if (!createDirectory().exists()) {
            createDirectory().mkdir()
        }
        val rootPath = createDirectory().absolutePath
        return "$rootPath/$filename$format"
    }

    fun bitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
        val drawable = checkNotNull(ContextCompat.getDrawable(context, drawableId))
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}