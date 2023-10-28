package vazlo.refaccionarias.ui.screens.usuarios_y_permisos

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.usuarios.NuevoUsuario
import vazlo.refaccionarias.data.model.usuarios.Usuario
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

sealed interface UsuariosUiState {
    data class Success(val usuarios: List<Usuario>) : UsuariosUiState
    object Error : UsuariosUiState
    object Loading : UsuariosUiState
}

class UsuariosViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {

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

    var nuevoUsuario: NuevoUsuario? = null

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

    private fun cargarUsuarios() {
        viewModelScope.launch {
            usuariosUiState = UsuariosUiState.Loading
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


    fun setNuevoUsuario() {
        viewModelScope.launch {
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
            Log.i("Amanda", nuevoUsuario!!.clienteNombre!!)
            val url = sesion.getUrlNuevoUsuario.first()
  /*          var urlPrueba = "https://apps.vazloonline.com//web_service//REFACCIONARIAS//usuarios_permisos//usuario_nuevo.php"*/
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
            cargarUsuarios()
            limpiarEntry()
        }

    }

    fun actualizarUsuario() {
        viewModelScope.launch {
            val response = servicesAppRepository.actualizarUsuario(
                url = sesion.getUrlActualizarUsuario.first(),
                clienteId = userSeleccionado,
                clienteNonbre = entryNombre,
                domicilio = entryDomicilio,
                poblacion = entryPoblacion,
                correo = entryCorreo,
                correo1 = entryCorreo1
            )

            cargarUsuarios()
            limpiarEntry()

            val datosOb = response.body()!!
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content ?: "")
        }
    }

    fun actualizarPassword() {
        viewModelScope.launch {
            val response = servicesAppRepository.actualizarPassword(
                url = sesion.getUrlActualizarPassword.first(),
                clienteId = userSeleccionado,
                clientePsw = entryPassword
            )
            val datosOb = response.body()!!
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content ?: "")
        }
    }

    fun eliminarUsuario() {
        viewModelScope.launch {
            val response = servicesAppRepository.eliminarUsuario(
                url = sesion.getUrlEliminarUsuario.first(),
                cte = userSeleccionado
            )
            val datosOb = response.body()!!
            Log.i("MaistroPipe", datosOb["mensaje"]?.jsonPrimitive?.content ?: "")
            cargarUsuarios()
        }
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