package uk.co.diegonovati.tasksdemo.data.datasources

import com.github.kittinunf.result.Result
import retrofit2.Retrofit
import retrofit2.http.GET
import uk.co.diegonovati.tasksdemo.data.models.TaskModel

interface IRetrofitRemote {
    @GET("/tasks.json")
    suspend fun list(): List<TaskModel>
}

interface IRemoteDataSource {
    suspend fun list(): Result<List<TaskModel>, Exception>
}

class RemoteDataSource(
    retrofit: Retrofit,
): IRemoteDataSource {

    private val retrofitRemote = retrofit.create(IRetrofitRemote::class.java)

    override suspend fun list(): Result<List<TaskModel>, Exception> =
        try {
            Result.success(retrofitRemote.list())
        } catch (e: Exception) {
            Result.error(e)
        }
}