package com.example.appdaftarkontak.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add // <<< PENTING: Import ikon Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appdaftarkontak.database.Contact
import com.example.appdaftarkontak.ui.theme.AppDaftarKontakTheme // Sesuaikan dengan nama tema Anda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    contacts: List<Contact>,
    onContactClick: (Int) -> Unit, // Callback untuk edit
    onDeleteClick: (Contact) -> Unit, // Callback untuk delete
    onAddContactClick: () -> Unit // <<< PENTING: Callback untuk menambah kontak (untuk FAB)
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daftar Kontak") })
        },
        floatingActionButton = { // <<< PENTING: Bagian ini menambahkan Floating Action Button
            FloatingActionButton(onClick = onAddContactClick) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Kontak")
            }
        },
        floatingActionButtonPosition = FabPosition.End // Posisi FAB (pojok kanan bawah)
    ) { paddingValues ->
        if (contacts.isEmpty()) {
            Text(
                text = "Belum ada kontak. Tambahkan sekarang!",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = androidx.compose.ui.Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts) { contact ->
                    ContactListItem(
                        contact = contact,
                        onClick = { onContactClick(contact.id) },
                        onDeleteClick = { onDeleteClick(contact) }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit,
    onDeleteClick: (Contact) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
                Text(text = contact.phoneNumber, style = MaterialTheme.typography.bodyMedium)
                contact.email?.let {
                    if (it.isNotBlank()) {
                        Text(text = it, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            IconButton(onClick = { onDeleteClick(contact) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Hapus Kontak")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    AppDaftarKontakTheme {
        val dummyContacts = listOf(
            Contact(1, "Budi Santoso", "081234567890", "budi@example.com"),
            Contact(2, "Siti Aminah", "08765432100", null),
            Contact(3, "Joko Susilo", "08987654321", "joko.s@mail.com")
        )
        // PREVIEW juga harus disesuaikan untuk onAddContactClick
        ContactListScreen(contacts = dummyContacts, onContactClick = {}, onDeleteClick = {}, onAddContactClick = {})
    }
}