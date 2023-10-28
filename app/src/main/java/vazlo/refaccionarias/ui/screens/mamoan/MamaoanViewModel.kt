package vazlo.refaccionarias.ui.screens.mamoan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MamoanViewModel: ViewModel() {
    var criterio by mutableStateOf("")
        private set

    fun onCriterioChange(busqueda: String) {
        criterio = busqueda
    }
}