@file:OptIn(ExperimentalMaterial3Api::class)

package vazlo.refaccionarias.ui.screens.pedidos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.pedidoData.InfoPedido
import vazlo.refaccionarias.data.model.pedidoData.ProductoPedido
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.AltScreen
import vazlo.refaccionarias.ui.theme.Amarillo_Vazlo

object PedidosDestination : NavigationDestination {
    override val route = "pedidos"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PedidosScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToDetallesPedido: () -> Unit,
    pedidosViewModel: PedidosViewModel = viewModel(factory = AppViewModelProvider.Factory),
    sharedViewModel: PedidoCompartidoViewModel
) {
    Scaffold(
        topBar = { PedidosTopAppBar(navigateBack = navigateBack) }
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
                        text = "Pedidos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            when (pedidosViewModel.pedidosUiState) {
                is PedidosUiState.Loading -> {
                    AltScreen(modifier = modifier)
                }

                is PedidosUiState.Success -> {
                    val pedidos =
                        (pedidosViewModel.pedidosUiState as PedidosUiState.Success).pedidos
                        //Log.i("sos1", "Encontrados: ${pedidos.size}")
                    ListaPedidos(
                        navigateToDetallesPedido = navigateToDetallesPedido,
                        pedidos = pedidos,
                        sharedViewModel = sharedViewModel
                    )
                }

                is PedidosUiState.Error -> {
                    AltScreen(modifier = modifier)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosTopAppBar(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier.height(50.dp)
        /*colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )*/
    )
}


@Composable
fun ListaPedidos(
    modifier: Modifier = Modifier,
    navigateToDetallesPedido: () -> Unit,
    pedidos: MutableList<Pair<InfoPedido, List<ProductoPedido>>>,
    sharedViewModel: PedidoCompartidoViewModel
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
    ) {
        items(pedidos) { pedido ->
            ItemPedido(
                pedido = pedido,
                navigateToDetallesPedido = navigateToDetallesPedido,
                sharedViewModel = sharedViewModel
            )
        }
    }
}


@Composable
fun ItemPedido(
    modifier: Modifier = Modifier,
    pedido: Pair<InfoPedido, List<ProductoPedido>>,
    navigateToDetallesPedido: () -> Unit,
    sharedViewModel: PedidoCompartidoViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.inverseSurface,
        label = "",
    )
    val colorText by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.inverseOnSurface,
        label = "",
    )
    Card(
        modifier = modifier
    ) {
        Column(
            Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(color = color)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoPedido(
                    fecha = pedido.first.fecha_recepcion,
                    folio = pedido.first.folio,
                    status = pedido.first.status,
                    total = pedido.first.total,
                    color = colorText
                )
                PedidoItemButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    InfoExtraPedido(
                        fechaAtencion = pedido.first.fecha_atencion,
                        comentarios = pedido.first.comentarios,
                        color = colorText
                    )
                }
                Button(
                    onClick = {
                        sharedViewModel.productos = pedido
                        navigateToDetallesPedido()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = modifier
                        .padding(bottom = 10.dp)
                        .height(40.dp)
                        .width(160.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ver_pedido),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorText
                    )
                }
            }
        }

    }
}

@Composable
fun PedidoItemButton(expanded: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = modifier
        )
    }
}


@Composable
fun InfoPedido(
    fecha: String,
    modifier: Modifier = Modifier,
    folio: String,
    status: String,
    total: String,
    color: Color
) {
    Column(
        modifier = modifier.width(300.dp)
    ) {
        Text(
            text = stringResource(R.string.folio_android, folio),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
        HorizontalDivider(
            modifier = modifier.width(400.dp),
            color = color
        )
        Text(
            text = stringResource(R.string.fecha_de_recepcion, fecha),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            softWrap = true
        )
        Text(
            text = stringResource(R.string.status, status),
            style = MaterialTheme.typography.bodyLarge,
            color = if (status == "Estatus: ATENDIDO") Amarillo_Vazlo else MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = stringResource(id = R.string.total, total),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
        )
    }
}

@Composable
fun InfoExtraPedido(
    fechaAtencion: String,
    comentarios: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 10.dp, bottom = 10.dp)
            .width(300.dp)
    ) {
        Text(
            text = stringResource(id = R.string.fecha_de_atencion, fechaAtencion),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            softWrap = true
        )
        Text(
            text = stringResource(R.string.comentarios, comentarios),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            softWrap = true
        )
    }
}


/*@Preview(device = "id:pixel_5")
@Composable
fun ScreenPreview() {
    RefaccionariasTheme {
        PedidosScreen()
    }
}*/





