package vazlo.refaccionarias.ui.screens.folletosQuincenales

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.local.Sesion
import vazlo.refaccionarias.data.model.folletosQuinceData.Folleto
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository


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
     private fun cargarFolletos() {
         viewModelScope.launch {
             val url = sesion.getUrlFolletosQuincenales.first()
             val user = sesion.id.first()

             folletosUiState = try {
                 val response = servicesAppRepository.getFolletos(url, user)
                 if (response.estado == 1) FolletosUiState.Success(response.folletos) else FolletosUiState.Error
             } catch (e: Exception) {
                 FolletosUiState.Error
             }
         }

     }

}