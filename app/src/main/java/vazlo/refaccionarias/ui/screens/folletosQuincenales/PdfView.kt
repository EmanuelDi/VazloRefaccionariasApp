package vazlo.refaccionarias.ui.screens.folletosQuincenales

import android.os.Build
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.navigation.NavigationDestination


object PdfDestination : NavigationDestination {
    override val route = "pdf_view"
    const val pdf = "pdf"
    val routeWithArgs = "$route/{$pdf}"
    /* override val titleRes = R.string.app_name*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PdfScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
    navigateHome: () -> Boolean,
    pdfViewViewModel: PdfViewViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = { PdfTopBar(navigateBack = navigateBack, navigateHome = navigateHome) }
    ) {
        Box(modifier = modifier.padding(it).fillMaxSize()) {
            PdfViewContainer(url = pdfViewViewModel.pdfUrl)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
    navigateHome: () -> Boolean
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = modifier.fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vazlo_blanco),
                    contentDescription = "",
                    modifier = modifier.size(30.dp)
                )
                Text(text = stringResource(R.string.refaccionarias))
            }
        },
        actions = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        },
        modifier = modifier.height(50.dp)
        /* colors = TopAppBarDefaults.smallTopAppBarColors(
             containerColor = MaterialTheme.colorScheme.secondaryContainer
         )*/
    )

}


@Composable
fun PdfViewContainer(url: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
                builtInZoomControls = true
                displayZoomControls = false
            }
            loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        }
    })
}