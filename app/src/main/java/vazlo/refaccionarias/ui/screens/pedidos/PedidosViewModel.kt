package vazlo.refaccionarias.ui.screens.pedidos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vazlo.refaccionarias.data.model.pedidoData.InfoPedido
import vazlo.refaccionarias.data.model.pedidoData.ProductoPedido
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.local.Sesion
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

sealed interface PedidosUiState {
    data class Success(val pedidos: MutableList<Pair<InfoPedido, List<ProductoPedido>>>) :
        PedidosUiState

    object Error : PedidosUiState
    object Loading : PedidosUiState
}

class PedidosViewModel(
    private val sesion: Sesion,
    private val servicesAppRepository: ServicesAppRepository
) : ViewModel() {

    var pedidosUiState: PedidosUiState by mutableStateOf(PedidosUiState.Loading)
        private set

    init {
        cargarPedidos()
    }

    private fun cargarPedidos() {
        viewModelScope.launch {
            val gson = Gson()
            val id = sesion.id.first()
            val url = sesion.verPedidosEnviados.first()
            val response = servicesAppRepository.cargarPedidos(url, id)

            val datosOb = response.body()

            if ((datosOb?.get("estado")?.jsonPrimitive?.int ?: 0) == 1) {
                val jsonArray = datosOb?.get("Info_Carrito")?.jsonArray
                val list = mutableListOf<Pair<InfoPedido, List<ProductoPedido>>>()
                var i = 0
                while (i < jsonArray!!.size) {
                    val infoCarrito = gson.fromJson(jsonArray[i].toString(), InfoPedido::class.java)
                    i++
                    val productos =
                        gson.fromJson(jsonArray[i].toString(), Array<ProductoPedido>::class.java)
                            .toList()
                    list.add(Pair(infoCarrito, productos))
                    i++
                }
                pedidosUiState = PedidosUiState.Success(list)
            } else {
                pedidosUiState = PedidosUiState.Error
            }
        }
    }

}