package vazlo.refaccionarias.ui.screens.guia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.local.Sesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GuiasViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel(

) {

    var idCliente = ""
        private set
    var urlGuias = ""
        private set
    init {
        setGuias()
    }
    fun setGuias(){
        viewModelScope.launch {
            idCliente=sesion.id.first()
        }
    }



}