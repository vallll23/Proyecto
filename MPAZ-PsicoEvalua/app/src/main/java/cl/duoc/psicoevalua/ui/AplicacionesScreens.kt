package cl.duoc.psicoevalua.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.psicoevalua.data.Aplicacion
import cl.duoc.psicoevalua.data.DatosDemo
import cl.duoc.psicoevalua.data.EstadoAplicacion
import cl.duoc.psicoevalua.data.Usuario


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaAplicacion(app: Aplicacion, mostrarProgreso: Boolean, onClick: () -> Unit) {
    val inst = DatosDemo.instrumento(app.instrumentoId)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${app.estudiante} · ${app.curso}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Spacer(Modifier.size(8.dp))
                ChipEstado(app.estado.etiqueta, colorEstado(app.estado))
            }
            Text(inst.nombre, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            if (mostrarProgreso) {
                ProgresoAnimado(app.progreso)
                Text(
                    "${(app.progreso * 100).toInt()}% completado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
            Text(
                "Inicio: ${app.inicio}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )
        }
    }
}


@Composable
fun AplicacionesScreen(modo: String, onVolver: () -> Unit, onAbrir: (Int) -> Unit) {
    val supervisar = modo == "supervisar"
    val lista = DatosDemo.aplicaciones.filter {
        if (supervisar) {
            it.estado == EstadoAplicacion.PENDIENTE || it.estado == EstadoAplicacion.EN_DESARROLLO
        } else {
            it.estado == EstadoAplicacion.FINALIZADA || it.estado == EstadoAplicacion.REVISADA
        }
    }

    PantallaBase(
        titulo = if (supervisar) "Supervisar progreso" else "Revisar respuestas",
        onVolver = onVolver
    ) { pad ->
        if (lista.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                Text("No hay aplicaciones en esta categoría.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(pad),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        if (supervisar) "Aplicaciones que aún están en curso dentro de la escuela."
                        else "Aplicaciones terminadas. Solo tú puedes ver estas respuestas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                itemsIndexed(lista) { i, app ->
                    Aparecer(i) {
                        TarjetaAplicacion(app, mostrarProgreso = supervisar, onClick = { onAbrir(app.id) })
                    }
                }
            }
        }
    }
}


@Composable
fun DetalleAplicacionScreen(id: Int, usuario: Usuario, onVolver: () -> Unit) {
    val app = DatosDemo.aplicacion(id)
    val inst = DatosDemo.instrumento(app.instrumentoId)
    val terminada = app.estado == EstadoAplicacion.FINALIZADA || app.estado == EstadoAplicacion.REVISADA
    val puntaje = DatosDemo.puntaje(app)

    var observacion by rememberSaveable { mutableStateOf(app.observacion) }
    var dialogo by remember { mutableStateOf(false) }
    var detalleDialogo by remember { mutableStateOf("") }

    PantallaBase(titulo = "Aplicación #${app.id}", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Aparecer(0) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Datos de la aplicación", fontWeight = FontWeight.Bold)
                            ChipEstado(app.estado.etiqueta, colorEstado(app.estado))
                        }
                        Spacer(Modifier.height(8.dp))
                        InfoFila("Estudiante", app.estudiante)
                        InfoFila("Curso", app.curso)
                        InfoFila("Instrumento", inst.nombre)
                        InfoFila("Inicio", app.inicio)
                        InfoFila("Finalización", app.fin ?: "—")
                        InfoFila("Responsable", usuario.correo)
                    }
                }
            }

            Aparecer(1) { SeccionTitulo("Respuestas") }

            if (terminada) {
                inst.preguntas.forEachIndexed { i, pregunta ->
                    Aparecer(2 + i) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("${i + 1}. $pregunta", style = MaterialTheme.typography.bodyMedium)
                                val r = app.respuestas.getOrNull(i)
                                Text(
                                    "Respuesta: " + (if (r != null) inst.escala[r] else "—"),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            } else {
                Aparecer(2) {
                    Text(
                        "Las respuestas estarán disponibles cuando el estudiante finalice la aplicación.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            if (terminada) {
                Aparecer(10) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SeccionTitulo("Puntaje de prueba")
                        if (puntaje != null) {
                            PuntajeAnimado(puntaje, DatosDemo.puntajeMaximo(inst))
                        } else {
                            Text(
                                "Este instrumento no contempla cálculo de puntaje.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Aparecer(11) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SeccionTitulo("Observación profesional (ficticia)")
                        CampoTexto(
                            valor = observacion,
                            alCambiar = { observacion = it },
                            etiqueta = "Observación",
                            lineasMin = 3
                        )
                        BotonPrincipal(
                            texto = "Guardar observación",
                            icono = Icons.Filled.Edit,
                            onClick = {
                                detalleDialogo = "La observación aún no se guarda en una base de datos."
                                dialogo = true
                            }
                        )
                        if (app.estado == EstadoAplicacion.FINALIZADA) {
                            OutlinedButton(
                                onClick = {
                                    detalleDialogo = "El cambio de estado a \"Revisada\" aún no se guarda en una base de datos."
                                    dialogo = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null)
                                Spacer(Modifier.size(8.dp))
                                Text("Marcar como revisada")
                            }
                        }
                    }
                }
            }
        }
    }

    DialogoFuturaActualizacion(
        visible = dialogo,
        detalle = detalleDialogo,
        onCerrar = { dialogo = false }
    )
}

@Composable
private fun PuntajeAnimado(puntaje: Int, maximo: Int) {
    var objetivo by remember { mutableIntStateOf(0) }
    LaunchedEffect(puntaje) { objetivo = puntaje }
    val mostrado by animateIntAsState(targetValue = objetivo, animationSpec = tween(1200), label = "puntaje")
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$mostrado / $maximo", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Text(
                "Puntaje de prueba calculado automáticamente. No es una interpretación ni un diagnóstico: la interpretación es exclusiva del psicólogo.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun HistorialScreen(onVolver: () -> Unit, onAbrir: (Int) -> Unit) {
    var tab by rememberSaveable { mutableStateOf(0) }

    PantallaBase(titulo = "Historial", onVolver = onVolver) { pad ->
        Column(Modifier.padding(pad)) {
            TabRow(
                selectedTabIndex = tab,
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Aplicaciones") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Trazabilidad") })
            }
            AnimatedContent(
                targetState = tab,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label = "historialTab"
            ) { actual ->
                if (actual == 0) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(DatosDemo.aplicaciones) { i, app ->
                            Aparecer(i) {
                                TarjetaAplicacion(app, mostrarProgreso = false, onClick = { onAbrir(app.id) })
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                "Registro de fecha, hora, usuario responsable y acción realizada.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        itemsIndexed(DatosDemo.trazabilidad) { i, reg ->
                            Aparecer(i) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(12.dp)
                                            .background(MaterialTheme.colorScheme.secondary, CircleShape)
                                    )
                                    Spacer(Modifier.size(12.dp))
                                    Column {
                                        Text(reg.accion, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            "${reg.fechaHora} · ${reg.usuario}",
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
        }
    }
}
