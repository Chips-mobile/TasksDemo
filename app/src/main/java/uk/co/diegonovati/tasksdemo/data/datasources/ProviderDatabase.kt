package uk.co.diegonovati.tasksdemo.data.datasources

import android.content.Context
import androidx.room.*
import uk.co.diegonovati.tasksdemo.data.models.LastUpdate
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import java.util.*

@Dao
interface ITaskModelDao {
    @Query("SELECT * FROM TaskModel ORDER BY id")
    suspend fun list(): List<TaskModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(taskModel: TaskModel): Long

    @Query("DELETE FROM TaskModel")
    suspend fun clear()
}

@Dao
interface ILastUpdate {
    @Query("SELECT * FROM LastUpdate")
    suspend fun get(): LastUpdate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(lastUpdate: LastUpdate): Long
}

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(dateLong: Long): Date = Date(dateLong)
}

@Database(
    entities = [(TaskModel::class), (LastUpdate::class)],
    version = ProviderDatabase.databaseLatestVersion
)
@TypeConverters(DateConverter::class)
abstract class TasksDemoDatabase : RoomDatabase() {
    abstract fun taskModelDao(): ITaskModelDao
    abstract fun lastUpdateDao(): ILastUpdate
}

object ProviderDatabase {

    const val databaseLatestVersion = 1
    private const val databaseName = "TasksDemo"

    fun createDatabase(applicationContext: Context): TasksDemoDatabase =
        Room.databaseBuilder(applicationContext, TasksDemoDatabase::class.java, databaseName)
            .build()
}