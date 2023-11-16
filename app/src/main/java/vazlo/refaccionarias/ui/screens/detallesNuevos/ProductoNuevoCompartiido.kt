package vazlo.refaccionarias.ui.screens.detallesNuevos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.homeData.Producto

class ProductoNuevoCompartiido: ViewModel() {
    private var productoCompartido: Producto by mutableStateOf(
        Producto(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        tabla = emptyList(),
        sucursales = emptyList()
    )
    )

    fun setProducto(producto: Producto){
        productoCompartido = producto
    }

    fun getProducto(): Producto {
        return productoCompartido
    }
}