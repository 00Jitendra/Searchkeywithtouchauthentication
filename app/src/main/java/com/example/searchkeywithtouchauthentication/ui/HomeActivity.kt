package com.example.searchkeywithtouchauthentication.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.searchkeywithtouchauthentication.BuildConfig
import com.example.searchkeywithtouchauthentication.R
import com.example.searchkeywithtouchauthentication.databinding.ActivityHomeBinding
import com.example.searchkeywithtouchauthentication.utils.hideKeyboard
import com.example.searchkeywithtouchauthentication.utils.notifyUser


class HomeActivity : AppCompatActivity() {
    private lateinit var viewDataBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var searchAdapter: SearchActivityAdapter
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    showPromptTOUser()
                    notifyUser("Authentication required")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication Success!")

                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewDataBinding.homeViewModel = viewModel
        viewDataBinding.lifecycleOwner = this
        checkBiometricSupport()
        setupListAdapter()
        showPromptTOUser()
        viewDataBinding.clearSearchTextBtn.setOnClickListener {
            if (viewDataBinding.etSearchMovie.text.toString() != null) {
                viewDataBinding.etSearchMovie.text = null
                hideKeyboard()
            }
        }

        viewDataBinding.goBtn.setOnClickListener {
            viewModel.loadSearchKey(
                BuildConfig.API_KEY, this,
                viewDataBinding.etSearchMovie.text.toString(), "photo", true
            )
            viewDataBinding.clearSearchTextBtn.visibility = View.VISIBLE
        }


    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.homeViewModel
        if (viewModel != null) {
            searchAdapter =
                SearchActivityAdapter(
                    viewModel, this
                )

            viewDataBinding.pictureListView.adapter = searchAdapter

        } else {
            //  Log.d("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager: KeyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showPromptTOUser() {
        val biometricPrompt: BiometricPrompt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricPrompt.Builder(this)
                .setTitle("Log in using your biometric credential")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .setDescription("PIN, Password or fingerprint")
                .build()
        } else {
            BiometricPrompt.Builder(this)
                .setTitle("Log in using your biometric credential")
                .setDescription("PIN, Password or fingerprint")
                .setNegativeButton(
                    "Cancel",
                    this.mainExecutor
                ) { dialog, which ->
                }.build()
        }
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)


    }
}