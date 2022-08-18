package pers.vaccae.testsrv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeremyliao.liveeventbus.LiveEventBus
import pers.vaccae.testsrv.databinding.ActivityMainBinding

const val MESSAGE_TYPE = "MESSAGE_TYPE"
const val MESSAGE_RET = "MESSAGE_RET"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LiveEventBus.get<String>(MESSAGE_RET)
            .observe(this) { t ->
                t?.let {
                    binding.tvmsg.text = it
                }
            }

        binding.tvmsg.setOnClickListener{
//            val broadcast = Intent()
//            broadcast.action = "MESSAGE_ACTION"
//            broadcast.putExtra("MESSAGE_ID","${packageName}中点击了tvmsg组件")
//            sendOrderedBroadcast(broadcast,null)
            LiveEventBus.get<String>(MESSAGE_TYPE)
                .postAcrossApp("${packageName}中点击了tvmsg组件")
        }
    }
}