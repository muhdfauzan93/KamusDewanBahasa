package com.caliphstudio.kamusdewanbahasa.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.caliphstudio.kamusdewanbahasa.R

class Splash : AppCompatActivity() {

    private val screenTime = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        },screenTime.toLong())
    }

}
