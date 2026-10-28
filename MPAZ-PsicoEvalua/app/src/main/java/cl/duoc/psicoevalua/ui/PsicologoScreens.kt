package cl.duoc.psicoevalua.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.psicoevalua.data.DatosDemo
import cl.duoc.psicoevalua.data.EstadoAplicacion
import cl.duoc.psicoevalua.data.EstadoInstrumento
import cl.duoc.psicoevalua.data.Usuario
import cl.duoc.psicoevalua.ui.theme.GradientePrincipal


@Composable
fun PsiHomeScreen(
    usuario: Usuario,
    onCatalogo: () -> Unit,
    onNueva: () -> Unit,
    onSupervisar: () -> Unit,
    onRevisar: () -> Unit,
    onHistorial: () -> Unit,
    onPrivacidad: () -> Unit,
    onPerfil: () -> Unit
) {
    val apps = DatosDemo.aplicaciones
    val pendientes = apps.count { it.estado == EstadoAplicacion.PENDIENTE }
    val enDesarrollo = apps.count { it.estado == EstadoAplicacion.EN_DESARROLLO }
    val finalizadas = apps.count {
        it.estado == EstadoAplicacion.FINALIZADA || it.estado == EstadoAplicacion.REVISADA
    }

    PantallaBase(
        titulo = "Panel del psicólogo",
        acciones = {
            IconButton(onClick = onPerfil) {
                Icon(Icons.Filled.Person, contentDescription = "Perfil")
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Aparecer(0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(GradientePrincipal)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "Hola, ${usuario.nombre}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Solo tú puedes ver respuestas y resultados. Toda la información de esta demostración es ficticia.",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Aparecer(1) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TarjetaContador(pendientes, "Pendientes", Color(0xFFB7791F), Modifier.weight(1f))
                    TarjetaContador(enDesarrollo, "En desarrollo", Color(0xFF2563A8), Modifier.weight(1f))
                    TarjetaContador(finalizadas, "Finalizadas", Color(0xFF2E9E6B), Modifier.weight(1f))
                }
            }

            Aparecer(2) { SeccionTitulo("¿Qué deseas hacer?") }

            Aparecer(3) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAccion(Icons.Filled.Category, "Catálogo", "Instrumentos autorizados o simulados", onCatalogo, Modifier.weight(1f))
                    TarjetaAccion(Icons.Filled.AddCircle, "Nueva aplicación", "Asignar un instrumento", onNueva, Modifier.weight(1f))
                }
            }
            Aparecer(4) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAccion(Icons.Filled.Visibility, "Supervisar", "Progreso en tiempo real", onSupervisar, Modifier.weight(1f))
                    TarjetaAccion(Icons.Filled.Description, "Revisar respuestas", "Respuestas y puntaje de prueba", onRevisar, Modifier.weight(1f))
                }
            }
            Aparecer(5) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAccion(Icons.Filled.History, "Historial", "Aplicaciones y trazabilidad", onHistorial, Modifier.weight(1f))
                    TarjetaAccion(Icons.Filled.Security, "Privacidad", "Confidencialidad y alcance", onPrivacidad, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TarjetaContador(valor: Int, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    var objetivo by remember { mutableIntStateOf(0) }
    LaunchedEffect(valor) { objetivo = valor }
    val mostrado by animateIntAsState(targetValue = objetivo, animationSpec = tween(1100), label = "contador")
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$mostrado", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = color)
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = color, textAlign = TextAlign.Center)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(onVolver: () -> Unit, onAbrir: (Int) -> Unit) {
    PantallaBase(titulo = "Catálogo de instrumentos", onVolver = onVolver) { pad ->
        LazyColumn(
            modifier = Modifier.padding(pad),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Solo se listan instrumentos propios, públicos, simulados o con autorización.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            itemsIndexed(DatosDemo.instrumentos) { i, inst ->
                Aparecer(i) {
                    Card(
                        onClick = { onAbrir(inst.id) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    inst.nombre,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.size(8.dp))
                                ChipEstado(inst.estado.etiqueta, colorInstrumento(inst.estado))
                            }
                            Text(inst.tipo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            Text(
                                "${inst.rangoEdad} · ${inst.tiempoMin} min",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetalleInstrumentoScreen(id: Int, onVolver: () -> Unit, onCrear: (Int) -> Unit) {
    val inst = DatosDemo.instrumento(id)
    var verPreguntas by remember { mutableStateOf(false) }
    val disponible = inst.estado == EstadoInstrumento.DISPONIBLE

    PantallaBase(titulo = "Detalle del instrumento", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Aparecer(0) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(inst.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    ChipEstado(inst.estado.etiqueta, colorInstrumento(inst.estado))
                    Text(inst.descripcion, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Aparecer(1) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        InfoFila("Tipo", inst.tipo)
                        InfoFila("Rango de edad", inst.rangoEdad)
                        InfoFila("Tiempo estimado", "${inst.tiempoMin} min")
                        InfoFila("Preguntas", if (inst.preguntas.isEmpty()) "—" else "${inst.preguntas.size}")
                        InfoFila("Puntaje de prueba", if (inst.puntuable) "Sí (autorizado/simulado)" else "No aplica")
                    }
                }
            }
            Aparecer(2) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SeccionTitulo("Instrucciones de aplicación")
                    Text(inst.instrucciones, style = MaterialTheme.typography.bodyMedium)
                    if (inst.escala.isNotEmpty()) {
                        Text(
                            "Escala: " + inst.escala.joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            if (inst.preguntas.isNotEmpty()) {
                Aparecer(3) {
                    Column {
                        TextButton(onClick = { verPreguntas = !verPreguntas }) {
                            Text(if (verPreguntas) "Ocultar preguntas" else "Ver preguntas del instrumento")
                        }
                        AnimatedVisibility(visible = verPreguntas) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                inst.preguntas.forEachIndexed { i, p ->
                                    Text("${i + 1}. $p", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
            Aparecer(4) {
                if (disponible) {
                    BotonPrincipal(
                        texto = "Crear aplicación con este instrumento",
                        onClick = { onCrear(inst.id) },
                        icono = Icons.Filled.AddCircle
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFB7791F).copy(alpha = 0.12f)
                    ) {
                        Text(
                            "Este instrumento no puede aplicarse todavía: requiere autorización institucional y licencia de uso, reproducción, digitalización y puntuación.",
                            modifier = Modifier.padding(14.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAplicacionScreen(
    instrumentoInicial: Int,
    onVolver: () -> Unit,
    onGenerarCodigo: () -> Unit
) {
    var estudiante by rememberSaveable { mutableStateOf("") }
    var curso by rememberSaveable { mutableStateOf("") }
    var seleccionado by rememberSaveable { mutableStateOf(instrumentoInicial) }
    var expandido by remember { mutableStateOf(false) }
    var intento by remember { mutableStateOf(false) }
    var dialogo by remember { mutableStateOf(false) }

    val disponibles = DatosDemo.instrumentos.filter { it.estado == EstadoInstrumento.DISPONIBLE }
    val instrumentoSel = disponibles.firstOrNull { it.id == seleccionado }

    val errEst = if (intento && estudiante.isBlank()) "Ingresa un identificador ficticio (ej. EST-006)." else null
    val errCurso = if (intento && curso.isBlank()) "Ingresa un curso ficticio (ej. 5.º A)." else null
    val errInst = if (intento && instrumentoSel == null) "Selecciona un instrumento." else null

    PantallaBase(titulo = "Nueva aplicación", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Aparecer(0) {
                Text(
                    "Usa solo identificadores y cursos ficticios. No ingreses nombres ni RUT reales.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Aparecer(1) {
                CampoTexto(estudiante, { estudiante = it }, "Identificador del estudiante (ficticio)", icono = Icons.Filled.Person, error = errEst)
            }
            Aparecer(2) {
                CampoTexto(curso, { curso = it }, "Curso (ficticio)", error = errCurso)
            }
            Aparecer(3) {
                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
                    OutlinedTextField(
                        value = instrumentoSel?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Instrumento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        isError = errInst != null,
                        supportingText = if (errInst != null) {
                            { Text(errInst) }
                        } else null,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        disponibles.forEach { inst ->
                            DropdownMenuItem(
                                text = { Text(inst.nombre) },
                                onClick = {
                                    seleccionado = inst.id
                                    expandido = false
                                }
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = instrumentoSel != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(instrumentoSel?.tipo ?: "", fontWeight = FontWeight.SemiBold)
                        Text(
                            "${instrumentoSel?.rangoEdad ?: ""} · ${instrumentoSel?.tiempoMin ?: 0} min",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Aparecer(4) {
                BotonPrincipal(
                    texto = "Crear aplicación y generar código",
                    icono = Icons.Filled.Key,
                    onClick = {
                        intento = true
                        if (estudiante.isNotBlank() && curso.isNotBlank() && instrumentoSel != null) {
                            dialogo = true
                        }
                    }
                )
            }
        }
    }

    DialogoFuturaActualizacion(
        visible = dialogo,
        detalle = "La aplicación aún no se guarda en una base de datos. Puedes ver una demostración del código temporal.",
        onCerrar = { dialogo = false },
        textoContinuar = "Ver código de demostración",
        onContinuar = {
            dialogo = false
            onGenerarCodigo()
        }
    )
}


@Composable
fun CodigoTemporalScreen(onVolver: () -> Unit, onSupervisar: () -> Unit, onPanel: () -> Unit) {
    val codigo = remember { "MPAZ-${(1000..9999).random()}" }
    val transicion = rememberInfiniteTransition(label = "pulso")
    val pulso by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulsoEscala"
    )

    PantallaBase(titulo = "Código temporal", onVolver = onVolver) { pad ->
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
                    "Entrega este código al estudiante. Solo le dará acceso al instrumento asignado, de forma temporal y supervisada.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Aparecer(1) {
                Box(
                    modifier = Modifier
                        .scale(pulso)
                        .clip(RoundedCornerShape(24.dp))
                        .background(GradientePrincipal)
                        .padding(horizontal = 36.dp, vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(codigo, color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Aparecer(2) {
                Text(
                    "Código de demostración · válido 30 minutos (simulado)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
            Aparecer(3) {
                BotonPrincipal(texto = "Supervisar progreso", onClick = onSupervisar, icono = Icons.Filled.Visibility)
            }
            Aparecer(4) {
                OutlinedButton(
                    onClick = onPanel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Volver al panel") }
            }
        }
    }
}
