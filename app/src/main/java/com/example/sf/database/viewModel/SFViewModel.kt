package com.example.sf.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.sf.auth.SecureStorage
import com.example.sf.database.fridge.Fridge
import com.example.sf.database.fridge.FridgeRoomDatabase
import com.example.sf.database.fridge.Meal
import com.example.sf.database.repositories.Repository
import com.example.sf.gemini.GeminiAi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SFViewModel(application: Application) : AndroidViewModel(application) {

    val foodList: LiveData<List<Fridge>>
    val mealsList: LiveData<List<Meal>>

    private val repository: Repository
    private val _isSelectedFood = MutableStateFlow<Set<Int>>(emptySet())
    val isSelectedFood = _isSelectedFood.asStateFlow()

    private val _isSelectedMeal = MutableStateFlow<Set<Int>>(emptySet())
    val isSelectedMeal = _isSelectedMeal.asStateFlow()
    private val secureStorage = SecureStorage(application)
    private val _userEmail = MutableStateFlow(secureStorage.getEmail() ?: "")
    private val _userPassword = MutableStateFlow(secureStorage.getPassword() ?: "")
    private val _userName = MutableStateFlow(secureStorage.getName() ?: "")

    init {
        val fridgeDb = FridgeRoomDatabase.getInstance(application)
        val fridgeDao = fridgeDb.fridgeDao()
        val mealsDao = fridgeDb.mealsDao()
        repository = Repository(fridgeDao, mealsDao)
        foodList = repository.fridgeList
        mealsList = repository.mealsList
    }

    fun addFood(food: Fridge) {
        viewModelScope.launch {
            repository.addFood(food)
        }
    }


    fun toggleFood(id: Int) {
        _isSelectedFood.value = if (id in _isSelectedFood.value) {
            _isSelectedFood.value - id
        } else {
            _isSelectedFood.value + id
        }
    }

    fun toggleMeal(id: Int) {
        _isSelectedMeal.value = if (id in _isSelectedMeal.value) {
            _isSelectedMeal.value - id
        } else {
            _isSelectedMeal.value + id
        }
    }

    fun clearSelected() {
        _isSelectedFood.value = emptySet()
        _isSelectedMeal.value = emptySet()
    }

    fun deleteFood() {
        viewModelScope.launch {
            _isSelectedFood.value.forEach { id ->
                repository.deleteFood(id)
            }
            _isSelectedFood.value = emptySet()

            _isSelectedMeal.value.forEach { id ->
                repository.deleteMeal(id)
            }
            _isSelectedMeal.value = emptySet()
        }
    }

    fun userCredential(email: String, password: String, name: String) {
        _userEmail.value = email
        _userPassword.value = password
        _userName.value = name
        secureStorage.saveCredentials(email, password, name)
    }

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            repository.addMeal(meal)
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch {
            repository.updateMeal(meal)
        }
    }

    private val _analysisResult = MutableStateFlow<String?>(null)
    suspend fun analyzeFoodResult(text: String): String? {
        return GeminiAi.analyzeFood(text)
    }

    suspend fun analyzeMealResult(text: String): String? {
        return GeminiAi.analyzeMeal(text)
    }

    fun clearAi(){
        _analysisResult.value = null
    }
}


