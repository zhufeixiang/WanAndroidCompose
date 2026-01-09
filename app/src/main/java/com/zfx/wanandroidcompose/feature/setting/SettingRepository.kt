package com.zfx.wanandroidcompose.feature.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zfx.wanandroidcompose.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.settingDataStore : DataStore<Preferences> by preferencesDataStore(name = "setting_preferences")


object SettingPreferencesKeys{
    val THEME = stringPreferencesKey("theme")
    val LANGUGAE = stringPreferencesKey("language")
}


class SettingRepository(context: Context) {


    private val dataStore = context.settingDataStore

    val themeFlow : Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[SettingPreferencesKeys.THEME]
        }


    suspend fun saveTheme(theme : AppTheme){
        dataStore.edit { preferences ->
            // 保存枚举名称（如 "DARK", "LIGHT"），而不是 themeName（如 "深色主题"）
            // 因为 AppTheme.valueOf() 需要枚举名称
            preferences[SettingPreferencesKeys.THEME] = theme.name
        }
    }

    suspend fun getTheme() : AppTheme{
        return dataStore.data.first().let { preferences ->
            val themeName = preferences[SettingPreferencesKeys.THEME]
            if (themeName == null) {
                AppTheme.AUTO  // 如果没有保存的主题，返回默认值
            } else {
                try {
                    AppTheme.valueOf(themeName)  // 将字符串转换为枚举
                } catch (e: IllegalArgumentException) {
                    AppTheme.AUTO  // 如果值不存在（比如枚举值被删除），返回默认值
                }
            }
        }
    }



}