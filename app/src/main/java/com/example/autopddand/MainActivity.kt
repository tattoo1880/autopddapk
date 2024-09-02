package com.example.autopddand

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RecentTaskInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.provider.Settings
import android.text.TextUtils


class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private val initialDelay = 5000L // 初始延迟5秒后启动拼多多应用
    private val launchInterval = 30000L // 每30秒后重新启动拼多多应用
    private val closeInterval = 20000L // 启动拼多多应用后20秒关闭应用

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo使用adb 命令启动pinduoduo
        val intent = Intent()
        intent.setClassName("com.xunmeng.pinduoduo", "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity")
        startActivity(intent)
        //todo等待10s
        Thread.sleep(10000)
        //todo使用adb 命令关闭pinduoduo
//        closePinduoduoApp()
        restartPinduoduoApp()

        //!跳转到无障碍服务
//        openAccessibilitySettings()
//        Thread.sleep(20000)
//        //!检测是否开启了无障碍服务
//        val whehter = isAccessibilitySettingsOn()
//        if (whehter) {
//            println("无障碍服务已开启")
//        } else {
//            println("无障碍服务未开启")
//        }
//
//        Thread.sleep(100000)
//        handler.postDelayed({
//            launchPinduoduoApp()
//        }, initialDelay)
    }

//        handler.postDelayed({
//                launchPinduoduoApp()
//            }, initialDelay)
//    }
    private fun isAccessibilitySettingsOn(): Boolean {
        val service = packageName + "/" + AutoCloseService::class.java.canonicalName
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        var isEnabled = 0
        try {
            isEnabled = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        if (isEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            colonSplitter.setString(settingValue)
            while (colonSplitter.hasNext()) {
                if (colonSplitter.next().equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    private fun launchPinduoduoApp() {
        val pddPackageName = "com.xunmeng.pinduoduo"
        val intent = packageManager.getLaunchIntentForPackage(pddPackageName)
        if (intent != null) {
            startActivity(intent)

            // 在启动拼多多应用后20秒尝试关闭拼多多应用
            handler.postDelayed({
                closePinduoduoApp()
            }, closeInterval)

            // 安排30秒后再次启动拼多多应用
            handler.postDelayed({
                launchPinduoduoApp()
            }, launchInterval)
        } else {
            // 如果拼多多没有安装，则打开其应用商店页面
            val marketIntent = Intent(Intent.ACTION_VIEW)
            marketIntent.data = Uri.parse("market://details?id=$pddPackageName")
            startActivity(marketIntent)
        }
    }

    fun closePinduoduoApp() {
        val command = "adb shell am force-stop com.xunmeng.pinduoduo"
        executeAdbCommand(command)
    }

    fun restartPinduoduoApp() {
        // 关闭拼多多应用

//        循环10次每次间隔5s
        for (i in 1..10) {
            val stopCommand = "am force-stop com.xunmeng.pinduoduo"
            executeAdbCommand(stopCommand)

            // 启动拼多多应用
            val startCommand = "monkey -p com.xunmeng.pinduoduo -c android.intent.category.LAUNCHER 1"
            executeAdbCommand(startCommand)
            Thread.sleep(5000)
        }

    }


    fun executeAdbCommand(command: String): String? {
        return try {
            // 使用 su -c 以 root 权限执行命令
            val process = Runtime.getRuntime().exec("su -c $command")

            // 读取命令执行结果
            val reader = process.inputStream.bufferedReader()
            val output = reader.readText()
            reader.close()

            // 等待命令执行完成
            process.waitFor()
            output
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清除所有未执行的任务，防止内存泄漏
        handler.removeCallbacksAndMessages(null)
    }


    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}
