package vazlo.refaccionarias.ui.screens.busquedaPorPartes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.sharp.MicNone
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import vazlo.refaccionarias.R
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider


object BusquedaPorPartesDestination : NavigationDestination {
    override val route = "busqueda_por_partes"
    /*override val titleRes = R.string.item_entry_title*/
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun BusquedaPorPartesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToResultadoParte: (String, String) -> Unit,
    busquedaPorParteViewModel: BusquedaPorParteViewModel  = viewModel(factory = AppViewModelProvider.Factory)
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val builder = rememberBalloonBuilder {
        setArrowSize(10)
        setArrowPosition(0.0f)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBackgroundColorResource(R.color.teal_200)
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
        setIsVisibleOverlay(true)
        setOverlayColorResource(R.color.overlay)
        setOverlayPadding(10f)
        setOverlayPaddingColorResource(R.color.overlayPadding)
        setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
        setDismissWhenOverlayClicked(false)
        setOverlayShape(BalloonOverlayRoundRect(12f, 12f))
        setLifecycleOwner(lifecycleOwner)
    }
    Scaffold(
        topBar = { PorPartesTopAppBar(navigateBack = navigateBack) }
    ) {
        Column(modifier = modifier.padding(it)) {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.busqueda_por_partes),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            BodyPorPartes(
                navigateToResultadoParte = navigateToResultadoParte,
                viewModel = busquedaPorParteViewModel,
                builder = builder
            )
        }
    }

}

@Composable
fun BodyPorPartes(
    modifier: Modifier = Modifier,
    navigateToResultadoParte: (String, String) -> Unit,
    viewModel: BusquedaPorParteViewModel,
    builder: Balloon.Builder
) {

    val context = LocalContext.current
    val scanner = GmsBarcodeScanning.getClient(context)
    val sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
    val moduleInstallClient = ModuleInstall.getClient(context)
    val optionalModuleApi = GmsBarcodeScanning.getClient(context)
    moduleInstallClient
        .areModulesAvailable(optionalModuleApi)
        .addOnSuccessListener {
            if (!it.areModulesAvailable()) {
                // Instala el scanner si no lo tiene
                val newRequest = ModuleInstallRequest.newBuilder().addApi(scanner).build()
                moduleInstallClient.installModules(newRequest)
                Toast.makeText(context, "Instalando scanner, espere un momento", Toast.LENGTH_SHORT).show()
            } else {
                // Abre el scanner si está instalado
                val scannerInstalado = sharedPreferences.getString("scannerInstalado", "")
                if(scannerInstalado!="1"){
                    Toast.makeText(context, "Scanner instalado", Toast.LENGTH_SHORT).show()
                    val editor = sharedPreferences.edit()
                    // Muestra el mensaje la primera vez
                    editor.putString("scannerInstalado", "1")
                    editor.apply()
                }
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Hubo un error al instalar el scanner", Toast.LENGTH_SHORT).show()
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(horizontal = 10.dp, vertical = 20.dp)
    ) {
        val errorSearch = remember { mutableStateOf(false) }
        EntryForm(
            viewModel = viewModel,
            errorSearch = errorSearch,
            navigateToResultadoParte = navigateToResultadoParte,
            onErrorResolve = { errorSearch.value = false }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {

                Button(
                    onClick = {
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                barcode.rawValue?.let { viewModel.onCriterioChange(it) }
                            }
                            .addOnFailureListener {
                                // Handle failure...
                            }
                            .addOnCanceledListener {
                                // Task canceled
                            }
                            .addOnFailureListener {
                            }
                    },
                    modifier
                        .height(50.dp)
                        .width(150.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.barcode),
                        contentDescription = "",
                        modifier = modifier
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }

                Button(
                    onClick = {
                        if (viewModel.busqueda.isNotBlank()) {
                            navigateToResultadoParte(viewModel.busqueda, "B")
                        } else {
                            errorSearch.value = true
                        }
                    },
                    modifier
                        .height(50.dp)
                        .width(150.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.buscar),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

        Spacer(modifier = Modifier.height(30.dp))
        PartesAyudaCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: BusquedaPorParteViewModel,
    errorSearch: MutableState<Boolean>,
    onErrorResolve: () -> Unit,
    navigateToResultadoParte: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = viewModel.busqueda,
        onValueChange = {
            viewModel.onCriterioChange(it)
            onErrorResolve()
        },
        textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
        label = { Text(text = stringResource(R.string.buscar), style = MaterialTheme.typography.bodyMedium) },
        trailingIcon = {
            VoiceRecognitionButton(navigateToResultadoParte)
        },
        leadingIcon = {
            IconButton(onClick = { viewModel.onCriterioChange("") }) {
                Icon(imageVector = Icons.TwoTone.Clear, contentDescription = "")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                errorSearch.value = focusState.isCaptured
            },
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
        isError = errorSearch.value,
        supportingText = {
            if (errorSearch.value) Text(
                text = "El campo no puede estar vacio",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            ) else Text(
                text = "Ejemplo: 1100",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    )
}

@Composable
fun VoiceRecognitionButton(navigateToResultadoParte: (String, String) -> Unit) {
    val context = LocalContext.current
    val voiceRecognitionResult = remember { mutableStateOf("") }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (matches != null && matches.isNotEmpty()) {
                    voiceRecognitionResult.value = matches[0]
                    navigateToResultadoParte(voiceRecognitionResult.value, "B")
                }
            }
        }

    IconButton(onClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga, 1100")
        }
        launcher.launch(intent)
    }) {
        Icon(imageVector = Icons.Sharp.MicNone, contentDescription = "")
    }

}





@Composable
fun PartesAyudaCard(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ) {
                PartesAyudaTitle()
                PartesButtonExpand(expanded = expanded, onClick = { expanded = !expanded })

            }
            if (expanded) {
                Column(
                    modifier = modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.texto_ayuda),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun PartesButtonExpand(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun PartesAyudaTitle(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono_ayuda),
            contentDescription = "",
            modifier = modifier.size(30.dp),
        )

        Column {
            Text(
                text = stringResource(R.string.ayuda),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.busqueda_por_partes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PorPartesTopAppBar(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
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
                    modifier = modifier.size(33.dp),
                    contentScale = ContentScale.Crop
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
    )
}

/*
@Preview
@Composable
fun PorPartesScreenPreview() {
    RefaccionariasTheme {
        BusquedaPorPartesScreen()
    }
}
*/

