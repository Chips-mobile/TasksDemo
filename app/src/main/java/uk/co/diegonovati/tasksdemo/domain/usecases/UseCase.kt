package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Result<Type, Exception>

    operator fun invoke(params: Params, onResult: (Result<Type, Exception>) -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = run(params)
            CoroutineScope(Dispatchers.Main).launch {
                onResult(result)
            }
        }
    }

    class None
}