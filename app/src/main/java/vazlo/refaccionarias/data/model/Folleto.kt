package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FolletorResponse(
    @SerialName("estado") val estado: Int,
    @SerialName("mensaje") val mensaje: String,
    @SerialName("folletos") val folletos: MutableList<Folleto>
)

@Serializable
data class Folleto(
    @SerialName("nombre") val nombre: String,
    @SerialName("url") val url: String
)
