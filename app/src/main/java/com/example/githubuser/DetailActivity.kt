package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.database.User
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        private const val TAG = "DetailActivity"
    }
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailuserviewmodel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawable = resources.getDrawable(R.drawable.heart_plus, theme)
        val drawable2 = resources.getDrawable(R.drawable.heart_minus, theme)
        detailuserviewmodel = obtainViewModel(this@DetailActivity)

        val nama : String = intent.getStringExtra("nama").toString()
        val avatar : String = intent.getStringExtra("avatar").toString()

        detailuserviewmodel.searchuser(nama).observe(this){
            if (it == null)
            {
                binding.favoritebutton.setImageDrawable(drawable)
                binding.favoritebutton.setOnClickListener {
                    val user = User(nama,avatar)
                    detailuserviewmodel.insert(user)
                    Toast.makeText(this,R.string.toast,Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.favoritebutton.setImageDrawable(drawable2)
                binding.favoritebutton.setOnClickListener {
                    val user = User(nama,avatar)
                    detailuserviewmodel.delete(user)
                    Toast.makeText(this,R.string.toast2,Toast.LENGTH_SHORT).show()
                }
            }
        }


        val client = ApiConfig.getApiService().getDetailUser(nama)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                lifecycleScope.launch(Dispatchers.Default){
                    loading(true)
                    delay(500)
                    withContext(Dispatchers.Main){
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                binding.detailNama.text = responseBody.name
                                binding.detailUsername.text = responseBody.login
                                binding.detailFollowers.text = "${responseBody.followers.toString()} Followers"
                                binding.detailFollowing.text = "${responseBody.following.toString()} Following"
                                Glide.with(this@DetailActivity)
                                    .load(avatar)
                                    .into(binding.detailImage)
                                loading(false)
                            }
                        }
                        else {
                            Log.e(DetailActivity.TAG, "onFailure: ${response.message()}")
                            loading(false)
                        }
                    }

                }

            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
                loading(false)
            }

        })

        val sectionPagerAdapter = SectionPagerAdapter(this,intent.getStringExtra("nama").toString())
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }

    private fun loading (isloading : Boolean){
        binding.progressbardetail.visibility = if (isloading) View.VISIBLE else View.GONE
    }

}