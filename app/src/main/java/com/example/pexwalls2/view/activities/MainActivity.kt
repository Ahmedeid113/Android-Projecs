package com.example.pexwalls2.view.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pexwalls2.R
import com.example.pexwalls2.databinding.ActivityMainBinding
import com.example.pexwalls2.view.adapters.ImageClickListener
import com.example.pexwalls2.view.fragments.ImageFragment

class MainActivity : AppCompatActivity(),ImageClickListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var fragmentManager:FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
       fragmentManager=supportFragmentManager


        val adapter = ArrayAdapter(
            this,
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.autocomplete_hints)
        )
        autoCompleteTextView = binding.mainAutoComplete
        autoCompleteTextView.setAdapter(adapter)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            100->{
                if(permissions.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    val fragment=supportFragmentManager.findFragmentById(binding.mainContainer.id) as ImageFragment
                    fragment.saveImage()
                    //Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "please grant permission", Toast.LENGTH_SHORT).show()
                    val intent=Intent(Settings.ACTION_APPLICATION_SETTINGS)
                    intent.data= Uri.fromParts("package",packageName,null)
                    startActivity(intent)

                }
            }
        }
    }

    override fun onImageClicked(imageSrc: String, id: String) {
        fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.mainContainer.id,ImageFragment.getInstance(imageSrc,id))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}