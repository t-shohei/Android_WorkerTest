import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.workmanagertest.data.repositry.imple.DefaultTaskRepository
import com.example.workmanagertest.domain.periodictask.tasks.PeriodicTask
import com.example.workmanagertest.domain.periodictask.tasks.TestIntervalWork1
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class DefaultTaskRepositoryTest{

    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        pref = mockk(relaxed = true)
        editor = mockk(relaxed = true)
        every { pref.edit() } returns editor
        every { editor.commit() } returns true
    }

    @Test
    fun `getTasks returns empty list when json is null`() = runTest {
        every { pref.getString("key", null) } returns null

        val repo = DefaultTaskRepository(pref)
        val result = repo.getTasks("key")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTasks returns list when valid JSON and instances are created`() = runTest {
        val className = "com.example.workmanagertest.domain.periodictask.tasks.TestIntervalWork2"
        val json = Json.encodeToString(listOf(className))
        every { pref.getString("key", any()) } returns json

        val repo = DefaultTaskRepository(pref)
        val result = repo.getTasks("key")

        println(result)
        assertTrue(result.isNotEmpty())
        assertEquals(className, result[0]::class.qualifiedName)
    }

    @Test
    fun `putTasks stores JSON`() = runTest {
        val task = TestIntervalWork1()
        val repo = DefaultTaskRepository(pref)

        repo.putTasks("key", listOf(task))

        val captured = slot<String>()
        verify { editor.putString(eq("key"), capture(captured)) }

        // JSON内容を確認
        val decoded = Json.decodeFromString<List<String>>(captured.captured)
        assertTrue(decoded.contains("com.example.workmanagertest.domain.periodictask.tasks.TestIntervalWork1"))
    }

    @Test
    fun `deleteTask removes key`() = runTest {
        val repo = DefaultTaskRepository(pref)
        repo.deleteTask("key")
        verify { editor.remove("key") }
    }

    @Test
    fun `deleteAllTasks removes all keys`() = runTest {
        every { pref.all } returns mapOf("a" to "x", "b" to "y")

        val repo = DefaultTaskRepository(pref)
        repo.deleteAllTasks()

        verify { editor.remove("a") }
        verify { editor.remove("b") }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
