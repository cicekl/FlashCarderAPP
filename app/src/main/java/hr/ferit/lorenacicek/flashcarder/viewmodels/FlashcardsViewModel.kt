package hr.ferit.lorenacicek.flashcarder.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.lorenacicek.flashcarder.data.Flashcard
import hr.ferit.lorenacicek.flashcarder.data.FlashcardSet

class FlashcardViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val flashcards = mutableStateListOf<Flashcard>()

    fun fetchFlashcards(setId: String) {
        db.collection("flashcards")
            .whereEqualTo("setId", setId)
            .get()
            .addOnSuccessListener { result ->
                flashcards.clear()
                for (document in result.documents) {
                    val flashcard = document.toObject(Flashcard::class.java)
                    if (flashcard != null) {
                        flashcards.add(flashcard)
                    }
                }
            }
    }

    fun getFavoriteStatus(setId: String, onComplete: (Boolean) -> Unit) {
        db.collection("flashcard__sets").document(setId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val isFavorited = document.getBoolean("isFavorited") ?: false
                    onComplete(isFavorited)
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun toggleFavoriteStatus(setId: String, isFavorited: Boolean, onComplete: () -> Unit) {
        db.collection("flashcard__sets").document(setId)
            .update("isFavorited", isFavorited)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onComplete() }
    }

    fun fetchFavoriteSets(onResult: (List<FlashcardSet>) -> Unit) {
        db.collection("flashcard__sets")
            .whereEqualTo("isFavorited", true)
            .get()
            .addOnSuccessListener { result ->
                val favoriteSets = result.documents.mapNotNull { document ->
                    val flashcardSet = document.toObject(FlashcardSet::class.java)
                    flashcardSet?.apply { id = document.id }
                }
                onResult(favoriteSets)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}


