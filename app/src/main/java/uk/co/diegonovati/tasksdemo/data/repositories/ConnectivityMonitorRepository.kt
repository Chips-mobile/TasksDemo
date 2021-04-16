package uk.co.diegonovati.tasksdemo.data.repositories

import uk.co.diegonovati.tasksdemo.data.datasources.IConnectivityMonitorDataSource
import uk.co.diegonovati.tasksdemo.domain.repositories.IChangeStateLister
import uk.co.diegonovati.tasksdemo.domain.repositories.IConnectivityMonitorRepository

class ConnectivityMonitorRepository(
    private val connectivityMonitorDataSource: IConnectivityMonitorDataSource
): IConnectivityMonitorRepository {

    override fun setChangeStateLister(changeStateLister: IChangeStateLister) {
        connectivityMonitorDataSource.setChangeStateLister(changeStateLister)
    }

    override fun startConnectivityMonitor() {
        connectivityMonitorDataSource.startMonitoring()
    }

    override fun stopConnectivityMonitor() {
        connectivityMonitorDataSource.startMonitoring()
    }
}