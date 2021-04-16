package uk.co.diegonovati.tasksdemo.data.datasources

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus
import uk.co.diegonovati.tasksdemo.domain.repositories.IChangeStateLister

interface IConnectivityMonitorDataSource {
    fun setChangeStateLister(changeStateLister: IChangeStateLister)
    fun startMonitoring()
    fun stopMonitoring()
}

class ConnectivityMonitorDataSource(
    private val context: Context,
): IConnectivityMonitorDataSource {

    private var monitorStarted: Boolean = false
    private var changeStateLister: IChangeStateLister? = null

    override fun setChangeStateLister(changeStateLister: IChangeStateLister) {
        this.changeStateLister = changeStateLister
    }

    override fun startMonitoring() {
        if (monitorStarted) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(
                builder.build(), object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        changeStateLister?.stateChanges(ConnectionStatus.Connected)
                    }
                    override fun onLost(network: Network) {
                        changeStateLister?.stateChanges(ConnectionStatus.Disconnected)
                    }
                }
            )
            monitorStarted = true
        }
    }

    override fun stopMonitoring() {
        if (! monitorStarted) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
            monitorStarted = false
        }
    }
}