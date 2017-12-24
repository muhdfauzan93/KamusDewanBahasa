package com.caliphstudio.kamusdewanbahasa.interfaces

interface HistoryInterface {

    fun addSearchHistory(keyword:String)
    fun getSearchHistory()
    fun removeSearchHistory(keyword: String)
}