package com.android.example.pdponline.fragments.course

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.BuildConfig
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.course.CourseSettingsAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.DialogLayoutBinding
import com.android.example.pdponline.databinding.FragmentCourseSettingsBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Module
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList

class CourseSettingsFragment : Fragment() {
    private lateinit var binding: FragmentCourseSettingsBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var courseList: ArrayList<Course>
    private lateinit var courseSettingsAdapter: CourseSettingsAdapter
    private lateinit var listModule: ArrayList<Module>
    private var fileAbsolutePath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseSettingsBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())
        courseList = ArrayList(appDatabase.courseDao().getAllCourses())

        binding.actionBarTv.text = "Sozlamalar"
        binding.image.setOnClickListener {
            checkPermission()
        }

        binding.addBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val course = Course(name = name, imagePath = fileAbsolutePath)
            Observable.fromCallable {
                appDatabase.courseDao().addCourse(course)
                courseSettingsAdapter.submitList(appDatabase.courseDao().getAllCourses())
            }.subscribe()
            binding.name.setText("")
            binding.image.setImageResource(R.drawable.place_holder)
            fileAbsolutePath = ""
        }

        courseSettingsAdapter =
            CourseSettingsAdapter(
                object :
                    CourseSettingsAdapter.OnItemClickListener {
                    override fun onItemListener(course: Course) {
                        val bundle = bundleOf("course" to course)
                        findNavController().navigate(R.id.moduleSettingsFragment, bundle)
                    }

                    override fun onEditListener(course: Course) {
                        val bundle = bundleOf("course" to course)
                        findNavController().navigate(R.id.editCourseFragment, bundle)
                    }

                    override fun onDeleteListener(course: Course) {
                        listModule =
                            ArrayList(appDatabase.moduleDao().getAllModulesByCourse(course.id))
                        val alertDialog = AlertDialog.Builder(requireContext())
                        if (listModule.size > 0) {
                            alertDialog.setMessage("Bu kurs ichida modullar kiritilgan. Modullar bilan birgalikda o’chib ketishiga rozimisiz?")
                        } else {
                            alertDialog.setMessage("Kurs o’chishiga rozimisiz?")
                        }
                        alertDialog.setPositiveButton("Ha") { dialog, which ->
                            appDatabase.courseDao().deleteCourse(course)
                            courseSettingsAdapter.submitList(
                                appDatabase.courseDao().getAllCourses()
                            )
                        }
                        alertDialog.setNegativeButton("Yo'q") { dialog, which ->
                            dialog.dismiss()
                        }
                        alertDialog.show()
                    }
                })
        courseSettingsAdapter.submitList(appDatabase.courseDao().getAllCourses())
        binding.rv.adapter = courseSettingsAdapter
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

    private fun checkPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.isAnyPermissionPermanentlyDenied) {
                        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
                        val uri = Uri.fromParts("package", activity?.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    if (p0.areAllPermissionsGranted()) {
                        setDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }

            }).check()
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