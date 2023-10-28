package vazlo.refaccionarias.ui.screens.catalagoElectronico

import androidx.annotation.StringRes
import vazlo.refaccionarias.R


sealed class TabItem(@StringRes var title: Int) {
    object Eagle : TabItem(title = R.string.eagle_name)
    object Shark : TabItem(title = R.string.shark_name)
    object Partech : TabItem(title = R.string.partech_name)
    object Rodatech : TabItem(title = R.string.rodatech_name)
    object TrackOne : TabItem(title = R.string.track_namae)

}

