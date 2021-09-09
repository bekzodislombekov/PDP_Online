package com.android.example.pdponline.fragments.course

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.BuildConfig
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.DialogLayoutBinding
import com.android.example.pdponline.databinding.FragmentEditCourseBinding
import com.android.example.pdponline.models.Course
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "course"

class EditCourseFragment : Fragment() {
    private var course: Course? = null
    private lateinit var binding: FragmentEditCourseBinding
    private lateinit var appDatabase: AppDatabase
    private var fileAbsolutePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable(ARG_PARAM1) as Course?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCourseBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = course?.name
        binding.image.setImageURI(Uri.fromFile(File(course?.imagePath!!)))
        fileAbsolutePath = course?.imagePath!!
        binding.name.setText(course?.name)
        binding.editBtn.setOnClickListener {
            val name = binding.name.text.toString()
            course?.name = name
            course?.imagePath = fileAbsolutePath
            appDatabase.courseDao().updateCourse(course!!)
            findNavController().popBackStack()
        }
        binding.image.setOnClickListener {
            setDialog()
        }
        return binding.root
    }

    private fun setDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)
        dialogBinding.cameraImg.setOnClickListener {
            getImageFromCamera()
            bottomSheetDialog.dismiss()
        }
        dialogBinding.galleryImg.setOnClickListener {
            getImageFromGallery()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun getImageFromGallery() {
        getImageContent.launch("image/*")
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.image.setImageURI(it)
        val openInputStream = activity?.contentResolver?.openInputStream(it)
        val currentTimeMillis = System.currentTimeMillis()
        val file = File(activity?.filesDir, "$currentTimeMillis.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        openInputStream?.close()
        fileOutputStream.close()
        fileAbsolutePath = file.absolutePath
    }

    private fun createImageFile(): File {
        val currentTimeMillis = System.currentTimeMillis()
        val filesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES!!)
        val tempFile = File.createTempFile("$currentTimeMillis", ".jpg", filesDir)
        fileAbsolutePath = tempFile.absolutePath
        return tempFile
    }

    private val getCameraContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            binding.image.setImageURI(Uri.fromFile(File(fileAbsolutePath)))
        }

    private fun getImageFromCamera() {
        val createImageFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID,
            createImageFile
        )
        getCameraContent.launch(photoUri)
    }
}