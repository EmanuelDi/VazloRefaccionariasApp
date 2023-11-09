package vazlo.refaccionarias.ui.screens.notificaciones

import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Mensaje
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.Azul_Vazlo
import vazlo.refaccionarias.ui.theme.Blanco


object NotificacionesDestination : NavigationDestination {
    override val route = "notificaciones"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NotificacionesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
    notificacionesViewModel: NotificacionesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val token = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

    LaunchedEffect(key1 = "", block = {notificacionesViewModel.cargarNotificaciones(token)})
    Scaffold(
        topBar = { NotificacionesTopBar(navigateBack = navigateBack) }
    ) {
        Column(modifier = modifier.padding(it)) {

            when (notificacionesViewModel.notificacionesUiState) {
                is NotificacionesUiState.Loading -> {
                    LoadingScreen()
                }
                is NotificacionesUiState.Success -> {
                    val mensajes =
                        (notificacionesViewModel.notificacionesUiState as NotificacionesUiState.Success).productos
                    ContentNot(
                        mensajes = mensajes,
                        notificacionesViewModel = notificacionesViewModel
                    )
                }
                is NotificacionesUiState.Error -> {
                    ErrorScreen()
                }
            }
        }
    }
}


@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No tienes notificaciones en este momento",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentNot(
    modifier: Modifier = Modifier,
    mensajes: List<Mensaje>,
    notificacionesViewModel: NotificacionesViewModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        items(mensajes) { mensaje ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                notificacionesViewModel.darBajaNotificacion(mensaje.id)
            }
            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                notificacionesViewModel.darBajaNotificacion(mensaje.id)
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                background = {
                    val color = MaterialTheme.colorScheme.surface
                    Card(shape = RoundedCornerShape(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = modifier.align(Alignment.CenterEnd)
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = modifier.align(Alignment.CenterStart)
                            )
                        }
                    }
                },
                dismissContent = {
                    Message(mensaje = mensaje, eliminar = {notificacionesViewModel.darBajaNotificacion(mensaje.id)})
                },
                modifier = modifier.padding(10.dp)
            )

            HorizontalDivider(modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surfaceVariant, thickness = 5.dp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Message(modifier: Modifier = Modifier, mensaje: Mensaje, eliminar: () -> Unit) {
    Card(shape = RoundedCornerShape(5.dp)) {
        ListItem(
            overlineContent = {
                Text(
                    text = mensaje.titulo,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            headlineContent = {
                Text(
                    text = mensaje.mensaje,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            trailingContent = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                }
            },
            colors = ListItemColors(
                containerColor = Azul_Vazlo,
                headlineColor = Blanco,
                leadingIconColor = Blanco,
                overlineColor = Blanco,
                supportingTextColor = Blanco,
                trailingIconColor = Blanco,
                disabledTrailingIconColor = Blanco,
                disabledLeadingIconColor = Blanco,
                disabledHeadlineColor = Blanco
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesTopBar(modifier: Modifier = Modifier, navigateBack: () -> Boolean) {
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
