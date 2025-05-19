package io.jarvis.pma.viewModel.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * mvi 简化版
 */
abstract class BaseViewModel<UiState : IUiState, uiIntent : IUiIntent> : ViewModel() {

    private val initialState: UiState by lazy { initialState() }

    private val _uiState: MutableStateFlow<UiState> by lazy { MutableStateFlow(initialState) }

    /** 对外暴露需要改变ui的控制 */
    val uiState: StateFlow<UiState> by lazy { _uiState }

    // 使用Channel创建数据流, Channel是消费者模式的, 保证了请求的正确性
    private val _uiEvent: Channel<IUiIntent> = Channel()
    private val uiEvent: Flow<IUiIntent> = _uiEvent.receiveAsFlow()

    init {
        // 初始化
        viewModelScope.launch {
            uiEvent.collect {// flow.collect 接受数据
                handleEvent(it)
            }
        }
    }

    /**
     * 配置响应数据, 表示接受到数据后需要更新ui
     */
    protected abstract fun initialState(): UiState

    /**
     * 处理响应
     */
    protected abstract suspend fun handleEvent(event: IUiIntent)

    /**
     * 通知数据流改变状态
     */
    protected fun sendState(state: UiState) {
        _uiState.update { state }
    }

    /**
     * 发送事件, 外部调用
     */
    fun sendIntent(uiIntent: IUiIntent) {
        viewModelScope.launch {
            _uiEvent.send(uiIntent)
        }
    }
}