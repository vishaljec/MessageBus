package com.android.messagebusexample

import com.android.messagebusexample.lib.Subscribe
import com.android.messagebusexample.lib.To
import org.slf4j.LoggerFactory

@Subscribe
class ActionCapture {

    private val LOGGER = LoggerFactory.getLogger(ActionCapture::class.java)

    @Subscribe(To(Messages.Destinations.SYSTEM_SHUT_DOWN))
    fun enforceSystemShutDown() {
        LOGGER.error("Action shut down received")
    }

}