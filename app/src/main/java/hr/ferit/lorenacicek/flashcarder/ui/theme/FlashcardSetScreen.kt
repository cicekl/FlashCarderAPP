package hr.ferit.lorenacicek.flashcarder.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hr.ferit.lorenacicek.flashcarder.data.FlashcardSet
import hr.ferit.lorenacicek.flashcarder.viewmodels.FlashcardSetViewModel

@Composable
fun FlashcardSetScreen(navController: NavHostController, categoryId: String?) {
    val viewModel: FlashcardSetViewModel = viewModel()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categoryId) {
        viewModel.fetchFlashcardSetsForCategory(categoryId)
    }

    val flashcardSets = viewModel.flashcardSets

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlashcardSetHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onBackClick = { navController.navigate("categories_screen") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FlashcardSetList(
                flashcardSets = flashcardSets,
                searchQuery = searchQuery,
                onNavigateToFlashcardScreen = { flashcardSetId ->
                    navController.navigate("flashcard_screen/$flashcardSetId")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSetHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.size(50.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            placeholder = { Text("Search...") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun FlashcardSetList(
    flashcardSets: List<FlashcardSet>,
    searchQuery: String,
    onNavigateToFlashcardScreen: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(flashcardSets.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }) { flashcardSet ->
            FlashcardSetItem(
                name = flashcardSet.name,
                description = flashcardSet.description,
                onClick = { onNavigateToFlashcardScreen(flashcardSet.id) }
            )
        }
    }
}

@Composable
fun FlashcardSetItem(
    name: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = description,
            style = Typography.bodyLarge
        )
    }
}
