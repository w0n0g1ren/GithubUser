package com.example.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.response.ResponseGithub2
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.tema.SettingPreferences
import com.example.githubuser.tema.TemaActivity
import com.example.githubuser.tema.TemaViewModel
import com.example.githubuser.tema.TemaViewModelFactory
import com.example.githubuser.tema.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val TAG = "MainActivity"
        private const val user = "arif"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settema()
        binding.pengumuman.visibility = View.GONE
        val layoutManager = LinearLayoutManager (this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    finduser(searchView.text.toString())
                    false
                }
        }
        finduser(user)

        binding.btntofavorite.setOnClickListener {
            val intent = Intent(this,FavoriteActivity::class.java)
            startActivity(intent)
        }
        binding.setpreference.setOnClickListener {
            val intent = Intent(this,TemaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun settema() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, TemaViewModelFactory(pref)).get(
            TemaViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun finduser(name : String? = "") {
            loading(true)
            val client = ApiConfig.getApiService().getUser(name.toString())
            client.enqueue(object : Callback<ResponseGithub2> {
                override fun onResponse(call: Call<ResponseGithub2>, response: Response<ResponseGithub2>) {
                    lifecycleScope.launch(Dispatchers.Default){
                        binding.progressbar.visibility = View.VISIBLE
                        delay(500)
                        withContext(Dispatchers.Main){
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    loading(false)
                                    setUserData(responseBody.items)

                                }
                                if (responseBody!!.totalCount == 0) {
                                    loading(false)
                                    binding.pengumuman.visibility = View.VISIBLE

                                }else{
                                    loading(false)
                                    binding.pengumuman.visibility = View.GONE
                                }

                            } else {
                                loading(false)
                                Log.e(MainActivity.TAG, "onFailure: ${response.message()}")

                            }
                        }
                    }
                }


                override fun onFailure(call: Call<ResponseGithub2>, t: Throwable) {
                    loading(false)
                    Log.e(MainActivity.TAG, "onFailure: ${t.message}")
                }

            })
    }



    private fun setUserData(consumerReviews: List<ItemsItem>) {
        val adapter = adapter(this)
        adapter.submitList(consumerReviews)
        binding.rvUser.adapter = adapter
    }

    private fun loading (isloading : Boolean){
        binding.progressbar.visibility = if (isloading) View.VISIBLE else View.GONE
    }



}
