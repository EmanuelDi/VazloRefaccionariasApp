package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sucursal(
    @SerialName("nombre")
    val id: String? = null,
    @SerialName("existencia")
    val existencia: String? = null,
)
