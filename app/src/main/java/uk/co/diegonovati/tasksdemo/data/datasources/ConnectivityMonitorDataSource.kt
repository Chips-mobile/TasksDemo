package uk.co.diegonovati.tasksdemo.data.datasources

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                        notifyStateChange(ConnectionStatus.Connected)
                    }
                    override fun onLost(network: Network) {
                        notifyStateChange(ConnectionStatus.Disconnected)
                    }
                }
            )
            notifyStateChange(if (connectivityManager.isDefaultNetworkActive) ConnectionStatus.Connected else ConnectionStatus.Disconnected)
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

    private fun notifyStateChange(connectionStatus: ConnectionStatus) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                changeStateLister?.stateChanges(connectionStatus)
            }
        }
    }
}