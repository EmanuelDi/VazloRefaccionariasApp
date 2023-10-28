package vazlo.refaccionarias.ui.screens.folletosQuincenales

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.Folleto
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


sealed interface FolletosUiState {
    data class Success(val folletos: MutableList<Folleto>) : FolletosUiState
    object Error : FolletosUiState
    object Loading : FolletosUiState
}

class FolletosQuincenalesViewModel(
    private val servicesAppRepository: ServicesAppRepository,
    private val sesion: Sesion
): ViewModel() {

    var folletosUiState: FolletosUiState by mutableStateOf(FolletosUiState.Loading)
        private set


    init {
        cargarFolletos()
    }
     fun cargarFolletos() {
         viewModelScope.launch {
             val url = sesion.getUrlFolletosQuincenales.first()
             val user = sesion.id.first()

             try {
                 val response = servicesAppRepository.getFolletos(url, user)
                 Log.i("pop", response.mensaje)
                 folletosUiState =
                     if (response.estado == 1) FolletosUiState.Success(response.folletos) else FolletosUiState.Error
             } catch (e: Exception) {
                 folletosUiState = FolletosUiState.Error
                 Log.i("pop", e.toString())

             }
         }

     }

}