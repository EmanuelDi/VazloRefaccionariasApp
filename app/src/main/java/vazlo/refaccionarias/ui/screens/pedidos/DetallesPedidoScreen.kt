package vazlo.refaccionarias.ui.screens.pedidos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.InfoPedido
import vazlo.refaccionarias.data.model.ProductoPedido
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.theme.Rojo_Vazlo

object DetailsPedidos : NavigationDestination {
    override val route = "detailsPedidos"
    /*override val titleRes = R.string.item_entry_title*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesPedidoScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    sharedViewModel: PedidoCompartidoViewModel
) {
    val infoPedido = sharedViewModel.productos!!.first
    val productos = sharedViewModel.productos!!.second
    val cantidad = productos.sumOf { it.cantidad.toInt() }
    Scaffold(
        topBar = { CartTopBar(navigateBack = navigateBack) },
        bottomBar = { FooterCart(infoPedido = infoPedido, cantidad = cantidad) },
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End
    ) {

        Column(modifier = modifier.padding(it)) {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle Pedido",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            ProductList(productList = productos)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
fun ProductList(modifier: Modifier = Modifier, productList: List<ProductoPedido>) {
    LazyColumn(
        contentPadding = PaddingValues(10.dp)
    ) {
        items(productList) { producto ->
            ItemProduct(producto = producto)
            HorizontalDivider(modifier = modifier.padding(30.dp), color = Rojo_Vazlo)
        }
    }
}

@Composable
fun ItemProduct(producto: ProductoPedido, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageProduct(imageId = producto.url)
        }

        Spacer(modifier = modifier.width(20.dp))
        InfoProduct(id = producto.producto, precio = producto.precio, cantidad = producto.cantidad)
    }

}


@Composable
fun ImageProduct(imageId: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imageId)
            .crossfade(true).build(),
        error = painterResource(R.drawable.imagen),
        placeholder = painterResource(R.drawable.imagen),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.size(150.dp)
    )
}

@Composable
fun InfoProduct(id: String, precio: String, cantidad: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.product_text, id),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 17.sp
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(R.string.cantidad, cantidad),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 17.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.precio_text, precio),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ImageFooter(modifier: Modifier = Modifier) {
    Image(
        imageVector = Icons.Default.ShoppingCart,
        contentDescription = "",
        modifier = modifier.size(150.dp)
    )
}

@Composable
fun InfoFooter(cantidad: Int, subtotal: String, iva: String, total: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text(
                text = stringResource(R.string.productos, cantidad),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
        Row {
            Text(
                text = subtotal,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            Text(
                text = iva,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            Text(
                text = total,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
        }
    }
}


@Composable
fun FooterCart(modifier: Modifier = Modifier, infoPedido: InfoPedido, cantidad: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = modifier.padding(horizontal = 30.dp),
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ImageFooter()
            InfoFooter(cantidad = cantidad, subtotal = infoPedido.subtotal, iva = infoPedido.iva, total = infoPedido.total)
        }
    }
}