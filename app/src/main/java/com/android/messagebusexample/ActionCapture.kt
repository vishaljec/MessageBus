package com.android.messagebusexample

import com.android.messagebusexample.lib.MessageBus
import com.android.messagebusexample.lib.Subscribe
import com.android.messagebusexample.lib.To
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(MessageBus::class.java)

@Subscribe(To(Messages.Destinations.SYSTEM_SHUT_DOWN))
fun enforceSystemShutDown() {
    LOGGER.error("Action shut down received")
}