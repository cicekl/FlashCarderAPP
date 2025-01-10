import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hr.ferit.lorenacicek.flashcarder.R
import hr.ferit.lorenacicek.flashcarder.data.FlashcardSet
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcardSet
import hr.ferit.lorenacicek.flashcarder.ui.theme.Typography
import hr.ferit.lorenacicek.flashcarder.viewmodels.FlashcardViewModel
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(setId: String?, navController: NavHostController, viewModel: FlashcardViewModel = viewModel(), viewModelMy: MyFlashcardViewModel = viewModel()) {
    var favoriteSets by remember { mutableStateOf(listOf<FlashcardSet>()) }
    var myfavoriteSets by remember { mutableStateOf(listOf<MyFlashcardSet>()) }
    var searchQuery by remember { mutableStateOf("") }
    var isFavorited by remember { mutableStateOf(false) }

    LaunchedEffect(setId) {
        if (setId != null) {
            viewModel.fetchFlashcards(setId)
            viewModelMy.fetchFlashcards(setId)
            viewModelMy.getFavoriteStatus(setId) { status ->
                isFavorited = status
            }
            viewModel.getFavoriteStatus(setId) { status ->
                isFavorited = status
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteSets { sets ->
            favoriteSets = sets
        }
        viewModelMy.fetchFavoriteSets { sets ->
            myfavoriteSets = sets
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFFE135)) {

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
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
                Text(
                    text = "FAVOURITES",
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 15.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
                LazyColumn {
                    items(favoriteSets) { flashcardSet ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("flashcard_screen/${flashcardSet.id}")
                                }
                                .padding(8.dp)
                        ) {
                            Row(
                               modifier = Modifier
                                   .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = flashcardSet.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                IconButton(onClick = {
                                    viewModel.toggleFavoriteStatus(
                                        flashcardSet.id,
                                        false
                                    ) {
                                        favoriteSets = favoriteSets.filter { it.id != flashcardSet.id }
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.defavoritize),
                                        contentDescription = "Remove Favorite",
                                        tint = Color.Black
                                    )
                                }
                            }
                            Text(
                                text = flashcardSet.description,
                                style = Typography.bodyLarge
                            )
                        }
                    }

                    items(myfavoriteSets) { flashcardSet ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("my_flashcard_screen/${flashcardSet.id}")
                                }
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = flashcardSet.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                IconButton(onClick = {
                                    viewModelMy.toggleFavoriteStatus(
                                        flashcardSet.id,
                                        false
                                    ) {
                                        myfavoriteSets = myfavoriteSets.filter { it.id != flashcardSet.id }
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.defavoritize),
                                        contentDescription = "Remove Favorite",
                                        tint = Color.Black
                                    )
                                }
                            }
                            Text(
                                text = flashcardSet.description,
                                style = Typography.bodyLarge
                            )
                        }
                    }
                }
        }
    }
}



