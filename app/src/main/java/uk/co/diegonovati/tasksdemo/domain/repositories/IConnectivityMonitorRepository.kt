package uk.co.diegonovati.tasksdemo.domain.repositories

import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus

interface IChangeStateLister {
    fun stateChanges(connectionStatus: ConnectionStatus)
}

interface IConnectivityMonitorRepository {
    fun setChangeStateLister(changeStateLister: IChangeStateLister)
    fun startConnectivityMonitor()
    fun stopConnectivityMonitor()
}