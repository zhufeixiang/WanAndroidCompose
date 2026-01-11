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
import androidx.compose.ui.res.stringResource
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
    
    // 在 Composable 中获取颜色，用于 drawBehind
    val backgroundColor = MaterialTheme.colorScheme.onPrimary
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary
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
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentDescription = stringResource(R.string.search_back_button)
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
                                text = stringResource(R.string.search_placeholder),
                                color = MaterialTheme.colorScheme.onTertiary
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
                                    contentDescription = stringResource(R.string.search_icon),
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        },
                        trailingIcon = {
                            if (query.isNotBlank()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.search_clear),
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }
                            }
                        },
                        // 自定义输入框内部颜色
                        colors = SearchBarDefaults.inputFieldColors(
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary, // 聚焦时输入框背景色（白色）
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary, // 未聚焦时输入框背景色（白色）
                            focusedTextColor = MaterialTheme.colorScheme.surface, // 聚焦时文本颜色
                            unfocusedTextColor = MaterialTheme.colorScheme.surface, // 未聚焦时文本颜色
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onTertiary, // 聚焦时占位符颜色
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onTertiary, // 未聚焦时占位符颜色
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onTertiary, // 聚焦时前导图标颜色
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onTertiary, // 未聚焦时前导图标颜色
                            focusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary, // 聚焦时后置图标颜色
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary // 未聚焦时后置图标颜色
                        )
                    )
                },
                expanded = active,
                onExpandedChange = { active = it },
                // 自定义 SearchBar 外观
                shape = RoundedCornerShape(20.dp), // 圆角（可根据需要调整，如 16.dp, 24.dp 等）
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onPrimary // SearchBar 容器背景色
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 0.dp) // 自定义 SearchBar 外部 padding（可根据需要调整）
                    .drawBehind {
                        // 绘制矩形覆盖下划线区域，去除下划线
                        // 注意：drawBehind 中不能使用 @Composable 函数，需要在外部获取颜色
                        drawRect(
                            color = backgroundColor,  // 使用外部获取的颜色
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
                        text = stringResource(R.string.search_history),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                    TextButton(onClick = onClearHistory) {
                        Text(stringResource(R.string.search_clear_history), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
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
                        text = stringResource(R.string.search_hot),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )

                    Image(
                        painter = painterResource(R.drawable.icon_hot_white),
                        modifier = Modifier.size(16.dp), // 设置图标大小
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.error),
                        contentDescription = stringResource(R.string.search_hot_icon)
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
                        text = stringResource(R.string.search_history),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                    TextButton(onClick = onClearHistory) {
                        Text(stringResource(R.string.search_clear_history), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
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
                    color = MaterialTheme.colorScheme.surface
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
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else if (results.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.search_no_results),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
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
                val articleToast = stringResource(R.string.article_favorite, article.title)
                ArticleItem(
                    data = article,
                    favoriteClick = {
                        ToastUtils.showShort(articleToast)
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
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        } else {
            MaterialTheme.colorScheme.tertiary
        },
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontSize = 14.sp,
            color = if (isHot) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    }
}

