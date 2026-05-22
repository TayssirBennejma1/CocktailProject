package fr.isen.tayssir.cocktailproject.data

import com.google.gson.annotations.SerializedName

data class CocktailResponse(
    val drinks: List<Cocktail>?
)

data class CategoryResponse(
    val drinks: List<Category>?
)

data class DrinkResponse(
    val drinks: List<DrinkPreview>?
)

data class Category(
    @SerializedName("strCategory")
    val name: String
)

data class DrinkPreview(
    @SerializedName("idDrink")
    val id: String,

    @SerializedName("strDrink")
    val name: String,

    @SerializedName("strDrinkThumb")
    val image: String?
)

data class Cocktail(
    @SerializedName("idDrink")
    val id: String,

    @SerializedName("strDrink")
    val name: String,

    @SerializedName("strCategory")
    val category: String?,

    @SerializedName("strAlcoholic")
    val alcoholic: String?,

    @SerializedName("strGlass")
    val glass: String?,

    @SerializedName("strInstructions")
    val instructions: String?,

    @SerializedName("strDrinkThumb")
    val image: String?,

    @SerializedName("strIngredient1")
    val ingredient1: String?,

    @SerializedName("strIngredient2")
    val ingredient2: String?,

    @SerializedName("strIngredient3")
    val ingredient3: String?,

    @SerializedName("strIngredient4")
    val ingredient4: String?,

    @SerializedName("strIngredient5")
    val ingredient5: String?,

    @SerializedName("strMeasure1")
    val measure1: String?,

    @SerializedName("strMeasure2")
    val measure2: String?,

    @SerializedName("strMeasure3")
    val measure3: String?,

    @SerializedName("strMeasure4")
    val measure4: String?,

    @SerializedName("strMeasure5")
    val measure5: String?
) {
    fun ingredientsList(): List<String> {
        val list = mutableListOf<String>()

        if (!ingredient1.isNullOrBlank()) list.add("${measure1 ?: ""} $ingredient1")
        if (!ingredient2.isNullOrBlank()) list.add("${measure2 ?: ""} $ingredient2")
        if (!ingredient3.isNullOrBlank()) list.add("${measure3 ?: ""} $ingredient3")
        if (!ingredient4.isNullOrBlank()) list.add("${measure4 ?: ""} $ingredient4")
        if (!ingredient5.isNullOrBlank()) list.add("${measure5 ?: ""} $ingredient5")

        return list
    }
}