package pers.vaccae.servicedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import pers.vaccae.servicedemo.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val srvintent = Intent(this, MyService::class.java)
        srvintent.action = "MY_SERVICE"
        startForegroundService(srvintent)

        LiveEventBus.get(MESSAGE_RET,String::class.java)
            .observe(this) { t ->
                t?.let {
                    binding.tvtest.text = it
                }
            }


        binding.tvtest.setOnClickListener {
//            val broadcast = Intent()
//            broadcast.action = "MESSAGE_ACTION"
//            broadcast.putExtra("MESSAGE_ID","我点击了tvtest组件")
//            sendOrderedBroadcast(broadcast,null)
            LiveEventBus.get<String>(MESSAGE_TYPE)
                .post("我点击了tvtest组件")
        }
    }

}