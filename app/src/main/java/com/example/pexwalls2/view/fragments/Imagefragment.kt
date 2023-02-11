package com.example.pexwalls2.view.fragments

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.pexwalls2.R
import com.example.pexwalls2.databinding.FragmentImagefragmentBinding
import com.google.android.material.appbar.AppBarLayout
import java.io.File
import java.io.FileOutputStream


class ImageFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentImagefragmentBinding
    private lateinit var url:String
    private lateinit var id:String
    private val permissionName = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val permissionCode = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagefragmentBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener(this)
        binding.textSetWallpaper.setOnClickListener(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this).load(url)
            .into(binding.imageview)
        requireActivity().findViewById<AppBarLayout>(R.id.main_appbar).visibility=View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            url=arguments?.getString("url")!!
            id=arguments?.getString("id")!!
        }
    }

    companion object {
        fun getInstance(url: String, id: String): ImageFragment {
            val fragment = ImageFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("id", id)
            fragment.arguments = bundle
            return fragment
        }
    }

     fun saveImage() {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/PexWalls")
        if (!directory.exists()) directory.mkdir()
        val name = "$id.jpg"
        val file = File(directory, name)
        if (file.exists()) {
            Toast.makeText(activity, "Image Already Exists", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val stream = FileOutputStream(file)
            Bitmap.createBitmap(binding.imageview.drawable.toBitmap())
                .compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Toast.makeText(activity, "Image Saved Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.d("ahmed", "saveImage: ")
        }
    }

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        requireActivity(),
        permissionName
    ) == PackageManager.PERMISSION_GRANTED

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fab.id -> {
                if (isPermissionGranted()) {
                    saveImage()
                } else {
                    val permissions = arrayOf(permissionName)
                    ActivityCompat.requestPermissions(requireActivity(),permissions,permissionCode)
                }
            }
            binding.textSetWallpaper.id ->{
                setWallpaper()
            }
        }
    }

    private fun setWallpaper(){
        val wallpaperManager=WallpaperManager.getInstance(requireContext())
        if(wallpaperManager.isSetWallpaperAllowed){
            try {
                wallpaperManager.setBitmap(binding.imageview.drawable.toBitmap())
                Toast.makeText(activity, "Wallpaper Set Successfully", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(activity, "Sorry Can't Set Wallpaper", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<AppBarLayout>(R.id.main_appbar).visibility=View.VISIBLE
    }

}