package vazlo.refaccionarias.ui.screens.catalagoElectronico

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.catalagoElectronicoData.Anio
import vazlo.refaccionarias.data.model.catalagoElectronicoData.Marcas
import vazlo.refaccionarias.data.model.catalagoElectronicoData.Modelos
import vazlo.refaccionarias.data.model.catalagoElectronicoData.Motor
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


sealed interface BusqCatElecUiState {
    data class Success(val aniosResponse: List<Anio>) : BusqCatElecUiState
    object Loading : BusqCatElecUiState

    object Error : BusqCatElecUiState
}

class CatElectronicoViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
): ViewModel() {
    var busqCatElecUiState: BusqCatElecUiState by mutableStateOf(BusqCatElecUiState.Loading)
    var anios: List<Anio> = listOf(Anio("", ""))
    var anioSeleccionado by mutableStateOf("")
    var marcas: List<Marcas> = listOf(Marcas("", ""))
    var marcaSeleccionada by mutableStateOf("")
    var idMarcaPrimera by mutableStateOf("")
    var marcaPrimera by mutableStateOf("")
    var modelos: List<Modelos> = listOf(Modelos("", ""))
    var modeloPrimero by mutableStateOf("")
    var idModeloPrimero by mutableStateOf("")
    var motores: List<Motor> = mutableListOf(Motor("", "", "", ""))
    var motorPrimero by mutableStateOf("")
    var idCilindrajePrimero by mutableStateOf("")
    var idLitroPrimero by mutableStateOf("")
    init {
        cargarAnios()
    }

    private fun cargarAnios() {
        viewModelScope.launch {
            val url = sesion.catElecAnios.first()
            val response = servicesAppRepository.getAnios(url)
            busqCatElecUiState = if (response.estado == 1) {
                BusqCatElecUiState.Success(response.anios)
            } else {
                BusqCatElecUiState.Error
            }
        }
    }

    fun cargarMarcas(anio: String) {
        viewModelScope.launch {
            val url = sesion.catElecMarca.first()
            anioSeleccionado = anio
            val response = servicesAppRepository.getMarcas(url, anio)
            marcas = response.marcas
            if (marcas.isNotEmpty()) {
                marcaPrimera = marcas[0].nombre_marca
                idMarcaPrimera = marcas[0].marca_id
                cargarModelos(idMarcaPrimera)
            }
        }

    }

    fun cargarModelos(marca: String) {
        viewModelScope.launch {
            val url = sesion.catElecModelo.first()
            val response = servicesAppRepository.getModelos(url, anioSeleccionado, marca)
            marcaSeleccionada = marca
            modelos = response.modelos
            if(modelos.isNotEmpty()){
                modeloPrimero = modelos[0].nombre_modelocarro
                idModeloPrimero = modelos[0].modelocarro_id
                cargarMotores(idModeloPrimero)
            }
        }
    }

    fun cargarMotores(modelo: String) {
        viewModelScope.launch {
            val url = sesion.catElectMotor.first()
            val response =
                servicesAppRepository.getMotores(url, anioSeleccionado, marcaSeleccionada, modelo)
            motores = response.motores
            (motores as MutableList).add(0, Motor("*", "_", "_", "*"))
            if(motores.isNotEmpty()){
                motorPrimero = motores[0].cilindraje_id+" "+motores[0].litro_id
                idCilindrajePrimero = motores[0].nombre_cilindraje
                idLitroPrimero = motores[0].nombre_litro
            }
        }
    }

    fun validateInput(): Boolean{
        return anio.isNotBlank()
    }

    var anio by mutableStateOf("")
        private set

    fun onAnioChange(anio: String) {
        this.anio = anio
    }

    var marca by mutableStateOf("")
        private set

    fun onMarcaChange(marca: String) {
        this.marca = marca
    }

    var modelo by mutableStateOf("")
        private set

    fun onModeloChange(modelo: String) {
        this.modelo = modelo
    }

    var cilindraje by mutableStateOf("")
        private set

    fun onCilindrajeChange(cilindraje: String) {
        this.cilindraje = cilindraje
    }

    var litros by mutableStateOf("")
        private set

    fun onLitrosChange(litros: String) {
        this.litros = litros
    }

    fun limpiar(){
        marcaPrimera = ""
        modeloPrimero = ""
        idCilindrajePrimero = ""
        idLitroPrimero = ""
        motorPrimero = ""
        anio = ""
    }

}
