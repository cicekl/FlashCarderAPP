package hr.ferit.lorenacicek.flashcarder.ui.theme
import AddFlashcardsScreen
import AddSetScreen
import CategoriesScreen
import FavouritesScreen
import FlashcardScreen
import MyFlashcardsScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.ferit.lorenacicek.flashcarder.viewmodels.FlashcardViewModel
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardSetViewModel
import hr.ferit.lorenacicek.flashcarder.viewmodels.MyFlashcardViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(navController)
        }
        composable("main_menu_screen") {
            MainMenuScreen(navController)
        }
        composable("categories_screen") {
            CategoriesScreen(navController)
        }
        composable("flashcard_set_screen/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            FlashcardSetScreen(navController = navController, categoryId = categoryId)
        }
        composable("my_flashcards_screen") {
            val viewModel: MyFlashcardSetViewModel = viewModel()
            MyFlashcardsScreen(navController, viewModel = viewModel)
        }
        composable("flashcard_screen/{setId}") { backStackEntry ->
            val viewModel: FlashcardViewModel = viewModel()
            val setId = backStackEntry.arguments?.getString("setId")
            FlashcardScreen(setId = setId, navController = navController, viewModel = viewModel)
        }
        composable("favourites_screen") { backStackEntry ->
            val viewModel: FlashcardViewModel = viewModel()
            val setId = backStackEntry.arguments?.getString("setId")
            FavouritesScreen(setId = setId, navController = navController, viewModel = viewModel)
        }
        composable("add_set_screen") {
            AddSetScreen(navController = navController)
        }
        composable("add_flashcards_screen?setId={setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId")
            val viewModel: MyFlashcardSetViewModel = viewModel()
            AddFlashcardsScreen(
                navController = navController,
                viewModel = viewModel,
                setId = setId
            )
        }

        composable("my_flashcard_screen/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: return@composable
            val viewModel: MyFlashcardViewModel = viewModel()
            MyFlashcardScreen(setId = setId, navController = navController, viewModel = viewModel)
        }

        composable("edit_card_set_screen/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: ""
            EditCardSetScreen(
                navController = navController,
                setId = setId,
                viewModel = MyFlashcardSetViewModel()
            )
        }

        composable("edit_card_screen/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: ""
            val viewModel: MyFlashcardViewModel = viewModel()

            EditCardScreen(
                navController = navController,
                viewModel = viewModel,
                setId = setId
            )
        }


    }
}
