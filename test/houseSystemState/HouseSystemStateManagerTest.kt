package houseSystemState

import com.jj.smarthouseserver.houseSystemState.DataSample
import com.jj.smarthouseserver.houseSystemState.HouseSystemStateManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HouseSystemStateManagerTest {

    private lateinit var houseSystemStateManager: HouseSystemStateManager

    private val deviceName = "deviceName"

    @BeforeEach
    fun setup() {
        houseSystemStateManager = HouseSystemStateManager()
    }

    @Test
    fun `after initialization manager should return empty temperatures map`() {
        val houseState = houseSystemStateManager.getHouseSystemState()
        assertEquals(0, houseState.temperatureState.temperatures.size)
    }

    @Test
    fun `first temperature sample should exist in temperatures after adding`() {
        val dataSample = DataSample(deviceName, "1", "C")

        houseSystemStateManager.updateTemperatureState(dataSample)
        val readSample = houseSystemStateManager.getHouseSystemState().temperatureState.temperatures[deviceName]

        assertEquals(dataSample, readSample)
    }

    @Test
    fun `temperatures map should have size equal to 1 after adding first sample`() {
        val dataSample = DataSample(deviceName, "1", "C")

        houseSystemStateManager.updateTemperatureState(dataSample)
        val houseState = houseSystemStateManager.getHouseSystemState()

        assertEquals(1, houseState.temperatureState.temperatures.size)
    }

    @Test
    fun `second temperature sample should exist in temperatures instead of first one after adding`() {
        val dataSample = DataSample(deviceName, "1", "C")
        val secondDataSample = DataSample(deviceName, "230", "C")

        houseSystemStateManager.updateTemperatureState(dataSample)
        houseSystemStateManager.updateTemperatureState(secondDataSample)
        val readSample = houseSystemStateManager.getHouseSystemState().temperatureState.temperatures[deviceName]

        assertEquals(secondDataSample, readSample)
    }

    @Test
    fun `temperatures map should have size equal to 1 after adding second sample from same device`() {
        val dataSample = DataSample(deviceName, "1", "C")
        val secondDataSample = DataSample(deviceName, "120", "C")

        houseSystemStateManager.updateTemperatureState(dataSample)
        houseSystemStateManager.updateTemperatureState(secondDataSample)
        val houseState = houseSystemStateManager.getHouseSystemState()

        assertEquals(1, houseState.temperatureState.temperatures.size)
    }

    @Test
    fun `temperatures map should have size equal to 2 after adding samples from different devices`() {
        val secondDeviceName = "secondDeviceName"
        val dataSample = DataSample(deviceName, "1", "C")
        val secondDataSample = DataSample(secondDeviceName, "120", "C")

        houseSystemStateManager.updateTemperatureState(dataSample)
        houseSystemStateManager.updateTemperatureState(secondDataSample)
        val houseState = houseSystemStateManager.getHouseSystemState()

        assertEquals(2, houseState.temperatureState.temperatures.size)
    }

    @Test
    fun `first and second sample should exist in temperatures if they came from different devices`() {
        val secondDeviceName = "secondDeviceName"
        val dataSample = DataSample(deviceName, "1", "C")
        val secondDataSample = DataSample(secondDeviceName, "120", "C")

        with(houseSystemStateManager) {
            updateTemperatureState(dataSample)
            updateTemperatureState(secondDataSample)
            val readSample = getHouseSystemState().temperatureState.temperatures[deviceName]
            val secondReadSample = getHouseSystemState().temperatureState.temperatures[secondDeviceName]

            assertEquals(dataSample, readSample)
            assertEquals(secondDataSample, secondReadSample)
        }
    }

    @Test
    fun `updating sample of second device should not affect sample of first device`() {
        val secondDeviceName = "secondDeviceName"
        val dataSample = DataSample(deviceName, "1", "C")
        val secondDataSample = DataSample(secondDeviceName, "120", "C")
        val updatedSecondDataSample = DataSample(secondDeviceName, "320", "C")

        with(houseSystemStateManager) {
            updateTemperatureState(dataSample)
            updateTemperatureState(secondDataSample)
            updateTemperatureState(updatedSecondDataSample)
            val readSample = getHouseSystemState().temperatureState.temperatures[deviceName]
            val secondReadSample = getHouseSystemState().temperatureState.temperatures[secondDeviceName]

            assertEquals(dataSample, readSample)
            assertEquals(updatedSecondDataSample, secondReadSample)
        }
    }
}