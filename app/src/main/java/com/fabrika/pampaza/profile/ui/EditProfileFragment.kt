package com.fabrika.pampaza.profile.ui

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.FragmentEditProfileBinding
import com.fabrika.pampaza.profile.viewmodel.EditProfileViewModel
import com.fabrika.pampaza.profile.viewmodel.ProfileViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditProfileFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentEditProfileBinding
    lateinit var viewmodel: EditProfileViewModel
    var isAvatarClicked = false

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val permissonResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->

            var canDialogBeOpened = true
            permissions.entries.forEach {
                if (it.value.not()) {
                    canDialogBeOpened = false
                    return@forEach
                }
            }
            if(canDialogBeOpened){
                startCameraWithoutUri()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        addListeners()
        addObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    private fun addObservers() {
        viewmodel.avatarImage.observe(this){
            Glide.with(requireContext()).load(it).into(binding.iAvatar)
        }

        viewmodel.backgroundImage.observe(this){
            Glide.with(requireContext()).load(it).into(binding.iBackground)
        }

        viewmodel.isError.observe(this){
            (requireActivity() as? ProfileActivity)?.showSnackbar(binding.bSave, if(it) "Failure" else "Success", !it)
        }
    }

    private fun addListeners() {
        binding.bSave.setOnClickListener(this)
        binding.iCamera.setOnClickListener(this)
        binding.iCameraAvatar.setOnClickListener(this)
        binding.iBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.b_save -> {
                // save
                viewmodel.saveData(binding.eUsername.text.toString(), binding.eBio.text.toString(), binding.eLocation.text.toString())
            }
            R.id.i_camera -> {
                // open camera for background
                isAvatarClicked = false
                permissonResultLauncher.launch(permissions)
            }
            R.id.i_cameraAvatar -> {
                // open camera for avatar
                isAvatarClicked = true
                permissonResultLauncher.launch(permissions)
            }
            R.id.i_back -> {
                findNavController().popBackStack()
            }
            R.id.e_birthday -> {

            }
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        when {
            result.isSuccessful -> {
                handleCropImageResult(result.uriContent.toString())
            }
        }
    }

    private fun handleCropImageResult(uri: String) {

        val file = File(context?.cacheDir, "${System.currentTimeMillis()}.png")

        if (file.parentFile?.exists()?.not() == true)
            file.parentFile?.mkdirs()

        requireContext().contentResolver.openInputStream(Uri.parse(uri))?.let { copyStreamToFile(it, file) }
        if(isAvatarClicked){
            val compressedFile = viewmodel.saveBitmapToFile(requireContext(), file, true)
            compressedFile?.let {
                viewmodel.addAvatarImage(compressedFile)
            }
        } else {
            val compressedFile = viewmodel.saveBitmapToFile(requireContext(), file, false)
            compressedFile?.let {
                viewmodel.addBackgroundImage(compressedFile)
            }
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    private fun startCameraWithoutUri() {
        cropImage.launch(
            CropImageContractOptions(
                uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = true,
                    imageSourceIncludeGallery = true,
                    aspectRatioY = 1,
                    aspectRatioX = if(isAvatarClicked) 1 else 3,
                    fixAspectRatio = true,
                    toolbarColor = ContextCompat.getColor(requireContext(), R.color.blue),
                    activityTitle = getString(R.string.edit_photo),
                    activityBackgroundColor = ContextCompat.getColor(requireContext(), R.color.green_success),
                    activityMenuIconColor = ContextCompat.getColor(requireContext(), R.color.red_violet),
                    activityMenuTextColor = ContextCompat.getColor(requireContext(), R.color.green_success),
                    cropperLabelText = getString(R.string.send),
                    cropperLabelTextColor = ContextCompat.getColor(requireContext(), R.color.text_primary),
                    toolbarBackButtonColor = ContextCompat.getColor(requireContext(), R.color.red_violet),
                ),
            ),
        )
    }
}