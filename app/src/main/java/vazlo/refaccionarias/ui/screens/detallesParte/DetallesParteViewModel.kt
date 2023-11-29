package vazlo.refaccionarias.ui.screens.detallesParte

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.busquedasData.ProductosResult
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion
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

    private var idResponsable by mutableStateOf("")

    var productosUiState: ProductosUiState by mutableStateOf(ProductosUiState.Loading)

    var productosExtraUiState: ProductosUiState by mutableStateOf(ProductosUiState.Loading)

    var hay360: Boolean = false
    var url360: String = ""

    var permisoCotizacion = "0"
    var permisoExistencia = "0"
    var permisoPrecio = "0"

    var sucursal by mutableStateOf("")
    var idSucursal by mutableStateOf("")

    var tooltipEstado by mutableStateOf(false)

    var cantidadSucSelec by mutableStateOf("0")
    init {
        getTooltipDetalleParte()
    }

    var listConversiones by mutableStateOf("")

    fun setCantidadDisp(disponible: String) {
        cantidadSucSelec = disponible
    }
    fun setTooltipDetalleParte(){
        sesion.setDetalleProd()
    }

    private fun getTooltipDetalleParte() {
        viewModelScope.launch {
            tooltipEstado = sesion.detalleProd.first()
        }
    }

    fun onSucursalSelected(nombreSucursal: String, idSuc: String) {
        sucursal = nombreSucursal
        idSucursal = idSuc
    }


    var nuevaCant by mutableStateOf("")
        private set

    fun onNuevaCantidadChange(inputCantidad: String) {
        nuevaCant = inputCantidad
    }


    init {
        cargarProductos()
        setIdResponsable()
        getListasConversiones()
    }


    private fun setIdResponsable(){
        viewModelScope.launch {
            idResponsable = sesion.idUserResponsable.first()
        }
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            productosUiState = ProductosUiState.Loading
            val url = sesion.catalogoBusquedaDetalle.first()

            val response = servicesAppRepository.getProductosDetalle(url = url, soporte = criterio)
            productosUiState = if (response.estado == 1) {
                permisoCotizacion = sesion.getPermisoCotizacion.first()
                permisoExistencia = sesion.getPermisoExistencia.first()
                permisoPrecio = sesion.getPermisoPrecio.first()
                ProductosUiState.Success(response.lineas)
            } else {
                ProductosUiState.Error
            }
        }
    }

    suspend fun agregarProducto( nuevaCantidad: Int = nuevaCant.toInt()): Boolean {
        val url = sesion.agregarProduCarrito.first()
        val idCte = sesion.id.first()
        val response = servicesAppRepository.actualizarCantidadProucto(
            url = url,
            idCte = idCte,
            cantidad = nuevaCantidad,
            nomsop = "$criterio($sucursal)"
        )
        val datosOb = response.body()!!
        return datosOb["estado"]?.jsonPrimitive?.int == 10 || datosOb["estado"]?.jsonPrimitive?.int == 1
    }

    suspend fun agregarABackOrder(nuevaCantidad: Int = nuevaCant.toInt()): Boolean
    {
        val url = sesion.getUrlBackOrder.first()
        val idCte = sesion.id.first()
        val response = servicesAppRepository.agregarBackOrder(url, idCte, criterio, nuevaCantidad, idSucursal)
        val datosOb = response.body()!!
        return datosOb["estado"]?.jsonPrimitive?.int == 1 || datosOb["estado"]?.jsonPrimitive?.int == 2
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
                aFinal,
                idUser
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

    fun getListasConversiones() {
        viewModelScope.launch {
            val url = sesion.listaConversiones.first()

            val idCte = sesion.id.first()
            val response = servicesAppRepository.getListaConversiones(url, criterio, idCte)

            listConversiones = response.resultado
        }
    }


    fun get360() {
        viewModelScope.launch {
            val url = sesion.getUrlObtener360.first()
            url360 = url
        }
    }
}