package vazlo.refaccionarias.ui.screens.detallesParte

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
import vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive


sealed interface ProductosUiState{
    data class Success(val productos: List<ProductosResult>): ProductosUiState
    object Error: ProductosUiState
    object Loading: ProductosUiState
}
class DetallesParteViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
): ViewModel() {
    private val criterio: String =
        checkNotNull(savedStateHandle[DetallesParteDestination.criterioArg])

    var productosUiState: ProductosUiState by mutableStateOf(ProductosUiState.Loading)

    var productosExtraUiState: ProductosUiState by mutableStateOf(ProductosUiState.Loading)

    var hay360: Boolean = false
    var url360: String = ""

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            productosUiState = ProductosUiState.Loading
            val url = sesion.catalogoBusquedaDetalle.first()

            val response = servicesAppRepository.getProductosDetalle(url = url, soporte = criterio)
            productosUiState = if (response.estado == 1) {
                ProductosUiState.Success(response.lineas)
            } else {
                ProductosUiState.Error
            }
        }
    }

    suspend fun agregarProducto(producto: String): Boolean {
        val url = sesion.agregarProduCarrito.first()
        val idCte = sesion.id.first()
        val response = servicesAppRepository.actulaziarCantidadProucto(
            url = url,
            idCte = idCte,
            cantidad = 1,
            nomsop = producto
        )
        val datosOb = response.body()!!
        return if (datosOb["estado"]?.jsonPrimitive?.int == 10 || datosOb["estado"]?.jsonPrimitive?.int == 1) {

            Log.i("MaistroPipe", "Se actualizó")
            true
        } else {
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            false
        }
    }

    fun cargarProductosExtra(
        marca: String,
        modelo: String,
        cilindraje: String,
        litros: String,
        aInicial: String,
        aFinal: String
    ) {
        viewModelScope.launch {
            productosExtraUiState = ProductosUiState.Loading
            val url = sesion.sGuias.first()

            val idUser = sesion.id.first()

            val response = servicesAppRepository.getSugeridoGuias(
                url,
                marca,
                modelo,
                cilindraje,
                litros,
                aInicial,
                aFinal
            )
            productosExtraUiState = if (response.estado == 1) {
                ProductosUiState.Success(response.lineas)
            } else {
                ProductosUiState.Error
            }
        }
    }

    fun verificar360(nombreSoporte: String) {
        viewModelScope.launch {
            val url = sesion.getUVerificar360.first()
            val response = servicesAppRepository.verificar360(url = url, soporte = nombreSoporte)
            if (response.estado == 1) {
                hay360 = response.resultado[0].hay360 == "1"
            }
        }
    }


    fun get360() {
        viewModelScope.launch {
            val url = sesion.getUrlObtener360.first()
            url360 = url
        }
    }
}