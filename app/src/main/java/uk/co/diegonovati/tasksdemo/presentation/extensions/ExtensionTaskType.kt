package uk.co.diegonovati.tasksdemo.presentation.extensions

import android.content.Context
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType

fun TaskType.toResId(): Int =
    when (this) {
        TaskType.General -> R.drawable.type_general
        TaskType.Hydration -> R.drawable.type_hydration
        TaskType.Medication -> R.drawable.type_medication
        TaskType.Nutrition -> R.drawable.type_nutrition
    }

fun TaskType.toContentDescription(context: Context): String {
    val resId = when (this) {
        TaskType.General -> R.string.taskTypeGeneral
        TaskType.Hydration -> R.string.taskTypeHydration
        TaskType.Medication -> R.string.taskTypeMedication
        TaskType.Nutrition -> R.string.taskTypeNutrition
    }
    return context.getString(resId)
}