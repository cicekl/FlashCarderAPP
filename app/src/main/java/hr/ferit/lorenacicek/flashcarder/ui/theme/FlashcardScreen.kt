import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import coil3.compose.AsyncImage
import hr.ferit.lorenacicek.flashcarder.R
import hr.ferit.lorenacicek.flashcarder.data.Flashcard
import hr.ferit.lorenacicek.flashcarder.viewmodels.FlashcardViewModel

@Composable
fun FlashcardScreen(setId: String?, navController: NavHostController, viewModel: FlashcardViewModel = viewModel()) {
    val flashcards = viewModel.flashcards
    var currentCardIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var isFavorited by remember { mutableStateOf(false) }

    LaunchedEffect(setId) {
        if (setId != null) {
            viewModel.fetchFlashcards(setId)
            viewModel.getFavoriteStatus(setId) { status ->
                isFavorited = status
            }
        }
    }

    if (flashcards.isEmpty()) {
        EmptyFlashcardsScreen(navController)
    } else {
        FlashcardsContent(
            flashcards = flashcards,
            currentCardIndex = currentCardIndex,
            isFlipped = isFlipped,
            isFavorited = isFavorited,
            onCardFlip = { isFlipped = !isFlipped },
            onFavoriteToggle = {
                isFavorited = !isFavorited
                setId?.let { viewModel.toggleFavoriteStatus(it, isFavorited) {} }
            },
            onPreviousCard = { currentCardIndex = if (currentCardIndex > 0) currentCardIndex - 1 else flashcards.lastIndex },
            onNextCard = { currentCardIndex = (currentCardIndex + 1) % flashcards.size },
            navController = navController
        )
    }
}

@Composable
fun EmptyFlashcardsScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFE135)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                }

                Spacer(modifier = Modifier.height(300.dp))

                Text(
                    text = "No flashcards available.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun FlashcardsContent(
    flashcards: List<Flashcard>,
    currentCardIndex: Int,
    isFlipped: Boolean,
    isFavorited: Boolean,
    onCardFlip: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onPreviousCard: () -> Unit,
    onNextCard: () -> Unit,
    navController: NavHostController
) {
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
            FlashcardsHeader(
                isFavorited = isFavorited,
                onBackClick = { navController.popBackStack() },
                onFavoriteToggle = onFavoriteToggle
            )

            Spacer(modifier = Modifier.height(70.dp))

            FlashcardCard(
                flashcard = flashcards[currentCardIndex % flashcards.size],
                isFlipped = isFlipped,
                onCardFlip = onCardFlip,
                cardNumber = currentCardIndex
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlashcardsNavigation(
                onPreviousCard = onPreviousCard,
                onNextCard = onNextCard
            )
        }
    }
}

@Composable
fun FlashcardsHeader(
    isFavorited: Boolean,
    onBackClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        IconButton(onClick = onFavoriteToggle) {
            Icon(
                painter = painterResource(id = if (isFavorited) R.drawable.favorite else R.drawable.favborder),
                contentDescription = "Favorite",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun FlashcardCard(
    flashcard: Flashcard,
    isFlipped: Boolean,
    cardNumber: Int,
    onCardFlip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(0.75f)
            .clickable(onClick = onCardFlip),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CARD #${cardNumber + 1}",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FlashcardContent(
                flashcard = flashcard,
                isFlipped = isFlipped
            )
        }
    }
}

@Composable
fun FlashcardContent(flashcard: Flashcard, isFlipped: Boolean) {
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
                FlashcardImageWithText(imageUrl = flashcard.termImageUrl, text = flashcard.termText)
            } else if (!flashcard.termText.isNullOrBlank()) {
                FlashcardTextOnly(text = flashcard.termText)
            }
        } else {
            if (!flashcard.definitionImageUrl.isNullOrBlank()) {
                FlashcardImageWithText(imageUrl = flashcard.definitionImageUrl, text = flashcard.definitionText)
            } else if (!flashcard.definitionText.isNullOrBlank()) {
                FlashcardTextOnly(text = flashcard.definitionText)
            }
        }
    }
}

@Composable
fun FlashcardImageWithText(imageUrl: String?, text: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )
        text?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FlashcardTextOnly(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )
    }
}

@Composable
fun FlashcardsNavigation(onPreviousCard: () -> Unit, onNextCard: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onPreviousCard,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.width(120.dp)
        ) {
            Text(text = "Previous", color = Color.Black, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        }

        Button(
            onClick = onNextCard,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.width(120.dp)
        ) {
            Text(text = "Next", color = Color.Black, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        }
    }
}




