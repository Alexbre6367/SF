package com.example.sf.database.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SFViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SFViewModel::class.java)) {
            return SFViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}