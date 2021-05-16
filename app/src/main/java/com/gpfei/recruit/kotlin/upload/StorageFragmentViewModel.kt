package com.gpfei.recruit.kotlin.upload

import androidx.lifecycle.ViewModel
import com.gpfei.recruit.kotlin.model.FileBean

class StorageFragmentViewModel : ViewModel() {

    var path: String? = null

    val storageList = ArrayList<FileBean>()
}