package com.jj.smarthouseserver.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import utils.coroutines.ICoroutineScopeProvider
import kotlin.coroutines.CoroutineContext

class CoroutineScopeProvider : ICoroutineScopeProvider {

    private val main: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private val io: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun getMain(): CoroutineScope = main
    override fun getIO(): CoroutineScope = io

    override fun getIODispatcher(): CoroutineContext = Dispatchers.IO
}