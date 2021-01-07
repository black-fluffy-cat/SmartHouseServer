package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.data.NgrokAddressesCallData
import org.slf4j.Logger

class NgrokAddressesProcessor(val logger: Logger) {
    fun process(ngrokAddressesCallData: NgrokAddressesCallData) {
        ngrokAddressesCallData.let { data ->
            logger.info("received Ngrok addresses from id: ${data.senderId}")
            data.tunnelsList?.forEachIndexed { index, ngrokAddress ->
                logger.info("$index. name: ${ngrokAddress?.name} publicUrl: ${ngrokAddress?.publicUrl} destination: ${ngrokAddress?.addr}")
            }
        }
    }
}