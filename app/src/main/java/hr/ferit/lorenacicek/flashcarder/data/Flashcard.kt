package hr.ferit.lorenacicek.flashcarder.data

data class Flashcard(
    val id: String = "",
    val termText: String? = null,
    val termImageUrl: String? = null,
    val definitionText: String? = null,
    val definitionImageUrl: String? = null
)