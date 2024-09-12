package com.example.autopddand

import android.app.Activity
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
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.jetbrains.annotations.TestOnly
import java.io.File

//log
import android.util.Log
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {

    val TAG = "AutoPddAnd"

    private val handler = Handler(Looper.getMainLooper())
    private val initialDelay = 5000L // 初始延迟5秒后启动拼多多应用
    private val launchInterval = 30000L // 每30秒后重新启动拼多多应用
    private val closeInterval = 20000L // 启动拼多多应用后20秒关闭应用

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //todo使用adb 命令启动pinduoduo
//        val intent = Intent()
//        intent.setClassName("com.xunmeng.pinduoduo", "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity")
//        startActivity(intent)


        restartPinduoduoApp()






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
            var seeyoucount = 0
            //!当seeyoucount小于20时，循环执行
            while (seeyoucount < 20) {

                //! 获取本机设备号
                val devicesId = getDevicesId()
                Log.d(TAG, "devicesId:$devicesId")
                val stopCommand = "am force-stop com.xunmeng.pinduoduo"
                executeAdbCommand(stopCommand)

                //!协程进行GetSms.getcount
                CoroutineScope(Dispatchers.IO).launch {
                    val getSms = GetSms()
                    seeyoucount = getSms.getCount(devicesId)
                    Log.d(TAG, "count:$seeyoucount")
                }

                Log.d(TAG, "stop pdd app")

                // 启动拼多多应用
                val startCommand =
                    "monkey -p com.xunmeng.pinduoduo -c android.intent.category.LAUNCHER 1"
                executeAdbCommand(startCommand)
//            Thread.sleep(5*60*1000)
                Thread.sleep(60 * 1000)

            }

            //!24小时
            Thread.sleep(24 * 60 * 60 * 1000)
        }

    }

//    fun regsms(){
//        val startCommand = "monkey -p com.xunmeng.pinduoduo -c android.intent.category.LAUNCHER 1"
//        executeAdbCommand(startCommand)
//
//        Thread.sleep(10000)
//
//        val tapcm1 = "input tap 1265 2250"
//        executeAdbCommand(tapcm1)
//
//        Thread.sleep(3000)
//
//        //!todo 使用adb命令页面下翻到底
//        val swipeCommand = "input swipe 500 1500 500 500"
//        executeAdbCommand(swipeCommand)
//
//        Thread.sleep(3000)
//
//        //！todo 页面上翻到顶
//        val swipeCommand2 = "input swipe 500 500 500 1500"
//        executeAdbCommand(swipeCommand2)
//
//        Thread.sleep(3000)
//        val swipeCommand3 = "input swipe 500 500 500 1500"
//        executeAdbCommand(swipeCommand3)
//
//        Thread.sleep(3000)
//
//        //使用adb 点击“设置”按钮
//        val tapcomd2 = "input tap 1358 150"
//        executeAdbCommand(tapcomd2)
//
//        val swipeCommand4 = "input swipe 500 1500 500 500"
//
//        for (i in 1..10) {
//            executeAdbCommand(swipeCommand4)
//            Thread.sleep(1000)
//        }
//
//        val quit = "input tap 750 2080"
//        executeAdbCommand(quit)
//
//        val quitconfirm ="input tap 1050 1410"
//        executeAdbCommand(quitconfirm)
//
//        var login = "input tap 780 1500"
//        executeAdbCommand(login)
//
//        var login2 = "input tap 893 1888"
//        executeAdbCommand(login2)
//
////+16592860240
//        Thread.sleep(13000)
//        val input = "input text 6592860240"
//        executeAdbCommand(input)
//
//        val confirmsendsms = "input tap 770 835"
//        executeAdbCommand(confirmsendsms)
//    }
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


    fun dazhongdianping(){
        //!todo 使用adb 打开微信
        val startcommand = "am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI"
        executeAdbCommand(startcommand)
        Thread.sleep(5000)
        //todo x=750 y=850 x2 =750 y2 =2000 拖动
        val swipeCommand = "input swipe 750 850 750 2000"
        executeAdbCommand(swipeCommand)

        //todo 点击"大众点评"
        val tapcommand = "input tap 222 800"
        executeAdbCommand(tapcommand)

        Thread.sleep(5000)
        val tapinputcommond = "input tap 500 168"
        executeAdbCommand(tapinputcommond)
        executeAdbCommand(tapinputcommond)

        Thread.sleep(5000)
        //!从231 361 输入文本
        val input231361command = "input tap 231 361"
        executeAdbCommand(input231361command)
        //! 输入文本:上海老弄堂
        Thread.sleep(2000)
        //!将上海老弄堂变成ascii码

        // 需要输入的中文文本
        val text = "南京市踏浪网咖大厂店"

        // 通过 adb 命令直接输入中文
        val c1 = "ime enable com.android.adbkeyboard/.AdbIME"
        executeAdbCommand(c1)
        val c2 = "ime set com.android.adbkeyboard/.AdbIME"
        executeAdbCommand(c2)
        val inputTextCommand = "am broadcast -a ADB_INPUT_TEXT --es msg '$text'"
        executeAdbCommand(inputTextCommand)


        Thread.sleep(1000)
        //adb keyboard的回车
        val c3 = "input tap 1325 458"
        executeAdbCommand(c3)

        Thread.sleep(2000)
        val c4 = "input tap 1000 1100"
        executeAdbCommand(c4)
    }


    fun getDevicesId():String{
        val webView = WebView(this)
        val userAgent = webView.settings.userAgentString
        Log.d("UserAgent", "User-Agent: $userAgent")
        return userAgent
    }


}
