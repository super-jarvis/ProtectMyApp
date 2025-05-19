package io.jarvis.pma.viewModel

import io.jarvis.pma.viewModel.mvi.BaseViewModel
import io.jarvis.pma.viewModel.mvi.IUiIntent
import io.jarvis.pma.viewModel.mvi.IUiState

object SettingViewModel : BaseViewModel<SettingViewState, SettingIntent>() {
    override fun initialState() = SettingViewState.IDLE

    override suspend fun handleEvent(event: IUiIntent) {
    }
}

sealed class SettingIntent : IUiIntent {
    data class UpdateMqtt(
        val enable: Boolean,/// 是否启用
        val url: String,///  mqtt地址
        val useToken: Boolean?,///  是否使用token
        val userName: String?,///  用户名
        val password: String?///  密码
    ) : SettingIntent()

    data class UpdateWebHook(
        val enable: Boolean,/// 是否启用
        val url: String?,///  webhook地址
    ) : SettingIntent()
}

sealed class SettingViewState : IUiState {
    object IDLE : SettingViewState()
}