package hr.ferit.lorenacicek.flashcarder.data

data class MyFlashcard(
    val id: String = "",
    val termText: String? = null,
    var termImageUrl: String? = null,
    val definitionText: String? = null,
    val setId: String = "",
    var definitionImageUrl: String? = null
)
