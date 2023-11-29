package vazlo.refaccionarias.data.model.detallesData

import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vazlo.refaccionarias.data.model.homeData.Producto


@Serializable
data class RelacionadoDetalle(
    @SerialName("nombre_marca")
    val nombreMarca: String,
    @SerialName("nombre_modelocarro")
    val nombreModeloCarro: String,
    @SerialName("nombre_cilindraje")
    val nombreCilindraje: String,
    @SerialName("nombre_litro")
    val nombreLitro: String,
    @SerialName("nombre_anio")
    val nombreAnio: String,
    @SerialName("descripcion")
    val descripcion: String,
) {
    companion object {
        val Saver = listSaver<List<RelacionadoDetalle>, Any>(
            save = { lista ->
                lista.flatMap { detalle ->
                    listOf(
                        detalle.nombreMarca,
                        detalle.nombreModeloCarro,
                        detalle.nombreCilindraje,
                        detalle.nombreLitro,
                        detalle.nombreAnio,
                        detalle.descripcion
                    )
                }
            },
            restore = { lista ->
                lista.chunked(6).map {
                    RelacionadoDetalle(
                        it[0] as String,
                        it[1] as String,
                        it[2] as String,
                        it[3] as String,
                        it[4] as String,
                        it[5] as String
                    )
                }
            }
        )
    }
}
