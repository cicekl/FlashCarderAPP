package hr.ferit.lorenacicek.flashcarder.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import hr.ferit.lorenacicek.flashcarder.R
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardViewModel

@Composable
fun MyFlashcardScreen(setId: String?, navController: NavHostController, viewModel: MyFlashcardViewModel) {
    val flashcards = viewModel.flashcards
    var currentCardIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var isFavorited by remember { mutableStateOf(false) }


        LaunchedEffect(setId) {
            if (setId != null) {
                viewModel.fetchFlashcards(setId)
                viewModel.fetchFlashcardsForSet(setId)
                viewModel.getFavoriteStatus(setId) { status ->
                    isFavorited = status
                }
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFFE135)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
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
                        isFavorited = !isFavorited
                        setId?.let {
                            viewModel.toggleFavoriteStatus(it, isFavorited) {
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = if (isFavorited) R.drawable.favorite else R.drawable.favborder),
                            contentDescription = "Favorite",
                            tint = Color.Black
                        )
                    }

                }

                Spacer(modifier = Modifier.height(70.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(0.75f)
                        .clickable {
                            isFlipped = !isFlipped
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    val flashcard = flashcards[currentCardIndex % flashcards.size]
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CARD #${currentCardIndex + 1}",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = if (!isFlipped) {
                                if (flashcard.termImageUrl.isNullOrBlank()) Alignment.Center else Alignment.TopCenter
                            } else {
                                if (flashcard.definitionImageUrl.isNullOrBlank()) Alignment.Center else Alignment.TopCenter
                            }
                        ) {
                            if (!isFlipped) {
                                if (!flashcard.termImageUrl.isNullOrBlank()) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            model = flashcard.termImageUrl,
                                            contentDescription = "Term Image",
                                            modifier = Modifier
                                                .fillMaxWidth(0.8f)
                                                .aspectRatio(1f)
                                        )
                                        if (!flashcard.termText.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = flashcard.termText,
                                                color = Color.Black,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else if (!flashcard.termText.isNullOrBlank()) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            text = flashcard.termText,
                                            color = Color.Black,
                                            fontSize = 40.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 40.sp
                                        )
                                    }
                                }
                            } else {
                                if (!flashcard.definitionImageUrl.isNullOrBlank()) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            model = flashcard.definitionImageUrl,
                                            contentDescription = "Definition Image",
                                            modifier = Modifier
                                                .fillMaxWidth(0.8f)
                                                .aspectRatio(1f)
                                        )
                                        if (!flashcard.definitionText.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = flashcard.definitionText,
                                                color = Color.Black,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else if (!flashcard.definitionText.isNullOrBlank()) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            text = flashcard.definitionText,
                                            color = Color.Black,
                                            fontSize = 40.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 40.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            isFlipped = false
                            currentCardIndex = if (currentCardIndex > 0) currentCardIndex - 1 else flashcards.lastIndex
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Previous", color = Color.Black, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                    }

                    Button(
                        onClick = {
                            isFlipped = false
                            currentCardIndex = (currentCardIndex + 1) % flashcards.size
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Next", color = Color.Black,fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }

}