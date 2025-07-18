package com.example.sf.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sf.auth.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SFViewModel(application: Application) : AndroidViewModel(application) {

    private val secureStorage = SecureStorage(application)
    private val _userEmail = MutableStateFlow(secureStorage.getEmail() ?: "")
    val userEmail = _userEmail.asStateFlow()

    private val _userPassword = MutableStateFlow(secureStorage.getPassword() ?: "")
    val userPassword = _userPassword.asStateFlow()
    private val _userName = MutableStateFlow(secureStorage.getName() ?: "")
    val userName = _userName.asStateFlow()

    fun userCredential(email: String, password: String, name: String) {
        _userEmail.value = email
        _userPassword.value = password
        _userName.value = name
        secureStorage.saveCredentials(email, password, name)
    }
}


