package vazlo.refaccionarias.data.model

import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductosResult(
    @SerialName("nombre_marca")
    val nombreMarca: String,
    @SerialName("Linea")
    val linea: String? = "",
    @SerialName("linea_id")
    val linea_id: String? = "",
    @SerialName("nombre_modelocarro")
    val nombreModeloCarro: String,
    @SerialName("nombre_cilindraje")
    val nombreCilidraje: String? = "",
    @SerialName("nombre_litro")
    val nombreLitro: String,
    @SerialName("a_ini")
    val aIni: String,
    @SerialName("a_fin")
    val aFin: String,
    @SerialName("nombre_posicion")
    val nombrePosicion: String,
    @SerialName("nombre_soporte")
    val nombreSoporte: String,
    @SerialName("status_existencia")
    val status_existencia: String? = "",
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("cillitmodsop_id")
    val cillitModSopId: String? = "",
    @SerialName("soporte_id")
    val soporte_id: String? = "",
    @SerialName("nombre_empresa")
    val nombre_empresa: String? = "",
    @SerialName("disponible")
    val disponible: String? = "",
    @SerialName("sucursales")
    val sucursales: List<Sucursal>? = emptyList(),
    @SerialName("precio")
    val precio: String? = "",
    @SerialName("url_soporte")
    val urlSoporte: String? = "",
){
    companion object {
        val Saver = listSaver<ProductosResult, Any>(
            save = {
                listOf(
                    it.nombreMarca!!,
                    it.linea!!,
                    it.linea_id!!,
                    it.nombreModeloCarro!!,
                    it.nombreCilidraje!!,
                    it.nombreLitro!!,
                    it.aIni!!,
                    it.aFin!!,
                    it.nombrePosicion!!,
                    it.nombreSoporte!!,
                    it.status_existencia!!,
                    it.descripcion!!,
                    it.cillitModSopId!!,
                    it.soporte_id!!,
                    it.nombre_empresa!!,
                    it.disponible!!,
                    it.sucursales!!,
                    it.precio!!,
                    it.urlSoporte!!
                )
            },
            restore = {
                ProductosResult(
                    it[0] as String,
                    it[1] as String?,
                    it[2] as String?,
                    it[3] as String,
                    it[4] as String,
                    it[5] as String,
                    it[6] as String,
                    it[7] as String,
                    it[8] as String,
                    it[9] as String,
                    it[10] as String,
                    it[11] as String,
                    it[12] as String,
                    it[13] as String?,
                    it[14] as String?,
                    it[15] as String?,
                    it[16] as List<Sucursal>?,
                    it[17] as String?,
                    it[18] as String?
                )
            }
        )
    }
}
