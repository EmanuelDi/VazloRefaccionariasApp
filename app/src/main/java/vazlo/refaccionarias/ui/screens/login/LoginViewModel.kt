package vazlo.refaccionarias.ui.screens.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.repositorios.WebServicesRefacRepository
import vazlo.refaccionarias.data.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

interface LogeandoState {
    object Success : LogeandoState
    data class  Error(val mensaje: String) : LogeandoState
    object Loading : LogeandoState
    object NoTry: LogeandoState
}




@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class LoginViewModel(
    private val webServicesRefacRepository: WebServicesRefacRepository,
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {

    var logeado: LogeandoState by mutableStateOf(LogeandoState.NoTry)

    var datosOb: JsonObject? = null
    var usuario by mutableStateOf("")
        private set

    var contrasenia by mutableStateOf("")
        private set

    var userActual by mutableStateOf("")
    var contraseniaActual by mutableStateOf("")

    var errorEnWebServices by mutableStateOf(false)


    suspend fun getSesionActual() {
        userActual = sesion.id.first()
        contraseniaActual = sesion.password.first()

        if (sesion.logueado.first()) {
            loginAuto()
        }
    }


    suspend fun loginAuto(): Int {
        logeado = LogeandoState.Loading
        val url = sesion.loginCliente.first()
        val response =
            servicesAppRepository.login(url = url, usuario = userActual, clave = contraseniaActual)

        val logear = sesion.setLogin(
            tipo = 2,
            id_user = response.usuario,
            password_user = contraseniaActual,
            id_cedis = response.cedis!!,
            id_responsable = response.responsable!!,
            perCaptura = response.captura!!,
            perCotizacion = response.cotizacion!!,
            perCsv = response.csv!!,
            perExistencia = response.existencia!!,
            perOtroCarrito = response.otroCarrito!!,
            perPedidos = response.pedidos!!,
            perPrecio = response.precio!!
        )
        return if ((response.estado) == 1) {
            if(logear) {
                logeado = LogeandoState.Success
            }
            response.estado
        } else {
            logeado = LogeandoState.Error("No se pudo iniciar sesión")
            response.estado
        }
    }


    init {
        viewModelScope.launch {
            getWebSerivce()
            delay(500)
            getSesionActual()
        }
    }


    fun onUsuarioChanged(user: String) {
        usuario = user
    }

    fun onPassChanged(pass: String) {
        contrasenia = pass
    }

    fun clearCampos() {
        usuario = ""
        contrasenia = ""
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getWebSerivce() {
        viewModelScope.launch {
            val response = webServicesRefacRepository.getWebServices()
            try {
                if (response.isSuccessful) {
                    datosOb = response.body()
                    if ((datosOb?.get("estado")?.jsonPrimitive?.int ?: 0) == 1) {
                        sesion.setUrlParaConexiones(
                            u1 = datosOb?.get("ImagenCarrito")?.jsonPrimitive?.content ?: "",
                            u2 = datosOb?.get("FragmentsLineasExtension2023")?.jsonPrimitive?.content ?: "",
                            u3 = datosOb?.get("listaArticulosNuevosCuadrados")?.jsonPrimitive?.content ?: "",
                            u4 = datosOb?.get("AgregarProduCarritoV2023")?.jsonPrimitive?.content ?: "",
                            u5 = datosOb?.get("EliminarProdCarritoV2023")?.jsonPrimitive?.content ?: "",
                            u6 = datosOb?.get("ePedidoApp")?.jsonPrimitive?.content ?: "",
                            u7 = datosOb?.get("VaciarCarrito")?.jsonPrimitive?.content ?: "",
                            u8 = datosOb?.get("VerCarrito_junio23")?.jsonPrimitive?.content ?: "",
                            u9 = datosOb?.get("RastreoPedCarrito")?.jsonPrimitive?.content ?: "",
                            u10 = datosOb?.get("MenuListaPromociones2023")?.jsonPrimitive?.content ?: "",
                            u11 = datosOb?.get("VerPedidosEnviados")?.jsonPrimitive?.content ?: "",
                            u12 = datosOb?.get("TabsEmpresas")?.jsonPrimitive?.content ?: "",
                            u13 = datosOb?.get("getURLConversiones2023")?.jsonPrimitive?.content ?: "",
                            u14 = datosOb?.get("getURLBParte2023")?.jsonPrimitive?.content ?: "",
                            u15 = datosOb?.get("getURLMamoan2023")?.jsonPrimitive?.content ?: "",
                            u16 = datosOb?.get("BajaMensajeNotificacion")?.jsonPrimitive?.content ?: "",
                            u17 = datosOb?.get("VerMensajeNotificaciones")?.jsonPrimitive?.content ?: "",
                            u18 = datosOb?.get("VerMensajeUno")?.jsonPrimitive?.content ?: "",
                            u19 = datosOb?.get("CambiarEstadoMensaje")?.jsonPrimitive?.content ?: "",
                            u20 = datosOb?.get("RegistrarTokenCliente")?.jsonPrimitive?.content ?: "",
                            u21 = datosOb?.get("LoginCliente")?.jsonPrimitive?.content ?: "",
                            u22 = datosOb?.get("CatElecAnios")?.jsonPrimitive?.content ?: "",
                            u23 = datosOb?.get("CatElecMarca")?.jsonPrimitive?.content ?: "",
                            u24 = datosOb?.get("CatElecModelo")?.jsonPrimitive?.content ?: "",
                            u25 = datosOb?.get("CatElecMotor")?.jsonPrimitive?.content ?: "",
                            u26 = datosOb?.get("CatalogoBusquedaDetalle")?.jsonPrimitive?.content ?: "",
                            u27 = datosOb?.get("NumeroWhats")?.jsonPrimitive?.content ?: "",
                            u28 = datosOb?.get("ImagenNoMapa")?.jsonPrimitive?.content ?: "",
                            u29 = datosOb?.get("MenuMarcadoresEventos")?.jsonPrimitive?.content ?: "",
                            u30 = datosOb?.get("DirMapaCoordenadas")?.jsonPrimitive?.content ?: "",
                            u31 = datosOb?.get("VERSION_DE_APK_REFACCIONARIAS")?.jsonPrimitive?.int ?: 0,
                            u32 = datosOb?.get("getURLCargarUsuarios")?.jsonPrimitive?.content ?: "",
                            u33 = datosOb?.get("getURLActualizarPassword")?.jsonPrimitive?.content ?: "",
                            u34 = datosOb?.get("getURLNuevoUsuario")?.jsonPrimitive?.content ?: "",
                            u35 = datosOb?.get("getURLActualizarUsuario")?.jsonPrimitive?.content ?: "",
                            u36 = datosOb?.get("getURLEliminarUsuario")?.jsonPrimitive?.content ?: "",
                            u37 = datosOb?.get("getURLVerificarPermisos")?.jsonPrimitive?.content ?: "",
                            u38 = datosOb?.get("getURLActualizarPermisos2023")?.jsonPrimitive?.content ?: "",
                            u39 = datosOb?.get("AtencionNombre")?.jsonPrimitive?.content ?: "",
                            u40 = datosOb?.get("AtencionNumero")?.jsonPrimitive?.content ?: "",
                            u41 = datosOb?.get("AtencionCorreo")?.jsonPrimitive?.content ?: "",
                            u42 = datosOb?.get("SoporteNombre")?.jsonPrimitive?.content ?: "",
                            u43 = datosOb?.get("SoporteNumero")?.jsonPrimitive?.content ?: "",
                            u44 = datosOb?.get("SoporteTecnicoNombre")?.jsonPrimitive?.content ?: "",
                            u45 = datosOb?.get("SoporteTecnicoCorreo")?.jsonPrimitive?.content ?: "",
                            u46 = datosOb?.get("EmpresaNumero")?.jsonPrimitive?.content ?: "",
                            u47 = datosOb?.get("getURLSoporteWhats")?.jsonPrimitive?.content ?: "",
                            u48 = datosOb?.get("getURLVerificarPuntosRecompensa")?.jsonPrimitive?.content ?: "",
                            u49 = datosOb?.get("getURLVerificarProductosRecompensa")?.jsonPrimitive?.content ?: "",
                            u50 = datosOb?.get("getURLVerProductosEnviadosRecompensa")?.jsonPrimitive?.content ?: "",
                            u51 = datosOb?.get("getURLVerInsertarRecompensa")?.jsonPrimitive?.content ?: "",
                            u52 = datosOb?.get("RegistrarTokenClienteFirebase")?.jsonPrimitive?.content ?: "",
                            u53 = datosOb?.get("getUVerificar360")?.jsonPrimitive?.content ?: "",
                            u54 = datosOb?.get("activarRecompensas")?.jsonPrimitive?.content ?: "",
                            u55 = datosOb?.get("getURLFolletosQuincenales")?.jsonPrimitive?.content ?: "",
                            u56 = datosOb?.get("getURLObtener360")?.jsonPrimitive?.content ?: "",
                            u57 = datosOb?.get("getURLSeguimientoGuias")?.jsonPrimitive?.content ?: "",
                            u58 = datosOb?.get("getURLImagenesAyuda")?.jsonPrimitive?.content ?: "",
                            u59 = datosOb?.get("getURLObtenerWebMundial")?.jsonPrimitive?.content ?: "",
                            u60 = datosOb?.get("activarWebMundial")?.jsonPrimitive?.content ?: "",
                            u61 = datosOb?.get("tituloWebMundial")?.jsonPrimitive?.content ?: "",
                            u62 = datosOb?.get("sguias")?.jsonPrimitive?.content ?: "",
                            u63 = datosOb?.get("backOrder")?.jsonPrimitive?.content ?: ""
                        )

                    } else {

                    }
                } else {
                    // Manejar el caso de una respuesta no exitosa
                }
            } catch (e: UnknownHostException) {
               errorEnWebServices = true
            } catch (e: ConnectException) {
                errorEnWebServices = true
            }
        }
    }


    suspend fun login(): Int {
        logeado = LogeandoState.Loading
        val url = sesion.loginCliente.first()
        val response =
            servicesAppRepository.login(url = url, usuario = usuario, clave = contrasenia)

        try {
            val logear = sesion.setLogin(
                tipo = 2,
                id_user = response.usuario,
                password_user = contrasenia,
                id_cedis = response.cedis!!,
                id_responsable = response.responsable!!,
                perCaptura = response.captura!!,
                perCotizacion = response.cotizacion!!,
                perCsv = response.csv!!,
                perExistencia = response.existencia!!,
                perOtroCarrito = response.otroCarrito!!,
                perPedidos = response.pedidos!!,
                perPrecio = response.precio!!
            )

            return if ((response.estado) == 1) {
               if(logear) {
                   logeado = LogeandoState.Success
               }
                response.estado
            } else {
                logeado = LogeandoState.Error("No se pudo iniciar sesión")
                response.estado
            }
        } catch (e: HttpException) {
            logeado = LogeandoState.Error("Ha ocurrido un error inesperado")
            return 0
        } catch (e: IOException) {
            logeado = LogeandoState.Error("No se pudo iniciar sesion revisa tu conexión a internet")
            return 0
        }
    }



    fun logout() {
        sesion.setLogout(0)
    }

}