package vazlo.refaccionarias.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class Sesion(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val SERVIDOR_PRINCIPAL = stringPreferencesKey("servidorPrincipal")
        val LOGUEADO = booleanPreferencesKey("logueado")
        val CATELEC = booleanPreferencesKey("catelec")
        val POR_PARTE = booleanPreferencesKey("porParte")
        val CONVER = booleanPreferencesKey("conver")
        val MAMOAN = booleanPreferencesKey("MaMoAn")
        val DETALLE_PROD = booleanPreferencesKey("detalleProd")
        val CARRITO = booleanPreferencesKey("carrito")
        val IMAGEN_CARRITO = stringPreferencesKey("ImagenCarrito")
        val FRAGMENTS_LINEAS = stringPreferencesKey("FragmentsLineas")
        val ARTICULOS_NUEVOS_LISTA_CUADRADOS = stringPreferencesKey("ArticulosNuevosListaCuadrados")
        val AGREGAR_PRODU_CARRITO = stringPreferencesKey("AgregarProduCarrito")
        val ELIMINAR_PROD_CARRITO = stringPreferencesKey("EliminarProdCarrito")
        val ENVIAR_PED_CARRITO = stringPreferencesKey("EnviarPedCarrito")
        val VACIAR_CARRITO = stringPreferencesKey("VaciarCarrito")
        val VER_CARRITO_JUNIO23 = stringPreferencesKey("VerCarrito_junio23")
        val RASTREO_PED_CARRITO = stringPreferencesKey("RastreoPedCarrito")
        val MENU_LISTA_PROMOCIONES = stringPreferencesKey("MenuListaPromociones")
        val VER_PEDIDOS_ENVIADOS = stringPreferencesKey("VerPedidosEnviados")
        val TABS_EMPRESAS = stringPreferencesKey("TabsEmpresas")
        val CATALOGOS_CONVERSIONES_BUSQUEDAS =
            stringPreferencesKey("CatalogosConversionesBusquedas")
        val CATALOGO_BUSQUEDA_POR_PARTE = stringPreferencesKey("CatalogoBusquedaPorParte")
        val MA_MO_AN_BUSQUEDAS = stringPreferencesKey("MaMoAnBusquedas")
        val BAJA_MENSAJE_NOTIFICACION = stringPreferencesKey("BajaMensajeNotificacion")
        val VER_MENSAJE_NOTIFICACIONES = stringPreferencesKey("VerMensajeNotificaciones")
        val VER_MENSAJE_UNO = stringPreferencesKey("VerMensajeUno")
        val CAMBIAR_ESTADO_MENSAJE = stringPreferencesKey("CambiarEstadoMensaje")
        val REGISTRAR_TOKEN_CLIENTE = stringPreferencesKey("RegistrarTokenCliente")
        val LOGIN_CLIENTE = stringPreferencesKey("LoginCliente")
        val CAT_ELEC_ANIOS = stringPreferencesKey("CatElecAnios")
        val CAT_ELEC_MARCA = stringPreferencesKey("CatElecMarca")
        val CAT_ELEC_MODELO = stringPreferencesKey("CatElecModelo")
        val CAT_ELEC_MOTOR = stringPreferencesKey("CatElecMotor")
        val CATALOGO_BUSQUEDA_DETALLE = stringPreferencesKey("CatalogoBusquedaDetalle")
        val NUMERO_WHATS = stringPreferencesKey("NumeroWhats")
        val IMAGEN_NO_MAPA = stringPreferencesKey("ImagenNoMapa")
        val MENU_MARCADORES_EVENTOS = stringPreferencesKey("MenuMarcadoresEventos")
        val DIR_MAPA_COORDENADAS = stringPreferencesKey("DirMapaCoordenadas")
        val VERSION_DE_APK_REFACCIONARIAS = intPreferencesKey("VERSION_DE_APK_REFACCIONARIAS")
        val GET_URL_CARGAR_USUARIOS = stringPreferencesKey("getURLCargarUsuarios")
        val GET_URL_ACTUALIZAR_PASSWORD = stringPreferencesKey("getURLActualizarPassword")
        val GET_URL_NUEVO_USUARIO = stringPreferencesKey("getURLNuevoUsuario")
        val GET_URL_ACTUALIZAR_USUARIO = stringPreferencesKey("getURLActualizarUsuario")
        val GET_URL_ELIMINAR_USUARIO = stringPreferencesKey("getURLEliminarUsuario")
        val GET_URL_VERIFICAR_PERMISOS = stringPreferencesKey("getURLVerificarPermisos")
        val GET_URL_ACTUALIZAR_PERMISOS = stringPreferencesKey("getURLActualizarPermisos")
        val ATENCION_NOMBRE = stringPreferencesKey("AtencionNombre")
        val ATENCION_NUMERO = stringPreferencesKey("AtencionNumero")
        val ATENCION_CORREO = stringPreferencesKey("AtencionCorreo")
        val SOPORTE_NOMBRE = stringPreferencesKey("SoporteNombre")
        val SOPORTE_NUMERO = stringPreferencesKey("SoporteNumero")
        val SOPORTE_TECNICO_NOMBRE = stringPreferencesKey("SoporteTecnicoNombre")
        val SOPORTE_TECNICO_CORREO = stringPreferencesKey("SoporteTecnicoCorreo")
        val EMPRESA_NUMERO = stringPreferencesKey("EmpresaNumero")
        val GET_URL_SOPORTE_WHATS = stringPreferencesKey("getURLSoporteWhats")
        val GET_URL_VERIFICAR_PUNTOS_RECOMPENSA =
            stringPreferencesKey("getURLVerificarPuntosRecompensa")
        val GET_URL_VERIFICAR_PRODUCTOS_RECOMPENSA =
            stringPreferencesKey("getURLVerificarProductosRecompensa")
        val GET_URL_VER_PRODUCTOS_ENVIADOS_RECOMPENSA =
            stringPreferencesKey("getURLVerProductosEnviadosRecompensa")
        val GET_URL_VER_INSERTAR_RECOMPENSA = stringPreferencesKey("getURLVerInsertarRecompensa")
        val REGISTRAR_TOKEN_CLIENTE_FIREBASE = stringPreferencesKey("RegistrarTokenClienteFirebase")
        val GET_U_VERIFICAR_360 = stringPreferencesKey("getUVerificar360")
        val ACTIVAR_RECOMPENSAS = stringPreferencesKey("activarRecompensas")
        val GET_URL_FOLLETOS_QUINCENALES = stringPreferencesKey("getURLFolletosQuincenales")
        val GET_URL_OBTENER_360 = stringPreferencesKey("getURLObtener360")
        val GET_URL_SEGUIMIENTO_GUIAS = stringPreferencesKey("getURLSeguimientoGuias")
        val GET_URL_IMAGENES_AYUDA = stringPreferencesKey("getURLImagenesAyuda")
        val GET_URL_OBTENER_WEB_MUNDIAL = stringPreferencesKey("getURLObtenerWebMundial")
        val ACTIVAR_WEB_MUNDIAL = stringPreferencesKey("activarWebMundial")
        val TITULO_WEB_MUNDIAL = stringPreferencesKey("tituloWebMundial")

        val TIPO = intPreferencesKey("tipo")
        val ID_USER = stringPreferencesKey("id_user")
        val PASSWORD_USER = stringPreferencesKey("password_user")
        val ID_RESPONSABLE = stringPreferencesKey("id_responsable")
        val ID_CEDIS = stringPreferencesKey("id_cedis")

        val SGUIAS = stringPreferencesKey("sguias")

        val BACKORDER = stringPreferencesKey("backOrder")

        val PERMISO_PRECIOS = stringPreferencesKey("permiso_precios")
        val PERMISO_EXISTENCIA = stringPreferencesKey("permiso_existencia")
        val PERMISO_PEDIDOS = stringPreferencesKey("permiso_pedidos")
        val PERMISO_CAPTURA = stringPreferencesKey("permiso_captura")
        val PERMISO_CSV = stringPreferencesKey("permiso_csv")
        val PERMISO_COTIZACION = stringPreferencesKey("permiso_cotizacion")
        val PERMISO_OTRO_CARRITO = stringPreferencesKey("permiso_orto_carrito")

    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[SERVIDOR_PRINCIPAL] = "www.vazloonline.com"
            }
        }
    }

    val servidorPrincipalFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SERVIDOR_PRINCIPAL] ?: ""
        }


    val getPermisoPrecio: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_PRECIOS] ?: ""
        }

    val getPermisoCotizacion: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_COTIZACION] ?: ""
        }

    val getPermisoExistencia: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_EXISTENCIA] ?: ""
        }

    val getPermisoPedidos: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_PEDIDOS] ?: ""
        }

    val getPermisocCaptura: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_CAPTURA] ?: ""
        }

    val getPermisoCsv: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_CSV] ?: ""
        }

    val getPermisoOtroCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PERMISO_OTRO_CARRITO] ?: ""
        }

    fun setCatelec() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[CATELEC] ?: ""
            }
        }
    }

    val catElec: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CATELEC] ?: false
        }

    fun setPorParte() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[POR_PARTE] ?: false
            }
        }
    }

    val porParte: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[POR_PARTE] ?: false
        }

    fun setConver(): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[CONVER] ?: false
            }
        }
    }

    val conver: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CONVER] ?: false
        }


    fun setMaMoAn(): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[MAMOAN] ?: false
            }
        }
    }

    val mamoan: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[MAMOAN] ?: false
        }

    fun setDetalleProd(): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[DETALLE_PROD] ?: false
            }
        }
    }

    val detalleProd: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DETALLE_PROD] ?: false
        }


    fun setCarrito(): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[CARRITO] ?: false
            }
        }
    }

    val carrito: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CARRITO] ?: false
        }


    fun restablecerToolTips() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[CATELEC] ?: true
                preferences[CARRITO] ?: true
                preferences[POR_PARTE] ?: true
                preferences[CONVER] ?: true
                preferences[MAMOAN] ?: true
                preferences[DETALLE_PROD] ?: true
            }
        }
    }




    suspend fun setUrlParaConexiones(
        u1: String,
        u2: String,
        u3: String,
        u4: String,
        u5: String,
        u6: String,
        u7: String,
        u8: String,
        u9: String,
        u10: String,
        u11: String,
        u12: String,
        u13: String,
        u14: String,
        u15: String,
        u16: String,
        u17: String,
        u18: String,
        u19: String,
        u20: String,
        u21: String,
        u22: String,
        u23: String,
        u24: String,
        u25: String,
        u26: String,
        u27: String,
        u28: String,
        u29: String,
        u30: String,
        u31: Int,
        u32: String,
        u33: String,
        u34: String,
        u35: String,
        u36: String,
        u37: String,
        u38: String,
        u39: String,
        u40: String,
        u41: String,
        u42: String,
        u43: String,
        u44: String,
        u45: String,
        u46: String,
        u47: String,
        u48: String,
        u49: String,
        u50: String,
        u51: String,
        u52: String,
        u53: String,
        u54: String,
        u55: String,
        u56: String,
        u57: String,
        u58: String,
        u59: String,
        u60: String,
        u61: String,
        u62: String,
        u63: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[IMAGEN_CARRITO] = u1
                preferences[FRAGMENTS_LINEAS] = u2
                preferences[ARTICULOS_NUEVOS_LISTA_CUADRADOS] = u3
                preferences[AGREGAR_PRODU_CARRITO] = u4
                preferences[ELIMINAR_PROD_CARRITO] = u5
                preferences[ENVIAR_PED_CARRITO] = u6
                preferences[VACIAR_CARRITO] = u7
                preferences[VER_CARRITO_JUNIO23] = u8
                preferences[RASTREO_PED_CARRITO] = u9
                preferences[MENU_LISTA_PROMOCIONES] = u10
                preferences[VER_PEDIDOS_ENVIADOS] = u11
                preferences[TABS_EMPRESAS] = u12
                preferences[CATALOGOS_CONVERSIONES_BUSQUEDAS] = u13
                preferences[CATALOGO_BUSQUEDA_POR_PARTE] = u14
                preferences[MA_MO_AN_BUSQUEDAS] = u15
                preferences[BAJA_MENSAJE_NOTIFICACION] = u16
                preferences[VER_MENSAJE_NOTIFICACIONES] = u17
                preferences[VER_MENSAJE_UNO] = u18
                preferences[CAMBIAR_ESTADO_MENSAJE] = u19
                preferences[REGISTRAR_TOKEN_CLIENTE] = u20
                preferences[LOGIN_CLIENTE] = u21

                preferences[CAT_ELEC_ANIOS] = u22
                preferences[CAT_ELEC_MARCA] = u23
                preferences[CAT_ELEC_MODELO] = u24
                preferences[CAT_ELEC_MOTOR] = u25

                preferences[CATALOGO_BUSQUEDA_DETALLE] = u26
                preferences[NUMERO_WHATS] = u27

                preferences[IMAGEN_NO_MAPA] = u28
                preferences[MENU_MARCADORES_EVENTOS] = u29
                preferences[DIR_MAPA_COORDENADAS] = u30
                preferences[VERSION_DE_APK_REFACCIONARIAS] = u31

                preferences[GET_URL_CARGAR_USUARIOS] = u32
                preferences[GET_URL_ACTUALIZAR_PASSWORD] = u33
                preferences[GET_URL_NUEVO_USUARIO] = u34
                preferences[GET_URL_ACTUALIZAR_USUARIO] = u35
                preferences[GET_URL_ELIMINAR_USUARIO] = u36
                preferences[GET_URL_VERIFICAR_PERMISOS] = u37
                preferences[GET_URL_ACTUALIZAR_PERMISOS] = u38

                preferences[ATENCION_NOMBRE] = u39
                preferences[ATENCION_NUMERO] = u40
                preferences[ATENCION_CORREO] = u41
                preferences[SOPORTE_NOMBRE] = u42
                preferences[SOPORTE_NUMERO] = u43
                preferences[SOPORTE_TECNICO_NOMBRE] = u44
                preferences[SOPORTE_TECNICO_CORREO] = u45
                preferences[EMPRESA_NUMERO] = u46
                preferences[GET_URL_SOPORTE_WHATS] = u47

                preferences[GET_URL_VERIFICAR_PUNTOS_RECOMPENSA] = u48
                preferences[GET_URL_VERIFICAR_PRODUCTOS_RECOMPENSA] = u49
                preferences[GET_URL_VER_PRODUCTOS_ENVIADOS_RECOMPENSA] = u50
                preferences[GET_URL_VER_INSERTAR_RECOMPENSA] = u51
                preferences[REGISTRAR_TOKEN_CLIENTE_FIREBASE] = u52

                preferences[GET_U_VERIFICAR_360] = u53
                preferences[ACTIVAR_RECOMPENSAS] = u54
                preferences[GET_URL_FOLLETOS_QUINCENALES] = u55
                preferences[GET_URL_OBTENER_360] = u56
                preferences[GET_URL_SEGUIMIENTO_GUIAS] = u57
                preferences[GET_URL_IMAGENES_AYUDA] = u58
                preferences[GET_URL_OBTENER_WEB_MUNDIAL] = u59
                preferences[ACTIVAR_WEB_MUNDIAL] = u60
                preferences[TITULO_WEB_MUNDIAL] = u61

                preferences[SGUIAS] = u62

                preferences[BACKORDER] = u63
            }

        }
    }


    val imagenCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[IMAGEN_CARRITO] ?: ""
        }

    val fragmentsLineas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[FRAGMENTS_LINEAS] ?: ""
        }

    val articulosNuevos: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ARTICULOS_NUEVOS_LISTA_CUADRADOS] ?: ""
        }

    val agregarProduCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[AGREGAR_PRODU_CARRITO] ?: ""
        }

    val eliminarProdCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ELIMINAR_PROD_CARRITO] ?: ""
        }

    val enviarPedCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ENVIAR_PED_CARRITO] ?: ""
        }

    val vaciarCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[VACIAR_CARRITO] ?: ""
        }

    val verCarritoJunio2023: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[VER_CARRITO_JUNIO23] ?: ""
        }

    val rastreoPedCarrito: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[RASTREO_PED_CARRITO] ?: ""
        }


    val menuListaPromociones: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[MENU_LISTA_PROMOCIONES] ?: ""
        }

    val verPedidosEnviados: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[VER_PEDIDOS_ENVIADOS] ?: ""
        }

    val tabsEmpresas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[TABS_EMPRESAS] ?: ""
        }

    val catalgoConversionesBusquedas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CATALOGOS_CONVERSIONES_BUSQUEDAS] ?: ""
        }

    val catalogoBusquedaPorParte: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CATALOGO_BUSQUEDA_POR_PARTE] ?: ""
        }

    val mamoanBusquedas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[MA_MO_AN_BUSQUEDAS] ?: ""
        }

    val bajaMensajeNotifiacion: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[BAJA_MENSAJE_NOTIFICACION] ?: ""
        }

    val verMensajesNotificaciones: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[VER_MENSAJE_NOTIFICACIONES] ?: ""
        }


    val verMensajeUno: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[VER_MENSAJE_UNO] ?: ""
        }

    val cambiarEstadoMensaje: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CAMBIAR_ESTADO_MENSAJE] ?: ""
        }

    val registrarTokenCliente: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[REGISTRAR_TOKEN_CLIENTE] ?: ""
        }

    val loginCliente: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[LOGIN_CLIENTE] ?: ""
        }


    val getUrlBackOrder: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[BACKORDER] ?: ""
        }


    val catElecAnios: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CAT_ELEC_ANIOS] ?: ""
        }

    val catElecMarca: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CAT_ELEC_MARCA] ?: ""
        }

    val catElecModelo: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CAT_ELEC_MODELO] ?: ""
        }

    val catElectMotor: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CAT_ELEC_MOTOR] ?: ""
        }

    val catalogoBusquedaDetalle: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CATALOGO_BUSQUEDA_DETALLE] ?: ""
        }

    val numeroWhats: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[NUMERO_WHATS] ?: ""
        }


    val imagenNoMapa: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[IMAGEN_NO_MAPA] ?: ""
        }

    val menuMarcadoresEventos: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[MENU_MARCADORES_EVENTOS] ?: ""
        }

    val dirMapaCoordenadas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[DIR_MAPA_COORDENADAS] ?: ""
        }

    val versionDeApkRefaccionarias: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[VERSION_DE_APK_REFACCIONARIAS] ?: 0
        }


    val getUrlActualizarUsuario: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_ACTUALIZAR_USUARIO] ?: ""
        }

    val getUrlCargarUsuario: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_CARGAR_USUARIOS] ?: ""
        }

    val getUrlActualizarPassword: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_ACTUALIZAR_PASSWORD] ?: ""
        }

    val getUrlNuevoUsuario: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_NUEVO_USUARIO] ?: ""
        }

    val getUrlEliminarUsuario: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_ELIMINAR_USUARIO] ?: ""
        }

    val getUrlVerificarPermisos: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_VERIFICAR_PERMISOS] ?: ""
        }

    val getUrlActualizarPermisos: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_ACTUALIZAR_PERMISOS] ?: ""
        }


    val atencionNombre: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ATENCION_NOMBRE] ?: ""
        }

    val atencionNumero: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ATENCION_NUMERO] ?: ""
        }

    val atencionCorreo: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ATENCION_CORREO] ?: ""
        }

    val soporteNombre: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SOPORTE_NOMBRE] ?: ""
        }

    val soporteNumero: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SOPORTE_NOMBRE] ?: ""
        }

    val soporteTecnicoNombre: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SOPORTE_TECNICO_NOMBRE] ?: ""
        }

    val soporteTecnicoCorreo: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SOPORTE_TECNICO_CORREO] ?: ""
        }

    val empreseNumero: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[EMPRESA_NUMERO] ?: ""
        }

    val getUrlSoporteWhats: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_SOPORTE_WHATS] ?: ""
        }


    val getUrlVerificarPuntosRecompensa: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_VERIFICAR_PUNTOS_RECOMPENSA] ?: ""
        }

    val getUrlVerificarProductosRecompensa: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_VERIFICAR_PRODUCTOS_RECOMPENSA] ?: ""
        }

    val getUrlVerProductosEnviadosRecompensa: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_VER_PRODUCTOS_ENVIADOS_RECOMPENSA] ?: ""
        }

    val getUrlVerInsertarRecompensa: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_VER_INSERTAR_RECOMPENSA] ?: ""
        }

    val registrarTokenClienteFireBase: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[REGISTRAR_TOKEN_CLIENTE_FIREBASE] ?: ""
        }


    val getUVerificar360: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_U_VERIFICAR_360] ?: ""
        }

    val activarRecompensas: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ACTIVAR_RECOMPENSAS] ?: ""
        }

    val getUrlFolletosQuincenales: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_FOLLETOS_QUINCENALES] ?: ""
        }

    val getUrlObtener360: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_OBTENER_360] ?: ""
        }

    val getUrlSeguimientoGuias: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_SEGUIMIENTO_GUIAS] ?: ""
        }

    val getUrlImagenesAyuda: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_IMAGENES_AYUDA] ?: ""
        }

    val getUrlObtenerWebMundial: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[GET_URL_OBTENER_WEB_MUNDIAL] ?: ""
        }

    val activarWebMundial: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ACTIVAR_WEB_MUNDIAL] ?: ""
        }

    val tituloWebMundial: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[TITULO_WEB_MUNDIAL] ?: ""
        }

    val sGuias: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SGUIAS] ?: ""
        }


    suspend fun setLogin(
        tipo: Int,
        id_user: String,
        id_responsable: String,
        password_user: String,
        id_cedis: String,
        perPrecio: String,
        perExistencia: String,
        perPedidos: String,
        perCaptura: String,
        perCsv: String,
        perCotizacion: String,
        perOtroCarrito: String
    ): Boolean {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                dataStore.edit { preferences ->
                    preferences[TIPO] = tipo
                    preferences[ID_USER] = id_user
                    preferences[PASSWORD_USER] = password_user
                    preferences[ID_RESPONSABLE] = id_responsable
                    preferences[LOGUEADO] = true
                    preferences[ID_CEDIS] = id_cedis
                    preferences[PERMISO_PRECIOS] = perPrecio
                    preferences[PERMISO_EXISTENCIA] = perExistencia
                    preferences[PERMISO_PEDIDOS] = perPedidos
                    preferences[PERMISO_CAPTURA] = perCaptura
                    preferences[PERMISO_CSV] = perCsv
                    preferences[PERMISO_COTIZACION] = perCotizacion
                    preferences[PERMISO_OTRO_CARRITO] = perOtroCarrito
                }
                return@async true // devolver verdadero si todo salió bien
            } catch (e: Exception) {
                return@async false // devolver falso si hubo algún error
            }
        }.await()
    }


    fun setLogout(tipo: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[TIPO] = tipo
                preferences[ID_USER] = ""
                preferences[ID_RESPONSABLE] = ""
                preferences[ID_CEDIS] = ""

                preferences[LOGUEADO] = false
            }
        }
    }


    val logueado: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[LOGUEADO] ?: false
        }


    val tipo: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[TIPO] ?: 0
        }

    val id: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ID_USER] ?: ""
        }

    val password: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PASSWORD_USER] ?: ""
        }

    val idUserResponsable: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ID_RESPONSABLE] ?: ""
        }

    val servidorPrincipal: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SERVIDOR_PRINCIPAL] ?: ""
        }

    val idCedis: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ID_CEDIS] ?: ""
        }

}



















