package vazlo.refaccionarias.ui.screens.resultadoPorPartes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vazlo.refaccionarias.data.model.busquedasData.ProductosResult
import vazlo.refaccionarias.data.model.detallesData.RelacionadoDetalle
import vazlo.refaccionarias.data.model.detallesData.Sucursal

class ProductoCompartidoViewModel: ViewModel() {
    private var productoCompartido: ProductosResult by mutableStateOf(
            ProductosResult(
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
        )
    )

    private var sucursalesCompartidas: List<Sucursal> by mutableStateOf(emptyList())

    private var tablasCompartidas: List<RelacionadoDetalle> by mutableStateOf(emptyList())

    fun setProducto(producto: ProductosResult){
        productoCompartido = producto
    }

    fun getProducto(): ProductosResult {
        return productoCompartido
    }

    fun setSucursales(sucursales: List<Sucursal>){
        sucursalesCompartidas = sucursales
    }

    fun setTabla(tabla: List<RelacionadoDetalle>){
        tablasCompartidas = tabla
    }

    fun getSuc(): List<Sucursal> {
        return sucursalesCompartidas
    }

    fun getTab(): List<RelacionadoDetalle> {
        return tablasCompartidas
    }
}