package uk.co.diegonovati.tasksdemo.data.datasources

import com.github.kittinunf.result.Result
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoteDataSourceTest {

    private val retrofit = ProviderRetrofit().getRetrofit()

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSource(retrofit)
    }

    @Test
    fun testList() = runBlocking {
        val actual = remoteDataSource.list()

        assertTrue(actual is Result.Success)
        assertTrue(actual.get().isNotEmpty())
    }
}