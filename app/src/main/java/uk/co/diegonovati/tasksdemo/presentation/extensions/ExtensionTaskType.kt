package uk.co.diegonovati.tasksdemo.presentation.extensions

import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType

fun TaskType.toResId(): Int =
    when (this) {
        TaskType.General -> R.drawable.type_general
        TaskType.Hydration -> R.drawable.type_hydration
        TaskType.Medication -> R.drawable.type_medication
        TaskType.Nutrition -> R.drawable.type_nutrition
    }
