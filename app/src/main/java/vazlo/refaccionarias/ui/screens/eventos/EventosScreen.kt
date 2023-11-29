package vazlo.refaccionarias.ui.screens.eventos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.LocationService
import vazlo.refaccionarias.data.model.eventosData.Marcador
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme

object Eventos : NavigationDestination {
    override val route = "Eventos"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun EventosScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    refacCercanasViewModel: RefacCercanasViewModel = viewModel(factory = AppViewModelProvider.Factory),
    locationServices: LocationService
) {
    val context = LocalContext.current
    var lat by remember {
        mutableStateOf(0.0)
    }
    var long by remember {
        mutableStateOf(0.0)
    }
    LaunchedEffect(locationServices) {
        val result = locationServices.getUserLocation(context)
        if (result != null) {
            refacCercanasViewModel.setCoordenadas(
                result.latitude,
                result.longitude
            )
            refacCercanasViewModel.cargarMarcadores()
            lat = result.latitude
            long = result.longitude
        } else {
            navigateBack()
        }
    }

    var refCercanas by remember { mutableStateOf(0) }
    VazloRefaccionariasTheme {
        Scaffold(
            topBar = { RefacCercanasTAB(navigateBack = navigateBack) }
        ) {

            Column(modifier.padding(it)) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Eventos: ${ refacCercanasViewModel.cantEventos }",
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Bold
                    )
                }
                when (refacCercanasViewModel.marcadoresUiState) {
                    is MarcadoresUiState.Loading -> AltScreen(estado = "Cargando")
                    is MarcadoresUiState.Success -> {
                        val marcadores =
                            (refacCercanasViewModel.marcadoresUiState as MarcadoresUiState.Success).marcadores
                        RefacCercanasGoogleMap(
                            modifier = modifier,
                            marcadores = marcadores,
                            latitud = lat,
                            longitud = long
                        )
                        LaunchedEffect(marcadores) {
                            refCercanas = marcadores.size
                        }
                    }
                    is MarcadoresUiState.Error -> AltScreen(estado = "Error")
                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RefacCercanasTAB(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logovazloblanco_sin_texto),
                    contentDescription = "",
                    modifier = modifier.size(30.dp)
                )
                Text(
                    text = stringResource(R.string.eventos),
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

@Composable
private fun RefacCercanasGoogleMap(
    modifier: Modifier = Modifier,
    marcadores: List<Marcador>,
    latitud: Double,
    longitud: Double
) {
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                mapToolbarEnabled = true,
            )
        )
    }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitud, longitud), 10F)
    }

    var show = remember{ mutableStateOf(false) }
    val currentMarcador = remember { mutableStateOf<Marcador?>(null) }

    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(latitud, longitud)),
            title = "Tu ubicación",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )
        marcadores.forEach { marcador ->
            MarkerInfoWindow(
                state = MarkerState(position = LatLng(marcador.lat, marcador.long)),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.eventos),
                onInfoWindowClick = {
                    currentMarcador.value = marcador
                    show.value = true
                }
            ) {
                Box (
                    modifier = modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Row(
                        modifier = modifier
                            .padding(15.dp)
                    ){
                        AsyncImage(
                            model = marcador.urlFoto,
                            error = painterResource(R.drawable.imagen),
                            placeholder = painterResource(R.drawable.imagen),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp)
                        )
                        Column {
                            Text(
                                text = marcador.refacNombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            Text(text = "Telefono: ${marcador.telefono}", color = MaterialTheme.colorScheme.onSurfaceVariant)

                        }
                    }
                }
            }
            if (show.value && currentMarcador.value != null) {
                ImageDialog(
                    close = {
                        show.value = false
                        currentMarcador.value = null
                    },
                    marcador = currentMarcador.value!!
                )
            }
        }
    }
}

@Composable
private fun ImageDialog(
    modifier: Modifier = Modifier,
    marcador: Marcador,
    close: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val minScale = 0.75f
    val maxScale = 3f

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        val newScale = scale * zoomChange
        if (newScale in minScale..maxScale) {
            scale = newScale
        }
        if (scale > 1f) {
            offset += offsetChange
        }
    }

    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onTertiary
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth()) {
                    IconButton(onClick = close) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
                    }
                }
                Spacer(modifier = modifier.height(50.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current).data(marcador.urlFoto)
                            .crossfade(true).build(),
                        error = painterResource(R.drawable.imagen),
                        placeholder = painterResource(R.drawable.downloading),
                        contentDescription = marcador.refacNombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun AltScreen(
    modifier: Modifier = Modifier,
    estado: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (estado.equals("Cargando")){
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(text = estado, fontSize = 25.sp, textAlign = TextAlign.Center)
        }
    }
}