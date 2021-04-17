package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.domain.repositories.IConnectivityMonitorRepository

open class UseCaseConnectivityMonitorStop(
    private val connectivityMonitorRepository: IConnectivityMonitorRepository,
): UseCase<Unit, UseCase.None>() {
    override suspend fun run(params: None): Result<Unit, Exception> {
        connectivityMonitorRepository.stopConnectivityMonitor()
        return Result.success(Unit)
    }
}