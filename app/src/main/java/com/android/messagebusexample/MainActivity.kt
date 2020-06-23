package com.android.messagebusexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.messagebusexample.lib.Message
import com.android.messagebusexample.lib.MessageBus
import com.android.messagebusexample.lib.MessageBusException
import com.android.messagebusexample.lib.MessageListener
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

    override fun onStart() {
        super.onStart()
        messageBus.registerListener(Messages.Destinations.SYSTEM_SHUT_DOWN, main)
    }

    override fun onStop() {
        super.onStop()
        messageBus.unregisterListener(Messages.Destinations.SYSTEM_SHUT_DOWN, main)
    }

    @Throws(MessageBusException::class)
    fun enterIntoShutDown() {
        messageBus.sendMessage(Messages.Destinations.SYSTEM_SHUT_DOWN);
    }

    object main : MessageListener {
        override fun receive(message: Message?) {
            if (message!!.isSameDestination(Messages.Destinations.SYSTEM_SHUT_DOWN)) {
                System.out.println("Action received")
            }
        }
    }


}