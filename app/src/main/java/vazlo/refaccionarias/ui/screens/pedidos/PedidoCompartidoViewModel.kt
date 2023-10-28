package vazlo.refaccionarias.ui.screens.pedidos

import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.InfoPedido
import vazlo.refaccionarias.data.model.ProductoPedido

class PedidoCompartidoViewModel: ViewModel() {
    var productos: Pair<InfoPedido, List<ProductoPedido>>? = null
}