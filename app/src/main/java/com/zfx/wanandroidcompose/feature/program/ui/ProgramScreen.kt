package com.zfx.wanandroidcompose.feature.program.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.feature.program.ProgramViewModel
import kotlinx.coroutines.launch

/**
 * author : zhufeixiang
 * date : 2026/1/25
 * des :
 */

@Composable
fun ProgramScreen(
    viewModel: ProgramViewModel = viewModel(),
    navController: NavController,
    onToggleBars: (Boolean) -> Unit = {},
    nestedScrollConnection: NestedScrollConnection? = null
){

    val programTree by viewModel.programTree.collectAsState()
    val tabData = remember(programTree) {
        programTree.map {
            it.name
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (tabData.isNotEmpty()){

             val pagerState = rememberPagerState(
                 initialPage = 0
             ) {
                 tabData.size
             }

            val scope = rememberCoroutineScope()

            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                edgePadding = 0.dp,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = pagerState.currentPage,
                            matchContentSize = true
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        height = 2.dp
                    )
                }
            ) {
                tabData.forEachIndexed { index, tabNme ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tabNme,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            )
                        }

                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                key = { page -> programTree[page].id }
            ) { page ->
                ProgramPage(
                    cid = programTree[page].id,
                    isActive = pagerState.currentPage == page,
                    viewModel = viewModel,
                    navController = navController,
                    onToggleBars = onToggleBars,
                    nestedScrollConnection = nestedScrollConnection
                )

            }
        }


    }
}