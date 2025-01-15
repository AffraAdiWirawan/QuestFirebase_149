package com.example.pertemuan14.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan14.model.Mahasiswa
import com.example.pertemuan14.repository.MahasiswaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repoMhs: MahasiswaRepository
) : ViewModel() {
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getMhs()
    }

    fun getMhs() {
        viewModelScope.launch {
            repoMhs.getMahasiswa().onStart {
                mhsUiState = HomeUiState.Loading
            }
                .catch {
                    mhsUiState = HomeUiState.Error(e = it)
                }
                .collect {
                    mhsUiState = if (it.isEmpty()) {
                        HomeUiState.Error(Exception("Belum ada data Mahasiswa"))
                    } else
                        HomeUiState.Success(it)
                }
        }
    }

    fun deleteMhs(mahasiswa: Mahasiswa) { // ViewModel untuk delete
        viewModelScope.launch {
            try {
                repoMhs.deleteMahasiswa(mahasiswa)
            } catch (e: Exception) {
                mhsUiState = HomeUiState.Error(e)
            }
        }
    }

    fun getMhsDetail(nim: String): Flow<Mahasiswa> {
        return repoMhs.getMahasiswaByNim(nim)
    }
}

sealed class HomeUiState {
    // Loading
    object Loading : HomeUiState()

    // Sukses
    data class Success(val data: List<Mahasiswa>) : HomeUiState()

    // Error
    data class Error(val e: Throwable) : HomeUiState()
}