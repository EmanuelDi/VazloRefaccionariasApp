package vazlo.refaccionarias.ui.screens.catalagoElectronico


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Anio
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.catalagoElectronico.BusqCatElecUiState
import vazlo.refaccionarias.ui.screens.catalagoElectronico.CatElectronicoViewModel
import vazlo.refaccionarias.ui.theme.Blanco
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme

object CatalogoElectronicoDestination : NavigationDestination {
    override val route = "catalogo_electronico"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatElectronicoScreen(
    modifier: Modifier = Modifier,
    navigateToResultCatElectronico: (anio: String, marca: String, modelo: String, cilindraje: String, litros: String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: CatElectronicoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    VazloRefaccionariasTheme {
        Scaffold(
            topBar = {
                CatElectronicoTopBar(modifier = modifier, navigateBack = navigateBack)
            }
        ) {

            Column(modifier = modifier.padding(it)) {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.catalogoelectronico),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Blanco,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Content(
                    modifier = modifier,
                    navigateToResultCatElec = navigateToResultCatElectronico,
                    busquedaCatElecViewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatElectronicoTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
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
                    text = "Refaccionarias",
                    fontWeight = FontWeight.Bold,
                    color = Blanco
                )
            }
        },
        actions = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = Blanco
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToResultCatElec: (anio: String, marca: String, modelo: String, cilindraje: String, litros: String) -> Unit,
    busquedaCatElecViewModel: CatElectronicoViewModel
) {
    when (busquedaCatElecViewModel.busqCatElecUiState) {
        is BusqCatElecUiState.Loading ->
            CargandoCatEL()
        is BusqCatElecUiState.Success -> {
            val anios =
                (busquedaCatElecViewModel.busqCatElecUiState as BusqCatElecUiState.Success).aniosResponse
            SuccessScreen(
                busquedaCatElecViewModel = busquedaCatElecViewModel,
                navigateToResultCatElec = navigateToResultCatElec,
                type = true,
                listaAnios = anios
            )
        }
        is BusqCatElecUiState.Error ->
            ErroScreenCatEl(estado = "Error")

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownAnio(
    modifier: Modifier = Modifier,
    busquedaCatElecViewModel: CatElectronicoViewModel,
    type: Boolean,
    label: String,
    listaAnios: List<Anio>
) {
    val confguration = LocalConfiguration.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf("") }

    val size = if (type)
        confguration.screenWidthDp - 20
    else
        (confguration.screenWidthDp / 2) - 15

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = busquedaCatElecViewModel.anio,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .width(size.dp),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }, tint = Color.Black
                )
            },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(size.dp)
                .background(Color.White)
                .heightIn(max = 400.dp)
        ) {
            listaAnios.forEach { anio ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = anio.ani_nombre,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    onClick = {
                        selectedText = anio.ani_nombre
                        expanded = false
                        busquedaCatElecViewModel.onAnioChange(selectedText)
                        Log.i("anioOnClick", busquedaCatElecViewModel.anio)
                        busquedaCatElecViewModel.cargarMarcas(selectedText)
                    }

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMarca(
    modifier: Modifier = Modifier,
    busquedaCatElecViewModel: CatElectronicoViewModel,
    type: Boolean,
    label: String
) {

    val confguration = LocalConfiguration.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.marcas[0].nombre_marca) }
    var selectedMarca by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.marcas[0].marca_id) }
    LaunchedEffect(busquedaCatElecViewModel.marcaPrimera) {
        selectedText = busquedaCatElecViewModel.marcaPrimera
        selectedMarca = busquedaCatElecViewModel.idMarcaPrimera
        busquedaCatElecViewModel.onMarcaChange(busquedaCatElecViewModel.idMarcaPrimera)
        Log.i("marcaInicializada", busquedaCatElecViewModel.marca)
    }
    val size = if (type)
        confguration.screenWidthDp - 20
    else
        (confguration.screenWidthDp / 2) - 15

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .width(size.dp),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }, tint = Color.Black
                )
            },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(size.dp)
                .background(Color.White)
                .heightIn(max = 300.dp)
        ) {
            if (busquedaCatElecViewModel.marcas[0].nombre_marca == "") {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Selecciona un año",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    onClick = {

                    }

                )
            } else {
                busquedaCatElecViewModel.marcas.forEach { marca ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = marca.nombre_marca,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        onClick = {
                            selectedText = marca.nombre_marca
                            selectedMarca = marca.marca_id
                            expanded = false
                            busquedaCatElecViewModel.onMarcaChange(selectedMarca)
                            Log.i("marcaOnClick", busquedaCatElecViewModel.marca)
                            busquedaCatElecViewModel.cargarModelos(selectedMarca)
                        }

                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownModelo(
    modifier: Modifier = Modifier,
    busquedaCatElecViewModel: CatElectronicoViewModel,
    type: Boolean,
    label: String,
) {
    val confguration = LocalConfiguration.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.modeloPrimero) }
    var selectedModelo by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.idModeloPrimero) }

    LaunchedEffect(busquedaCatElecViewModel.modeloPrimero) {
        selectedText = busquedaCatElecViewModel.modeloPrimero
        selectedModelo = busquedaCatElecViewModel.idModeloPrimero
        busquedaCatElecViewModel.onModeloChange(busquedaCatElecViewModel.idModeloPrimero)
        Log.i("modeloInicializado", busquedaCatElecViewModel.modelo)
    }
    val size = if (type)
        confguration.screenWidthDp - 20
    else
        (confguration.screenWidthDp / 2) - 15

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .width(size.dp),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }, tint = Color.Black
                )
            },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(size.dp)
                .background(Color.White)
                .heightIn(max = 295.dp)
        ) {
            if (busquedaCatElecViewModel.modelos[0].nombre_modelocarro == "") {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Selecciona una marca",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    onClick = {

                    }

                )
            } else {
                busquedaCatElecViewModel.modelos.forEach { modelo ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = modelo.nombre_modelocarro,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        onClick = {
                            selectedText = modelo.nombre_modelocarro
                            selectedModelo = modelo.modelocarro_id
                            expanded = false
                            busquedaCatElecViewModel.onModeloChange(selectedModelo)
                            Log.i("modeloOnClick", busquedaCatElecViewModel.modelo)
                            busquedaCatElecViewModel.cargarMotores(selectedModelo)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMotor(
    modifier: Modifier = Modifier,
    busquedaCatElecViewModel: CatElectronicoViewModel,
    type: Boolean,
    label: String,
) {
    val confguration = LocalConfiguration.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.motorPrimero) }
    var selectedCilindraje by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.idCilindrajePrimero) }
    var selectedLitro by rememberSaveable { mutableStateOf(busquedaCatElecViewModel.idLitroPrimero) }

    LaunchedEffect(busquedaCatElecViewModel.motorPrimero) {
        selectedText = busquedaCatElecViewModel.motorPrimero
        selectedCilindraje = busquedaCatElecViewModel.idCilindrajePrimero
        selectedLitro = busquedaCatElecViewModel.idLitroPrimero
        busquedaCatElecViewModel.onCilindrajeChange(busquedaCatElecViewModel.idCilindrajePrimero)
        Log.i("cilindrajeInicializado", busquedaCatElecViewModel.cilindraje)
        busquedaCatElecViewModel.onLitrosChange(busquedaCatElecViewModel.idLitroPrimero)
        Log.i("litrosInicializado", busquedaCatElecViewModel.litros)
    }
    val size = if (type)
        confguration.screenWidthDp - 20
    else
        (confguration.screenWidthDp / 2) - 15

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .width(size.dp),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }, tint = Color.Black
                )
            },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(size.dp)
                .background(Color.White)
                .heightIn(max = 215.dp)
        ) {
            if (busquedaCatElecViewModel.motores[0].nombre_cilindraje == "") {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Selecciona un modelo",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    onClick = {

                    }

                )
            } else {
                busquedaCatElecViewModel.motores.forEach { motor ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (motor.nombre_cilindraje == "_") {
                                    "* *"
                                } else {
                                    motor.nombre_cilindraje + " " + motor.nombre_litro
                                },
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        onClick = {
                            selectedText = if (motor.nombre_cilindraje == "_") {
                                "* *"
                            } else {
                                motor.nombre_cilindraje + " " + motor.nombre_litro
                            }
                            selectedCilindraje = motor.nombre_cilindraje
                            selectedLitro = motor.nombre_litro
                            expanded = false
                            busquedaCatElecViewModel.motorPrimero = selectedText
                            busquedaCatElecViewModel.idCilindrajePrimero = selectedCilindraje
                            busquedaCatElecViewModel.idLitroPrimero = selectedLitro
                            busquedaCatElecViewModel.onCilindrajeChange(selectedCilindraje)
                            Log.i("cilindrajeOnClick", busquedaCatElecViewModel.cilindraje)
                            busquedaCatElecViewModel.onLitrosChange(selectedLitro)
                            Log.i("litrosOnClick", busquedaCatElecViewModel.litros)
                        }
                    )
                }
            }
        }
    }
}

//Tiene que recibir un obj
@Composable
private fun AyudaCard(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant,
        label = "",
    )
    Card(
        modifier = modifier.padding(top = 20.dp),
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
                    .padding(15.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_ayuda),
                        contentDescription = "",
                        modifier.size(40.dp)
                    )
                    Column(modifier = modifier.padding(start = 10.dp)) {
                        Text(
                            text = stringResource(R.string.ayuda),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(id = R.string.catalogoelectronico),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                Spacer(Modifier.weight(1f))

                ExpandedButton(expanded = expanded, onClick = { expanded = !expanded })
            }

            if (expanded) {
                /*Divider(
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )*/
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = stringResource(R.string.desc_ayuda_cat),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedButton(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.onTertiary,
            contentDescription = "Expandir"
        )
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier = Modifier,
    busquedaCatElecViewModel: CatElectronicoViewModel,
    navigateToResultCatElec: (anio: String, marca: String, modelo: String, cilindraje: String, litros: String) -> Unit,
    type: Boolean,
    listaAnios: List<Anio>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Blanco)


    ) {
        item{
            Column(modifier = modifier .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,){
                //type: true equals full width
                DropDownAnio(
                    modifier = modifier,
                    busquedaCatElecViewModel = busquedaCatElecViewModel,
                    type = true,
                    label = "Año",
                    listaAnios = listaAnios
                )
                DropDownMarca(
                    modifier = modifier,
                    busquedaCatElecViewModel = busquedaCatElecViewModel,
                    type = true,
                    label = "Marca",
                )
                DropDownModelo(
                    modifier = modifier,
                    busquedaCatElecViewModel = busquedaCatElecViewModel,
                    type = true,
                    label = "Modelo",
                )
                DropDownMotor(
                    modifier = modifier,
                    busquedaCatElecViewModel = busquedaCatElecViewModel,
                    type = true,
                    label = "Motor",
                )

                Button(
                    onClick = {
                        navigateToResultCatElec.invoke(
                            busquedaCatElecViewModel.anio,
                            busquedaCatElecViewModel.marca,
                            busquedaCatElecViewModel.modelo,
                            busquedaCatElecViewModel.cilindraje,
                            busquedaCatElecViewModel.litros,
                        )
                        busquedaCatElecViewModel.limpiar()
                        Log.i("boton", "boton")
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    enabled = busquedaCatElecViewModel.validateInput()
                ) {
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.buscar))
                }

                AyudaCard()

            }
        }
    }
}

@Composable
private fun CargandoCatEL(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErroScreenCatEl(
    modifier: Modifier = Modifier,
    estado: String
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = estado
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CatElectronicoPreview() {
    RefaccionariasTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CatElectronicoScreen()
        }
    }
}*/
