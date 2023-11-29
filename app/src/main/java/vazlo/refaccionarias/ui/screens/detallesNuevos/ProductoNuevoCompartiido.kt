package vazlo.refaccionarias.ui.screens.detallesNuevos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.detallesData.RelacionadoDetalle
import vazlo.refaccionarias.data.model.detallesData.Sucursal
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

    private var sucursalesCompartidas: List<Sucursal> by mutableStateOf(emptyList())

    private var tablasCompartidas: List<RelacionadoDetalle> by mutableStateOf(emptyList())

    fun setProducto(producto: Producto){
        productoCompartido = producto
    }

    fun setSucursales(sucursales: List<Sucursal>){
        sucursalesCompartidas = sucursales
    }

    fun setTabla(tabla: List<RelacionadoDetalle>){
        tablasCompartidas = tabla
    }

    fun getProducto(): Producto {
        return productoCompartido
    }

    fun getSuc(): List<Sucursal> {
        return sucursalesCompartidas
    }

    fun getTab(): List<RelacionadoDetalle> {
        return tablasCompartidas
    }
}