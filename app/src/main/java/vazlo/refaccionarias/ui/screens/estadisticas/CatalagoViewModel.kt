package vazlo.refaccionarias.ui.screens.estadisticas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion

class CatalagoViewModel(private val sesion: Sesion, private val servicesAppRepository: ServicesAppRepository): ViewModel() {

    var permisoEstadoDeCuenta by mutableStateOf(false)
    var permisoFacturas by mutableStateOf(false)
    var permisoComplementos by mutableStateOf(false)
    var permisoNotas by mutableStateOf(false)

    fun verificarPermisos() {
        viewModelScope.launch {
            val idCte = sesion.id.first()
            val url = sesion.getUrlVerificarPermisos.first()
            val response = servicesAppRepository.verificarPermisos(
                url = url,
                idCte = idCte
            )
            if (response.estado == 1) {
                permisoEstadoDeCuenta = response.estadoCuenta == 1
                permisoFacturas = response.permisoFactura == 1
                permisoComplementos = response.permisoComplemento == 1
                permisoNotas = response.permisoNota == 1
            }
        }
    }

    init {
        verificarPermisos()
    }

}