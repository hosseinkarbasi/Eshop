package com.example.eshop.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eshop.R
import com.example.eshop.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationBinding.bind(view)

        val request = PeriodicWorkRequestBuilder<NotifyWork>(1, TimeUnit.MINUTES)
            .build()

        val request2 = OneTimeWorkRequestBuilder<NotifyWork>().build()

        binding.notifyBtn.setOnClickListener {
            WorkManager.getInstance(requireContext()).enqueue(request2)
        }

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request2.id)
            .observe(viewLifecycleOwner) {
                val status: String = it.state.name
                Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}