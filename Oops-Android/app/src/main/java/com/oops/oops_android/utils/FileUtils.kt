package com.oops.oops_android.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.FileProvider
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/* 파일과 관련함 함수 및 변수를 저장한 파일 */
object FileUtils {
    // drawable 리소스를 uri형태로 변환하는 함수
    fun drawableToFileUri(@DrawableRes drawableImage: Int): Uri? {
        // drawable -> 비트맵
        val bitmap = BitmapFactory.decodeResource(applicationContext().resources, drawableImage)

        // 비트맵 -> 파일
        val filesDir = applicationContext().filesDir
        val imageFile = File(filesDir, "defaultProfile") // 저장될 파일 이름명
        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // 이미지 품질 높게
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            Log.e("Drawable To FileUri", e.stackTraceToString())
            return null
        }

        // 저장된 파일 -> URI
        return FileProvider.getUriForFile(applicationContext(), applicationContext().packageName + ".fileprovider", imageFile)
    }

    // 비트맵을 uri형태로 변환하는 함수
    fun bitmapToFileUri(context: Context, bitmap: Bitmap, imageFileName: String): Uri {
        // 저장할 파일 경로
        val filesDir = applicationContext().filesDir
        val storageDir: File = File(filesDir, "picture")
        if (!storageDir.exists()) {
            storageDir.mkdirs() // 폴더가 없다면 생성하기
        }

        val imageFile = File(storageDir, imageFileName)

        // 기존에 파일이 있었다면 삭제
        val file = File(storageDir, imageFileName)
        val deleted: Boolean = file.delete()
        Log.d("Bitmap To Uri", "중복 파일 삭제: $deleted")

        // 파일 저장
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: FileNotFoundException) {
            Log.e("Bitmap To Uri", e.stackTraceToString())
        } catch (e: Exception) {
            Log.e("Bitmap To Uri", e.stackTraceToString())
        }
        finally {
            try {
                assert(outputStream != null)
                outputStream?.close()
            }
            catch (e: IOException) {
                Log.e("Bitmap To Uri", e.stackTraceToString())
            }
        }
        return FileProvider.getUriForFile(context, "${applicationContext().packageName}.fileprovider", imageFile)
    }

    // drawable 리소스를 file형태로 변환하는 함수
    @SuppressLint("DiscouragedApi")
    fun drawableToFile(): File? {
        val resourceId = applicationContext().resources.getIdentifier("ic_friends_profile_default", "drawable", applicationContext().packageName)
        val inputStream: InputStream = applicationContext().resources.openRawResource(resourceId)
        val outputFile = File.createTempFile("temp", ".png", applicationContext().cacheDir)

        return try {
            inputStream.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            outputFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // 이미지 절대경로 가져오기
    fun getUriPath(context: Context, fileUri: Uri?): String? {
        val contentResolver: ContentResolver = context.contentResolver ?: return null

        // 파일 경로 만들기
        val filePath: String = (context.applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(filePath)
        try {
            // uri로 이미지에 필요한 데이터를 불러오기
            val inputStream = contentResolver.openInputStream(fileUri!!) ?: return null
            // 이미지 데이터를 다시 내보내면서 file 객체에 만들었던 경로 사용
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var length: Int
            while (inputStream.read(buf).also { length = it } > 0)
                outputStream.write(buf, 0, length)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            Log.d("Get Uri Path", ignore.stackTraceToString())
            return null
        }
        return file.absolutePath
    }

    // 외부 저장소에 있는 파일 경로 파악하기
    fun getRealPathFromURI(fileUri: Uri?): String? {
        var realFilePath: String? = null
        try {
            val cursor: Cursor? = applicationContext().contentResolver.query(fileUri!!, null, null, null, null)
            if (cursor == null) {
                realFilePath = fileUri.path
            } else {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                realFilePath = cursor.getString(idx)
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            Log.d("Get Real Path", e.stackTraceToString())
        }
        return realFilePath
    }
}