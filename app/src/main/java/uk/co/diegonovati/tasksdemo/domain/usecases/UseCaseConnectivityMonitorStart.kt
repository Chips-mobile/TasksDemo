package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.domain.repositories.IConnectivityMonitorRepository

class UseCaseConnectivityMonitorStart(
    private val connectivityMonitorRepository: IConnectivityMonitorRepository,
): UseCase<Unit, UseCase.None>() {

    override suspend fun run(params: None): Result<Unit, Exception> {
        connectivityMonitorRepository.startConnectivityMonitor()
        return Result.success(Unit)
    }
}