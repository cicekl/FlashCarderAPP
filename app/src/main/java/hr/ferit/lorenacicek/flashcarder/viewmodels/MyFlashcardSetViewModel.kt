package hr.ferit.lorenacicek.flashcarder.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcard
import hr.ferit.lorenacicek.flashcarder.data.MyFlashcardSet

class MyFlashcardSetViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun fetchMyFlashcardSets(onResult: (List<MyFlashcardSet>) -> Unit) {
        db.collection("my_flashcard_sets")
            .get()
            .addOnSuccessListener { result ->
                val flashcardSets = result.documents.mapNotNull { it.toObject(MyFlashcardSet::class.java) }
                onResult(flashcardSets)
            }
    }

    fun deleteFlashcardSet(setId: String, onComplete: () -> Unit) {
        db.collection("my_flashcards")
            .whereEqualTo("setId", setId)
            .get()
            .addOnSuccessListener { result ->
                val batch = db.batch()

                for (document in result.documents) {
                    batch.delete(document.reference)
                }

                val setRef = db.collection("my_flashcard_sets").document(setId)
                batch.delete(setRef)

                batch.commit()
                    .addOnSuccessListener { onComplete() }
                    .addOnFailureListener { onComplete() }
            }
            .addOnFailureListener { onComplete() }
    }

    fun fetchSetById(setId: String, onComplete: (MyFlashcardSet) -> Unit) {
        db.collection("my_flashcard_sets").document(setId)
            .get()
            .addOnSuccessListener { document ->
                val flashcardSet = document.toObject(MyFlashcardSet::class.java)
                if (flashcardSet != null) {
                    onComplete(flashcardSet)
                }
            }
    }

    fun updateSet(setId: String, newName: String, newDescription: String, onComplete: (Boolean) -> Unit) {
        val updates = mapOf(
            "name" to newName,
            "description" to newDescription
        )
        db.collection("my_flashcard_sets").document(setId)
            .update(updates)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun uploadImageToFirebase(uri: Uri, fileName: String, onResult: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onResult(downloadUri.toString())
                }.addOnFailureListener {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun addSet(set: MyFlashcardSet, onComplete: (String?) -> Unit) {
        val setRef = db.collection("my_flashcard_sets").document()
        val setWithId = set.copy(id = setRef.id)

        setRef.set(setWithId)
            .addOnSuccessListener { onComplete(setWithId.id) }
            .addOnFailureListener { onComplete(null) }
    }

    fun addFlashcardToSet(setId: String, flashcard: MyFlashcard, onComplete: (Boolean) -> Unit) {
        val flashcardRef = db.collection("my_flashcards").document()
        val flashcardWithSetId = flashcard.copy(id = flashcardRef.id, setId = setId)

        flashcardRef.set(flashcardWithSetId)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun addFlashcardWithImages(
        setId: String,
        flashcard: MyFlashcard,
        onComplete: (Boolean) -> Unit
    ) {
        val termImageUri = flashcard.termImageUrl
        val definitionImageUri = flashcard.definitionImageUrl

        val uploadTermImage = { callback: (String?) -> Unit ->
            termImageUri?.let { uri ->
                uploadImageToFirebase(Uri.parse(uri), "term_${System.currentTimeMillis()}.jpg") { url ->
                    callback(url)
                }
            } ?: callback(null)
        }

        val uploadDefinitionImage = { callback: (String?) -> Unit ->
            definitionImageUri?.let { uri ->
                uploadImageToFirebase(Uri.parse(uri), "definition_${System.currentTimeMillis()}.jpg") { url ->
                    callback(url)
                }
            } ?: callback(null)
        }

        uploadTermImage { termUrl ->
            uploadDefinitionImage { definitionUrl ->
                val updatedFlashcard = flashcard.copy(
                    termImageUrl = termUrl,
                    definitionImageUrl = definitionUrl
                )
                addFlashcardToSet(setId, updatedFlashcard) { success ->
                    onComplete(success)
                }
            }
        }
    }
}
