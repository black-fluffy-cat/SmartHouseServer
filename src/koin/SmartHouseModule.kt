package com.jj.smarthouseserver.koin

import com.jj.smarthouseserver.cameraData.ImageSaver
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import com.jj.smarthouseserver.managers.NodeDataProcessor
import com.jj.smarthouseserver.managers.RaspberryCallManager
import com.jj.smarthouseserver.monitoring.Monitoring
import org.koin.dsl.module
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val smartHouseModule = module {
    single<Logger> { LoggerFactory.getLogger("MainLogger") }
    single { RaspberryCallManager(get()) }
    single { NgrokAddressesProcessor(get()) }
    single { NodeDataProcessor(get()) }
    single { Monitoring(get()) }
    single { ImageSaver(get()) }
}