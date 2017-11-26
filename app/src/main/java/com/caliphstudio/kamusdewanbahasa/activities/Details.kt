package com.caliphstudio.kamusdewanbahasa.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.caliphstudio.kamusdewanbahasa.R
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.AnkoLogger

@Suppress("DEPRECATION")
class Details : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val b:Bundle=intent.extras


        if (b!=null){
            val tajuk = Html.fromHtml(b.getString("tajuk"))
            val maksud = Html.fromHtml(b.getString("maksud"))
            val edisi = b.getString("edisi")
            setMeaning(tajuk.toString(),maksud.toString(),edisi)
        }else
            finish()

    }

    private fun setMeaning(tajuk:String, maksud:String, edisi:String){
        tv_tittle.text = tajuk
        tv_content.text = maksud
        tv_edition.text = edisi
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
