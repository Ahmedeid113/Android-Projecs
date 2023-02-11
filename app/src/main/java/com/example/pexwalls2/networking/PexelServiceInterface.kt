package com.example.pexwalls2.networking

import com.example.pexwalls2.models.Imagemodel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PexelServiceInterface {
    @Headers("Authorization:AomZyYBbUio7mnf3ryMD5h5OnSKQ8tuoIAQCDhRhHPkQpdLUu7kOoveJ")
    @GET("curated")
    suspend fun getallimages(
    ): Response<Imagemodel>

    @GET("search")
    suspend fun getimagewithsearch(@Query("query") value:String, @Query("per_page") perpage:Int): Response<Imagemodel>

}