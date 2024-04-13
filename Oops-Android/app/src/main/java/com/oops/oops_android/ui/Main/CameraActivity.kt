package com.oops.oops_android.ui.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
//noinspection ExifInterface
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/* 카메라 호출 화면 */
class CameraActivity : AppCompatActivity(), CommonView {
    private lateinit var imageUri: Uri // 사진 uri
    private lateinit var imageFilePath: String // 사진 경로
    private lateinit var imageFileName: String // 사진 이름
    private val TAG = "CameraActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // 화면 켜진 상태 유지
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 카메라 실행
        showCamera()
    }

    // 카메라 실행 함수
    private fun showCamera() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 카메라 액티비티가 있다면
        if (intent.resolveActivity(packageManager) != null) {
            var imageFile: File? = null // 촬영한 사진을 저장할 파일

            try {
                imageFile = createImageFile()
            } catch (e: IOException) {
                Log.e(TAG, e.stackTraceToString()) // 파일 생성 오류
            }

            // 파일 객체가 생성되었다면
            if (imageFile != null) {
                imageUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", imageFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                getResult.launch(intent)
            }
        }
    }

    // 이미지 파일 생성
    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // 임시 파일 생성
        var tempDir: File = cacheDir

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date(System.currentTimeMillis()))
        imageFileName = timeStamp + "_" // 이미지 파일 이름 설정, 중복X
        //val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            tempDir
        )
        imageFilePath = image.absolutePath // 이미지 파일 경로
        return image
    }

    // 액티비티 인텐트 실행
    private var getResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        Log.d("인텐트", it.resultCode.toString())
        when (it.resultCode) {
            RESULT_OK -> {
                val bitmap = BitmapFactory.decodeFile(imageFilePath)
                var exif: ExifInterface? = null

                try {
                    exif = ExifInterface(imageFilePath)
                } catch (e: IOException) {
                    Log.e(TAG, e.stackTraceToString()) // 실행 오류
                }

                var exifDegree = 0f
                if (exif != null) {
                    val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    exifDegree = exifOrientationToDegrees(exifOrientation).toFloat() // 사진의 회전 방향 설정
                }

                // 회전시킨 bitmap 파일을 저장
                saveImage(rotateImage(bitmap, exifDegree))
            }
            RESULT_CANCELED -> {
                finish() // 이전 화면으로 이동
            }
            else -> {
                finish() // 이전 화면으로 이동
            }
        }
    }

    // 카메라로 찍은 사진을 회전시켜주는 함수
    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    // 카메라에 맞게 이미지 회전시켜주는 함수
    private fun rotateImage(bitmap: Bitmap, exifDegree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(exifDegree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // 카메라로 찍은 사진 저장
    private fun saveImage(bitmap: Bitmap) {
        val fileUri = FileUtils.bitmapToFileUri(this, bitmap, imageFileName)
        changeProfile(fileUri)
    }

    // 프로필 사진 변경 API 연결
    private fun changeProfile(fileUri: Uri?) {
        val filePath = FileUtils.getUriPath(applicationContext, fileUri)
        if (filePath != null) {
            try {
                //val path = FileUtils.getRealPathFromURI(fileUri) => null값이 나옴
                val imageFile = File(filePath)
                if (!imageFile.exists()) {       // 원하는 경로에 폴더가 있는지 확인
                    imageFile.createNewFile()    // 하위폴더를 포함한 폴더를 전부 생성
                }

                val requestBody: RequestBody = imageFile.asRequestBody("image/*".toMediaType())
                val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("imageFile", imageFile.name, requestBody)

                val myPageService = MyPageService()
                myPageService.setCommonView(this)
                myPageService.changeProfile(multipartBody)
                Log.d("카메라", filePath.toString())

            } catch(e: Exception) {
                Log.d("CameraActivity", e.stackTraceToString())
            }
        }
    }

    // 프로필 사진 변경 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 성공
                val intent = Intent()
                intent.putExtra("Camera", true)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    // 프로필 사진 변경 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            409 -> {
                // 기존 프로필 사진과 동일한 경우
                Log.d("CameraActivity", message)
                intent.putExtra("Camera", false)
                setResult(RESULT_OK, intent)
                finish()
            }
            500 -> {
                // 프로필 사진 업로드에 실패한 경우
                Toast.makeText(applicationContext, getString(R.string.toast_server_error), Toast.LENGTH_SHORT).show()
                intent.putExtra("Camera", false)
                setResult(RESULT_OK, intent)
                finish()
            }
            else -> {
                Toast.makeText(applicationContext, getString(R.string.toast_server_error), Toast.LENGTH_SHORT).show()
                intent.putExtra("Camera", false)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}