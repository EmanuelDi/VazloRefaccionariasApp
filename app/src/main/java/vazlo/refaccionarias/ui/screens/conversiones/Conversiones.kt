package vazlo.refaccionarias.ui.screens.conversiones

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vazlo.refaccionarias.R
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.screens.conversiones.ConversionesViewModel


object ConversionesDestination : NavigationDestination {
    override val route = "conversiones"
    /*override val titleRes = R.string.item_entry_title*/
}

@Composable
fun ConversionesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToResultadoParte: (String, String) -> Unit
) {
    Scaffold(
        topBar = { ConversionesTopAppBar(navigateBack = navigateBack) }
    ) {
        Column(modifier = modifier.padding(it)) {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment =     Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.conversiones),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            BodyConversiones(navigateToResultadoParte = navigateToResultadoParte)
        }
    }

}

@Composable
fun BodyConversiones(
    modifier: Modifier = Modifier,
    navigateToResultadoParte: (String, String) -> Unit,
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(horizontal = 10.dp, vertical = 20.dp)
    ) {
        val viewModel = ConversionesViewModel ()
        val errorSearch = remember { mutableStateOf(false) }
        EntryForm(
            viewModel= viewModel,
            errorSearch = errorSearch,
            navigateToResultadoParte = navigateToResultadoParte,
            onErrorResolve = { errorSearch.value=false})
        Row(
            /*horizontalArrangement = Arrangement.spacedBy(20.dp)*/
        ) {

            Button(
                onClick = {
                          if(viewModel.busqueda.isNotBlank()){
                              navigateToResultadoParte(viewModel.busqueda, "C")
                          }else{
                              errorSearch.value=true
                          }
                },/*navigateToConversiones() */
                modifier
                    .height(50.dp)
                    .width(150.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.buscar),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        ConversionesAyudaCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    modifier : Modifier = Modifier,
    viewModel: ConversionesViewModel,
    errorSearch: MutableState<Boolean>,
    onErrorResolve: () -> Unit,
    navigateToResultadoParte: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {FocusRequester() }
    OutlinedTextField(
        value = viewModel.busqueda,
        onValueChange = {
            viewModel.onCriterioChange(it)
            onErrorResolve()
        },
        label = { Text(text = stringResource(R.string.buscar)) },
        trailingIcon = {
            IconButton(onClick = {
                if (viewModel.busqueda.isNotBlank()) {
                    navigateToResultadoParte(viewModel.busqueda, "B")
                } else {
                    errorSearch.value = true
                    focusManager.clearFocus()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    modifier.size(30.dp)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
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
                text = "El campo no puede estar vacío",
                fontSize = 15.sp
            ) else Text(text = "")
        }
    )
}

@Composable
fun ConversionesAyudaCard(modifier: Modifier = Modifier) {
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
                ConversionesAyudaTitle()
                ConversionesButtonExpand(expanded = expanded, onClick = { expanded = !expanded })

            }
            if (expanded) {
                Column(
                    modifier = modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.texto_ayuda_conversiones),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ConversionesButtonExpand(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun ConversionesAyudaTitle(modifier: Modifier = Modifier) {
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
                text = stringResource(R.string.conversiones),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionesTopAppBar(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
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
    )
}

/*
@Preview
@Composable
fun PorPartesScreenPreview() {
    RefaccionariasTheme {
        ConversionesScreen()
    }
}
*/


