package com.example.appdaftarkontak.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdaftarkontak.database.AppDatabase
import com.example.appdaftarkontak.database.Contact
import com.example.appdaftarkontak.repository.ContactRepository
import com.example.appdaftarkontak.viewmodel.ContactViewModel
import com.example.appdaftarkontak.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    contactId: Int? = null, // Null jika menambah, ada ID jika mengedit
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = viewModel(
        factory = ContactViewModelFactory(ContactRepository(AppDatabase.getDatabase(context).contactDao()))
    )

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Memuat data kontak jika dalam mode edit
    LaunchedEffect(contactId) {
        if (contactId != null && contactId != -1) {
            val existingContact = contactViewModel.getContactById(contactId)
            existingContact?.let {
                name = it.name
                phoneNumber = it.phoneNumber
                email = it.email ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contactId != null && contactId != -1) "Edit Kontak" else "Tambah Kontak Baru") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Nomor Telepon") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (Opsional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (name.isBlank() || phoneNumber.isBlank()) {
                        Toast.makeText(context, "Nama dan Nomor Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    } else {
                        val contact = Contact(
                            id = contactId ?: 0, // Jika null, Room akan autoGenerate
                            name = name,
                            phoneNumber = phoneNumber,
                            email = email.ifBlank { null } // Simpan null jika kosong
                        )
                        coroutineScope.launch {
                            if (contactId != null && contactId != -1) {
                                contactViewModel.update(contact)
                                Toast.makeText(context, "Kontak diperbarui", Toast.LENGTH_SHORT).show()
                            } else {
                                contactViewModel.insert(contact)
                                Toast.makeText(context, "Kontak ditambahkan", Toast.LENGTH_SHORT).show()
                            }
                            onBack() // Kembali setelah simpan
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (contactId != null && contactId != -1) "Perbarui Kontak" else "Tambah Kontak")
            }
        }
    }
}