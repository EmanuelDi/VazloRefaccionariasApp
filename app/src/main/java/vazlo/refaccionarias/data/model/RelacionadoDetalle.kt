package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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
)
