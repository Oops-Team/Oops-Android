package com.oops.oops_android.ui.Main.MyPage

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.databinding.FragmentProfileBottomSheetBinding
import com.oops.oops_android.ui.Main.CameraActivity
import com.oops.oops_android.ui.Main.MainActivity
import com.oops.oops_android.utils.FileUtils.drawableToFile
import com.oops.oops_android.utils.FileUtils.getRealPathFromURI
import com.oops.oops_android.utils.FileUtils.getUriPath
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/* 프로필 사진 변경 바텀 시트 */
class ProfileBottomSheetFragment: BottomSheetDialogFragment(), CommonView {
    private var mBinding: FragmentProfileBottomSheetBinding? = null
    private val binding get() = mBinding!!
    private var mainActivity: MainActivity? = null // 메인 액티비티 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 네비게이션 바 나타내기
        mainActivity?.hideBnv(false)

        mBinding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivity = context as MainActivity
        } catch (e: java.lang.Exception) {
            Log.d("ProfileBottomSheetFrm - onAttach", e.stackTraceToString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    // 카메라 화면에서 넘어왔다면, 바텀 시트 닫기
    private val cameraResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val isSuccess = intent?.getBooleanExtra("Camera", false)
            Log.d("카메라 사진 촬영 성공 여부", isSuccess.toString())
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 네비게이션 바 숨기기
        mainActivity?.hideBnv(true)

        // 네비게이션 값 읽기
        try {
            /*Log.d("확인", requireArguments().getBoolean("isDefault").toString())
            // 프로필 사진이 있다면 프로필 삭제 메뉴 띄우기
            if (!requireArguments().getBoolean("isDefault")) {
                binding.tvMypageProfileDelete.visibility = View.VISIBLE
                binding.viewMypageProfileDelete.visibility = View.VISIBLE
            }*/
            val args: ProfileBottomSheetFragmentArgs by navArgs()
            if (args.profileDivision) {
                binding.tvMypageProfileDelete.visibility = View.VISIBLE
                binding.viewMypageProfileDelete.visibility = View.VISIBLE
            }
        }
        catch (e: Exception) {
            Log.d("ProfileBottomSheetFrm - Get Navigation Data", e.stackTraceToString())
        }

        // 갤러리에서 설정 버튼을 누른 경우
        binding.tvMypageProfileGallery.setOnClickListener {
            // 권한 체크
            checkGalleryPermission()
        }

        // 프로필 사진 촬영 버튼을 누른 경우
        binding.tvMypageProfileCamera.setOnClickListener {
            // 권한 체크
            checkCameraPermission()
        }

        // 프로필 사진 삭제 버튼을 누른 경우
        binding.tvMypageProfileDelete.setOnClickListener {
            // 프로필 사진 변경 API 연결
            //val fileUri = drawableToFileUri(R.drawable.ic_friends_profile_default)
            changeProfile(null, true)
        }
    }

    // 프로필 사진 변경 API 연결
    private fun changeProfile(fileUri: Uri?, isDelete: Boolean) {
        // 프로필 사진을 삭제하는 경우
        if (isDelete) {
            try {
                //val filePath = getUriPath(requireContext(), fileUri)
                //val imageFile = File(getRealPathFromURI(fileUri)!!)
                val imageFile = drawableToFile() // drawable값을 file값으로 변경

                val requestBody: RequestBody = imageFile!!.asRequestBody("image/*".toMediaType())
                val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("imageFile", "defaultProfile.png", requestBody)

                val myPageService = MyPageService()
                myPageService.setCommonView(this)
                myPageService.changeProfile(multipartBody)

            } catch(e: Exception) {
                Log.d("ProfileBottomSheetFragment", e.stackTraceToString())
            }
        }
        // 프로필 사진을 변경하는 경우
        else {
            val filePath = getUriPath(requireContext(), fileUri)
            if (filePath != null) {
                try {
                    val path = getRealPathFromURI(fileUri)
                    val imageFile = File(path!!)
                    if (!imageFile.exists()) {       // 원하는 경로에 폴더가 있는지 확인
                        imageFile.createNewFile()    // 하위폴더를 포함한 폴더를 전부 생성
                    }

                    val requestBody: RequestBody = imageFile.asRequestBody("image/*".toMediaType())
                    val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("imageFile", imageFile.name, requestBody)

                    val myPageService = MyPageService()
                    myPageService.setCommonView(this)
                    myPageService.changeProfile(multipartBody)

                } catch(e: Exception) {
                    Log.d("ProfileBottomSheetFragment", e.stackTraceToString())
                }
            }
        }
    }

    // 카메라 권한 목록
    private val permissionArray = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(
            android.Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    // 카메라 접근 권한이 있는지 확인하는 함수
    private fun checkCameraPermission() {
        // 카메라 접근 권한이 있다면
        if (permissionArray.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED })
        {
            // 카메라 화면으로 이동
            Log.d("카메라 권한 허용 여부", "1-1")
            val intent = Intent(requireContext(), CameraActivity::class.java)
            cameraResultActivity.launch(intent)
        }
        // 권한이 없다면
        else {
            Log.d("카메라 권한 허용 여부", "1-2")
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    // 갤러리 접근 권한 있는지 확인하는 함수
    private fun checkGalleryPermission() {
        // 갤러리 접근 권한이 있다면
        if (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED) {
            Log.d("갤러리 권한 허용 여부", "1-1")
            goGallery() // 갤러리로 이동
        }
        // 권한이 없다면
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("갤러리 권한 허용 여부", "1-2-1")
                requestGalleryPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
            else {
                Log.d("갤러리 권한 허용 여부", "1-2-2")
                requestGalleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // 갤러리 권한 여부 묻기
    private val requestGalleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        // 권한 허용
        if (isGranted) {
            goGallery() // 갤러리로 이동
        }
        // 권한 거부
        else {
            showMediaPermissionPopup() // 권한 설명 팝업 띄우기
            /*// 명시적으로 권한을 거부한 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                        1000
                    )
                }
            }
            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1000
                    )
                }
            }
            else {
                showMediaPermissionPopup() // 권한 설명 팝업 띄우기
            }*/
        }
    }

    // 카메라 권한 여부 묻기
    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        // 권한 허용
        if (isGranted) {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            cameraResultActivity.launch(intent)
        }
        // 권한 거부
        else {
            showCameraPermissionPopup() // 권한 설명 팝업 띄우기
            /*// 명시적으로 권한을 거부한 경우
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    1000
                )
            }
            else {
                showCameraPermissionPopup() // 권한 설명 팝업 띄우기
            }*/
        }
    }

    // 갤러리 접근 권한 확인 팝업 띄우기
    private fun showMediaPermissionPopup() {
        AlertDialog.Builder(context)
            .setTitle("권한을 허용해주세요")
            .setMessage("갤러리 접근 권한을 허용해주세요")
            .setPositiveButton("동의") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireContext().packageName}"))
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    // 카메라 접근 권한 확인 팝업 띄우기
    private fun showCameraPermissionPopup() {
        AlertDialog.Builder(context)
            .setTitle("권한을 허용해주세요")
            .setMessage("카메라 접근 권한을 허용해주세요")
            .setPositiveButton("예") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireContext().packageName}"))
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    // 갤러리로 이동한 후의 로직 처리 함수
    private fun goGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startForResult.launch(intent) // 이미지 선택 후, 정보 가져오기
    }

    // 갤러리에서 이미지를 선택한 후 정보 가져오기
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                // 이미지 가져와서 적용하기
                Activity.RESULT_OK -> {
                    // 서버에 사진 전달
                    val fileUri = result.data?.data
                    if  (fileUri != null) {
                        changeProfile(fileUri, false)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // 사진 선택 취소
                }
                else -> Toast.makeText(requireContext(), "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }

    // 프로필 사진 변경 API 연결 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 성공
                val actionToMyPage = ProfileBottomSheetFragmentDirections.actionProfileBottomSheetFrmToMyPageFrm()
                findNavController().navigate(actionToMyPage)
            }
        }
    }

    // 프로필 사진 변경 API 연결 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            409 -> {
                // 기존 프로필 사진과 동일한 경우
                Log.d("ProfileBottomSheetFragment", message)
                dismiss()
            }
            500 -> {
                // 프로필 사진 업로드에 실패한 경우
                Toast.makeText(requireContext(), getString(R.string.toast_server_error), Toast.LENGTH_SHORT).show()
                dismiss()
            }
            else -> {
                Toast.makeText(requireContext(), getString(R.string.toast_server_error), Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}