package com.caliphstudio.kamusdewanbahasa.models

import com.google.gson.annotations.SerializedName

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
