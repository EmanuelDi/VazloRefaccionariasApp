package vazlo.refaccionarias.ui.screens.home

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.local.Sesion
import vazlo.refaccionarias.data.model.homeData.Producto
import vazlo.refaccionarias.data.model.homeData.Promocion
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository

sealed interface HomeUiState {
    data class Success(val productos: List<Producto>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}


sealed interface BannersState {
    data class Success(val promos: MutableList<Promocion>) : BannersState
    object Error : BannersState
    object Loading : BannersState
}


class HomeViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var esAdmin = false

    var bannerState: BannersState by mutableStateOf(BannersState.Loading)
        private set

    var usuario by mutableStateOf("")

    private var latitud by mutableDoubleStateOf(0.0)
    private var longitud by mutableDoubleStateOf(0.0)

    var hayEventos by mutableStateOf(false)

    var versionApk by mutableIntStateOf(0)

    fun restablecerToolTips() {
        sesion.restablecerToolTips()
    }


    fun getVersion(){
        viewModelScope.launch {
            versionApk = sesion.versionDeApkRefaccionarias.first()
        }
    }

    init {
        getVersion()
        cargarPromos()
        cargarNuevosProductos()
        cargarMarcadores()
        verificarAdmin()
    }


    fun verificarAdmin() {
        viewModelScope.launch {
            val user = sesion.id.first()
            val responsable = sesion.idUserResponsable.first()

            if(user.equals(responsable))
            {
                esAdmin = true
            }
        }
    }

    fun cargarNuevosProductos() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            val url = sesion.articulosNuevos.first()
            val idUser = sesion.id.first()
            usuario = idUser
            val response = servicesAppRepository.cargarProductosNuevos(url = url, idUser = idUser)
            homeUiState = if (response.estado == 1) {
                HomeUiState.Success(response.clientes)
            } else {
                HomeUiState.Error
            }
        }
    }

    fun cargarPromos(){
        viewModelScope.launch {
            val url = sesion.menuListaPromociones.first()
            val idUser = sesion.id.first()

            val response = servicesAppRepository.getPromos(url, idUser)

            bannerState = if (response.estado == 1) {
                BannersState.Success(response.promociones)
            } else {
                BannersState.Error
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            sesion.setLogout(0)
        }
    }

    private fun cargarMarcadores() {
        viewModelScope.launch {
            val url = sesion.menuMarcadoresEventos.first()
            val response = servicesAppRepository.getMarcadores(url = url, lat = latitud, long = longitud)
            hayEventos = response.estado == 1
        }
    }

}