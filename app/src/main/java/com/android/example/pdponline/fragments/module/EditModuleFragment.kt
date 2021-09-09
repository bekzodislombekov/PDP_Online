package com.android.example.pdponline.fragments.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentEditModuleBinding
import com.android.example.pdponline.models.Module

private const val ARG_PARAM1 = "module"

class EditModuleFragment : Fragment() {
    private var module: Module? = null
    private lateinit var binding: FragmentEditModuleBinding
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getSerializable(ARG_PARAM1) as Module?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditModuleBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = module?.name
        binding.name.setText(module?.name)
        binding.position.setText(module?.position.toString())
        binding.editBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val pos = binding.position.text.toString()
            module?.name = name
            module?.position = pos.toInt()
            appDatabase.moduleDao().updateModule(module!!)
            findNavController().popBackStack()
        }
        return binding.root
    }

}