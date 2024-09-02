package com.example.autopddand

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoCloseService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 处理无障碍事件
    }

    override fun onInterrupt() {
        // 中断无障碍服务
    }

    fun closePinduoduoApp() {
        //点击home键
    }

    //!todo 通过无障碍服务关闭拼多多应用
    private fun closeApp() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskList = am.getRunningTasks(100)
        for (task in taskList) {
            if (task.topActivity!!.packageName == "com.xunmeng.pinduoduo") {
                am.killBackgroundProcesses("com.xunmeng.pinduoduo")
            }
        }
    }




}
