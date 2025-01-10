import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.ferit.lorenacicek.flashcarder.R
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcardSet
import hr.ferit.lorenacicek.flashcarder.ui.theme.Typography
import hr.ferit.lorenacicek.flashcarder.ui.theme.Yellow62
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFlashcardsScreen(navController: NavHostController, viewModel: MyFlashcardSetViewModel) {
    val flashcardSets = remember { mutableStateListOf<MyFlashcardSet>() }
    var searchQuery by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var flashcardSetToDelete by remember { mutableStateOf<MyFlashcardSet?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchMyFlashcardSets { sets ->
            flashcardSets.clear()
            flashcardSets.addAll(sets)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navController.navigate("main_menu_screen") {
                        popUpTo("main_menu_screen") { inclusive = true }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    placeholder = { Text("Search...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )

                IconButton(onClick = { navController.navigate("add_set_screen") }) {
                    Icon(Icons.Sharp.AddCircle, contentDescription = "Add Flashcard Set")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (flashcardSets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No card set.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Click on the plus to create your first card set.",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(flashcardSets.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.description.contains(searchQuery, ignoreCase = true)
                    }) { flashcardSet ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("my_flashcard_screen/${flashcardSet.id}")
                                },
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = flashcardSet.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            flashcardSetToDelete = flashcardSet
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.recycle_bin_line_icon),
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            navController.navigate("edit_card_set_screen/${flashcardSet.id}")
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.edit_square_line_icon),
                                            contentDescription = "Edit",
                                            tint = Color.Blue
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = flashcardSet.description,
                                style = Typography.bodyLarge,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Confirmation", color = Color.Black, fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Are you sure you want to delete this flashcard set?", color = Color.Black)
            },
            confirmButton = {
                TextButton(onClick = {
                    flashcardSetToDelete?.let { set ->
                        viewModel.deleteFlashcardSet(set.id) {
                            flashcardSets.remove(set)
                            showDeleteDialog = false
                        }
                    }
                }) {
                    Text("Yes", color = Color.Black, fontWeight = FontWeight.Bold)
                }

            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Yellow62
        )
    }
}



