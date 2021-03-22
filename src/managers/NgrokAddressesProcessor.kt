package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.io.disc.LogSaver
import com.jj.smarthouseserver.data.NgrokAddressesCallData
import org.slf4j.Logger

class NgrokAddressesProcessor(private val logger: Logger) {

    companion object {
        private const val NGROK_TAG = "NgrokAddressesProcessor"
    }

    fun process(ngrokAddressesCallData: NgrokAddressesCallData) {
        val logMessage = "received Ngrok addresses from id: ${ngrokAddressesCallData.senderId}"
        printLogAndSave(logMessage)
        ngrokAddressesCallData.tunnelsList?.forEachIndexed { index, ngrokAddress ->
            ngrokAddress?.apply {
                val logUrlMessage = "$index. name: $name publicUrl: $publicUrl destination: $addr"
                printLogAndSave(logUrlMessage)
            }
        }
    }

    private fun printLogAndSave(message: String) {
        logger.info(message)
        LogSaver.saveNgrokLog(NGROK_TAG, message)
    }
}