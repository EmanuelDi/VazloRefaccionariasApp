package vazlo.refaccionarias.ui.screens.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.ProductoCart
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.net.URLEncoder

sealed interface CarritoUiState {
    data class Success(val productos: MutableList<ProductoCart>) : CarritoUiState
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

    var total by mutableStateOf("")
        private set

    var comentarios by mutableStateOf("")
        private set


    var nuevaCant by mutableStateOf("")
        private set

    var productoSeleccionado by mutableStateOf("")

    val productosCargando = mutableStateListOf<String>()

    fun onNuevaCantidadChange(inputCantidad: String) {
        nuevaCant = inputCantidad
    }


    fun onSelectProducto(productoId: String) {
        productoSeleccionado = productoId
    }

    fun onComentariosChange(inputComentario: String) {
        comentarios = inputComentario
    }

    suspend fun cargarCarrito(): Boolean {
        val url = sesion.verCarritoJunio2023.first()
        val idCte = sesion.id.first()
        val response = servicesAppRepository.cargarProductosCarrito(url, idCte)
        return if (response.estado == 1) {
            carritoUiState = CarritoUiState.Success(response.productos)
            cantidad = response.productos.size
            subtotal = response.subtotal.toString()
            iva = response.iva.toString()
            total = response.total.toString()
            true
        } else {
            carritoUiState = CarritoUiState.Error
            false
        }
    }


    suspend fun actualizarCantidadProducto(nuevaCantidad: Int = nuevaCant.toInt()): Boolean {
        productosCargando.add(productoSeleccionado)
        val url = sesion.agregarProduCarrito.first()
        val idCte = sesion.id.first()
        val response = servicesAppRepository.actulaziarCantidadProucto(
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
            Log.i("MaistroPipe", "Se actualizó")
            true
        } else {
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            false
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
            Log.i("MaistroPipe", "Se actualizó")
            true
        } else {
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            Log.i("MaistroPipe", productoSeleccionado)
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
            Log.i("MaistroPipe", "Se actualizó")
            true
        } else {
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            Log.i("MaistroPipe", productoSeleccionado)
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
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            true
        } else {
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content!!)
            false
        }

    }
}