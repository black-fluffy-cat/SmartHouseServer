package io.disc

import com.jj.smarthouseserver.io.disc.DiscSpaceMonitor
import com.jj.smarthouseserver.io.disc.FileCreator
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.File

class DiscSpaceMonitorTest {

    @Mock
    private lateinit var rootDirFile: File

    @Mock
    private lateinit var videosDirFile: File

    @Mock
    private lateinit var fileCreator: FileCreator

    private lateinit var discSpaceMonitor: DiscSpaceMonitor

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        discSpaceMonitor = DiscSpaceMonitor(fileCreator)
    }

    @Test
    fun `should call listFiles if disc usage is above 80 percent`() {
        val totalDiscSpace = 100L
        val usedDiscSpace = 80 + 1L
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        discSpaceMonitor.onDiscOperationPerformed()
        verify(videosDirFile).listFiles()
    }

    @Test
    fun `should call listFiles if disc usage is equal to 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        discSpaceMonitor.onDiscOperationPerformed()
        verify(videosDirFile).listFiles()
    }

    @Test
    fun `should not call listFiles if disc usage is less than 80 percent`() {
        val totalDiscSpace = 100L
        val usedDiscSpace = 80L - 1
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        discSpaceMonitor.onDiscOperationPerformed()
        verify(videosDirFile, never()).listFiles()
    }

    @Test
    fun `should delete ten video recordings if disc usage is above 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L + 1
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        repeat(10) { i ->
            val mockFile = mockFiles[i]
            verify(mockFile).delete()
        }
    }

    @Test
    fun `should not delete eleventh video recording if disc usage is above 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L + 1
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        val mockFile = mockFiles[10]
        verify(mockFile, never()).delete()
    }

    @Test
    fun `should delete ten video recordings if disc usage is equal to 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        repeat(10) { i ->
            val mockFile = mockFiles[i]
            verify(mockFile).delete()
        }
    }

    @Test
    fun `should not delete eleventh video recording if disc usage is equal to 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        val mockFile = mockFiles[10]
        verify(mockFile, never()).delete()
    }

    @Test
    fun `should not delete video recordings if disc usage is less than 80 percent`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L - 1
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        repeat(11) { i ->
            val mockFile = mockFiles[i]
            verify(mockFile, never()).delete()
        }
    }

    @Test
    fun `should check when video recordings were modified before deleting them`() {
        val totalDiscSpace = 1000L
        val usedDiscSpace = 800L + 1
        setupFileCreator(totalDiscSpace, usedDiscSpace)
        val mockFiles = createMockFiles()
        whenever(videosDirFile.listFiles()).thenReturn(mockFiles)
        discSpaceMonitor.onDiscOperationPerformed()

        for (mockFile in mockFiles) {
            verify(mockFile, atLeastOnce()).lastModified()
        }
    }

    private fun createMockFiles(): Array<File> {
        val mockFiles = arrayListOf<File>()
        repeat(11) {
            val mockFile = mock(File::class.java)
            mockFiles.add(mockFile)
        }
        return mockFiles.toTypedArray()
    }

    private fun setupFileCreator(totalDiscSpace: Long, usedDiscSpace: Long) {
        whenever(rootDirFile.totalSpace).thenReturn(totalDiscSpace)
        whenever(rootDirFile.usableSpace).thenReturn(totalDiscSpace - usedDiscSpace)
        whenever(fileCreator.createFile(DiscSpaceMonitor.ROOT_DIR_PATH)).thenReturn(rootDirFile)
        whenever(fileCreator.createFile(DiscSpaceMonitor.VIDEOS_DIR_PATH)).thenReturn(videosDirFile)
    }
}