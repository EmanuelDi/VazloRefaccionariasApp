package vazlo.refaccionarias.ui.screens.usuarios_y_permisos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.users_y_permisosData.Permisos
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
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
    private var checkedEntrar = mutableStateOf(false)
    private var checkedFacturas = mutableStateOf(false)
    private var checkedComplementos = mutableStateOf(false)
    private var checkedNotas = mutableStateOf(false)
    private var checkedEstadoCuenta = mutableStateOf(false)


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
        Permisos(
            R.string.permiso_para_entrar,
            R.string.el_usuario_tendra_permiso_para_acceder_a_la_web_y_a_la_app,
            checkedEntrar
        ),
        Permisos(
            R.string.descargar_facturas,
            R.string.descarga_xml_y_pdf_de_facturas,
            checkedFacturas
        ),
        Permisos(
            R.string.descargar_complementos,
            R.string.descargar_xml_y_pdf_de_complementos,
            checkedComplementos
        ),
        Permisos(
            R.string.descargar_notas,
            R.string.descargar_xml_y_pdf_de_notas,
            checkedNotas
        ),
        Permisos(
            R.string.visualizar_estado_cuenta,
            R.string.visualizar_el_estado_de_cuenta,
            checkedEstadoCuenta
        )
    )

    init {
        verificarPermisos()
    }

    suspend fun actualizarPermisos(): Boolean {
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
            if (checkedEntrar.value) 1 else 0,
            if (checkedEstadoCuenta.value) 1 else 0,
            if (checkedFacturas.value) 1 else 0,
            if (checkedComplementos.value) 1 else 0,
            if (checkedNotas.value) 1 else 0
        )
        val datosOb = response.body()!!
        return datosOb["estado"]?.jsonPrimitive?.int == 1

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
                checkedEntrar.value = response.recompensas == 1
                checkedEstadoCuenta.value = response.estadoCuenta == 1
                checkedFacturas.value = response.permisoFactura == 1
                checkedComplementos.value = response.permisoComplemento == 1
                checkedNotas.value = response.permisoNota == 1
            }
        }
    }


}