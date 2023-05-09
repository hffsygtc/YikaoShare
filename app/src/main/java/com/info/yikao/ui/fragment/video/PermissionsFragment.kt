/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.info.yikao.ui.fragment.video

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.info.yikao.R
import com.info.yikao.databinding.FragmentPermissionBinding
import me.hgj.jetpackmvvm.ext.util.loge

private var PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO)

/**
 * 请求授权的页面
 */
class PermissionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }

        if (!hasPermissions(requireContext())) {
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPermissionBinding.inflate(inflater, container, false).also {
            if (hasPermissions(requireContext())) {
                navigateToCapture()
            } else {
                Log.e(PermissionsFragment::class.java.simpleName,
                    "Re-requesting permissions ...")
                activityResultLauncher.launch(PERMISSIONS_REQUIRED)
            }
        }.root
    }
    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    //跳转授权
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && it.value == false)
                    permissionGranted = false
            }
            if (permissionGranted && !permissions.isEmpty()) {
                navigateToCapture()
            }
            if (!permissionGranted) {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun navigateToCapture() {
        lifecycleScope.launchWhenStarted {
            //跳转授权
            "navigateToCapture called".loge()

            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                //跳转到下一个页面
                R.id.action_permissionsFragment_to_captureFragment)
        }
    }
}