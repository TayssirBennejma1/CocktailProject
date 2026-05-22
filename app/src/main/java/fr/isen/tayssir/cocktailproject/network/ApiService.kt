package fr.isen.tayssir.cocktailproject.network

import fr.isen.tayssir.cocktailproject.data.CategoryResponse
import fr.isen.tayssir.cocktailproject.data.CocktailResponse
import fr.isen.tayssir.cocktailproject.data.DrinkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(
        @Query("c") category: String
    ): DrinkResponse

    @GET("lookup.php")
    suspend fun getCocktailById(
        @Query("i") drinkId: String
    ): CocktailResponse
}