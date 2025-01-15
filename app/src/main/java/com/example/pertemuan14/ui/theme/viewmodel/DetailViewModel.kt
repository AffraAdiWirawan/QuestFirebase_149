package com.example.pertemuan14.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan14.model.Mahasiswa
import com.example.pertemuan14.repository.MahasiswaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val exception: Throwable) : DetailUiState()
}

class DetailViewModel(private val repository: MahasiswaRepository) : ViewModel() {

    var mahasiswaUiState by mutableStateOf<DetailUiState>(DetailUiState.Loading)
        private set

    fun getMahasiswaByNim(nim: String) {
        viewModelScope.launch {
            try {
                mahasiswaUiState = DetailUiState.Loading
                // Collect the Flow to get the data asynchronously
                repository.getMahasiswaByNim(nim).collect { mahasiswa ->
                    // Once data is collected, update the UI state
                    mahasiswaUiState = DetailUiState.Success(mahasiswa)
                }
            } catch (e: Exception) {
                mahasiswaUiState = DetailUiState.Error(e)
            }
        }
    }

    fun getMhsDetail(nim: String): Flow<Mahasiswa> {
        return repository.getMahasiswaByNim(nim)
    }
}

