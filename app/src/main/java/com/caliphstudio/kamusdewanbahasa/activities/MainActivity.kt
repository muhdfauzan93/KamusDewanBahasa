@file:Suppress("DEPRECATION")

package com.caliphstudio.kamusdewanbahasa.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.adapters.SearchAdapter
import com.caliphstudio.kamusdewanbahasa.api.APIClient
import com.caliphstudio.kamusdewanbahasa.api.APIInterface
import com.caliphstudio.kamusdewanbahasa.models.KamusDewan
import com.caliphstudio.kamusdewanbahasa.models.Result
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var searchAdapter: SearchAdapter
    private var searchList = ArrayList<KamusDewan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        @Suppress("DEPRECATION")
        supportActionBar!!.title = Html.fromHtml("<medium>"+getString(R.string.app_name)+"</medium>")

        searchAdapter = SearchAdapter(this,searchList)
        recycler_view.adapter = searchAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)



        btn_search.setOnClickListener {
            val searchKey = et_search.text.toString().trim()
            if (searchKey.isNotEmpty()){
                searchAdapter.notifyDataSetChanged()
                checkWord(searchKey)
            }
            else{
                toast("Sila masukkan istilah untuk carian")
            }
        }

        swipe_layout.setOnRefreshListener {
            val searchKey = et_search.text.toString().trim()
            swipe_layout.isRefreshing = true
            checkWord(searchKey)
            swipe_layout.isRefreshing = false
        }



    }

    private fun checkWord(key:String){
        searchList.clear()
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call = apiInterface.checkWord(key)

        val checkURL = call.request().url().toString()
        error { checkURL }

        @Suppress("DEPRECATION")
        val progressDialog = ProgressDialog.show(this, "Muat turun ...",  "Semak istilah ...", true)
        progressDialog.setCancelable(true)

        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                val result: Result = response!!.body()!!
                if (result.success == 1) {
                    searchList.addAll(result.KamusDewan)
                    error { searchList.size.toString() }

                } else{
                    longToast("Carian istilah tiada di dalam kamus terkini.")
                }
                searchAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<Result>?, t: Throwable?) {

                progressDialog.dismiss()
                error(t!!.message)
            }

        })

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


}
