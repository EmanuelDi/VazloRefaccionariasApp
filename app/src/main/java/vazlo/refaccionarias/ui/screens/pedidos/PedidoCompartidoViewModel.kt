package vazlo.refaccionarias.ui.screens.pedidos

import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.pedidoData.InfoPedido
import vazlo.refaccionarias.data.model.pedidoData.ProductoPedido

class PedidoCompartidoViewModel: ViewModel() {
    var productos: Pair<InfoPedido, List<ProductoPedido>>? = null
}