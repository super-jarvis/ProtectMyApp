package io.jarvis.pma.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * 使用无障碍服务监听当前运行在前端的程序
 */
class AccessibilityMonitorService : AccessibilityService() {
    private var mWindowClassName: CharSequence? = null
    private var mCurrentPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val type = event.eventType
        when (type) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                mWindowClassName = event.className
                mCurrentPackage =
                    if (event.packageName == null) "" else event.packageName.toString()
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED, AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {}
            else -> {}
        }
    }

    override fun onInterrupt() {
        //todo 重启无障碍服务
    }
}
