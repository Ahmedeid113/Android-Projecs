package com.example.pexwalls2.view.fragments

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.pexwalls2.R
import com.example.pexwalls2.databinding.FragmentMainImagesBinding
import com.example.pexwalls2.view.activities.MainActivity
import com.example.pexwalls2.view.adapters.ImageClickListener
import com.example.pexwalls2.view.adapters.ImagesAdapter
import com.example.pexwalls2.viewmodel.MainFragmentViewModel
import kotlinx.coroutines.*



class MainImagesFragment : Fragment(),View.OnClickListener{
    private lateinit var binding: FragmentMainImagesBinding
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:ImagesAdapter
    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var listener:ImageClickListener
    private lateinit var title:TextView
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var isTitleShown=true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as MainActivity

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainImagesBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(this)[MainFragmentViewModel::class.java]
        recycler=binding.mainRecycle
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()
        if(viewModel.data.value.isEmpty()){
            viewModel.fetchAllImages().invokeOnCompletion {
                GlobalScope.launch {
                    viewModel.data.collect{
                        withContext(Dispatchers.Main){
                            adapter=ImagesAdapter(it,listener,requireActivity())
                            recycler.setHasFixedSize(true)
                            recycler.adapter=adapter
                            binding.loadingProgress.visibility=View.GONE
                        }
                    }
                }
            }
        }
        else{
            adapter=ImagesAdapter(viewModel.data.value,listener,requireActivity())
            recycler.setHasFixedSize(true)
            recycler.adapter=adapter
            binding.loadingProgress.visibility=View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        title=requireActivity().findViewById(R.id.main_title)
        autoCompleteTextView=requireActivity().findViewById(R.id.main_autoComplete)
        requireActivity().findViewById<ImageButton>(R.id.search_button).setOnClickListener(this)

    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_button -> {
                if(isTitleShown){
                    title.visibility=View.GONE
                    autoCompleteTextView.visibility=View.VISIBLE
                    autoCompleteTextView.layoutParams=LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,10f)
                    isTitleShown=false
                    return
                }
                if(autoCompleteTextView.text.isNotEmpty()){
                    viewModel.searchImages(autoCompleteTextView.text.toString()).invokeOnCompletion {
                        GlobalScope.launch {
                            viewModel.data.collect{
                                withContext(Dispatchers.Main){
                                    adapter=ImagesAdapter(it,listener,requireActivity())
                                    recycler.adapter=adapter
                                    recycler.setHasFixedSize(true)
                                }.apply {
                                    withContext(Dispatchers.Main){
                                        autoCompleteTextView.text.clear()
                                        title.visibility=View.VISIBLE
                                        autoCompleteTextView.visibility=View.INVISIBLE
                                        autoCompleteTextView.layoutParams=LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,7f)
                                        isTitleShown=true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}