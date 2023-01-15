package com.example.searchkeywithtouchauthentication.api

import com.example.searchkeywithtouchauthentication.data.model.SearchKeyModel
import retrofit2.Response
import retrofit2.http.*


interface SearchKeyService {

    @GET(ApiSetting.SEARCH)
    suspend fun getSearchList(
        @Query(ApiSetting.API_KEY) apiKey: String,
        @Query(ApiSetting.SEARCH_KEY) keyToSearch: String,
        @Query(ApiSetting.TYPE) type: String,
        @Query(ApiSetting.PRETTY) format: Boolean
    ): Response<SearchKeyModel>
}