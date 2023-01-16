package com.example.searchkeywithtouchauthentication.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.searchkeywithtouchauthentication.BuildConfig
import com.example.searchkeywithtouchauthentication.R
import com.example.searchkeywithtouchauthentication.databinding.ActivityHomeBinding
import com.example.searchkeywithtouchauthentication.utils.hideKeyboard


class HomeActivity : AppCompatActivity() {
    private lateinit var viewDataBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var searchAdapter: SearchActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewDataBinding.homeViewModel = viewModel
        viewDataBinding.lifecycleOwner = this

        setupListAdapter()

        viewDataBinding.clearSearchTextBtn.setOnClickListener {
            if (viewDataBinding.etSearchMovie.text.toString()!= null) {
                viewDataBinding.etSearchMovie.text=null
                hideKeyboard()
            }
        }

        viewDataBinding.goBtn.setOnClickListener {
            viewModel.loadSearchKey(
                BuildConfig.API_KEY, this,
                viewDataBinding.etSearchMovie.text.toString(), "photo",true
            )
            viewDataBinding.clearSearchTextBtn.visibility= View.VISIBLE
        }

    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.homeViewModel
        if (viewModel != null) {
            searchAdapter =
                SearchActivityAdapter(
                    viewModel, this
                )

            viewDataBinding.pictureListView.adapter = searchAdapter

        } else {
            //  Log.d("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}