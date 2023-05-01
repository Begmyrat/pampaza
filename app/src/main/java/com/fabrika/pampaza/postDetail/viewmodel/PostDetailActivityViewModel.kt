package com.fabrika.pampaza.postDetail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fabrika.pampaza.common.utils.BaseViewModel

class PostDetailActivityViewModel(application: Application) : BaseViewModel(application) {
    var likeCount = MutableLiveData<Long>()
}