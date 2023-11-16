package vazlo.refaccionarias.data.model.catalagoElectronicoData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anio(
    @SerialName("id_anio")
    val id_anio: String,
    @SerialName("ani_nombre")
    val ani_nombre: String
)
