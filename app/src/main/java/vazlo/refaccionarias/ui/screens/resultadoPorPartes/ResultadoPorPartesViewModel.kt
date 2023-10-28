package vazlo.refaccionarias.ui.screens.resultadoPorPartes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.ProductosResult
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ResultadoPorPartesDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface ResultadoParteUiState {
    data class Success(val productos: List<ProductosResult>) : ResultadoParteUiState
    object Error : ResultadoParteUiState
    object Loading : ResultadoParteUiState
}
class ResultadoPorPartesViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
): ViewModel() {
    val criterio: String = checkNotNull(savedStateHandle[ResultadoPorPartesDestination.criterio])
    val funcArg: String = checkNotNull(savedStateHandle[ResultadoPorPartesDestination.funcArg])
    var resultadoPartesUiState: ResultadoParteUiState by mutableStateOf(ResultadoParteUiState.Loading)
        private set

    init {
        if (funcArg == "M"){
            cargarProductosMamoan()
        }
        else if (funcArg == "B") {
            cargarProductos()
        }
        else if (funcArg == "C") {
            cargarProductosConversiones()
        }
    }

    fun cargarProductosMamoan() {
        viewModelScope.launch {
            val url = sesion.mamoanBusquedas.first()
            val user = sesion.id.first()
            try {
                val response = servicesAppRepository.cargarProductossMamoan(
                    url = url,
                    idUser = user,
                    bmamoan = criterio
                )
                Log.i("pop", response.total.toString())
                resultadoPartesUiState = if (response.estado == 1) ResultadoParteUiState.Success(
                    response.lineas
                ) else ResultadoParteUiState.Error
            } catch (e: Exception) {
                Log.i("pop", e.toString())
                resultadoPartesUiState = ResultadoParteUiState.Error
            }
        }
    }

    fun cargarProductos() {
        viewModelScope.launch {
            val url = sesion.catalogoBusquedaPorParte.first()
            val user = sesion.id.first()
            val tipo = sesion.tipo.first()
             try {
                val response = servicesAppRepository.cargarProductoss(
                    url = url,
                    idUser = user,
                    tipo = tipo,
                    soporte = criterio
                )
                Log.i("pop", response.lineas[0].cillitModSopId.toString())
                resultadoPartesUiState = if (response.estado == 1) ResultadoParteUiState.Success(
                    response.lineas
                ) else ResultadoParteUiState.Error
            } catch (e: Exception) {
                resultadoPartesUiState = ResultadoParteUiState.Error
            }
        }
    }

    fun cargarProductosConversiones() {
        viewModelScope.launch {
            val url = sesion.catalgoConversionesBusquedas.first()
            val user = sesion.id.first()
            val tipo = sesion.tipo.first()
            try {
                val response = servicesAppRepository.cargarProductosConversiones(
                    url = url,
                    idUser = user,
                    tipo = tipo,
                    soporte = criterio
                )
                Log.i("pop", response.lineas[0].cillitModSopId.toString())
                resultadoPartesUiState = if (response.estado == 1) ResultadoParteUiState.Success(
                    response.lineas
                ) else ResultadoParteUiState.Error
            } catch (e: Exception) {
                Log.i("pop", e.toString())
                resultadoPartesUiState = ResultadoParteUiState.Error
            }
        }
    }
}