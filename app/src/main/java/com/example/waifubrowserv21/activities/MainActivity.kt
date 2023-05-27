package com.example.waifubrowserv21.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.waifubrowserv2.models.ImageObj
import com.example.waifubrowserv2.models.WaifuIm
import com.example.waifubrowserv21.Adapter.CategoryListAdapter
import com.example.waifubrowserv21.Adapter.MultipleWaifuViewAdapter
import com.example.waifubrowserv21.R
import com.example.waifubrowserv21.databinding.ActivityMainBinding
import com.example.waifubrowserv21.utils.AppFunctions
import com.example.waifubrowserv21.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isImageTypeNSFW : Boolean = false
    private val imageList : ArrayList<ImageObj> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProgressDialog()
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbarText.text = "Waifu"

        setupCategoryRecyclerView(Constants.SFW_LINK_CATEGORY)

        val rgImageType = binding.navView.getHeaderView(0)
            .findViewById<RadioGroup>(R.id.rg_image_type)
        val refreshBtn = binding.appBarMain.contentMain.btnRefresh

        rgImageType.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rb_sfw){
                isImageTypeNSFW = false
                setupCategoryRecyclerView(Constants.SFW_LINK_CATEGORY)

            }else{
                isImageTypeNSFW = true
                setupCategoryRecyclerView(Constants.NSFW_LINK_CATEGORY)
            }
        }

        refreshBtn.setOnClickListener {
            if(AppFunctions().isInternetAvailable(this)){
                showWaifuView()
            }else{
                hideWaifuView()
            }
        }

        if(AppFunctions().isInternetAvailable(this)){
            showWaifuView()
        } else{
            hideWaifuView()
        }

    }

    private fun showWaifuView() {
        binding.appBarMain.contentMain.tvNoInternet.visibility = View.GONE
        binding.appBarMain.contentMain.btnRefresh.visibility = View.GONE
        binding.appBarMain.contentMain.rvWaifuImages.visibility = View.VISIBLE
        getMultipleWaifuImages("waifu")
    }

    private fun hideWaifuView(){
        binding.appBarMain.contentMain.tvNoInternet.visibility = View.VISIBLE
        binding.appBarMain.toolbarText.text = ":/"
        binding.appBarMain.contentMain.btnRefresh.visibility = View.VISIBLE
        binding.appBarMain.contentMain.rvWaifuImages.visibility = View.GONE
        hideProgressDialog()
    }


    /**
     * Get multiple waifu images and setup the adapter for the corresponding recycler view
     */
    fun getMultipleWaifuImages(tag: String){
        if(AppFunctions().isInternetAvailable(this)){
            val client = OkHttpClient()
            val link = if(isImageTypeNSFW) Constants.MULTIPLE_WAIFU_NSFW_LINK +tag
            else Constants.MULTIPLE_WAIFU_SFW_LINK +tag

            val request = Request.Builder()
                .url(link)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Response", "Network request failed", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        Log.e(
                            "Response",
                            "Network request was not successful: ${response.code()}"
                        )
                        return
                    }

                    val gson = Gson()
                    val data = gson.fromJson(response.body()?.charStream(), WaifuIm::class.java)
                    Log.e("response", data.toString())

                    for(image in data.images){
                        if(image.byte_size < Constants.IDEAL_IMAGE_SIZE){
                            imageList.add(image)
                        }
                    }
                    setupWaifuRecyclerView(imageList, tag)
                }
            })
        }else{
            hideProgressDialog()
        }

    }

    private fun setupWaifuRecyclerView(imageList: List<ImageObj>, category: String) {
        runOnUiThread {
            val adapter = object : MultipleWaifuViewAdapter(this, imageList, category) {
                override fun loadMoreWaifu() {
                    getMultipleWaifuImages(category)
                }
            }

            val rvWaifuImage : RecyclerView =  binding.appBarMain.contentMain.rvWaifuImages
            if(rvWaifuImage.adapter != null){
                rvWaifuImage.adapter?.notifyItemRangeChanged(20, 20)
                hideProgressDialog()
                return@runOnUiThread
            }
            rvWaifuImage.layoutManager = StaggeredGridLayoutManager(
                2, LinearLayoutManager.VERTICAL)
            rvWaifuImage.adapter = adapter
            adapter.setOnClickListener(object : MultipleWaifuViewAdapter.OnClickListener{
                override fun onClick(position: Int, model: ImageObj) {
                    val intent = Intent(this@MainActivity, WaifuViewActivity::class.java)
                    intent.putExtra(Constants.IMAGE_URL, model.url)
                    intent.putExtra(Constants.IMAGE_ID, model.image_id)
                    startActivity(intent)
                }
            })
            hideProgressDialog()
        }
    }

    private fun setupCategoryRecyclerView(categoryList: Array<String>) {
        val adapter = CategoryListAdapter(categoryList, this)
        binding.navView.getHeaderView(0)
            .findViewById<RecyclerView>(R.id.rv_category_list).adapter = adapter
        adapter.setOnClickListener(object : CategoryListAdapter.OnClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(category: String) {
                if(!AppFunctions().isInternetAvailable(this@MainActivity)){
                    hideWaifuView()
                    binding.drawerLayout.closeDrawers()
                    return
                }
                showProgressDialog()
                imageList.clear()
                if(binding.appBarMain.contentMain.rvWaifuImages.visibility == View.GONE){
                    binding.appBarMain.contentMain.tvNoInternet.visibility = View.GONE
                    binding.appBarMain.contentMain.btnRefresh.visibility = View.GONE
                    binding.appBarMain.contentMain.rvWaifuImages.visibility = View.VISIBLE
                }
                binding.appBarMain.contentMain.rvWaifuImages.adapter = null
                getMultipleWaifuImages(category)
                binding.drawerLayout.closeDrawers()
                binding.appBarMain.toolbarText.text = category.substring(0, 1).toUpperCase() + category.substring(1)
            }
        })
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

}