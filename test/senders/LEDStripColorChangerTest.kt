package senders

import TestCoroutineScopeProvider
import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.managers.AlertState
import com.jj.smarthouseserver.managers.AlertStateManager
import com.jj.smarthouseserver.senders.LEDStripColorChanger
import com.nhaarman.mockitokotlin2.times
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
class LEDStripColorChangerTest {

    @Mock
    private lateinit var pingCreator: PingCreator

    @Mock
    private lateinit var alertStateManager: AlertStateManager

    private lateinit var ledStripColorChanger: LEDStripColorChanger

    private val testScopeProvider = TestCoroutineScopeProvider()

    private lateinit var alertStateFlow: MutableStateFlow<AlertState>

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        alertStateFlow = MutableStateFlow(AlertState(false))
        whenever(alertStateManager.observeAlertState()).thenReturn(alertStateFlow)

        ledStripColorChanger = LEDStripColorChanger(pingCreator, alertStateManager, testScopeProvider)
    }

    @Test
    fun `initial false alert state should make get call to proper endpoint`(): Unit = runBlocking {
        verify(pingCreator).get(endsWith("/green"))
    }

    @Test
    fun `changing alert state to true should make get call to proper endpoint`(): Unit = runBlocking {
        alertStateFlow.value = AlertState(true)
        verify(pingCreator).get(endsWith("/red"))
    }

    @Test
    fun `setting red color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setRedColor()
        verify(pingCreator).get(endsWith("/red"))
    }

    @Test
    fun `setting green color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setGreenColor()
        // Two times, because initial false state also invokes call to /green endpoint
        verify(pingCreator, times(2)).get(endsWith("/green"))
    }

    @Test
    fun `setting blue color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setBlueColor()
        verify(pingCreator).get(endsWith("/blue"))
    }

    @Test
    fun `setting yellow color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setYellowColor()
        verify(pingCreator).get(endsWith("/yellow"))
    }

    @Test
    fun `setting white color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setWhiteColor()
        verify(pingCreator).get(endsWith("/white"))
    }

    @Test
    fun `setting purple color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setPurpleColor()
        verify(pingCreator).get(endsWith("/purple"))
    }

    @Test
    fun `setting cyan color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setCyanColor()
        verify(pingCreator).get(endsWith("/cyan"))
    }

    @Test
    fun `setting rainbow color should make call to proper endpoint`(): Unit = runBlocking {
        ledStripColorChanger.setRainbowColor()
        verify(pingCreator).get(endsWith("/rainbow"))
    }
}