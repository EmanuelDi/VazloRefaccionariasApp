package vazlo.refaccionarias.ui.screens.eventos


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.model.eventosData.Marcador
import vazlo.refaccionarias.data.local.Sesion

sealed interface MarcadoresUiState {
    data class Success(val marcadores: List<Marcador>): MarcadoresUiState
    object Error: MarcadoresUiState
    object Loading: MarcadoresUiState
}

class RefacCercanasViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {
    var marcadoresUiState: MarcadoresUiState by mutableStateOf(MarcadoresUiState.Loading)

    private var latitud by mutableDoubleStateOf(0.0)
    private var longitud by mutableDoubleStateOf(0.0)
    var cantEventos = 0

    fun setCoordenadas(lat: Double, long: Double) {
        latitud = lat
        longitud = long
    }

    fun cargarMarcadores() {
        viewModelScope.launch {
            marcadoresUiState = MarcadoresUiState.Loading
            val url = sesion.menuMarcadoresEventos.first()

            val response = servicesAppRepository.getMarcadores(url = url, lat = latitud, long = longitud)

            marcadoresUiState = if(response.estado == 1) {
                cantEventos = response.marcadoresCliente.size
                MarcadoresUiState.Success(response.marcadoresCliente)
            } else {
                MarcadoresUiState.Success(emptyList())
            }
        }
    }
}