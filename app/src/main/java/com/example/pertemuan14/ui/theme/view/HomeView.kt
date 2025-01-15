package com.example.pertemuan14.ui.theme.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan14.R
import com.example.pertemuan14.model.Mahasiswa
import com.example.pertemuan14.ui.theme.viewmodel.HomeUiState
import com.example.pertemuan14.ui.theme.viewmodel.HomeViewModel
import com.example.pertemuan14.ui.theme.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry:()->Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit ,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {Text("Home")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Mahasiswa")
            }
        },
    ) { innerPadding->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = {viewModel.getMhs()},
            modifier = Modifier
                .padding(innerPadding),
            //.verticalScroll(rememberScrollState()),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deleteMhs(it) // panggil fungsi delete
            },
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    var deleteConfirm by rememberSaveable { mutableStateOf<Mahasiswa?>(null) }

    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success -> MhsLayout (
            listMhs = homeUiState.data,
            modifier = modifier.fillMaxWidth(),
            onClick = { onDetailClick(it) },
            onDelete = { deleteConfirm = it }
        )

        is HomeUiState.Error -> OnError(
            message = homeUiState.e.message ?: "Error",
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }

    deleteConfirm?.let { data ->
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                onDeleteClick(data)
                deleteConfirm = null
            },
            onDeleteCancel = { deleteConfirm = null }
        )
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.umy),
            contentDescription = null
        )
    }
}

@Composable
fun OnError(
    message: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.umy),
                contentDescription = null
            )
            Text(
                text = message,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}

@Composable
fun MhsLayout(
    listMhs: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onDelete: (Mahasiswa) -> Unit
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(items = listMhs) { mhs ->
            MhsCard(
                mahasiswa = mhs,
                onClick = { mhs.nim?.let { onClick(it) } },
                onDeleteClick = { onDelete(mhs) }
            )
        }
    }
}

@Composable
fun MhsCard(
    mahasiswa: Mahasiswa,
    onClick: () -> Unit = {},
    onEditClick: (Mahasiswa) -> Unit = {},
    onDeleteClick: (Mahasiswa) -> Unit = {},
    modifier: Modifier = Modifier

) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mahasiswa.nama ?: "No Name",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onEditClick(mahasiswa) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Delete Mahasiswa"
                    )
                }
                IconButton(onClick = { onDeleteClick(mahasiswa) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Mahasiswa"
                    )
                }
                Text(
                    text = mahasiswa.nim ?: "No NIM",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = mahasiswa.alamat ?: "No Address",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = mahasiswa.judulskripsi ?: "Skripsi",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDeleteCancel() },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) { Text("Cancel") }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) { Text("Yes") }
        }
    )
}