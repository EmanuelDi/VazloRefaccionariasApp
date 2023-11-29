package vazlo.refaccionarias.ui.screens.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.busquedasData.ProductoCart
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import vazlo.refaccionarias.data.model.detallesData.Sucursal
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ResultadoParteUiState
import java.net.URLEncoder

sealed interface CarritoUiState {
    data class Success(val productos: List<ProductoCart>) : CarritoUiState
    object Error : CarritoUiState
    object Loading : CarritoUiState
}

class CartViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
): ViewModel() {

    var carritoUiState: CarritoUiState by mutableStateOf(CarritoUiState.Loading)
        private set

    var cantidad by mutableIntStateOf(0)
        private set

    var subtotal by mutableStateOf("")
        private set

    var iva by mutableStateOf("")
        private set

    var mensaje by mutableStateOf("");

    var total by mutableStateOf("")
        private set

    var comentarios by mutableStateOf("")
        private set

    var tooltipEstado by mutableStateOf(false)

    var nuevaCant by mutableStateOf("")
        private set

    private var productoSeleccionado by mutableStateOf("")

    val productosCargando = mutableStateListOf<String>()

    private var sucursales: MutableList<Sucursal>? = null

    init {
        getToolTipCarrito()
    }


    private fun getToolTipCarrito() {
        viewModelScope.launch {
            tooltipEstado = sesion.carrito.first()
        }
    }

    fun setCarrito() {
        sesion.setCarrito()
    }

    fun onNuevaCantidadChange(inputCantidad: String) {
        nuevaCant = inputCantidad
    }

    var permisoCotizacion = "0"
    var permisoExistencia = "0"
    var permisoPrecio = "0"
    var permisoHacerPedido = "0"

    fun onSelectProducto(productoId: String) {
        productoSeleccionado = productoId
    }

    fun onComentariosChange(inputComentario: String) {
        comentarios = inputComentario
    }


    var falloCantidad by mutableStateOf(false)
    private suspend fun cargarSucus() {
        val url = sesion.catalogoBusquedaPorParte.first()
        val user = sesion.id.first()
        val tipo = sesion.tipo.first()

        val response = servicesAppRepository.cargarProductoss(
            url = url,
            idUser = user,
            tipo = tipo,
            soporte = productoSeleccionado.substringBefore("(")
        )
        sucursales = response.lineas[0].sucursales as MutableList<Sucursal>?

    }

    suspend fun cargarCarrito(): Boolean {
        val url = sesion.verCarritoJunio2023.first()
        val idCte = sesion.id.first()


        val response = servicesAppRepository.cargarProductosCarrito(url, idCte)
        return if (response.estado == 1 || response.estado == 10) {
            carritoUiState = CarritoUiState.Success(response.productos)
            cantidad = response.productos.size
            subtotal = response.subtotal.toString()
            iva = response.iva.toString()
            total = response.total.toString()
            permisoCotizacion = sesion.getPermisoCotizacion.first()
            permisoExistencia = sesion.getPermisoExistencia.first()
            permisoPrecio = sesion.getPermisoPrecio.first()
            permisoHacerPedido = sesion.getPermisoPedidos.first()
            true
        } else {
            carritoUiState = CarritoUiState.Error
            false
        }
    }


    suspend fun actualizarCantidadProducto(nuevaCantidad: Int = nuevaCant.toInt()): Boolean {
        cargarSucus()
        productosCargando.add(productoSeleccionado)
        val url = sesion.agregarProduCarrito.first()
        val idCte = sesion.id.first()
        val producto = productoSeleccionado
        val sucursal = sucursales!!.filter {
            it.nombre.equals(
                producto.substringAfter("(").substringBefore(")")
            )
        }
        if (sucursal[0].existencia!!.toInt() >= nuevaCantidad) {
            val response = servicesAppRepository.actualizarCantidadProucto(
                url = url,
                idCte = idCte,
                cantidad = nuevaCantidad,
                nomsop = productoSeleccionado
            )
            val datosOb = response.body()!!
            return if (datosOb["estado"]?.jsonPrimitive?.int == 10 || datosOb["estado"]?.jsonPrimitive?.int == 1) {
                if (cargarCarrito()) {
                    productosCargando.remove(productoSeleccionado)
                }
                true
            } else {
                false
            }
        } else{
            productosCargando.remove(productoSeleccionado)
            falloCantidad = true
           return false
        }
    }

    suspend fun eliminarProd(idSoporte: String): Boolean {
        productosCargando.add(idSoporte)
        val url = sesion.eliminarProdCarrito.first()
        val cliente = sesion.id.first()
        val response = servicesAppRepository.eliminarProdCarrito(
            url = url,
            idCte = cliente,
            soporte = idSoporte
        )
        val datosOb = response.body()!!
        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            if (cargarCarrito()) {
                productosCargando.remove(productoSeleccionado)
            }
            true
        } else {
            false
        }
    }

    suspend fun vaciarCarrito(): Boolean {
        val url = sesion.vaciarCarrito.first()
        val cliente = sesion.id.first()
        val response = servicesAppRepository.vaciarCarrito(url, cliente)
        val datosOb = response.body()!!
        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            cargarCarrito()
            true
        } else {
            false
        }
    }

    suspend fun enviarCarrito(): Boolean {
        val url = sesion.enviarPedCarrito.first()
        val id = sesion.id.first()
        val subtotalCod = withContext(Dispatchers.IO) {
            URLEncoder.encode(subtotal, "UTF-8").replace(" ","%20").replace("$","pesitos")
        }
        val ivaCod = withContext(Dispatchers.IO) {
            URLEncoder.encode(iva, "UTF-8").replace("%","porcentaje").replace(" ","%20").replace("$","pesitos")
        }
        val totalCod = withContext(Dispatchers.IO) {
            URLEncoder.encode(total, "UTF-8").replace(" ","%20").replace("$","pesitos")
        }


        val response = servicesAppRepository.enviarCarrito(
            url = url,
            idCte = id,
            iteracion = cantidad.toString(),
            subtotal = subtotalCod,
            iva = ivaCod,
            total = totalCod,
            comentarios = comentarios
        )


        val datosOb = response.body()!!

        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            cargarCarrito()
            mensaje = datosOb["mensaje"]?.jsonPrimitive?.content!!
            true
        } else {
            mensaje = datosOb["mensaje"]?.jsonPrimitive?.content!!
            false
        }

    }
}