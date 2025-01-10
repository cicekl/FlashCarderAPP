package hr.ferit.lorenacicek.flashcarder.ui.theme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    var flashcards by remember { mutableStateOf(listOf<MyFlashcard>()) }
    var currentIndex by remember { mutableStateOf(0) }

    val termImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            flashcards = flashcards.toMutableList().apply {
                val currentCard = this[currentIndex]
                this[currentIndex] = currentCard.copy(termImageUrl = selectedUri.toString())
            }
        }
    }
    val definitionImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            flashcards = flashcards.toMutableList().apply {
                val currentCard = this[currentIndex]
                this[currentIndex] = currentCard.copy(definitionImageUrl = selectedUri.toString())
            }
        }
    }

    LaunchedEffect(setId) {
        viewModel.fetchFlashcardsBySetId(setId) { cards ->
            flashcards = cards
        }
    }

    if (flashcards.isEmpty()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFFE135)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    IconButton(onClick = {
                        navController.navigate("add_flashcards_screen?setId=$setId")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Flashcards", tint = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(200.dp))

                Text(
                    text = "No cards available",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click on the '+' icon to add a new card.",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    } else {
        val currentCard = flashcards[currentIndex]

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

                Spacer(modifier = Modifier.height(50.dp))

                Text(text = "CARD #${currentIndex + 1}", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = currentCard.termText ?: "",
                    onValueChange = { newTerm ->
                        flashcards = flashcards.toMutableList().apply {
                            this[currentIndex] = currentCard.copy(termText = newTerm)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Term (front side)") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { termImagePicker.launch("image/*") },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.picture_icon),
                            contentDescription = "Add Term Image",
                            tint = Color.Black
                        )
                    }

                    currentCard.termImageUrl?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Term Image",
                            modifier = Modifier
                                .size(56.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }

                TextField(
                    value = currentCard.definitionText ?: "",
                    onValueChange = { newDefinition ->
                        flashcards = flashcards.toMutableList().apply {
                            this[currentIndex] = currentCard.copy(definitionText = newDefinition)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Definition (back side)") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { definitionImagePicker.launch("image/*") },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.picture_icon),
                            contentDescription = "Add Definition Image",
                            tint = Color.Black
                        )
                    }

                    currentCard.definitionImageUrl?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Definition Image",
                            modifier = Modifier
                                .size(56.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (currentIndex > 0) {
                                currentIndex -= 1
                            }
                        },
                        enabled = currentIndex > 0,
                        modifier = Modifier.width(120.dp)

                    ) {
                        Text(text = "Previous", color = Color.Black,fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic)
                    }

                    Button(
                        onClick = {
                            if (currentIndex < flashcards.size - 1) {
                                currentIndex += 1
                            }
                        },
                        enabled = currentIndex < flashcards.size - 1,
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Next", color = Color.Black,fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }
}





