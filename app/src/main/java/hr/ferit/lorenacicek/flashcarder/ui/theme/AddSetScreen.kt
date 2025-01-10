import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcardSet
import hr.ferit.lorenacicek.flashcarder.ui.theme.GreyBtn
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSetScreen(navController: NavHostController, viewModel: MyFlashcardSetViewModel = viewModel()) {
    var setName by remember { mutableStateOf("") }
    var setDescription by remember { mutableStateOf("") }

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
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            Spacer(modifier = Modifier.height(150.dp))

            TextField(
                value = setName,
                onValueChange = { setName = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                label = { Text("Enter the name...") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = setDescription,
                onValueChange = { setDescription = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                label = { Text("Enter short description...") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = {
                    val newSet = MyFlashcardSet(name = setName, description = setDescription)

                    viewModel.addSet(newSet) { setId ->
                        if (setId != null) {
                            navController.navigate("add_flashcards_screen?setId=$setId")
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
            ) {
                Text(
                    "Add new flashcards",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }
    }
}
