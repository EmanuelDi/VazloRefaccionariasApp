package vazlo.refaccionarias.ui.screens.conversiones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConversionesViewModel(
) : ViewModel() {

    var busqueda by mutableStateOf("")
        private set

    fun onCriterioChange(valor: String) {
        busqueda = valor
    }
}