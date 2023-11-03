package vazlo.refaccionarias.ui.screens.catalagoElectronico

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.ProductosResult
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.catalagoElectronico.EmpresasUiState
import vazlo.refaccionarias.ui.screens.catalagoElectronico.ProductosUiState
import vazlo.refaccionarias.ui.screens.catalagoElectronico.ResultadosCatElViewModel
import vazlo.refaccionarias.ui.screens.catalagoElectronico.TabItem
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ProductoCompartidoViewModel
import vazlo.refaccionarias.ui.theme.Amarillo_Vazlo
import vazlo.refaccionarias.ui.theme.Blanco
import vazlo.refaccionarias.ui.theme.Gris_Vazlo
import vazlo.refaccionarias.ui.theme.Partech
import kotlinx.coroutines.launch
import vazlo.refaccionarias.ui.screens.home.LoadingScreen


object ResultadosCatElDestination : NavigationDestination {
    override val route = "resul-cat-elec"
    const val anio = "anio"
    const val marca = "marca"
    const val modelo = "modelo"
    const val cilindraje = "cilindraje"
    const val litros = "litros"
    val routeWithArgs = "$route/{$anio}/{$marca}/{$modelo}/{$cilindraje}/{$litros}"
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ResultadosCatElScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelCompartido: ProductoCompartidoViewModel,
) {

    val tabs = listOf(
        TabItem.Eagle,
        TabItem.TrackOne,
//        TabItem.Partech,
//        TabItem.Rodatech,
//        TabItem.Shark
    )
    when (resultadosCatElViewModel.empresasUiState) {
        is EmpresasUiState.Loading -> {
            LoadingScreen()
        }

        is EmpresasUiState.Success -> {
            val empresas =
                (resultadosCatElViewModel.empresasUiState as EmpresasUiState.Success).empresas
            val nombresEmpresas = empresas.joinToString(separator = "\n") { it.empresa }
            EmpresasDialog(
                nombresEmpresas = nombresEmpresas,
            )
        }

        is EmpresasUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        tabs.size
    }

    Scaffold(
        topBar = { ResultadosCatElTopAppBar(navigateBack = navigateBack) }
    ) {
        Column(modifier = modifier.padding(it)) {
            Surface() {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TabsResultsCatEl(tabs = tabs, pagerState = pagerState)
                    TabsContent(
                        tabs,
                        pagerState,
                        navigateToDetallesParte,
                        resultadosCatElViewModel = resultadosCatElViewModel,
                        viewModelCompartido = viewModelCompartido
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun TabsContent(
    tabs: List<TabItem>,
    pagerState: PagerState,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    HorizontalPager(
        modifier = Modifier,
        state = pagerState,
        pageSpacing = 0.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        beyondBoundsPageCount = 0,
        key = null,
    ) { page ->
        Surface(color = MaterialTheme.colorScheme.background) {
            when (page) {
                0 -> EagleContent(
                    navigateToDetallesParte = navigateToDetallesParte,
                    resultadosCatElViewModel = resultadosCatElViewModel,
                    viewModelCompartido = viewModelCompartido
                )

                1 -> TrackContent(
                    navigateToDetallesParte = navigateToDetallesParte,
                    resultadosCatElViewModel = resultadosCatElViewModel,
                    viewModelCompartido = viewModelCompartido
                )

                /*2 -> SharkContent(
                    navigateToDetallesParte = navigateToDetallesParte,
                    resultadosCatElViewModel = resultadosCatElViewModel,
                    viewModelCompartido = viewModelCompartido
                )

                3 -> PartechContent(
                    navigateToDetallesParte = navigateToDetallesParte,
                    resultadosCatElViewModel = resultadosCatElViewModel,
                    viewModelCompartido = viewModelCompartido
                )

                4 -> RodatechContent(
                    navigateToDetallesParte = navigateToDetallesParte,
                    resultadosCatElViewModel = resultadosCatElViewModel,
                    viewModelCompartido = viewModelCompartido
                )*/


            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultadosCatElTopAppBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = modifier
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vazlo_blanco),
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(R.string.catalogoelectronico),
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}


@Composable
private fun FancyIndicator(color: Color, modifier: Modifier = Modifier) {
    // Draws a rounded rectangular with border around the Tab, with a 5.dp padding from the edges
    // Color is passed in as a parameter [color]
    HorizontalDivider(
        modifier
            .padding(5.dp)
            .height(2.dp)
            .border(BorderStroke(2.dp, color), RoundedCornerShape(5.dp))
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FancyAnimatedIndicator(tabPositions: List<TabPosition>, pagerState: PagerState) {
    val selectedTabIndex = pagerState.currentPage
    val colors = listOf(
        MaterialTheme.colorScheme.secondary,
        Amarillo_Vazlo,
        Gris_Vazlo,
        Partech,
        Amarillo_Vazlo
    )
    val transition = updateTransition(selectedTabIndex, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            // Handle directionality here, if we are moving to the right, we
            // want the right side of the indicator to move faster, if we are
            // moving to the left, we want the left side to move faster.
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            // Handle directionality here, if we are moving to the right, we
            // want the right side of the indicator to move faster, if we are
            // moving to the left, we want the left side to move faster.
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    val indicatorColor by transition.animateColor(label = "") {
        colors[it % colors.size]
    }

    FancyIndicator(
        // Pass the current color to the indicator
        indicatorColor,
        modifier = Modifier
            // Fill up the entire TabRow, and place the indicator at the start
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            // Apply an offset from the start to correctly position the indicator around the tab
            .offset(x = indicatorStart)
            // Make the width of the indicator follow the animated width as we move between tabs
            .width(indicatorEnd - indicatorStart)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsResultsCatEl(modifier: Modifier = Modifier, pagerState: PagerState, tabs: List<TabItem>) {

    val scope = rememberCoroutineScope()


    val indicator = @Composable { tabPositions: List<TabPosition> ->
        FancyAnimatedIndicator(tabPositions = tabPositions, pagerState = pagerState)
    }


    Column(modifier = modifier.fillMaxWidth()) {
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = indicator,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            divider = {},
            edgePadding = 0.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        /*state = index*/
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = stringResource(id = tab.title),
                            style = MaterialTheme.typography.labelMedium,
                            color = Blanco,
                            fontSize = 15.sp
                        )
                    },
                    modifier = modifier.width(200.dp)
                )
            }
        }

    }
}


@Composable
private fun EagleContent(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    when (resultadosCatElViewModel.productosUiStateEagle) {
        is ProductosUiState.Loading -> {
            LoadingScreen()
        }

        is ProductosUiState.Success -> {
            val productos =
                (resultadosCatElViewModel.productosUiStateEagle as ProductosUiState.Success).productos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = productos[0].nombreMarca + " " + productos[0].nombreModeloCarro,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                val productosList = productos.groupBy {
                    it.linea
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier
                        .padding(top = 8.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    productosList.forEach { (linea, productos) ->
                        item {
                            TittleScreens(
                                modifier = modifier,
                                soporte = linea ?: ""
                            )
                        }
                        items(productos) { producto ->
                            CardProducto(
                                producto = producto,
                                navigateToDetallesParte = navigateToDetallesParte,
                                viewModelCompartido = viewModelCompartido,
                                resultadosCatElViewModel = resultadosCatElViewModel
                            )
                        }
                    }
                }
            }

        }

        is ProductosUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }

}

/*@Composable
private fun SharkContent(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    when (resultadosCatElViewModel.productosUiStateShark) {
        is ProductosUiState.Loading -> {
            AltScreen(texto = stringResource(R.string.cargando))
        }

        is ProductosUiState.Success -> {
            val productos =
                (resultadosCatElViewModel.productosUiStateShark as ProductosUiState.Success).productos

            Log.i(
                "logra3",
                productos[0].nombreMarca + " " + productos[0].linea + " " + productos[0].linea_id
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = productos[0].nombreMarca + " " + productos[0].nombreModeloCarro,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier.padding(vertical = 10.dp)
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                val productosList = productos.groupBy {
                    it.linea
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier.padding(top = 8.dp)
                ) {
                    productosList.forEach { (linea, productos) ->
                        item {
                            TittleScreens(
                                modifier = modifier,
                                soporte = linea ?: ""
                            )
                        }
                        items(productos) { producto ->
                            CardProducto(
                                producto = producto,
                                navigateToDetallesParte = navigateToDetallesParte,
                                viewModelCompartido = viewModelCompartido,
                                resultadosCatElViewModel = resultadosCatElViewModel
                            )
                        }
                    }
                }
            }

        }

        is ProductosUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }
}

@Composable
private fun PartechContent(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel

) {
    when (resultadosCatElViewModel.productosUiStatePartech) {
        is ProductosUiState.Loading -> {
            AltScreen(texto = stringResource(R.string.cargando))
        }

        is ProductosUiState.Success -> {
            val productos =
                (resultadosCatElViewModel.productosUiStatePartech as ProductosUiState.Success).productos

            Log.i(
                "logra3",
                productos[0].nombreMarca + " " + productos[0].linea + " " + productos[0].linea_id
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = productos[0].nombreMarca + " " + productos[0].nombreModeloCarro,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier.padding(vertical = 10.dp)
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                val productosList = productos.groupBy {
                    it.linea
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier.padding(top = 8.dp)
                ) {
                    productosList.forEach { (linea, productos) ->
                        item {
                            TittleScreens(
                                modifier = modifier,
                                soporte = linea ?: ""
                            )
                        }
                        items(productos) { producto ->
                            CardProducto(
                                producto = producto,
                                navigateToDetallesParte = navigateToDetallesParte,
                                viewModelCompartido = viewModelCompartido,
                                resultadosCatElViewModel = resultadosCatElViewModel
                            )
                        }
                    }
                }
            }

        }

        is ProductosUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }
}


@Composable
private fun RodatechContent(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    when (resultadosCatElViewModel.productosUiStateRodaTech) {
        is ProductosUiState.Loading -> {
            AltScreen(texto = stringResource(R.string.cargando))
        }

        is ProductosUiState.Success -> {
            val productos =
                (resultadosCatElViewModel.productosUiStateRodaTech as ProductosUiState.Success).productos

            Log.i(
                "logra3",
                productos[0].nombreMarca + " " + productos[0].linea + " " + productos[0].linea_id
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = productos[0].nombreMarca + " " + productos[0].nombreModeloCarro,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier.padding(vertical = 10.dp)
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                val productosList = productos.groupBy {
                    it.linea
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier.padding(top = 8.dp)
                ) {
                    productosList.forEach { (linea, productos) ->
                        item {
                            TittleScreens(
                                modifier = modifier,
                                soporte = linea ?: ""
                            )
                        }
                        items(productos) { producto ->
                            CardProducto(
                                producto = producto,
                                navigateToDetallesParte = navigateToDetallesParte,
                                viewModelCompartido = viewModelCompartido,
                                resultadosCatElViewModel = resultadosCatElViewModel
                            )
                        }
                    }
                }
            }

        }

        is ProductosUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }
}*/

@Composable
private fun TrackContent(
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    resultadosCatElViewModel: ResultadosCatElViewModel,
    viewModelCompartido: ProductoCompartidoViewModel
) {
    when (resultadosCatElViewModel.productosUiStateTrackone) {
        is ProductosUiState.Loading -> {
            LoadingScreen()
        }

        is ProductosUiState.Success -> {
            val productos =
                (resultadosCatElViewModel.productosUiStateTrackone as ProductosUiState.Success).productos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = productos[0].nombreMarca + " " + productos[0].nombreModeloCarro,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                val productosList = productos.groupBy {
                    it.linea
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier.padding(top = 8.dp)
                ) {
                    productosList.forEach { (linea, productos) ->
                        item {
                            TittleScreens(
                                modifier = modifier,
                                soporte = linea ?: ""
                            )
                        }
                        items(productos) { producto ->
                            CardProducto(
                                producto = producto,
                                navigateToDetallesParte = navigateToDetallesParte,
                                viewModelCompartido = viewModelCompartido,
                                resultadosCatElViewModel = resultadosCatElViewModel
                            )
                        }
                    }
                }
            }

        }

        is ProductosUiState.Error -> {
            AltScreen(texto = stringResource(R.string.no_hay_registros_para_mostrar))
        }
    }
}


@Composable
private fun TittleScreens(modifier: Modifier, soporte: String) {
    Text(
        text = soporte,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun CardProducto(
    producto: ProductosResult,
    modifier: Modifier = Modifier,
    navigateToDetallesParte: (String) -> Unit,
    viewModelCompartido: ProductoCompartidoViewModel,
    resultadosCatElViewModel: ResultadosCatElViewModel
) {
    var criterio: String = producto.nombreSoporte
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    viewModelCompartido.setProducto(producto)
                    navigateToDetallesParte.invoke(criterio)

                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(producto.urlSoporte)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.imagen),
                placeholder = painterResource(R.drawable.download_file__1_),
                contentDescription = producto.descripcion,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = producto.nombreSoporte,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/*@Composable
private fun ProductInfo(
    producto: Lineas
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "40-834",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = producto.nombreSoporte,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = stringResource(
                R.string.desc_prod_resultados,
                producto.nombrePosicion,
                producto.nombreCilidraje,
                producto.nombreLitro,
                producto.aIni,
                producto.aFin,
                producto.descripcion
            ),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}*/

@Composable
private fun AltScreen(
    modifier: Modifier = Modifier,
    texto: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = texto, fontSize = 25.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun EmpresasDialog(
    modifier: Modifier = Modifier,
    nombresEmpresas: String,
) {
    var empresasDialog by rememberSaveable { mutableStateOf(true) }
    if (empresasDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { empresasDialog = false },
            title = {
                Text(
                    text = "Productos encontrados",
                )
            },
            text = {
                Text(
                    text = "Se encontraron productos en las pestañas: \n$nombresEmpresas",
                    color = MaterialTheme.colorScheme.onTertiary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    empresasDialog = false
                }) {
                    Text(text = "Aceptar")
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


/*
@Preview
@Composable
fun TabsPreview() {
    RefaccionariasTheme {
        ResultadosCatElScreen()
    }
}*/

/*@Preview
@Composable
fun CardPreivew() {
    RefaccionariasTheme {
        ResultadosCatElScreen(navigateBack = { navController.popBackStack() }) {
            navController.navigate(
                vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteDestination.route
            )
        }
    }
}*/

