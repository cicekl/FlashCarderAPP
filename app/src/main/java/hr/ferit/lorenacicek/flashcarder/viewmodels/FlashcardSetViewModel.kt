package hr.ferit.lorenacicek.flashcarder.viewmodels
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.lorenacicek.flashcarder.data.FlashcardSet

class FlashcardSetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val flashcardSets = mutableStateListOf<FlashcardSet>()

    fun fetchFlashcardSetsForCategory(categoryId: String?) {
        if (categoryId == null) return

        db.collection("flashcard__sets")
            .whereEqualTo("categoryId", categoryId)
            .get()
            .addOnSuccessListener { result ->
                flashcardSets.clear()
                for (data in result.documents) {
                    val flashcardSet = data.toObject(FlashcardSet::class.java)
                    if (flashcardSet != null) {
                        flashcardSet.id = data.id
                        flashcardSets.add(flashcardSet)
                    }
                }

            }

    }
}
