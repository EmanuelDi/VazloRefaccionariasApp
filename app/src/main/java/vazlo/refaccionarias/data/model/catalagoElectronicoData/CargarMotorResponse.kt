package vazlo.refaccionarias.data.model.catalagoElectronicoData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CargarMotorResponse(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("motor")
    val motores: List<Motor>
)