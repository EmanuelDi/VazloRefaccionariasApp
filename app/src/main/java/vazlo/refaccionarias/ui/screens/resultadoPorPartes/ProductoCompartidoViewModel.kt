package vazlo.refaccionarias.ui.screens.resultadoPorPartes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.ProductosResult
import vazlo.refaccionarias.data.model.Sucursal

class ProductoCompartidoViewModel: ViewModel() {
    private var productoCompartido: ProductosResult by mutableStateOf(ProductosResult(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        sucursales = emptyList()
    ))

    fun setProducto(producto: ProductosResult){
        productoCompartido = producto
    }

    fun getProducto(): ProductosResult {
        return productoCompartido
    }
}