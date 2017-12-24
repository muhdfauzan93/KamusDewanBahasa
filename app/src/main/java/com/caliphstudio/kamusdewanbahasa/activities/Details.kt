package com.caliphstudio.kamusdewanbahasa.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.models.BookmarkModel
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class Details : AppCompatActivity(), AnkoLogger {

    lateinit var id:String
    private lateinit var tajuk:String
    private lateinit var maksud:String
    private lateinit var edisi:String

    // Open the realm for the UI thread.
    //creating  database oject
    private val realm = Realm.getDefaultInstance()
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val b:Bundle=intent.extras

        tajuk = Html.fromHtml(b.getString("tajuk")).toString()
        //capitalize the first letter of a String
        val convertString = tajuk.substring(0,1).toUpperCase() + tajuk.substring(1).toLowerCase()
        maksud = Html.fromHtml(b.getString("maksud")).toString()
        edisi = b.getString("edisi").toString()
        supportActionBar!!.title = Html.fromHtml("<strong><small>$convertString</small></strong>")
        setMeaning(tajuk, maksud,edisi)


    }

    @SuppressLint("SetTextI18n")
    private fun setMeaning(tajuk:String, maksud:String, edisi:String){
        tv_tittle.text = tajuk.substring(0,1).toUpperCase() + tajuk.substring(1).toLowerCase()
        tv_content.text = maksud
        tv_edition.text = edisi

        id = (tajuk+edisi).replace(" ", "")


    }

    private fun isBookmark(id:String):Boolean{
        val isBookmark = realm.where(BookmarkModel::class.java).equalTo("id", id).findAll()

        if (isBookmark.isEmpty())
            return false

    return true
    }

    private fun bookmark(id:String,title:String,content:String,edition:String):Boolean{
        val isBookmark = realm.where(BookmarkModel::class.java).equalTo("id", id).findAll()

        if (isBookmark.isEmpty()){
            realm.executeTransaction {
                val bookmark = realm.createObject(BookmarkModel::class.java,id)
                bookmark.title = title
                bookmark.content = content
                bookmark.edition = edition

                toast("Penanda buku telah disimpan")

            }
            return true
        }
        else {
            realm.executeTransaction {
                isBookmark.deleteAllFromRealm()
                toast("Penanda buku telah dipadam")
            }
            return false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        if (isBookmark(id)){
            menu!!.findItem(R.id.bookmark).setIcon(R.drawable.ic_turned_in_white_48dp)
        }
        else{
            menu!!.findItem(R.id.bookmark).setIcon(R.drawable.ic_turned_in_not_white_48dp)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId){
                R.id.bookmark->{
                    if (bookmark(id,tajuk,maksud,edisi))
                        item.setIcon(R.drawable.ic_turned_in_white_48dp)
                    else
                        item.setIcon(R.drawable.ic_turned_in_not_white_48dp)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
