package com.example.eshop.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eshop.R
import com.example.eshop.databinding.FragmentNotificationBinding
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationBinding.bind(view)

        setNotification()

    }

    private fun setNotification() = binding.apply {

        val text = choiceEd.text.toString()
        createWorkManager(text.toLong())
        Log.d("as", text)

        notifyRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                cancelNotification.id -> {
                    choiceLayout.gone()
                    cancelWorkManager()
                }
                threeHour.id -> {
                    choiceLayout.gone()
                    createWorkManager(3)
                }
                fiveHour.id -> {
                    choiceLayout.gone()
                    createWorkManager(5)
                }
                eightHour.id -> {
                    choiceLayout.gone()
                    createWorkManager(8)
                }
                twelveHour.id -> {
                    choiceLayout.gone()
                    createWorkManager(12)
                }
                userChoice.id -> {
                    choiceLayout.visible()
                }
            }
        }
    }

    private fun createWorkManager(period: Long) {
        val request = PeriodicWorkRequestBuilder<NotifyWork>(period, TimeUnit.HOURS)
            .build()

        binding.notifyBtn.setOnClickListener {
            WorkManager
                .getInstance(requireContext())
                .enqueueUniquePeriodicWork(
                    "notification",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request
                )
        }
    }

    private fun cancelWorkManager() {
        binding.notifyBtn.setOnClickListener {
            WorkManager
                .getInstance(requireContext())
                .cancelUniqueWork("notification")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}