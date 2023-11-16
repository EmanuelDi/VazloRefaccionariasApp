package vazlo.refaccionarias.data.model.catalagoElectronicoData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CargarAniosResponse(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("anios")
    val anios: List<Anio>
)