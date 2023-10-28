package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Modelos(
    @SerialName("modelocarro_id")
    val modelocarro_id: String,
    @SerialName("nombre_modelocarro")
    val nombre_modelocarro: String
)