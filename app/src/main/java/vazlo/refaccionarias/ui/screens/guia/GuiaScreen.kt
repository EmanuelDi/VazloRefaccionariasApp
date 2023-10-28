package vazlo.refaccionarias.ui.screens.guia

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import vazlo.refaccionarias.navigation.NavigationDestination


object GuiaDestination : NavigationDestination {
    override val route = "guia"
    /*override val titleRes = R.string.item_entry_title*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuiaScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("GFG | WebView", color = Color.White) }) },
    ) {
        Column(Modifier.padding(it)) {
            MyContent()
        }
    }
}

// Creating a composable
// function to create WebView
// Calling this function as
// content in the above function
@Composable
fun MyContent(){

    // Declare a string that contains a url
    val mUrl = "https://www.geeksforgeeks.org"

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(mUrl)
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GuiaScreen()
}