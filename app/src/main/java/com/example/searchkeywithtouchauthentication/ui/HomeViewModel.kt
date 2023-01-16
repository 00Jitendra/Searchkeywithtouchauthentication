package com.example.searchkeywithtouchauthentication.ui

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchkeywithtouchauthentication.data.model.SearchKeyModel
import com.example.searchkeywithtouchauthentication.manager.ApiManager
import com.example.searchkeywithtouchauthentication.utils.Connectivity
import com.example.searchkeywithtouchauthentication.utils.hideKeyboard
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _searchItems =
        MutableLiveData<MutableList<SearchKeyModel.Hit>>().apply {
            value = mutableListOf()
        }
    val searchItems: LiveData<MutableList<SearchKeyModel.Hit>> = _searchItems

    private val _noDataFoundVisibility = MutableLiveData<Int>().apply { value = View.GONE }
    val noDataFoundVisibility: LiveData<Int> = _noDataFoundVisibility

    private val mUpdatedSearchList = mutableListOf<SearchKeyModel.Hit>()

    /**
     * function to search keys related to pictures list
     * */
    fun loadSearchKey(
        apiKey: String,
        activity: HomeActivity,
        keyName: String,
        type: String,
        format: Boolean
    ) {
        if (Connectivity.isInternetOn()) {
            val service = ApiManager.initRetrofit(activity)
            viewModelScope.launch {

                val response = service.getSearchList(
                    apiKey, keyName, type, format
                )
                if (response.isSuccessful) {
                    if (!response.body()!!.hits.isNullOrEmpty()) {

                        mUpdatedSearchList.addAll(response.body()!!.hits)
                        _searchItems.value = mUpdatedSearchList
                        activity.hideKeyboard()
                    }
                }
            }
        } else {
            activity.runOnUiThread {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }
}