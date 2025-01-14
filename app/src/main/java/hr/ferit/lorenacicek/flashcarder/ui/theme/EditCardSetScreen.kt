package hr.ferit.lorenacicek.flashcarder.ui.theme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardSetViewModel

@Composable
fun EditCardSetScreen(
    navController: NavHostController,
    setId: String,
    viewModel: MyFlashcardSetViewModel
) {
    var setName by remember { mutableStateOf("") }
    var setDescription by remember { mutableStateOf("") }

    LaunchedEffect(setId) {
        viewModel.fetchSetById(setId) { set ->
            setName = set.name
            setDescription = set.description
        }
    }

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
            EditCardSetHeader(
                navController = navController,
                setId = setId,
                setName = setName,
                setDescription = setDescription,
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(150.dp))
            EditCardSetInputs(
                setName = setName,
                onNameChange = { setName = it },
                setDescription = setDescription,
                onDescriptionChange = { setDescription = it }
            )
            Spacer(modifier = Modifier.height(100.dp))
            EditFlashcardsButton(navController, setId)
        }
    }
}

@Composable
fun EditCardSetHeader(
    navController: NavHostController,
    setId: String,
    setName: String,
    setDescription: String,
    viewModel: MyFlashcardSetViewModel
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
            viewModel.updateSet(setId, setName, setDescription) {
                navController.popBackStack()
            }
        }) {
            Icon(Icons.Filled.Done, contentDescription = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardSetInputs(
    setName: String,
    onNameChange: (String) -> Unit,
    setDescription: String,
    onDescriptionChange: (String) -> Unit
) {
    TextField(
        value = setName,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth(0.9f),
        label = { Text("Name") },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
    Spacer(modifier = Modifier.height(20.dp))
    TextField(
        value = setDescription,
        onValueChange = onDescriptionChange,
        modifier = Modifier.fillMaxWidth(0.9f),
        label = { Text("Description") },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun EditFlashcardsButton(navController: NavHostController, setId: String) {
    Button(
        onClick = { navController.navigate("edit_card_screen/$setId") },
        colors = ButtonDefaults.buttonColors(containerColor = GreyBtn),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(55.dp)
            .width(228.dp)
    ) {
        Text(
            text = "Edit Flashcards",
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}
