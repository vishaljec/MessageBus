package com.android.messagebusexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.messagebusexample.lib.MessageBus
import com.android.messagebusexample.lib.MessageBusException
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    lateinit var messageBus: MessageBus
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shut_down.setOnClickListener {
            enterIntoShutDown()
        }
        messageBus = MessageBus(Executors.newSingleThreadExecutor {
            Thread(it, "messagebus-async")
        })
    }

    @Throws(MessageBusException::class)
    fun enterIntoShutDown() {
        messageBus.sendMessageAsync(Messages.Destinations.SYSTEM_SHUT_DOWN);

    }


}