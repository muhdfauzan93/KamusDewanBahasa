package com.caliphstudio.kamusdewanbahasa.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


data class Result(

        @SerializedName("KamusDewan")
        val KamusDewan:List<KamusDewan>,

        @SerializedName("success")
        val success:Int
)

data class KamusDewan(
        @SerializedName("tajuk")
        val tajuk:String,

        @SerializedName("maksud")
        val maksud:String,

        @SerializedName("edisi")
        val edisi:String
)

open class BookmarkModel : RealmObject(){

    @PrimaryKey
    lateinit var id:String
    lateinit var title:String
    lateinit var content:String
    lateinit var edition:String
}
