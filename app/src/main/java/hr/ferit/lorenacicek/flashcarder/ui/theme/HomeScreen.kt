package hr.ferit.lorenacicek.flashcarder.ui.theme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.lorenacicek.flashcarder.R

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.pozadina),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize().padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(180.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(350.dp)
                )
                Button(
                    onClick = {
                        navController.navigate("main_menu_screen")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Yellow62
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(50.dp).width(150.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = "Get Smarter",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
