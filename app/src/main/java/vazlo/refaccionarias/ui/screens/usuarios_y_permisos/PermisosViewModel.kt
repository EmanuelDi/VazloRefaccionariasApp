package vazlo.refaccionarias.ui.screens.usuarios_y_permisos

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Permisos
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.PermisosDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class PermisosViewModel(
    savedStateHandle: SavedStateHandle,
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {

    val itemId: String = checkNotNull(savedStateHandle[PermisosDestination.itemIdArg])

    private var checkedPrecio = mutableStateOf(false)
    private var checkedExistencias = mutableStateOf(false)
    private var checkedPedido = mutableStateOf(false)
    private var checkedCapturaRapida = mutableStateOf(false)
    private var checkedCargarArchivo = mutableStateOf(false)
    private var checkedCotizacion = mutableStateOf(false)
    private var checkedCotizarCarrito = mutableStateOf(false)
    private var checkedHistorial = mutableStateOf(false)


    val permisosList = listOf<Permisos>(
        Permisos(
            R.string.consultar_precios,
            R.string.precios_precios_al_usuario_creado,
            checkedPrecio
        ),
        Permisos(
            R.string.consultar_existencias,
            R.string.mostrara_existencia_del_producto_al_usuario,
            checkedExistencias
        ),
        Permisos(
            R.string.hacer_pedido,
            R.string.habilita_la_opcion_de_enviar_pedido_desde_el_carrito_de_compra,
            checkedPedido
        ),
        Permisos(
            R.string.captura_rapida,
            R.string.habilita_menu_captura_rapida,
            checkedCapturaRapida
        ),
        Permisos(
            R.string.cargar_archivo_csv,
            R.string.habilita_men_cargar_archivo_csv,
            checkedCargarArchivo
        ),
        Permisos(
            R.string.hacer_cotizacion,
            R.string.habilita_la_opcion_de_agregar_carrito_de_compra,
            checkedCotizacion
        ),
        Permisos(
            R.string.cotizar_en_el_mismo_carrito,
            R.string.mismo_carrito_info,
            checkedCotizarCarrito
        ),
        Permisos(R.string.productos_historial, R.string.habilita_historial_info, checkedHistorial)
    )

    init {
        verificarPermisos()
    }

    fun actualizarPermisos() {
        viewModelScope.launch {
            val url = sesion.getUrlActualizarPermisos.first()
            val response = servicesAppRepository.actualizarPermisos(
                url = url,
                idCte = itemId,
                if (checkedPrecio.value) 1 else 0,
                if (checkedExistencias.value) 1 else 0,
                if (checkedPedido.value) 1 else 0,
                if (checkedCapturaRapida.value) 1 else 0,
                if (checkedCargarArchivo.value) 1 else 0,
                if (checkedCotizacion.value) 1 else 0,
                if (checkedCotizarCarrito.value) 1 else 0,
                if (checkedHistorial.value) 1 else 0
            )
            val datosOb = response.body()!!
            Log.i("permisoUpdate", datosOb["mensaje"]?.jsonPrimitive?.content ?: "")
        }
    }

    fun verificarPermisos() {
        viewModelScope.launch {
            val url = sesion.getUrlVerificarPermisos.first()
            val response = servicesAppRepository.verificarPermisos(
                url = url,
                idCte = itemId
            )
            if (response.estado == 1) {
                checkedPrecio.value = response.precios == 1
                checkedExistencias.value = response.existencias == 1
                checkedPedido.value = response.pedido == 1
                checkedCapturaRapida.value = response.captura == 1
                checkedCargarArchivo.value = response.csv == 1
                checkedCotizacion.value = response.cotizacion == 1
                checkedCotizarCarrito.value = response.carrito == 1
                checkedHistorial.value = response.recompensas == 1
            }
        }
    }


}