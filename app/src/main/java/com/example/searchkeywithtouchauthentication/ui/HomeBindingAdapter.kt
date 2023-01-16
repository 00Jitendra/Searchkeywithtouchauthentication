package com.example.searchkeywithtouchauthentication.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchkeywithtouchauthentication.data.model.SearchKeyModel


@BindingAdapter("app:items")
fun setList(listView: RecyclerView, items: MutableList<SearchKeyModel.Hit>) {
    (listView.adapter as SearchActivityAdapter)
}