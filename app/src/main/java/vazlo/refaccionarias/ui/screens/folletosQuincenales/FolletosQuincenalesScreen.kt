package vazlo.refaccionarias.ui.screens.folletosQuincenales

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Folleto
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.Azul_Vazlo
import vazlo.refaccionarias.ui.theme.Blanco
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object FolletosQuincenalesDestination : NavigationDestination {
    override val route = "folletos_quincenales"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FolletosQuincelasScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
    navigateToPdfView: (String) -> Unit,
    folletosQuincenalesViewModel: FolletosQuincenalesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = { FolletosQuincenalesTopBar(navigateBack = navigateBack) }
    ) {
        Column(modifier = modifier.padding(it)) {

            when (folletosQuincenalesViewModel.folletosUiState) {
                is FolletosUiState.Loading -> {
                    LoadingScreenFo()
                }

                is FolletosUiState.Success -> {
                    val folletos =
                        (folletosQuincenalesViewModel.folletosUiState as FolletosUiState.Success).folletos
                    /*Log.i("sos1", "Encontrados: ${productos.size}")*/
                    ContentFolletos(
                        folletos = folletos,
                        navigateToPdfView = navigateToPdfView
                    )
                }

                is FolletosUiState.Error -> {
                    ErrorScreenFo()
                }
            }
        }
    }
}

@Composable
fun ContentFolletos(folletos: List<Folleto>, navigateToPdfView: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(folletos) { folleto ->
            CardFolleto(folleto = folleto, navigateToPdfView = navigateToPdfView)
        }
    }
}

@Composable
fun CardFolleto(
    modifier: Modifier = Modifier,
    folleto: Folleto,
    navigateToPdfView: (String) -> Unit
) {
    val url = URLEncoder.encode(folleto.url, StandardCharsets.UTF_8.toString())
    ElevatedCard(
        modifier = modifier
            .padding(10.dp)
            .clickable { navigateToPdfView(url) },
        shape = RoundedCornerShape(23.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Azul_Vazlo)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.aa),
                contentDescription = "Imagen",
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = folleto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Blanco
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolletosQuincenalesTopBar(modifier: Modifier = Modifier, navigateBack: () -> Boolean) {
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
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
fun ErrorScreenFo(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "No hay folletos disponibles en este momento")
    }
}


@Composable
fun LoadingScreenFo(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }

}