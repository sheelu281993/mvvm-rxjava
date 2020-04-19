package com.example.halodoc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halodoc.R
import com.example.halodoc.base.BaseActivity
import com.example.halodoc.data.viewmodel.NewsViewModel
import com.example.halodoc.model.Hit
import com.example.halodoc.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.fixedRateTimer

class MainActivity : BaseActivity(), View.OnClickListener, androidx.appcompat.widget.SearchView.OnQueryTextListener, AdapterResponseListener {

    private val newsViewModel: NewsViewModel by viewModel()

    private lateinit var mNewsAdapter: SearchNewsAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var isLoading = false
    private var hasNext = true

    private var offset = 0

    private var currentQuery : String ?= null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 ) {
                //check for scroll down
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                //
                // total item count = 20
                // visible count = 5

                // 15 item scrolled

                //15 -current  index first visible item

                // 15 + 5 > =20

                if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading && hasNext) {
                    currentQuery?.let {
                        Toast.makeText(this@MainActivity, "fetching more!", Toast.LENGTH_SHORT).show()
                        getNewsData(it)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    override fun extractData() {}

    override fun initComponents() {
        layoutManager = LinearLayoutManager(this)
        mNewsAdapter = SearchNewsAdapter(this, this)
        rvNews.adapter = mNewsAdapter
        rvNews.layoutManager = layoutManager
        rvNews.addOnScrollListener(scrollListener)


        etSearch.setOnQueryTextListener(this)
    }

    private fun getNewsData(query: String){

        if(validateInput(query)) {
           if(Utils.isInternetConnected(this)) {
               if (isLoading) return
               isLoading = true
                //fetching api data
                showLoaders()
                observeNewsData()
                newsViewModel.getNewsData(query, offset.toString())
            } else Toast.makeText(this, "Check Your Network Connection", Toast.LENGTH_LONG).show()
        }
        else Toast.makeText(this, "Invalid Entries!", Toast.LENGTH_LONG).show()
    }

    private fun showLoaders(){
        //loading
    }

    private fun observeNewsData(){
        //observers to observe the changes from web api
        newsViewModel.observeNewsDataFromServer()?.observe(this,
                Observer {
                    //handle response ui
                    it?.hits?.run {

                        currentQuery?.run {
                            newsViewModel.setCache(this, offset.toString(), it)
                        }

                        isLoading = false

                        val totalPages = it.nbPages

                        hasNext = offset <= totalPages

                        if (hasNext) offset += 1

                        updateUI(this)
                    }

                    //else show error
                })

    }

    private fun updateUI(hits: List<Hit>){
        mNewsAdapter.updateList(hits, true)
    }

    private fun validateInput(query: String?): Boolean{
       return !query.isNullOrEmpty()
    }

    override fun navigateBack() {}

    override fun onClick(v: View?) {}

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            currentQuery = it
            getNewsData(it)
            mNewsAdapter.updateList(arrayListOf(), false)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    override fun onAdapterResponded(response: Any?) {
        response?.let {
            //url
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("URL", it as String)
            startActivity(intent)
        }
    }
}