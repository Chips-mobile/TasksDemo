package uk.co.diegonovati.tasksdemo.data.datasources

import android.content.Context
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel

interface ILocalDataSource {
    suspend fun load(): Result<TaskDataModel, Exception>
    suspend fun save(taskDataModel: TaskDataModel): Result<Unit, Exception>
}

class LocalDataSourceSharedPreferences(
    private val context: Context
) : ILocalDataSource {

    private val sharedPreferenceName = "TasksDemoPersistence"
    private val sharedPreferenceKey = "TaskDataModelKey"

    override suspend fun load(): Result<TaskDataModel, Exception> {
        context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
            .getString(sharedPreferenceKey, null)?.let {
            return try {
                Result.success(Gson().fromJson(it, TaskDataModel::class.java))
            } catch (e: Exception) {
                Result.error(e)
            }
        }
        return Result.error(Exception())
    }

    override suspend fun save(taskDataModel: TaskDataModel): Result<Unit, Exception> =
        try {
            val json = Gson().toJson(taskDataModel)
            context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE).edit()
                .putString(sharedPreferenceKey, json).apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }

    fun clear(): Result<Unit, Exception> =
        try {
            context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE).edit()
                .remove(sharedPreferenceKey).apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
}