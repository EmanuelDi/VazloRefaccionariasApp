package vazlo.refaccionarias.ui.screens.guia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GuiasViewModel(
    private val sesion: Sesion
) : ViewModel() {

    var idCliente = ""
        private set

    init {
        setGuias()
    }
    private fun setGuias(){
        viewModelScope.launch {
            idCliente=sesion.id.first()
        }
    }



}