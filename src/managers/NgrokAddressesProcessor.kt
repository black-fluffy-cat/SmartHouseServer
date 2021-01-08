package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.utils.LogSaver
import com.jj.smarthouseserver.data.NgrokAddressesCallData
import org.slf4j.Logger

class NgrokAddressesProcessor(private val logger: Logger) {

    companion object {
        private const val NGROK_TAG = "NgrokAddressesProcessor"
    }

    fun process(ngrokAddressesCallData: NgrokAddressesCallData) {
        ngrokAddressesCallData.let { data ->
            val logMessage = "received Ngrok addresses from id: ${data.senderId}"
            logger.info(logMessage)
            LogSaver.saveNgrokLog(NGROK_TAG, logMessage)
            data.tunnelsList?.forEachIndexed { index, ngrokAddress ->
                val logUrlMessage =
                    "$index. name: ${ngrokAddress?.name} publicUrl: ${ngrokAddress?.publicUrl} destination: ${ngrokAddress?.addr}"
                logger.info(logUrlMessage)
                LogSaver.saveNgrokLog(NGROK_TAG, logUrlMessage)
            }
        }
    }
}