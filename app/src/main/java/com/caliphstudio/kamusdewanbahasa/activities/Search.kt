@file:Suppress("DEPRECATION")

package com.caliphstudio.kamusdewanbahasa.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.adapters.SearchAdapter
import com.caliphstudio.kamusdewanbahasa.api.APIClient
import com.caliphstudio.kamusdewanbahasa.api.APIInterface
import com.caliphstudio.kamusdewanbahasa.models.KamusDewan
import com.caliphstudio.kamusdewanbahasa.models.Result
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search : AppCompatActivity() {

    private lateinit var searchAdapter: SearchAdapter
    private var searchList = ArrayList<KamusDewan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = Html.fromHtml("<strong><small>"+getString(R.string.app_name)+"</small></strong>")
        val b:Bundle=intent.extras
        val searchText =b.getString("searchText")

        searchAdapter = SearchAdapter(this,searchList)
        recycler_view_search.adapter = searchAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recycler_view_search.layoutManager = layoutManager
        recycler_view_search.setHasFixedSize(true)

        toast(searchText)
        checkWord(searchText)

        swipe_layout.setOnRefreshListener {
            swipe_layout.isRefreshing = true
            checkWord(searchText)
            swipe_layout.isRefreshing = false
        }



    }

    private fun checkWord(key:String){
        searchList.clear()
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val call = apiInterface.checkWord(key)

        //val checkURL = call.request().url().toString()

        //display progress dialog
        val progressDialog = ProgressDialog.show(this, "Muat turun ...",  "Semak istilah ...", true)
        progressDialog.setCancelable(true)


        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                try {
                    val result: Result = response!!.body()!!
                    if (result.success == 1) {
                        searchList.addAll(result.KamusDewan)

                    } else {
                        longToast("Carian istilah tiada di dalam kamus terkini.")
                    }
                    searchAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }catch (ex:Exception){
                    progressDialog.dismiss()
                    toast(ex.message.toString())
                }
            }

            override fun onFailure(call: Call<Result>?, t: Throwable?) {
                toast(t!!.message.toString())
                progressDialog.dismiss()
            }

        })


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
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

