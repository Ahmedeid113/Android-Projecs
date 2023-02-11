package com.example.pexwalls2.models

data class Imagemodel(val page:Int,val per_page:Int,val next_page:String,val photos:ArrayList<Image>)
data class Image(val id:Int,val src:Source,val url:String)
data class Source(val original:String,val portrait:String)
