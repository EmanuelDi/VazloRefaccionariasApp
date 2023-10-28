package vazlo.refaccionarias.ui.screens.catalagoElectronico

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.Empresas
import vazlo.refaccionarias.data.model.ProductosResult
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface ProductosUiState {
    data class Success(val productos: List<ProductosResult>) : ProductosUiState

    object Error : ProductosUiState

    object Loading : ProductosUiState
}
sealed interface EmpresasUiState {
    data class Success(val empresas: List<Empresas>) : EmpresasUiState

    object Error : EmpresasUiState

    object Loading : EmpresasUiState
}

class ResultadosCatElViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {
    private val anio: String = checkNotNull(savedStateHandle[ResultadosCatElDestination.anio])
    private val marca: String = checkNotNull(savedStateHandle[ResultadosCatElDestination.marca])
    private val modelo: String = checkNotNull(savedStateHandle[ResultadosCatElDestination.modelo])
    private val cilindraje: String =
        checkNotNull(savedStateHandle[ResultadosCatElDestination.cilindraje])
    private val litros: String = checkNotNull(savedStateHandle[ResultadosCatElDestination.litros])

    var productosUiStateEagle: ProductosUiState by mutableStateOf(ProductosUiState.Loading)
    var productosUiStateShark: ProductosUiState by mutableStateOf(ProductosUiState.Loading)
    var productosUiStatePartech: ProductosUiState by mutableStateOf(ProductosUiState.Loading)
    var productosUiStateRodaTech: ProductosUiState by mutableStateOf(ProductosUiState.Loading)
    var productosUiStateTrackone: ProductosUiState by mutableStateOf(ProductosUiState.Loading)


    var empresasUiState: EmpresasUiState by mutableStateOf(EmpresasUiState.Loading)
    var totalProductos: Int by mutableStateOf(0)
    init {
        cargarEmpresas()
        cargarProductosEagle() // Para Eagle
        cargarProductosShark() // Para Shark
        cargarProductosPartech()
        cargarProductosRodatech()
        cargarProductosTrackone()
    }

    fun cargarProductosEagle() {
        viewModelScope.launch {
            this@ResultadosCatElViewModel.productosUiStateEagle = ProductosUiState.Loading
            val url = sesion.fragmentsLineas.first()
            val idUser = sesion.id.first()

            try {
                val response = servicesAppRepository.getProductosLineas(
                    url = url,
                    litros = litros,
                    modelo = modelo,
                    marca = marca,
                    cilindraje = cilindraje,
                    anio = anio,
                    linea = "1",
                    idUser = idUser
                )
                this@ResultadosCatElViewModel.productosUiStateEagle = if (response.estado == 1) {
                    ProductosUiState.Success(response.lineas)
                } else {
                    ProductosUiState.Error
                }
            } catch (e: Exception) {
                this@ResultadosCatElViewModel.productosUiStateEagle = ProductosUiState.Error
            }
        }
    }
    fun cargarProductosShark() {
        viewModelScope.launch {
            this@ResultadosCatElViewModel.productosUiStateShark = ProductosUiState.Loading
            val url = sesion.fragmentsLineas.first()
            val idUser = sesion.id.first()

            try {
                val response = servicesAppRepository.getProductosLineas(
                    url = url,
                    litros = litros,
                    modelo = modelo,
                    marca = marca,
                    cilindraje = cilindraje,
                    anio = anio,
                    linea = "2",
                    idUser = idUser
                )
                this@ResultadosCatElViewModel.productosUiStateShark = if (response.estado == 1) {
                    ProductosUiState.Success(response.lineas)
                } else {
                    ProductosUiState.Error
                }
            } catch (e: Exception) {
                this@ResultadosCatElViewModel.productosUiStateShark = ProductosUiState.Error
            }
        }
    }
    fun cargarProductosPartech() {
        viewModelScope.launch {
            this@ResultadosCatElViewModel.productosUiStatePartech = ProductosUiState.Loading
            val url = sesion.fragmentsLineas.first()
            val idUser = sesion.id.first()

            try {
                val response = servicesAppRepository.getProductosLineas(
                    url = url,
                    litros = litros,
                    modelo = modelo,
                    marca = marca,
                    cilindraje = cilindraje,
                    anio = anio,
                    linea = "3",
                    idUser = idUser
                )
                this@ResultadosCatElViewModel.productosUiStatePartech = if (response.estado == 1) {
                    ProductosUiState.Success(response.lineas)
                } else {
                    ProductosUiState.Error
                }
            } catch (e: Exception) {
                this@ResultadosCatElViewModel.productosUiStatePartech = ProductosUiState.Error
            }
        }
    }
    fun cargarProductosRodatech() {
        viewModelScope.launch {
            this@ResultadosCatElViewModel.productosUiStateRodaTech = ProductosUiState.Loading
            val url = sesion.fragmentsLineas.first()
            val idUser = sesion.id.first()

            try {
                val response = servicesAppRepository.getProductosLineas(
                    url = url,
                    litros = litros,
                    modelo = modelo,
                    marca = marca,
                    cilindraje = cilindraje,
                    anio = anio,
                    linea = "4",
                    idUser = idUser
                )
                this@ResultadosCatElViewModel.productosUiStateRodaTech = if (response.estado == 1) {
                    ProductosUiState.Success(response.lineas)
                } else {
                    ProductosUiState.Error
                }
            } catch (e: Exception) {
                Log.i("ELPROSTA", e.toString())
                this@ResultadosCatElViewModel.productosUiStateRodaTech = ProductosUiState.Error
            }
        }
    }
    fun cargarProductosTrackone() {
        viewModelScope.launch {
            this@ResultadosCatElViewModel.productosUiStateTrackone = ProductosUiState.Loading
            val url = sesion.fragmentsLineas.first()
            val idUser = sesion.id.first()

            try {
                val response = servicesAppRepository.getProductosLineas(
                    url = url,
                    litros = litros,
                    modelo = modelo,
                    marca = marca,
                    cilindraje = cilindraje,
                    anio = anio,
                    linea = "5",
                    idUser = idUser
                )
                this@ResultadosCatElViewModel.productosUiStateTrackone = if (response.estado == 1) {
                    ProductosUiState.Success(response.lineas)
                } else {
                    ProductosUiState.Error
                }
            } catch (e: Exception) {
                this@ResultadosCatElViewModel.productosUiStateTrackone = ProductosUiState.Error
            }
        }
    }

    private fun cargarEmpresas() {
        viewModelScope.launch {
            empresasUiState = EmpresasUiState.Loading
            val url = sesion.tabsEmpresas.first()
            val usuario = sesion.id.first()
            try {
                val response = servicesAppRepository.getEmpresas(
                    url = url,
                    litros = litros,
                    cilindraje = cilindraje,
                    anio = anio,
                    modelo = modelo,
                    marca = marca,
                    usuario = usuario,
                    tipo = "1"
                )
                empresasUiState = if (response.estado == 1) {
                    EmpresasUiState.Success(response.empresas)
                } else {
                    EmpresasUiState.Error
                }
            } catch (e: Exception) {
                empresasUiState = EmpresasUiState.Error
            }
        }
    }

}