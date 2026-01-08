package com.zfx.wanandroidcompose.feature.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.feature.home.ui.ArticleItem
import com.zfx.wanandroidcompose.feature.search.SearchViewModel
import com.zfx.wanandroidcompose.navigation.Routes
import com.blankj.utilcode.util.ToastUtils
import com.zfx.wanandroidcompose.data.HotKey

/**
 * Material3 搜索页面
 * 
 * 使用 SearchBar 组件实现完整的搜索功能：
 * - 搜索栏（支持展开/收起）
 * - 搜索历史
 * - 搜索结果列表
 * - 热门搜索推荐
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val hotSearch by viewModel.hotSearch.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // SearchBar 的状态
    var active by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    
    // 控制是否显示搜索结果
    val showResults = query.isNotBlank() && searchResults.isNotEmpty()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(
                    color = colorResource(R.color.theme)
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(
                Modifier.size(24.dp)
            )

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.popBackStack() },
                painter = painterResource(R.drawable.icon_back_white),
                contentDescription = "返回按钮"
            )
            Spacer(
                Modifier.size(12.dp)
            )

            // SearchBar 组件（使用新 API，自定义外观）
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { newQuery ->
                            query = newQuery
                            if (newQuery.isNotBlank()) {
                                viewModel.search(newQuery)
                            }
                        },
                        onSearch = { searchQuery ->
                            if (searchQuery.isNotBlank()) {
                                viewModel.search(searchQuery)
                                viewModel.addToHistory(searchQuery)
                            }
                        },
                        expanded = active,
                        onExpandedChange = { active = it },
                        placeholder = { 
                            Text(
                                text = "搜索文章、作者...",
                                color = colorResource(R.color.color_FF8A8A8A)
                            ) 
                        },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    if (active) {
                                        active = false
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "搜索",
                                    tint = colorResource(R.color.color_FF8A8A8A)
                                )
                            }
                        },
                        trailingIcon = {
                            if (query.isNotBlank()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "清除",
                                        tint = colorResource(R.color.color_FF8A8A8A)
                                    )
                                }
                            }
                        },
                        // 自定义输入框内部颜色
                        colors = SearchBarDefaults.inputFieldColors(
                            focusedContainerColor = colorResource(R.color.white), // 聚焦时输入框背景色（白色）
                            unfocusedContainerColor = colorResource(R.color.white), // 未聚焦时输入框背景色（白色）
                            focusedTextColor = colorResource(R.color.black), // 聚焦时文本颜色
                            unfocusedTextColor = colorResource(R.color.black), // 未聚焦时文本颜色
                            focusedPlaceholderColor = colorResource(R.color.color_FF8A8A8A), // 聚焦时占位符颜色
                            unfocusedPlaceholderColor = colorResource(R.color.color_FF8A8A8A), // 未聚焦时占位符颜色
                            focusedLeadingIconColor = colorResource(R.color.color_FF8A8A8A), // 聚焦时前导图标颜色
                            unfocusedLeadingIconColor = colorResource(R.color.color_FF8A8A8A), // 未聚焦时前导图标颜色
                            focusedTrailingIconColor = colorResource(R.color.color_FF8A8A8A), // 聚焦时后置图标颜色
                            unfocusedTrailingIconColor = colorResource(R.color.color_FF8A8A8A) // 未聚焦时后置图标颜色
                        )
                    )
                },
                expanded = active,
                onExpandedChange = { active = it },
                // 自定义 SearchBar 外观
                shape = RoundedCornerShape(20.dp), // 圆角（可根据需要调整，如 16.dp, 24.dp 等）
                colors = SearchBarDefaults.colors(
                    containerColor = colorResource(R.color.white) // SearchBar 容器背景色
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 0.dp) // 自定义 SearchBar 外部 padding（可根据需要调整）
                    .drawBehind {
                        // 绘制白色矩形覆盖下划线区域，去除下划线
                        // 注意：drawBehind 中不能使用 @Composable 函数，需要使用 Color.White
                        drawRect(
                            color = Color.White,
                            topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - 2.dp.toPx()),
                            size = androidx.compose.ui.geometry.Size(size.width, 2.dp.toPx())
                        )
                    }
            ) {
                // SearchBar 展开时显示的内容（搜索建议、历史记录等）
                SearchSuggestions(
                    searchHistory = searchHistory,
                    hotSearch = hotSearch,
                    onHistoryClick = { history ->
                        query = history
                        viewModel.search(history)
                        active = false
                    },
                    onHotSearchClick = { hot ->
                        query = hot.name
                        viewModel.search(hot.name)
                        viewModel.addToHistory(hot.name)
                        active = false
                    },
                    onClearHistory = { viewModel.clearHistory() }
                )
            }

            Spacer(
                Modifier.size(24.dp)
            )

        }

        
        // 搜索结果列表
        if (showResults) {
            SearchResultsList(
                results = searchResults,
                isLoading = isLoading,
                onArticleClick = { article ->
                    navController?.navigate(
                        Routes.buildLinkRoute(article.title, article.link)
                    )
                }
            )
        } else if (query.isBlank() && !active) {
            // 默认显示：搜索历史和热门搜索
            DefaultSearchContent(
                searchHistory = searchHistory,
                hotSearch = hotSearch,
                onHistoryClick = { history ->
                    query = history
                    viewModel.search(history)
                },
                onHotSearchClick = { hot ->
                    query = hot.name
                    viewModel.search(hot.name)
                    viewModel.addToHistory(hot.name)
                },
                onClearHistory = { viewModel.clearHistory() }
            )
        }
    }
}

/**
 * 搜索建议内容（SearchBar 展开时显示）
 */
@Composable
fun SearchSuggestions(
    searchHistory: List<String>,
    hotSearch: List<HotKey>,
    onHistoryClick: (String) -> Unit,
    onHotSearchClick: (HotKey) -> Unit,
    onClearHistory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 搜索历史
        if (searchHistory.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "搜索历史",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.black)
                    )
                    TextButton(onClick = onClearHistory) {
                        Text("清除", fontSize = 14.sp)
                    }
                }
            }
            
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    searchHistory.forEach { history ->
                        SearchChip(
                            text = history,
                            onClick = { onHistoryClick(history) }
                        )
                    }
                }
            }
        }
        
        // 热门搜索
        if (hotSearch.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "热门搜索",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.black)
                    )

                    Image(
                        painter = painterResource(R.drawable.icon_hot_white),
                        modifier = Modifier.size(16.dp), // 设置图标大小
                        colorFilter = ColorFilter.tint(color = colorResource(R.color.color_FFD81E06)),
                        contentDescription = "火热图标"
                    )
                }
            }
            
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    hotSearch.forEach { hot ->
                        SearchChip(
                            text = hot.name,
                            onClick = { onHotSearchClick(hot) },
                            isHot = true
                        )
                    }
                }
            }
        }
    }
}

/**
 * 默认搜索内容（未搜索时显示）
 */
@Composable
fun DefaultSearchContent(
    searchHistory: List<String>,
    hotSearch: List<HotKey>,
    onHistoryClick: (String) -> Unit,
    onHotSearchClick: (HotKey) -> Unit,
    onClearHistory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 搜索历史
        if (searchHistory.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "搜索历史",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.black)
                    )
                    TextButton(onClick = onClearHistory) {
                        Text("清除", fontSize = 14.sp)
                    }
                }
            }
            
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    searchHistory.forEach { history ->
                        SearchChip(
                            text = history,
                            onClick = { onHistoryClick(history) }
                        )
                    }
                }
            }
        }
        
        // 热门搜索
        if (hotSearch.isNotEmpty()) {
            item {
                Text(
                    text = "热门搜索",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.black)
                )
            }
            
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    hotSearch.forEach { hot ->
                        SearchChip(
                            text = hot.name,
                            onClick = { onHotSearchClick(hot) },
                            isHot = true
                        )
                    }
                }
            }
        }
    }
}

/**
 * 搜索结果列表
 */
@Composable
fun SearchResultsList(
    results: List<Article>,
    isLoading: Boolean,
    onArticleClick: (Article) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(R.color.theme)
            )
        }
    } else if (results.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无搜索结果",
                fontSize = 16.sp,
                color = colorResource(R.color.color_FF8A8A8A)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = results,
                key = { article -> "${article.id}_${article.publishTime}" }
            ) { article ->
                ArticleItem(
                    data = article,
                    favoriteClick = {
                        ToastUtils.showShort("收藏文章：${article.title}")
                    },
                    cardClick = { onArticleClick(article) }
                )
            }
        }
    }
}

/**
 * 搜索标签（Chip）
 */
@Composable
fun SearchChip(
    text: String,
    onClick: () -> Unit,
    isHot: Boolean = false
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isHot) {
            colorResource(R.color.theme).copy(alpha = 0.1f)
        } else {
            colorResource(R.color.color_FFF5F5F5)
        },
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontSize = 14.sp,
            color = if (isHot) {
                colorResource(R.color.theme)
            } else {
                colorResource(R.color.black)
            }
        )
    }
}

