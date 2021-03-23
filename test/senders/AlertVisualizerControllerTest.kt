package senders

import TestCoroutineScopeProvider
import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.managers.AlertState
import com.jj.smarthouseserver.managers.AlertStateManager
import com.jj.smarthouseserver.senders.AlertVisualizerController
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.endsWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AlertVisualizerControllerTest {

    @Mock
    private lateinit var pingCreator: PingCreator

    @Mock
    private lateinit var alertStateManager: AlertStateManager

    private lateinit var alertVisualizerController: AlertVisualizerController

    private val testScopeProvider = TestCoroutineScopeProvider()

    private lateinit var alertStateFlow: MutableStateFlow<AlertState>

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        alertStateFlow = MutableStateFlow(AlertState(false))
        whenever(alertStateManager.observeAlertState()).thenReturn(alertStateFlow)

        alertVisualizerController = AlertVisualizerController(pingCreator, alertStateManager, testScopeProvider)
    }

    @Test
    fun `initial false alert state should make get call to proper endpoint`(): Unit = runBlocking {
        verify(pingCreator).get(endsWith("/alertOff"))
    }

    @Test
    fun `changing alert state to true should make get call to proper endpoint`(): Unit = runBlocking {
        alertStateFlow.value = AlertState(true)
        verify(pingCreator).get(endsWith("/alert"))
    }
}