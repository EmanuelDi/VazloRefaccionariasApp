package vazlo.refaccionarias.ui.screens.busquedaPorPartes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BusquedaPorParteViewModel(
) : ViewModel() {

    var busqueda by mutableStateOf("")
        private set

    fun onCriterioChange(valor: String) {
        busqueda = valor
    }
}