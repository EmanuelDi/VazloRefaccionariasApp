package vazlo.refaccionarias.ui.screens.estadisticas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.local.Sesion

class ApartadosWebViewModel(private val sesion: Sesion, savedStateHandle: SavedStateHandle): ViewModel() {

    val apartadoUrl: String = checkNotNull(savedStateHandle[ApartadosDestination.url])

    var idCliente by mutableStateOf("")

    fun getUser()  {
        viewModelScope.launch {
            idCliente =  sesion.id.first()
        }
    }

    init {
        getUser()
    }

}