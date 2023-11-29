package vazlo.refaccionarias.ui.screens.usuarios_y_permisos


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import vazlo.refaccionarias.data.model.usuariosData.NuevoUsuario
import vazlo.refaccionarias.data.model.usuariosData.Usuario
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion




sealed interface UsuariosUiState {
    data class Success(val usuarios: MutableList<Usuario>) : UsuariosUiState
    object Error : UsuariosUiState
    object Loading : UsuariosUiState
}

class UsuariosViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {


    var busquedaEntry by mutableStateOf("")
    var userSeleccionado by mutableStateOf("")
        private set


    var usuariosUiState: UsuariosUiState by mutableStateOf(UsuariosUiState.Loading)
        private set
    var entryUsuario by mutableStateOf("")
        private set
    var entryNombre by mutableStateOf("")
    var entryPassword by mutableStateOf("")
    var entryDomicilio by mutableStateOf("")
    var entryPoblacion by mutableStateOf("")
    var entryCorreo by mutableStateOf("")
    var entryCorreo1 by mutableStateOf("")

    private var nuevoUsuario: NuevoUsuario? = null





    fun onBusquedaChange(criterio: String){
        busquedaEntry = criterio
    }

    fun onSelectUser(userId: String) {
        userSeleccionado = userId
    }

    fun onUserChange(user: String) {
        entryUsuario = user
    }

    fun onNombreChange(nombre: String) {
        entryNombre = nombre
    }

    fun onPassChange(password: String) {
        entryPassword = password
    }

    fun onDomicilioChange(domicilio: String) {
        entryDomicilio = domicilio
    }

    fun onPoblacionChange(poblacion: String) {
        entryPoblacion = poblacion
    }

    fun onCorreoChange(correo: String) {
        entryCorreo = correo
    }

    fun onCorreo1Change(correo1: String) {
        entryCorreo1 = correo1
    }


    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            val url = sesion.getUrlCargarUsuario.first()
/*
            val url =
                "https://apps.vazloonline.com/web_service//REFACCIONARIAS//usuarios_permisos//cargarUsuarios.php?id_cte=5"
*/
            val response = servicesAppRepository.cargarUsuarios(url = url, sesion.id.first())
            usuariosUiState = if (response.estado == 1) {
                UsuariosUiState.Success(response.infoUsuarios)
            } else {
                UsuariosUiState.Error
            }
        }
    }


    suspend fun setNuevoUsuario(): Boolean {

        nuevoUsuario = NuevoUsuario(
            clienteId = entryUsuario,
            clienteNombre = entryNombre,
            clienteResponsable = sesion.id.first(),
            clientePsw = entryPassword,
            domicilio = entryDomicilio,
            poblacion = entryPoblacion,
            correo = entryCorreo,
            correo1 = entryCorreo1
        )
        val url = sesion.getUrlNuevoUsuario.first()
            val response = servicesAppRepository.nuevoUsuario(
                url = url,
                cliente = nuevoUsuario!!.clienteId!!,
                clienteResponsable = nuevoUsuario!!.clienteResponsable,
                clientePsw = nuevoUsuario!!.clientePsw!!,
                clienteNonbre = nuevoUsuario!!.clienteNombre!!,
                domicilio = nuevoUsuario!!.domicilio!!,
                poblacion = nuevoUsuario!!.poblacion!!,
                correo = nuevoUsuario!!.correo!!,
                correo1 = nuevoUsuario!!.correo1!!
            )

        val datosOb = response.body()!!
        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            cargarUsuarios()
            limpiarEntry()
            true
        } else
            false
    }

    suspend fun actualizarUsuario(): Boolean {

        val response = servicesAppRepository.actualizarUsuario(
            url = sesion.getUrlActualizarUsuario.first(),
            clienteId = userSeleccionado,
            clienteNonbre = entryNombre,
            domicilio = entryDomicilio,
            poblacion = entryPoblacion,
            correo = entryCorreo,
            correo1 = entryCorreo1
        )

        val datosOb = response.body()!!
        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            cargarUsuarios()
            limpiarEntry()
            true
        } else
            false

    }

    suspend fun actualizarPassword(): Boolean {

        val response = servicesAppRepository.actualizarPassword(
            url = sesion.getUrlActualizarPassword.first(),
            clienteId = userSeleccionado,
            clientePsw = entryPassword
        )
        val datosOb = response.body()!!

        return if (datosOb["estado"]?.jsonPrimitive?.int == 1) {
            cargarUsuarios()
            limpiarEntry()
            true
        } else
            false

    }

    suspend fun eliminarUsuario(): Boolean {
        val response = servicesAppRepository.eliminarUsuario(
            url = sesion.getUrlEliminarUsuario.first(),
            cte = userSeleccionado
        )
        val datosOb = response.body()!!
        return datosOb["estado"]?.jsonPrimitive?.int == 1

    }

    fun limpiarEntry() {
        entryUsuario = ""
        entryNombre = ""
        entryPassword = ""
        entryDomicilio = ""
        entryPoblacion = ""
        entryCorreo = ""
        entryCorreo1 = ""
    }
}