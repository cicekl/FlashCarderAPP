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

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MyFlashcardsHeader(
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onBackClick = {
                    navController.navigate("main_menu_screen") {
                        popUpTo("main_menu_screen") { inclusive = true }
                    }
                },
                onAddSetClick = { navController.navigate("add_set_screen") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (flashcardSets.isEmpty()) {
                NoFlashcardSetsMessage()
            } else {
                FlashcardSetsList(
                    flashcardSets = flashcardSets,
                    searchQuery = searchQuery,
                    onSetClick = { setId ->
                        navController.navigate("my_flashcard_screen/$setId")
                    },
                    onDeleteSetClick = { set ->
                        flashcardSetToDelete = set
                        showDeleteDialog = true
                    },
                    onEditSetClick = { setId ->
                        navController.navigate("edit_card_set_screen/$setId")
                    }
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                flashcardSetToDelete?.let { set ->
                    viewModel.deleteFlashcardSet(set.id) {
                        flashcardSets.remove(set)
                        showDeleteDialog = false
                    }
                }
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFlashcardsHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onAddSetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
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

        IconButton(onClick = onAddSetClick) {
            Icon(Icons.Sharp.AddCircle, contentDescription = "Add Flashcard Set")
        }
    }
}

@Composable
fun NoFlashcardSetsMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
}

@Composable
fun FlashcardSetsList(
    flashcardSets: List<MyFlashcardSet>,
    searchQuery: String,
    onSetClick: (String) -> Unit,
    onDeleteSetClick: (MyFlashcardSet) -> Unit,
    onEditSetClick: (String) -> Unit
) {
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
            FlashcardSetItem(
                flashcardSet = flashcardSet,
                onClick = { onSetClick(flashcardSet.id) },
                onDeleteClick = { onDeleteSetClick(flashcardSet) },
                onEditClick = { onEditSetClick(flashcardSet.id) }
            )
        }
    }
}

@Composable
fun FlashcardSetItem(
    flashcardSet: MyFlashcardSet,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = flashcardSet.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.recycle_bin_line_icon),
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }

                IconButton(onClick = onEditClick) {
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

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Delete Confirmation", color = Color.Black, fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Are you sure you want to delete this flashcard set?", color = Color.Black)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Yellow62
    )
}



