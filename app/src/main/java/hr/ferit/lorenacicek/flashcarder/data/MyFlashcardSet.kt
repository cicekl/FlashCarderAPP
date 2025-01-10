package hr.ferit.lorenacicek.flashcarder.data

data class MyFlashcardSet(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    var isFavorited: Boolean = false
)