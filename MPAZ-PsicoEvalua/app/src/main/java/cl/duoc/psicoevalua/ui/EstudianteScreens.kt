package cl.duoc.psicoevalua.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.psicoevalua.data.DatosDemo
import cl.duoc.psicoevalua.ui.theme.GradientePrincipal


@Composable
fun EstCodigoScreen(
    nombre: String,
    onContinuar: () -> Unit,
    onPrivacidad: () -> Unit,
    onSalir: () -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var intento by remember { mutableStateOf(false) }
    var dialogo by remember { mutableStateOf(false) }
    val error = if (intento && codigo.isBlank()) "Ingresa el código que te entregó el psicólogo." else null

    PantallaBase(
        titulo = "Ingreso del estudiante",
        acciones = {
            IconButton(onClick = onSalir) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Aparecer(0) {
                Text(
                    "¡Hola, $nombre!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Aparecer(1) {
                Text(
                    "Escribe el código que te entregó el psicólogo para comenzar. Hazlo dentro de la escuela y con supervisión.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
            Aparecer(2) {
                CampoTexto(
                    valor = codigo,
                    alCambiar = { codigo = it.uppercase() },
                    etiqueta = "Código de acceso",
                    icono = Icons.Filled.Key,
                    error = error,
                    teclado = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                )
            }
            Aparecer(3) {
                BotonPrincipal(
                    texto = "Ingresar",
                    icono = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = {
                        intento = true
                        if (codigo.isNotBlank()) dialogo = true
                    }
                )
            }
            Aparecer(4) {
                TextButton(onClick = onPrivacidad) { Text("Aviso de privacidad") }
            }
        }
    }

    DialogoFuturaActualizacion(
        visible = dialogo,
        detalle = "El código solo puede utilizarse una vez. Una vez que lo hayas ingresado, quedará invalidado y no podrá volver a utilizarse.",
        onCerrar = { dialogo = false },
        textoContinuar = "Continuar",
        onContinuar = {
            dialogo = false
            onContinuar()
        }
    )
}


@Composable
fun EstInstruccionesScreen(id: Int, onVolver: () -> Unit, onComenzar: () -> Unit) {
    val inst = DatosDemo.instrumento(id)

    PantallaBase(titulo = "Instrucciones", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Aparecer(0) {
                Text(inst.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            Aparecer(1) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.size(8.dp))
                    Text("Tiempo aproximado: ${inst.tiempoMin} minutos", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Aparecer(2) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Antes de comenzar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(inst.instrucciones, fontSize = 18.sp, lineHeight = 26.sp)
                        Text(
                            "Son ${inst.preguntas.size} preguntas. No Puedes volver a la pregunta anterior.",
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Aparecer(3) {
                BotonPrincipal(texto = "Comenzar", icono = Icons.Filled.PlayArrow, onClick = onComenzar)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstCuestionarioScreen(id: Int, onFinalizar: () -> Unit, onSalir: () -> Unit) {
    val inst = DatosDemo.instrumento(id)
    val total = inst.preguntas.size
    var indice by rememberSaveable { mutableStateOf(0) }
    val respuestas = remember(id) {
        mutableStateListOf<Int>().apply { repeat(total) { add(-1) } }
    }
    var dialogoEnvio by remember { mutableStateOf(false) }
    var dialogoSalir by remember { mutableStateOf(false) }

    BackHandler { dialogoSalir = true }

    if (total == 0) {
        PantallaBase(titulo = "Cuestionario", onVolver = onSalir) { pad ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Este instrumento no contiene preguntas disponibles.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    BotonPrincipal(texto = "Volver", onClick = onSalir)
                }
            }
        }
        return
    }

    val respondidas = respuestas.count { it >= 0 }

    PantallaBase(titulo = "Pregunta ${indice + 1} de $total") { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProgresoAnimado(respondidas.toFloat() / total)

            AnimatedContent(
                targetState = indice,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(tween(320)) { it } + fadeIn(tween(320))) togetherWith
                            (slideOutHorizontally(tween(320)) { -it } + fadeOut(tween(200)))
                    } else {
                        (slideInHorizontally(tween(320)) { -it } + fadeIn(tween(320))) togetherWith
                            (slideOutHorizontally(tween(320)) { it } + fadeOut(tween(200)))
                    }
                },
                label = "pregunta"
            ) { i ->
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        inst.preguntas[i],
                        fontSize = 22.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    inst.escala.forEachIndexed { idx, texto ->
                        val seleccionado = respuestas[i] == idx
                        val fondo by animateColorAsState(
                            targetValue = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else Color.White,
                            animationSpec = tween(250),
                            label = "fondoOpcion"
                        )
                        Card(
                            onClick = { respuestas[i] = idx },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = fondo),
                            border = BorderStroke(
                                2.dp,
                                if (seleccionado) MaterialTheme.colorScheme.primary else Color(0xFFD0D7E2)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = seleccionado, onClick = { respuestas[i] = idx })
                                Spacer(Modifier.size(6.dp))
                                Text(texto, fontSize = 19.sp)
                            }
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { if (indice > 0) indice-- },
                    enabled = indice > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Anterior", fontSize = 17.sp) }

                if (indice < total - 1) {
                    androidx.compose.material3.Button(
                        onClick = { indice++ },
                        enabled = respuestas[indice] >= 0,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Siguiente", fontSize = 17.sp) }
                } else {
                    androidx.compose.material3.Button(
                        onClick = { dialogoEnvio = true },
                        enabled = respuestas.all { it >= 0 },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                        Spacer(Modifier.size(6.dp))
                        Text("Finalizar", fontSize = 17.sp)
                    }
                }
            }
        }
    }

    DialogoFuturaActualizacion(
        visible = dialogoEnvio,
        detalle = "El envío de respuestas aún no se guarda en una base de datos. Continúa para ver la confirmación.",
        onCerrar = { dialogoEnvio = false },
        textoContinuar = "Enviar",
        onContinuar = {
            dialogoEnvio = false
            onFinalizar()
        }
    )

    if (dialogoSalir) {
        AlertDialog(
            onDismissRequest = { dialogoSalir = false },
            title = { Text("¿Salir del cuestionario?") },
            text = { Text("Si sales ahora se perderán las respuestas que llevas.") },
            confirmButton = { TextButton(onClick = onSalir) { Text("Salir") } },
            dismissButton = { TextButton(onClick = { dialogoSalir = false }) { Text("Seguir respondiendo") } }
        )
    }
}


@Composable
fun EstConfirmacionScreen(onSalir: () -> Unit) {
    val escala = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        escala.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
    }

    PantallaBase(titulo = "Aplicación finalizada") { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF2E9E6B),
                modifier = Modifier
                    .size(140.dp)
                    .scale(escala.value)
            )
            Spacer(Modifier.height(16.dp))
            Aparecer(1) {
                Text(
                    "¡Respuestas enviadas!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Aparecer(2) {
                Text(
                    "Gracias por responder. Avisa al psicólogo que ya terminaste.\nEsta aplicación no muestra resultados ni puntajes.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(28.dp))
            Aparecer(3) {
                BotonPrincipal(texto = "Salir", onClick = onSalir, icono = Icons.AutoMirrored.Filled.Logout)
            }
        }
    }
}
