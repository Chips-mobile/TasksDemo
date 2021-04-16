package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.domain.repositories.IChangeStateLister
import uk.co.diegonovati.tasksdemo.domain.repositories.IConnectivityMonitorRepository

class UseCaseConnectivitySetChangeListener(
    private val connectivityMonitorRepository: IConnectivityMonitorRepository,
): UseCase<Unit, IChangeStateLister>() {
    override suspend fun run(params: IChangeStateLister): Result<Unit, Exception> {
        connectivityMonitorRepository.setChangeStateLister(params)
        return Result.Success(Unit)
    }
}