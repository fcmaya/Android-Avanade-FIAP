package br.com.equipequatro.traveltips.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _numeroTab = MutableLiveData<Int>()
    val numeroTab = MutableLiveData<Int>()

    init {
        _numeroTab.value = 0
        numeroTab.value = 0
    }

    fun mudarTab() {
        numeroTab.value = _numeroTab.value
    }
}