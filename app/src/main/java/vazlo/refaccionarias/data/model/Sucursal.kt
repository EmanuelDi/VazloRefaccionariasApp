package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sucursal(
    @SerialName("nombre")
    val nombre: String? = null,
    @SerialName("existencia")
    val existencia: String? = null,
    @SerialName("id")
    val idSuc: String? = null
)
