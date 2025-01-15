package com.example.pertemuan14.ui.theme.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan14.ui.theme.viewmodel.FormErrorState
import com.example.pertemuan14.ui.theme.viewmodel.FormState
import com.example.pertemuan14.ui.theme.viewmodel.InsertUiState
import com.example.pertemuan14.ui.theme.viewmodel.InsertViewModel
import com.example.pertemuan14.ui.theme.viewmodel.MahasiswaEvent
import com.example.pertemuan14.ui.theme.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// fungsi insert view
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState // State utama untuk loading, success, error
    val uiEvent = viewModel.uiEvent // State untuk form dan validasi
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Event perubahan state untuk snackbar dan navigasi
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                println(
                    "InsertMhsView: uiState is FormState.Success, navigate to home " + uiState.message
                )
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message) //Tampilkan snackbar
                }
                delay(700)
                // Navigasi langsung
                onNavigate()
                viewModel.resetSnackBarMessage() // Reset snackbar state
            }
            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updatedEvent ->
                    viewModel.updateState(updatedEvent)
                },
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.insertMhs()
                    }
                }
            )
        }
    }
}
@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading,
        ) {
            if (homeUiState is FormState.Loading) {
                CircularProgressIndicator(
                    color = Color.Red,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading...")
            } else {
                Text("Tambah Data")
            }
        }
    }
}
@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier

) {
    val gender = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A", "B", "C", "D", "E")

    Column (
        modifier = modifier.fillMaxWidth()
    ) {
        mahasiswaEvent.nama?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(nama = it))
                },
                label = { Text("Nama") },
                isError = errorState.nama != null,
                placeholder = { Text("Masukkan nama") },
            )
        }
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )
        mahasiswaEvent.nim?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it, onValueChange = {
                    onValueChange(mahasiswaEvent.copy(nim = it))
                },
                label = { Text("NIM") },
                isError = errorState.nim != null,
                placeholder = { Text("Masukkan NIM") },
                keyboardOptions = KeyboardOptions(keyboardType =
                KeyboardType.Number)
            )
        }
        Text(text = errorState.nim ?: "", color = Color.Red)
        Text(text = "Jenis Kelamin")
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            gender.forEach { jk ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mahasiswaEvent.jenis_kelamin == jk,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(jenis_kelamin
                            = jk))
                        },
                    )
                    Text(
                        text = jk,
                    )
                }
            }
        }
        Text(
            text = errorState.jenis_kelamin ?: "",
            color = Color.Red
        )
        mahasiswaEvent.alamat?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(alamat = it))
                },
                label = { Text("Alamat") },
                isError = errorState.alamat != null,
                placeholder = { Text("Masukkan alamat") },
            )
        }
        Text(text = errorState.alamat ?: "", color = Color.Red)
        Text(text = "Kelas")
        Row {
            kelas.forEach { kelas ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(kelas =
                            kelas))
                        },
                    )
                    Text(text = kelas)
                }
            }
        }
        Text(
            text = errorState.kelas ?: "",
            color = Color.Red
        )
        mahasiswaEvent.angkatan?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(angkatan = it))
                },
                label = { Text("Angkatan") },
                isError = errorState.angkatan != null,
                placeholder = { Text("Masukkan angkatan") },
                keyboardOptions = KeyboardOptions(keyboardType =
                KeyboardType.Number)
            )
        }
        Text(text = errorState.angkatan ?: "", color = Color.Red)

        mahasiswaEvent.judulskripsi?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(judulskripsi = it))
                },
                label = { Text("Judul Skripsi") },
                isError = errorState.judulskripsi != null,
                placeholder = { Text("Masukkan Judul Skripsi") },
            )
        }
        Text(
            text = errorState.judulskripsi ?: "",
            color = Color.Red
        )

        mahasiswaEvent.dosensatu?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(dosensatu = it))
                },
                label = { Text("Dosen Pembimbing Pertama") },
                isError = errorState.dosensatu != null,
                placeholder = { Text("Masukkan Dosen Pertama Kamu") },
            )
        }
        Text(
            text = errorState.dosensatu ?: "",
            color = Color.Red
        )

        mahasiswaEvent.dosendua?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = {
                    onValueChange(mahasiswaEvent.copy(dosendua = it))
                },
                label = { Text("Dosen Pembimbing Kedua") },
                isError = errorState.dosendua != null,
                placeholder = { Text("Masukkan Dosen Kedua Kamu") },
            )
        }
        Text(
            text = errorState.dosendua ?: "",
            color = Color.Red
        )
    }
}

