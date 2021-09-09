package com.android.example.pdponline.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pdponline.R
import com.android.example.pdponline.databinding.FragmentLessonBinding
import com.android.example.pdponline.models.Lesson

private const val ARG_PARAM1 = "lesson"

class LessonFragment : Fragment() {
    private var lesson: Lesson? = null
    private lateinit var binding: FragmentLessonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getSerializable(ARG_PARAM1) as Lesson?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLessonBinding.inflate(inflater, container, false)
        binding.pos.text = "${lesson?.position}-dars"
        binding.theme.text = lesson?.theme
        binding.conspect.text = lesson?.conspect

        return binding.root
    }
}