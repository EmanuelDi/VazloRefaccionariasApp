package vazlo.refaccionarias.ui.screens.soporteTecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vazlo.refaccionarias.data.local.Sesion

class SoporteTecnicoViewModel (private val sesion: Sesion): ViewModel(){
    var atencionNombre = ""
    var atencionNumero = ""
    var atencionCorreo = ""
    var soporteNombre = ""
    var soporteNumero = ""
    var soporteTecnicoNombre = ""
    var soporteTecnicoCorreo = ""
    var empresaNumero = ""
    var numeroWhatsapp = ""
    var urlSoporteWhatsapp = ""

    //var soporteTooltip = false
//    fun getSoporteTooltip(){
//        viewModelScope.launch {
//            soporteTooltip = sesion.soporteTooltip.first()
//        }
//    }
//    fun soporteTooltipMostrada(){
//        sesion.mostrarSoporteTooltip()
//    }

    init {
        obtenerContactos()
    }

    private fun obtenerContactos(){
        viewModelScope.launch {
            atencionNombre = sesion.atencionNombre.first()
            atencionNumero = sesion.atencionNumero.first()
            atencionCorreo = sesion.atencionCorreo.first()
            soporteNombre = sesion.soporteNombre.first()
            soporteNumero = sesion.soporteNumero.first()
            numeroWhatsapp = sesion.numeroWhats.first()
            urlSoporteWhatsapp = sesion.soporteWhats.first()
            soporteTecnicoNombre = sesion.soporteTecnicoNombre.first()
            soporteTecnicoCorreo = sesion.soporteTecnicoCorreo.first()
            empresaNumero = sesion.empresaNumero.first()
        }
    }
}