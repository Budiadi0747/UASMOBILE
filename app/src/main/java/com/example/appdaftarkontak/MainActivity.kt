package com.example.appdaftarkontak

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appdaftarkontak.database.AppDatabase
import com.example.appdaftarkontak.repository.ContactRepository
import com.example.appdaftarkontak.ui.screens.AddEditContactScreen
import com.example.appdaftarkontak.ui.screens.ContactListScreen
import com.example.appdaftarkontak.ui.theme.AppDaftarKontakTheme // Sesuaikan dengan nama tema Anda
import com.example.appdaftarkontak.viewmodel.ContactViewModel
import com.example.appdaftarkontak.viewmodel.ContactViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppDaftarKontakTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    // Fungsi untuk menampilkan dialog konfirmasi hapus
    private fun showDeleteConfirmationDialog(
        contact: com.example.appdaftarkontak.database.Contact,
        contactViewModel: ContactViewModel
    ) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Kontak")
            .setMessage("Anda yakin ingin menghapus kontak ${contact.name}?")
            .setPositiveButton("Hapus") { dialog, _ ->
                contactViewModel.delete(contact)
                Toast.makeText(this, "Kontak ${contact.name} dihapus", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val context = LocalContext.current

        // Inisialisasi ViewModel di level NavHost
        val contactViewModel: ContactViewModel = viewModel(
            factory = ContactViewModelFactory(ContactRepository(AppDatabase.getDatabase(context).contactDao()))
        )

        NavHost(navController = navController, startDestination = "contactList") {
            composable("contactList") {
                val contacts by contactViewModel.allContacts.observeAsState(initial = emptyList())
                ContactListScreen(
                    contacts = contacts,
                    onContactClick = { contactId ->
                        navController.navigate("addEditContact/$contactId")
                    },
                    onDeleteClick = { contact ->
                        // Panggil dialog dari Activity karena AlertDialog.Builder perlu Context dari Activity
                        // Tidak bisa langsung dari Composable tanpa Context yang tepat
                        (context as? MainActivity)?.showDeleteConfirmationDialog(contact, contactViewModel)
                    },
                    onAddContactClick = { // Ini adalah penambahan penting untuk FAB
                        navController.navigate("addEditContact") // Arahkan ke rute tambah baru
                    }
                )
            }
            composable("addEditContact/{contactId}") { backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull()
                AddEditContactScreen(
                    contactId = contactId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("addEditContact") { // Route untuk menambah baru (tanpa ID)
                AddEditContactScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}