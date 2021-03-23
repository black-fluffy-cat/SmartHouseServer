import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import utils.coroutines.ICoroutineScopeProvider
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestCoroutineScopeProvider : ICoroutineScopeProvider {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    override fun getMain(): TestCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)
    override fun getIO(): TestCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun getIODispatcher(): CoroutineContext = testCoroutineDispatcher
}