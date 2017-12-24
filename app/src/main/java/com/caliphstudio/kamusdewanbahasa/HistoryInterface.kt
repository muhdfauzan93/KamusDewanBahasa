package com.caliphstudio.kamusdewanbahasa

interface HistoryInterface {

    fun addSearchHistory(keyword:String)
    fun getSearchHistory()
    fun removeSearchHistory(keyword: String)
}