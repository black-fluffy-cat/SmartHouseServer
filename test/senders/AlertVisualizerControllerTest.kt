package senders

import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.senders.AlertVisualizerController
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AlertVisualizerControllerTest {

    @Mock
    private lateinit var pingCreator: PingCreator

    @Captor
    private lateinit var urlCaptor: ArgumentCaptor<String>

    private lateinit var alertVisualizerController: AlertVisualizerController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        alertVisualizerController = AlertVisualizerController(pingCreator)
    }

    @Test
    fun `turning visualizer on should make get call to proper endpoint`() = runBlocking {
        alertVisualizerController.turnVisualizerOn()
        verify(pingCreator).get(capture(urlCaptor))
        assertTrue(urlCaptor.value.endsWith("/alert"))
    }

    @Test
    fun `turning visualizer off should make get call to proper endpoint`() = runBlocking {
        alertVisualizerController.turnVisualizerOff()
        verify(pingCreator).get(capture(urlCaptor))
        assertTrue(urlCaptor.value.endsWith("/alertOff"))
    }
}