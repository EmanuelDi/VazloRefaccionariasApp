package vazlo.refaccionarias.ui.screens.notificaciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.Mensaje
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.flow.first
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


sealed interface NotificacionesUiState {
    data class Success(val productos: MutableList<Mensaje>) : NotificacionesUiState
    object Error : NotificacionesUiState
    object Loading : NotificacionesUiState
}

class NotificacionesViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {
    var notificacionesUiState: NotificacionesUiState by mutableStateOf(NotificacionesUiState.Loading)
        private set


    suspend fun cargarNotificaciones(token: String) {
        val url = sesion.verMensajesNotificaciones.first()
        val idCte = sesion.id.first()

        try {
            val response = servicesAppRepository.cargarNotificaciones(url, idCte, token)
            Log.i("pop", response.mensaje)
            notificacionesUiState =
                if (response.estado == 1) NotificacionesUiState.Success(response.mensajes) else NotificacionesUiState.Success(response.mensajes)
        } catch (e: Exception) {
            Log.e("Notis", e.toString())
            notificacionesUiState = NotificacionesUiState.Error

        }
    }

    fun darBajaNotificacion(id: String) {
        viewModelScope.launch {
            val url = sesion.bajaMensajeNotifiacion.first()

            val response = servicesAppRepository.bajarNotificacion(url, id)

            val obData = response.body()
        }
    }

}