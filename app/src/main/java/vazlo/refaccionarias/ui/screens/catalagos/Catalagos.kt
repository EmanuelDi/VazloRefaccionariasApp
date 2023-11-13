package vazlo.refaccionarias.ui.screens.catalagos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.Verde_Success
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object EstadisticasDestination : NavigationDestination {
    override val route = "estadisticas"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstadisticasTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
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
                    text = "Estadísticas",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun EstadisticasScreen(
    navigateBack: () -> Unit,
    navigateToApartado: (String) -> Unit,
    viewModel: CatalagoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val catalagoItems = listOf(
        CatalagoItem(
            title = "Piezas - Pedidos",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/estadisticas/apps_piezas_pedidos.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.order_tracking,
            color = MaterialTheme.colorScheme.surfaceVariant,
            permiso = true,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            colorIcono = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        CatalagoItem(
            title = "Catalogos Generales",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/corporativo/apps_catalogosgeneral.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.catalogue,
            color = MaterialTheme.colorScheme.inversePrimary,
            permiso = true,
            colorTexto = MaterialTheme.colorScheme.onSurface,
            colorIcono = MaterialTheme.colorScheme.onSurface
        ),
        CatalagoItem(
            title = "Facturas ERP",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/estadisticas/apps_facturas_erp.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.preview,
            color = MaterialTheme.colorScheme.surfaceVariant,
            permiso = viewModel.permisoFacturas,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            colorIcono = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        CatalagoItem(
            title = "Complemento ERP",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/estadisticas/apps_complemento_erp.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.preview,
            color = MaterialTheme.colorScheme.surfaceVariant,
            permiso = viewModel.permisoComplementos,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            colorIcono = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        CatalagoItem(
            title = "Conversión de Precios",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/estadisticas/apps_preciosConversiones.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.list,
            color = Verde_Success,
            permiso = true,
            colorTexto = MaterialTheme.colorScheme.onSurface,
            colorIcono = MaterialTheme.colorScheme.onSurface
        ),
        CatalagoItem(
            title = "Notas ERP",
            destination = {
                navigateToApartado(
                    URLEncoder.encode(
                        "https://www.vazloonline.com/contenido/estadisticas/app_notas_erp.php?c=",
                        StandardCharsets.UTF_8.name()
                    )
                )
            },
            icon = R.drawable.preview,
            color = MaterialTheme.colorScheme.surfaceVariant,
            permiso = viewModel.permisoNotas,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            colorIcono = MaterialTheme.colorScheme.onSurfaceVariant
        ),
    )

    var showAlertaPermission by remember {
        mutableStateOf(false)
    }
    if (showAlertaPermission) {
        AlertaPermiso(onDismiss = { showAlertaPermission = false })
    }

    Scaffold(
        topBar = {
            EstadisticasTopBar(navigateBack = { navigateBack() })
        }) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(catalagoItems) {
                CatCard(
                    navegar = { it.destination() },
                    title = it.title,
                    icon = it.icon,
                    color = it.color,
                    permiso = it.permiso,
                    colorTexto = it.colorTexto,
                    onSinPermiso = { showAlertaPermission = true }
                )
            }
        }
    }
}

@Composable
fun AlertaPermiso(onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = { onDismiss() }) {
            Text(text = "Aceptar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        text = {
            Text(
                text = "No tienes permisos para ingresar a este apartado",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        title = {
            Text(
                text = "Mensaje",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatCard(
    navegar: () -> Unit,
    title: String,
    icon: Int,
    color: Color,
    permiso: Boolean,
    onSinPermiso: () -> Unit,
    colorTexto: Color
) {
    ElevatedCard(
        onClick = {
            if (permiso) {
                navegar()
            } else {
                onSinPermiso()
            }
        }, modifier = Modifier
            .padding(10.dp)
            .width(100.dp).height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Image(painterResource(id = icon)  , contentDescription = "", Modifier.size(100.dp))
            HorizontalDivider(color = colorTexto)
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = colorTexto,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}