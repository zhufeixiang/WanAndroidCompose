package com.zfx.wanandroidcompose.feature.program

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.zfx.commonlib.base.viewmodel.BaseViewModel
import com.zfx.commonlib.ext.collectResult
import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.WeChatAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * author : zhufeixiang
 * date : 2026/1/25
 * des :
 */
class ProgramViewModel : BaseViewModel() {

    private val repository = ProgramRepository()

    private val _programTree = MutableStateFlow<List<WeChatAccount>>(emptyList())
    val programTree : StateFlow<List<WeChatAccount>> = _programTree.asStateFlow()



    init {
        getProgramTree()
    }

    private fun getProgramTree() {
        viewModelScope.launch {
            collectResult(
                flow = repository.getProgramTree(),
                onError = { error ->
                    ToastUtils.showShort(error.message)
                },
                onSuccess = { programList ->
                    _programTree.value = programList
                }
            )
        }
    }


}