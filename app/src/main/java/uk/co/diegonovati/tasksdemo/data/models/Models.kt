package uk.co.diegonovati.tasksdemo.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity
data class TaskModel(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val type: String,
)

@Keep
data class TaskDataModel(
    val lastUpdate: Date,
    val taskList: List<TaskModel>,
)

@Keep
@Entity
data class LastUpdate(
    @PrimaryKey val id: Long,
    val lastUpdate: Date,
)
