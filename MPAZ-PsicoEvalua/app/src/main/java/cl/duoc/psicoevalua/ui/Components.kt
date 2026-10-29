package cl.duoc.psicoevalua.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.List
import cl.duoc.psicoevalua.data.EstadoAplicacion
import cl.duoc.psicoevalua.data.EstadoInstrumento
import kotlinx.coroutines.delay

data class ItemNav(val ruta:String, val icono: ImageVector, val titulo: String)

@Composable
fun BarraNavegacionInferior(
    rutaActual: String?,
    alSeleccionar: (String) -> Unit
){
    val items = listOf(
        ItemNav(Rutas.PSI_HOME,Icons.Filled.Home,"Inicio"),
        ItemNav(Rutas.CATALOGO, Icons.AutoMirrored.Filled.List,"Catálogo"),
        ItemNav(Rutas.HISTORIAL, Icons.Filled.History,"Historial"),
        ItemNav(Rutas.PERFIL,Icons.Filled.Person,"Perfil")
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ){
        items.forEach { item ->
            NavigationBarItem(
                icon = {Icon(item.icono, contentDescription = item.titulo)},
                label = {Text(item.titulo)},
                selected = rutaActual == item.ruta,
                onClick = {alSeleccionar(item.ruta)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}


@Composable
fun Aparecer(
    indice: Int = 0,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(70L * minOf(indice, 8))
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(tween(450)) + slideInVertically(tween(450)) { it / 5 }
    ) {
        contenido()
    }
}



@Composable
fun LogoAnimado(modifier: Modifier = Modifier, tamano: Dp = 80.dp) {
    val transicion = rememberInfiniteTransition(label = "logo")
    val dy by transicion.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flotar"
    )
    Surface(
        modifier = modifier.offset(y = dy.dp),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.18f)
    ) {
        Box(Modifier.size(tamano + 28.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.Psychology,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(tamano)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBase(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {},
    contenido: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (onVolver != null) {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = acciones,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = contenido
    )
}



@Composable
fun CampoTexto(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    error: String? = null,
    teclado: KeyboardOptions = KeyboardOptions.Default,
    visual: VisualTransformation = VisualTransformation.None,
    lineasMin: Int = 1,
    trailing: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = if (icono != null) {
            { Icon(icono, contentDescription = null) }
        } else null,
        trailingIcon = trailing,
        isError = error != null,
        supportingText = if (error != null) {
            { Text(error) }
        } else null,
        keyboardOptions = teclado,
        visualTransformation = visual,
        singleLine = lineasMin == 1,
        minLines = lineasMin,
        shape = RoundedCornerShape(14.dp)
    )
}



@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    enabled: Boolean = true,
    cargando: Boolean = false
) {
    val fuente = remember { MutableInteractionSource() }
    val presionado by fuente.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionado) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "botonEscala"
    )
    Button(
        onClick = onClick,
        enabled = enabled && !cargando,
        interactionSource = fuente,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(escala),
        shape = RoundedCornerShape(14.dp)
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            if (icono != null) {
                Icon(icono, contentDescription = null)
                Spacer(Modifier.width(8.dp))
            }
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}



@Composable
fun DialogoFuturaActualizacion(
    visible: Boolean,
    detalle: String = "Esta acción aún no está conectada a una base de datos.",
    onCerrar: () -> Unit,
    textoContinuar: String? = null,
    onContinuar: () -> Unit = {}
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onCerrar,
        icon = { Icon(Icons.Filled.Info, contentDescription = null) },
        title = { Text("Para una futura actualización") },
        text = { Text(detalle) },
        confirmButton = {
            if (textoContinuar != null) {
                TextButton(onClick = onContinuar) { Text(textoContinuar) }
            } else {
                TextButton(onClick = onCerrar) { Text("Entendido") }
            }
        },
        dismissButton = {
            if (textoContinuar != null) {
                TextButton(onClick = onCerrar) { Text("Cerrar") }
            }
        }
    )
}


fun colorEstado(estado: EstadoAplicacion): Color = when (estado) {
    EstadoAplicacion.PENDIENTE -> Color(0xFFB7791F)
    EstadoAplicacion.EN_DESARROLLO -> Color(0xFF2563A8)
    EstadoAplicacion.FINALIZADA -> Color(0xFF2E9E6B)
    EstadoAplicacion.REVISADA -> Color(0xFF6B46C1)
}

fun colorInstrumento(estado: EstadoInstrumento): Color = when (estado) {
    EstadoInstrumento.DISPONIBLE -> Color(0xFF2E9E6B)
    EstadoInstrumento.NO_DISPONIBLE -> Color(0xFFC0392B)
    EstadoInstrumento.PENDIENTE -> Color(0xFFB7791F)
}

@Composable
fun ChipEstado(texto: String, color: Color) {
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.15f)) {
        Text(
            text = texto,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaAccion(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fuente = remember { MutableInteractionSource() }
    val presionado by fuente.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionado) 0.95f else 1f,
        animationSpec = spring(),
        label = "tarjetaEscala"
    )
    Card(
        onClick = onClick,
        interactionSource = fuente,
        modifier = modifier
            .height(140.dp)
            .scale(escala),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
            Column {
                Text(titulo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(
                    descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}



@Composable
fun ProgresoAnimado(progreso: Float, modifier: Modifier = Modifier) {
    var objetivo by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(progreso) { objetivo = progreso }
    val valor by animateFloatAsState(
        targetValue = objetivo,
        animationSpec = tween(900),
        label = "progreso"
    )
    LinearProgressIndicator(
        progress = { valor },
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(50))
    )
}



@Composable
fun SeccionTitulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun InfoFila(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            etiqueta,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.width(12.dp))
        Text(
            valor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}
