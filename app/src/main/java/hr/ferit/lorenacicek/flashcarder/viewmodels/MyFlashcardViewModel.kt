package hr.ferit.lorenacicek.flashcarder.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcard
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcardSet

class MyFlashcardViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val flashcards = mutableStateListOf<MyFlashcard>()

    fun fetchFlashcards(setId: String) {
        db.collection("my_flashcards")
            .whereEqualTo("setId", setId)
            .get()
            .addOnSuccessListener { result ->
                flashcards.clear()
                for (document in result.documents) {
                    val flashcard = document.toObject(MyFlashcard::class.java)
                    if (flashcard != null) {
                        flashcards.add(flashcard)
                    }
                }
            }
    }

    fun getFavoriteStatus(setId: String, onComplete: (Boolean) -> Unit) {
        db.collection("my_flashcard_sets").document(setId)
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
        db.collection("my_flashcard_sets").document(setId)
            .update("isFavorited", isFavorited)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onComplete() }
    }

    fun fetchFlashcardsForSet(setId: String) {
        db.collection("my_flashcards")
            .whereEqualTo("setId", setId)
            .get()
            .addOnSuccessListener { result ->
                flashcards.clear()
                for (document in result.documents) {
                    val flashcard = document.toObject(MyFlashcard::class.java)
                    if (flashcard != null) {
                        flashcards.add(flashcard)
                    }
                }
            }
    }

    fun fetchFavoriteSets(onResult: (List<MyFlashcardSet>) -> Unit) {
        db.collection("my_flashcard_sets")
            .whereEqualTo("isFavorited", true)
            .get()
            .addOnSuccessListener { result ->
                val favoriteSets = result.documents.mapNotNull { document ->
                    val flashcardSet = document.toObject(MyFlashcardSet::class.java)
                    flashcardSet?.apply { id = document.id }
                }
                onResult(favoriteSets)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun updateCard(
        cardId: String,
        termText: String?,
        definitionText: String?,
        termImageUrl: String?,
        definitionImageUrl: String?,
        onComplete: (Boolean) -> Unit
    ) {
        val cardRef = db.collection("my_flashcards").document(cardId)

        val updatedCard = mapOf(
            "termText" to termText,
            "definitionText" to definitionText,
            "termImageUrl" to termImageUrl,
            "definitionImageUrl" to definitionImageUrl
        )

        cardRef.update(updatedCard)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun fetchFlashcardsBySetId(setId: String, onResult: (List<MyFlashcard>) -> Unit) {
        db.collection("my_flashcards")
            .whereEqualTo("setId", setId)
            .get()
            .addOnSuccessListener { result ->
                val flashcards = result.documents.mapNotNull { it.toObject(MyFlashcard::class.java) }
                onResult(flashcards)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
