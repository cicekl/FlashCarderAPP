package hr.ferit.lorenacicek.flashcarder.ui.theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import hr.ferit.lorenacicek.flashcarder.R

@Composable
fun MainMenuScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                style = Typography.titleLarge,
                text = "MENU",
                modifier = Modifier.padding(top = 150.dp)
            )
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = {
                    navController.navigate("categories_screen")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreyBtn
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(55.dp).width(228.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Categories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(52.dp))
            Button(
                onClick = {
                    navController.navigate("my_flashcards_screen")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreyBtn
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(55.dp).width(228.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "My Flashcards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(52.dp))
            Button(
                onClick = {
                    navController.navigate("favourites_screen")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreyBtn
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(55.dp).width(228.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Favourites",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(220.dp))

            var showTooltip by remember { mutableStateOf(false) }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd).offset(y = (-30).dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    showTooltip = true
                                    tryAwaitRelease()
                                    showTooltip = false
                                }
                            )
                        }
                )
                if (showTooltip) {
                    Popup(
                        alignment = Alignment.BottomEnd,
                        offset = IntOffset(x = 0, y = -300),
                        properties = PopupProperties(focusable = false),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Yellow62)
                                .padding(12.dp)
                                .width(200.dp)
                        ) {
                            Text(
                                text = "This application was made by Lorena Čiček.\nHave fun and learn something new!",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal

                            )
                        }
                    }
                }
            }
        }
    }
}