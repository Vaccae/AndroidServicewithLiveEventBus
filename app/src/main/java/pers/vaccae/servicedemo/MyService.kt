package pers.vaccae.servicedemo

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus


const val TAG = "MyService"
const val MESSAGE_ACTION = "MESSAGE_ACTION"
const val MESSAGE_ID = "MESSAGE_ID"
const val MESSAGE_TYPE = "MESSAGE_TYPE"
const val MESSAGE_RET = "MESSAGE_RET"

class MyService : Service() {

    private lateinit var mMsgRecv: MessageReceiver

    private val observer = Observer<String>{
        try {
            showNotification(it)
            val retstr = "服务端接收到消息：${it}"
            LiveEventBus.get<String>(MESSAGE_RET)
                .postAcrossApp(retstr)
        }
        catch (e:Exception){
            showNotification(e.message.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service onCreate()")

        //注册广播
//        mMsgRecv = MessageReceiver()
//        val mFilter = IntentFilter()
//        mFilter.addAction(MESSAGE_ACTION)
//        registerReceiver(mMsgRecv, mFilter)
        InitLiveEventBus()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "service onStartCommand()")

        NotificationUtil.getInstance(this, MainActivity::class.java, packageName)

        val notification = NotificationUtil.mNotifiCationBuilder
            .setContentTitle("前台服务测试")
            .setContentText("我是一个前台服务的Demo")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentIntent(NotificationUtil.mPendingIntent)
            .build()
        // 开始前台服务
        startForeground(110, notification)
        NotificationUtil.mNotificationManager.notify(1, notification)
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "service onBind()")
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        Log.d(TAG, "service onDestroy")
        //停止前台服务
        stopForeground(true)
        //终止广播
        //unregisterReceiver(mMsgRecv)

        LiveEventBus
            .get<String>(MESSAGE_TYPE)
            .removeObserver(observer)

        super.onDestroy()

    }

    /**
     * 初始化LiveEventBus
     * 1、配置LifecycleObserver（如Activity）接收消息的模式（默认值true）：
     * true：整个生命周期（从onCreate到onDestroy）都可以实时收到消息
     * false：激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状
     * 态，方可收到消息
     * 2、autoClear
     * 配置在没有Observer关联的时候是否自动清除LiveEvent以释放内存（默认值false）
     * */
    fun InitLiveEventBus(){
        LiveEventBus.config()
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)

        LiveEventBus.get<String>(MESSAGE_TYPE)
            .observeForever(observer)
    }

    fun showNotification(msg:String,title:String="前台服务监听"){
        Log.d(TAG, msg)
        val notification = NotificationUtil.mNotifiCationBuilder
            .setContentTitle(title)
            .setContentText(msg)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentIntent(NotificationUtil.mPendingIntent)
            .setSound(null)
            .build()
        NotificationUtil.mNotificationManager.notify(1, notification)
    }
}