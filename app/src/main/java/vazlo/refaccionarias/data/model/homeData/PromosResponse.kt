package vazlo.refaccionarias.data.model.homeData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PromosResponse(
    @SerialName("clientes") val promociones: MutableList<Promocion>,
    @SerialName("estado") val estado: Int,
    @SerialName("mensaje") val mensaje: String
)


@Serializable
data class Promocion(
    @SerialName("descripcion") val descripcion: String,
    @SerialName("foto") val foto: String
)