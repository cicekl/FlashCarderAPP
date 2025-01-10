import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import hr.ferit.lorenacicek.flashcarder.ui.theme.GreyBtn
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFlashcardsScreen(
    navController: NavHostController,
    viewModel: MyFlashcardSetViewModel = viewModel(),
    setId: String?
) {
    var term by remember { mutableStateOf("") }
    var definition by remember { mutableStateOf("") }
    var termImageUri by remember { mutableStateOf<Uri?>(null) }
    var definitionImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val termImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        termImageUri = uri
    }
    val definitionImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        definitionImageUri = uri
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFE135)
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

                IconButton(onClick = {
                    navController.navigate("my_flashcards_screen")
                }) {
                    Icon(Icons.Filled.Done, contentDescription = "Save and Exit")
                }
            }

            Spacer(modifier = Modifier.height(50.dp))


            TextField(
                value = term,
                onValueChange = { term = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                label = { Text("Term (front side)") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
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

                termImageUri?.let { uri ->
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
                value = definition,
                onValueChange = { definition = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                label = { Text("Definition (back side)") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
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

                definitionImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Definition Image",
                        modifier = Modifier
                            .size(56.dp)
                            .padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    isUploading = true
                    val flashcard = MyFlashcard(
                        termText = term,
                        termImageUrl = termImageUri?.toString(),
                        definitionText = definition,
                        definitionImageUrl = definitionImageUri?.toString()
                    )

                    setId?.let { id ->
                        viewModel.addFlashcardWithImages(id, flashcard) { success ->
                            isUploading = false
                            if (success) {
                                term = ""
                                definition = ""
                                termImageUri = null
                                definitionImageUri = null

                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreyBtn
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(55.dp)
                    .width(228.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = !isUploading
            ) {
                Text(
                    if (isUploading) "Uploading..." else "Add card",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }
    }
}
