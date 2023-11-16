package vazlo.refaccionarias.data.model.detallesData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Resultado(
    @SerialName("hay360")
    val hay360: String
)

@Serializable
data class Verificar360Response(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("Resultado")
    val resultado: List<Resultado>
)