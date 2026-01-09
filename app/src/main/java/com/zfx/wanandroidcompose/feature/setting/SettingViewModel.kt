package com.zfx.wanandroidcompose.feature.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.zfx.wanandroidcompose.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: SettingRepository
) : ViewModel() {

    private var _curTheme = MutableStateFlow<AppTheme>(AppTheme.AUTO)
    val curTheme: StateFlow<AppTheme> = _curTheme.asStateFlow()

    init {
        // 监听 DataStore 的变化，Flow 会在订阅时立即发出当前值，无需单独 loadTheme()
        viewModelScope.launch {
            repository.themeFlow.collect { themeName ->
                val theme = if (themeName == null) {
                    AppTheme.AUTO
                } else {
                    try {
                        AppTheme.valueOf(themeName)
                    } catch (e: IllegalArgumentException) {
                        AppTheme.AUTO
                    }
                }
                _curTheme.value = theme
            }
        }
    }

    fun saveTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.saveTheme(theme)
            // 立即更新状态，确保 UI 立即响应
            _curTheme.value = theme
        }
    }
    
    fun setTheme(theme: AppTheme) {
        saveTheme(theme)
    }
}

/**
 * ViewModel Factory，用于创建 SettingViewModel
 */
class SettingViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(SettingRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * 在 Composable 中获取 SettingViewModel 的辅助函数
 */
@Composable
fun settingViewModel(): SettingViewModel {
    val context = LocalContext.current
    return viewModel(
        factory = SettingViewModelFactory(context)
    )
}