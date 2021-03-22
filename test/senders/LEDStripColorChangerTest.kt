package senders

import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.senders.AlertVisualizerController
import com.jj.smarthouseserver.senders.LEDStripColorChanger
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LEDStripColorChangerTest {

    @Mock
    private lateinit var pingCreator: PingCreator

    @Captor
    private lateinit var urlCaptor: ArgumentCaptor<String>

    private lateinit var ledStripColorChanger: LEDStripColorChanger

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        ledStripColorChanger = LEDStripColorChanger(pingCreator)
    }

    @Test
    fun `setting red color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setRedColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/red"))
    }

    @Test
    fun `setting green color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setGreenColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/green"))
    }

    @Test
    fun `setting blue color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setBlueColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/blue"))
    }

    @Test
    fun `setting yellow color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setYellowColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/yellow"))
    }

    @Test
    fun `setting white color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setWhiteColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/white"))
    }

    @Test
    fun `setting purple color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setPurpleColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/purple"))
    }

    @Test
    fun `setting cyan color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setCyanColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/cyan"))
    }

    @Test
    fun `setting rainbow color should make call to proper endpoint`() = runBlocking {
        ledStripColorChanger.setRainbowColor()
        verify(pingCreator).get(capture(urlCaptor))
        Assertions.assertTrue(urlCaptor.value.endsWith("/rainbow"))
    }
}