package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Empresas(
    @SerialName("empresa")
    val empresa: String
)