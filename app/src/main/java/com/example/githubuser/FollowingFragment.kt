package com.example.githubuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.FragmentFollowingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment(username1 : String? = null) : Fragment() {

    companion object{
        private const val TAG = "FollowingFragment"
    }
    private var Username : String ? = username1
    private  var _binding : FragmentFollowingBinding? = null
    private val Binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Binding.pengumumanFollowing.visibility = View.GONE
        val client = ApiConfig.getApiService().getFollowing(Username.toString())
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                lifecycleScope.launch(Dispatchers.Default){
                    loading(true)
                    delay(500)
                    withContext(Dispatchers.Main){
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val manager = LinearLayoutManager(context)
                                Binding.rvFollowing.setLayoutManager(manager)
                                Binding.rvFollowing.setHasFixedSize(true)
                                var adapter = adapter(requireActivity())
                                adapter.submitList(responseBody.subList(0, responseBody.lastIndex + 1))
                                Binding.rvFollowing.adapter = adapter
                                loading(false)
                            }
                            if(responseBody!!.isEmpty()){
                                Binding.pengumumanFollowing.visibility = View.VISIBLE
                                loading(false)
                            }
                        } else {
                            Log.e(FollowingFragment.TAG, "onFailure: ${response.message()}")
                            loading(false)
                        }
                    }
                    }
                }


            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(FollowingFragment.TAG, "onFailure: ${t.message}")
                loading(false)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater,container,false)
        return Binding.root
    }

    private fun loading (isloading : Boolean){
        Binding.progressbarfollowing.visibility = if (isloading) View.VISIBLE else View.GONE
    }
}