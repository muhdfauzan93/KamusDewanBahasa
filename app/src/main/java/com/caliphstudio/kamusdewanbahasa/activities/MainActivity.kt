@file:Suppress("DEPRECATION")

package com.caliphstudio.kamusdewanbahasa.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.caliphstudio.kamusdewanbahasa.HistoryInterface
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.adapters.HistoryAdapter
import com.caliphstudio.kamusdewanbahasa.models.History
import com.caliphstudio.kamusdewanbahasa.models.SearchHistory
import com.caliphstudio.kamusdewanbahasa.models.SwitchToggle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.error
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), AnkoLogger, HistoryInterface {


    private lateinit var myAdapter: HistoryAdapter
    private var historyList = ArrayList<History>()

    // Open the realm for the UI thread.
    //creating  database oject
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        @Suppress("DEPRECATION")
        supportActionBar!!.title = Html.fromHtml("<strong><small>"+getString(R.string.app_name)+"</small></strong>")

        myAdapter = HistoryAdapter(this,historyList)
        history_recycler.adapter = myAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        history_recycler.layoutManager = layoutManager
        history_recycler.setHasFixedSize(true)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        getSearchHistory()
        checkHistorySwitch()

        history_switch.setOnCheckedChangeListener { button, b ->

            setSwitch(b)
            if (b){
                toast("Button is on")
            }
            else
                toast("Button is off")
        }
        btn_semak.setOnClickListener {
            val searchKey = et_search_text.text.toString().trim()
            if (searchKey.isNotEmpty()){
                myAdapter.notifyDataSetChanged()
                addSearchHistory(searchKey.toLowerCase())
                val intent = Intent(this,Search::class.java)
                intent.putExtra("searchText", searchKey)
                startActivity(intent)
            }
            else{
                toast("Sila masukkan istilah untuk carian")
            }
        }

        btn_clear_history.setOnClickListener {
            removeAllSearchHistory()
        }

    }

    private fun checkHistorySwitch(){

        val isSwitchSet = realm.where(SwitchToggle::class.java).equalTo("id",1.toInt()).findFirst()
        if (isSwitchSet==null) {
            realm.executeTransaction {
                val switch = realm.createObject(SwitchToggle::class.java,1)
                switch.switchStatus = false
                !history_switch.isChecked
            }
        } else{
            history_switch.isChecked = isSwitchSet.switchStatus
        }

    }

    private fun setSwitch(status: Boolean){
        realm.executeTransaction {
            val isSwitchSet = realm.where(SwitchToggle::class.java).equalTo("id",1.toInt()).findFirst()
            isSwitchSet!!.switchStatus = status
        }

    }

    override fun addSearchHistory(keyword: String) {
        if (history_switch.isChecked) {
            val isSave = realm.where(SearchHistory::class.java).equalTo("searchWord", keyword.toString()).findFirst()
            if (isSave == null) {
                realm.executeTransaction {
                    realm.createObject(SearchHistory::class.java, keyword)
                }
                //toast("Keyword added")

            }
        }
    }

    override fun getSearchHistory() {
        btn_clear_history.isClickable = false
        historyList.clear()
        val checkHistory = realm.where(SearchHistory::class.java).findAll()
        if (checkHistory.isNotEmpty()){
            for (i in checkHistory){
                historyList.add(History(i.searchWord))
            }
            btn_clear_history.isClickable = true
        }
        myAdapter.notifyDataSetChanged()
    }

    override fun removeSearchHistory(keyword: String) {
        val removeHistory = realm.where(SearchHistory::class.java).equalTo("searchWord", keyword.toString()).findAll()
        if (removeHistory.isNotEmpty()) {
            realm.executeTransaction {
                removeHistory.deleteAllFromRealm()
            }
        }
    }

    private fun removeAllSearchHistory(){
        alert("Adakah anda ingin memadamkan semua sejarah carian?","Padam Sejarah Carian") {
            positiveButton("Ya") {
                try {
                    realm.executeTransaction {
                        realm.delete(SearchHistory::class.java)
                    }
                    getSearchHistory()
                } catch (ex: Exception) {
                    error { ex.message.toString() }
                }
            }
            negativeButton("Batal") {
            }
        }.show()
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId){
                R.id.bookmarks->{
                    startActivity(Intent(this,Bookmarks::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getSearchHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
