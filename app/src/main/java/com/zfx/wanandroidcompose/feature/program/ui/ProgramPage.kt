package com.zfx.wanandroidcompose.feature.program.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.feature.program.ProgramViewModel
import com.zfx.wanandroidcompose.ui.components.RefreshableLazyList

/**
 * author : zhufeixiang
 * date : 2026/1/25
 * des :
 */

@Composable
fun ProgramPage(
    cid : Int,
    isActive : Boolean,
    viewModel: ProgramViewModel,
    navController: NavController,
    onToggleBars: (Boolean) -> Unit = {},
    nestedScrollConnection: NestedScrollConnection? = null
){


    LaunchedEffect(cid,isActive) {
        if (isActive){
            if (viewModel.cid != cid){
                viewModel.setCid(id = cid)
                viewModel.refresh()
            }
        }
    }

    val programList by viewModel.programList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()

    RefreshableLazyList(
        items = programList,
        modifier = Modifier.fillMaxSize(),
        isLoading = isLoadingMore,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        onLoadMore = { viewModel.loadMore() },
        hasMore = hasMore
    ) { article ->
        ProgramItem( article)
    }





}