package vazlo.refaccionarias.ui.screens.folletosQuincenales

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class PdfViewViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    val pdfUrl: String = checkNotNull(savedStateHandle[PdfDestination.pdf])

}