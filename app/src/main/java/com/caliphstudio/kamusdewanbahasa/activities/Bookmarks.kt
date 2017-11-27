package com.caliphstudio.kamusdewanbahasa.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.adapters.SearchAdapter
import com.caliphstudio.kamusdewanbahasa.models.BookmarkModel
import com.caliphstudio.kamusdewanbahasa.models.KamusDewan
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_bookmarks.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.error


class Bookmarks : AppCompatActivity(), AnkoLogger {

    private lateinit var searchAdapter: SearchAdapter
    private var bookmarkList = ArrayList<KamusDewan>()

    // Open the realm for the UI thread.
    //creating  database oject
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        @Suppress("DEPRECATION")
        supportActionBar!!.title = Html.fromHtml("<medium>"+getString(R.string.penanda_buku)+"</medium>")

        searchAdapter = SearchAdapter(this,bookmarkList)
        recycler_view.adapter = searchAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        crud()
    }

    private fun crud(){

        bookmarkList.clear()
        val results = realm.where(BookmarkModel::class.java).findAll()
        if (results!!.load()) {
            for (i in results) {
                val test = KamusDewan(i.title,i.content,i.edition)
                bookmarkList.add(test)
            }
        }
        searchAdapter.notifyDataSetChanged()
    }

    private fun checkBookmark():Boolean{
        val results = realm.where(BookmarkModel::class.java).findAll()
        if (results.isEmpty()){
            return false
        }
        return true
    }


    override fun onResume() {
        error { "onResume" }
        super.onResume()
        crud()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bookmark_menu,menu)
        menu!!.findItem(R.id.delete_all).isEnabled = checkBookmark()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when (item.itemId) {
                R.id.delete_all -> {

                    alert("Adakah anda ingin memadamkan semua penanda?","Padam Penanda") {
                        positiveButton("Ya") {
                            try {
                                realm.executeTransaction {
                                    realm.delete(BookmarkModel::class.java)
                                }
                                item.isEnabled = false
                                crud()
                            } catch (ex: Exception) {
                                throw ex
                            }
                        }
                        negativeButton("Batal") {

                        }
                    }.show()

                }
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
