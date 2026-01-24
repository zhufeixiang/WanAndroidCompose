package com.zfx.wanandroidcompose.feature.program.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.feature.program.ProgramViewModel

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

}