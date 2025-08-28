package com.example.sf.database.viewModel


import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.sf.gemini.GeminiAi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    var imageCapture: ImageCapture? = null

    fun bindToCamera(
        lifecycleOwner: LifecycleOwner,
        surfaceProvider: Preview.SurfaceProvider
    ) { //жизненный цикл
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            try {
                cameraProvider = ProcessCameraProvider.getInstance(context).await()

                previewUseCase = Preview.Builder().build().also {
                    it.surfaceProvider = surfaceProvider
                }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider?.unbindAll()

                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка привязки к камере: ${e.message}", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }

    private val _loadedBitmap = MutableStateFlow<Bitmap?>(null)
    private val _aiAnalysisResult = MutableStateFlow<String?>(null)
    val aiAnalysisResult: StateFlow<String?> = _aiAnalysisResult.asStateFlow()


    fun processImageUriDirectly(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            _loadedBitmap.value = null
            _aiAnalysisResult.value = null
            try {
                val source = ImageDecoder.createSource(contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)

                _loadedBitmap.value = bitmap

                if(_loadedBitmap.value != null) {
                    Log.d("MyLog", "Загружено изображение: $_loadedBitmap.value")
                    val analysis = GeminiAi.analyzeIMG(_loadedBitmap.value!!)
                    _aiAnalysisResult.value = analysis
                }
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка загрузки изображения: ${e.message}", e)
            }
        }
    }

    fun processCapturedImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _loadedBitmap.value = null
            _aiAnalysisResult.value = null
            try {
                _loadedBitmap.value = bitmap

                Log.d("MyLog", "Загружено изображение: $_loadedBitmap.value")
                val analysis = GeminiAi.analyzeIMG(bitmap)
                _aiAnalysisResult.value = analysis

            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка загрузки изображения: ${e.message}", e)
            }
        }
    }

    fun clearAi() {
        _aiAnalysisResult.value = null
    }
}