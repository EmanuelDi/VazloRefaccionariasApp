package vazlo.refaccionarias.ui.screens.resultadoPorPartes


import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.busquedasData.ProductosResult
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.Gris_Vazlo
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme


object ResultadoPorPartesDestination : NavigationDestination {
    override val route = "resultado_por_parte"
    const val criterio = "criterio"
    const val funcArg = "funcArg"
    val routeWithArgs = "$route/{$criterio}/{$funcArg}"
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ResultadoPorPartesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToDetallesParte: (String) -> Unit,
    resultadoPorPartesViewModel: ResultadoPorPartesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelCompartido: ProductoCompartidoViewModel
) {
    var show by rememberSaveable { mutableStateOf(false) }
    var cantProductos by remember { mutableStateOf(0) }
    VazloRefaccionariasTheme {
        Scaffold(topBar = {
            ResultadoPorPartesTopBar(modifier = modifier, navigateBack = navigateBack)
        }) {
            var show by remember { mutableStateOf(false) }
            var cantProductos by remember { mutableStateOf(0) }
            Column(modifier = modifier.padding(it)) {

                when (resultadoPorPartesViewModel.resultadoPartesUiState) {
                    is ResultadoParteUiState.Loading -> {
                        AltScreen(modifier = modifier)
                    }

                    is ResultadoParteUiState.Success -> {
                        val productos =
                            (resultadoPorPartesViewModel.resultadoPartesUiState as ResultadoParteUiState.Success).productos
                        cantProductos = resultadoPorPartesViewModel.totalProductos
                        /*Log.i("sos1", "Encontrados: ${productos.size}")*/
                        Content(
                            navigateToDetallesParte = navigateToDetallesParte,
                            productos = productos,
                            viewModelCompartido = viewModelCompartido
                        )
                        LaunchedEffect(cantProductos) {
                            show = true
                        }
                    }

                    is ResultadoParteUiState.Error -> {
                        ErrorScreenCart(
                            modifier = modifier.padding(horizontal = 20.dp),
                            texto = "Parece que no se encontraron productos en base a tu búsqueda"
                        )
                    }

                    else -> {}
                }
            }
            if (show) {
                ProductosDialog(
                    modifier = modifier,
                    onDismiss = { show = false },
                    mensaje = "Se encontraron: $cantProductos productos"
                )
            }
        }
    }
}

@Composable
private fun ProductosDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    mensaje: String
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Resultados de Búsqueda",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Aceptar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Composable
fun ErrorScreenCart(modifier: Modifier = Modifier, texto: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = texto,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Justify
        )
    }
}


@Composable
fun AltScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    productos: List<ProductosResult>,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    val productosList = productos.groupBy {
        Triple(
            first = it.nombreMarca,
            second = it.nombreModeloCarro,
            third = it.cillitModSopId
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        productosList.forEach { (keys, productos) ->
            item {
                HeaderTipo(titulo = "${keys.first} - ${keys.second} - ${keys.third}")
            }
            items(productos) { producto ->
                Productos(
                    producto = producto,
                    navigateToDetallesParte = navigateToDetallesParte,
                    viewModelCompartido = viewModelCompartido
                )
            }
        }
    }
}

@Composable
fun HeaderTipo(
    modifier: Modifier = Modifier,
    titulo: String
) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 17.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoPorPartesTopBar(modifier: Modifier, navigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logovazloblanco_sin_texto),
                contentDescription = "",
                modifier = modifier.size(30.dp)
            )
            Text(
                text = stringResource(R.string.refaccionarias),
                fontWeight = FontWeight.Bold,
            )
        }
    }, actions = {
        IconButton(onClick = { navigateBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }/*IconButton(onClick = { *//*TODO*//* }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = ""
                )
            }*/

        }, colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun Productos(
    modifier: Modifier = Modifier,
    producto: ProductosResult,
    navigateToDetallesParte: (String) -> Unit,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = Gris_Vazlo)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    viewModelCompartido.setProducto(producto)
                    viewModelCompartido.setSucursales(producto.sucursales!!)
                    navigateToDetallesParte.invoke(producto.nombreSoporte)
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .data(producto.urlSoporte)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.imagen),
                placeholder = painterResource(R.drawable.imagen),
                contentDescription = producto.descripcion,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = producto.nombreSoporte,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(
                        R.string.desc_prod_resultados,
                        producto.nombrePosicion,
                        producto.nombreCilidraje!!,
                        producto.nombreLitro,
                        producto.aIni,
                        producto.aFin,
                        producto.descripcion
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ResultadoPorPartesPreview() {
    ResultadoPorPartesScreen()
}*/

