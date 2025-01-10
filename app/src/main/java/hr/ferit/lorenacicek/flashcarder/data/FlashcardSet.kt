package hr.ferit.lorenacicek.flashcarder.data

data class FlashcardSet(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val categoryId: String = "",
    var isFavorited: Boolean = false
)
