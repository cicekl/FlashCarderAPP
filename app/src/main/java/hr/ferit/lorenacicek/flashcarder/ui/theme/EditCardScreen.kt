package hr.ferit.lorenacicek.flashcarder.ui.theme

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import hr.ferit.lorenacicek.flashcarder.R
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcard
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardViewModel

@Composable
fun EditCardScreen(
    navController: NavHostController,
    viewModel: MyFlashcardViewModel = viewModel(),
    setId: String
) {
    var flashcards = remember { mutableStateListOf<MyFlashcard>() }
    var currentIndex by remember { mutableStateOf(0) }

    val termImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            flashcards[currentIndex] = flashcards[currentIndex].copy(termImageUrl = selectedUri.toString())
        }
    }

    val definitionImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            flashcards[currentIndex] = flashcards[currentIndex].copy(definitionImageUrl = selectedUri.toString())
        }
    }

    LaunchedEffect(setId) {
        viewModel.fetchFlashcardsBySetId(setId) { cards ->
            flashcards.clear()
            flashcards.addAll(cards)
        }
    }

    if (flashcards.isEmpty()) {
        EmptyFlashcardsScreen(navController, setId)
    } else {
        FlashcardsEditor(
            navController = navController,
            viewModel = viewModel,
            flashcards = flashcards,
            currentIndex = currentIndex,
            onIndexChange = { currentIndex = it },
            termImagePicker = termImagePicker,
            definitionImagePicker = definitionImagePicker
        )
    }
}

@Composable
fun EmptyFlashcardsScreen(navController: NavHostController, setId: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFE135)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(navController = navController, setId = setId)
            Spacer(modifier = Modifier.height(200.dp))
            Text(
                text = "No cards available",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Header(navController: NavHostController, setId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        IconButton(onClick = { navController.navigate("add_flashcards_screen?setId=$setId") }) {
            Icon(Icons.Default.Add, contentDescription = "Add Flashcards", tint = Color.Black)
        }
    }
}

@Composable
fun FlashcardsEditor(
    navController: NavHostController,
    viewModel: MyFlashcardViewModel,
    flashcards: MutableList<MyFlashcard>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    termImagePicker: ManagedActivityResultLauncher<String, Uri?>,
    definitionImagePicker: ManagedActivityResultLauncher<String, Uri?>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFE135)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            EditorHeader(navController, viewModel, flashcards[currentIndex])
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "CARD #${currentIndex + 1}", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))
            CardEditor(
                currentCard = flashcards[currentIndex],
                onFlashcardChange = { updatedCard ->
                    flashcards[currentIndex] = updatedCard
                },
                termImagePicker = termImagePicker,
                definitionImagePicker = definitionImagePicker
            )
            Spacer(modifier = Modifier.height(16.dp))
            NavigationButtons(
                currentIndex = currentIndex,
                flashcardsSize = flashcards.size,
                onIndexChange = onIndexChange
            )
        }
    }
}

@Composable
fun EditorHeader(navController: NavHostController, viewModel: MyFlashcardViewModel, currentCard: MyFlashcard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        IconButton(onClick = {
            viewModel.updateCard(
                cardId = currentCard.id,
                termText = currentCard.termText,
                definitionText = currentCard.definitionText,
                termImageUrl = currentCard.termImageUrl,
                definitionImageUrl = currentCard.definitionImageUrl
            ) {
                navController.popBackStack()
            }
        }) {
            Icon(Icons.Filled.Done, contentDescription = "Save")
        }
    }
}

@Composable
fun CardEditor(
    currentCard: MyFlashcard,
    onFlashcardChange: (MyFlashcard) -> Unit,
    termImagePicker: ManagedActivityResultLauncher<String, Uri?>,
    definitionImagePicker: ManagedActivityResultLauncher<String, Uri?>
) {
    TextField(
        value = currentCard.termText ?: "",
        onValueChange = { newTerm -> onFlashcardChange(currentCard.copy(termText = newTerm)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Term (front side)") }
    )
    Spacer(modifier = Modifier.height(8.dp))
    ImagePickerRow(
        imageUri = currentCard.termImageUrl,
        onPickImage = { termImagePicker.launch("image/*") }
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = currentCard.definitionText ?: "",
        onValueChange = { newDefinition -> onFlashcardChange(currentCard.copy(definitionText = newDefinition)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Definition (back side)") }
    )
    Spacer(modifier = Modifier.height(8.dp))
    ImagePickerRow(
        imageUri = currentCard.definitionImageUrl,
        onPickImage = { definitionImagePicker.launch("image/*") }
    )
}

@Composable
fun ImagePickerRow(imageUri: String?, onPickImage: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        IconButton(onClick = onPickImage, modifier = Modifier.size(56.dp)) {
            Icon(painter = painterResource(id = R.drawable.picture_icon), contentDescription = "Add Image", tint = Color.Black)
        }
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = Modifier.size(56.dp).padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun NavigationButtons(currentIndex: Int, flashcardsSize: Int, onIndexChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { if (currentIndex > 0) onIndexChange(currentIndex - 1) },
            enabled = currentIndex > 0,
            modifier = Modifier.width(120.dp)
        ) {
            Text("Previous")
        }
        Button(
            onClick = { if (currentIndex < flashcardsSize - 1) onIndexChange(currentIndex + 1) },
            enabled = currentIndex < flashcardsSize - 1,
            modifier = Modifier.width(120.dp)
        ) {
            Text("Next")
        }
    }
}
