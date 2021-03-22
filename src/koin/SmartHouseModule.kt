package com.jj.smarthouseserver.koin

import com.jj.smarthouseserver.io.disc.FileSaver
import com.jj.smarthouseserver.houseSystemState.HouseSystemStateManager
import com.jj.smarthouseserver.io.network.NetworkPing
import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.managers.AlertStateManager
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import com.jj.smarthouseserver.managers.NodeDataProcessor
import com.jj.smarthouseserver.managers.RaspberryCallManager
import com.jj.smarthouseserver.monitoring.Monitoring
import com.jj.smarthouseserver.senders.AlertVisualizerController
import com.jj.smarthouseserver.senders.LEDStripColorChanger
import org.koin.dsl.module
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val smartHouseModule = module {
    single<Logger> { LoggerFactory.getLogger("MainLogger") }
    single { RaspberryCallManager(get(), get()) }
    single { NgrokAddressesProcessor(get()) }
    single { NodeDataProcessor(get(), get(), get(), get()) }
    single { Monitoring(get()) }
    single { FileSaver(get()) }
    single { HouseSystemStateManager() }

    single { LEDStripColorChanger(get()) }
    single { AlertStateManager(get(), get()) }
    single { AlertVisualizerController(get()) }

    single<PingCreator> { NetworkPing() }
}