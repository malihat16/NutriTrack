package com.fit2081.a1_maliha33473692.data.api

import com.fit2081.a1_maliha33473692.data.model.FruitInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApi {
    @GET("api/fruit/{name}")
    suspend fun getFruitInfo(
        @Path("name") fruitName: String
    ): FruitInfoResponse
}
