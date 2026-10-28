package cl.duoc.psicoevalua.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.psicoevalua.data.Usuario
import cl.duoc.psicoevalua.ui.theme.GradientePrincipal

private data class PuntoPrivacidad(val icono: ImageVector, val titulo: String, val texto: String)


@Composable
fun PrivacidadScreen(onVolver: () -> Unit) {
    val puntos = listOf(
        PuntoPrivacidad(
            Icons.Filled.Lock, "Acceso exclusivo",
            "Solo el psicólogo PIE puede ver respuestas, puntajes y observaciones. El estudiante únicamente responde el instrumento asignado."
        ),
        PuntoPrivacidad(
            Icons.Filled.Block, "Sin diagnósticos",
            "La aplicación es una herramienta de apoyo: no diagnostica, no emite conclusiones clínicas ni toma decisiones automáticas."
        ),
        PuntoPrivacidad(
            Icons.Filled.Science, "Datos ficticios",
            "En esta demostración todos los estudiantes, respuestas y puntajes son inventados. No se usan nombres, RUT ni antecedentes PIE reales."
        ),
        PuntoPrivacidad(
            Icons.Filled.VerifiedUser, "Instrumentos autorizados",
            "Solo se usan instrumentos propios, públicos, simulados o con licencia. Las pruebas estandarizadas protegidas no se reproducen."
        ),
        PuntoPrivacidad(
            Icons.Filled.School, "Uso supervisado",
            "La aplicación se usa dentro de la escuela, con supervisión del psicólogo. No usa cámara, GPS, micrófono ni biometría."
        ),
        PuntoPrivacidad(
            Icons.Filled.Security, "Futura implementación real",
            "Antes de usar datos de estudiantes reales se requiere revisión institucional: finalidad, acceso, seguridad, retención y eliminación de datos."
        )
    )

    PantallaBase(titulo = "Privacidad y alcance", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            puntos.forEachIndexed { i, p ->
                Aparecer(i) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(p.icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.size(12.dp))
                            Column {
                                Text(p.titulo, fontWeight = FontWeight.Bold)
                                Text(p.texto, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PerfilScreen(usuario: Usuario, onVolver: () -> Unit, onCerrarSesion: () -> Unit) {
    var confirmar by remember { mutableStateOf(false) }

    PantallaBase(titulo = "Mi perfil", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Aparecer(0) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(GradientePrincipal, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        usuario.nombre.firstOrNull()?.uppercase() ?: "?",
                        color = Color.White,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Aparecer(1) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        InfoFila("Nombre", usuario.nombre)
                        InfoFila("Correo", usuario.correo)
                        InfoFila("Perfil", usuario.rol.etiqueta)
                    }
                }
            }
            Aparecer(2) {
                BotonPrincipal(
                    texto = "Cerrar sesión",
                    icono = Icons.AutoMirrored.Filled.Logout,
                    onClick = { confirmar = true }
                )
            }
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres salir de tu cuenta?") },
            confirmButton = { TextButton(onClick = onCerrarSesion) { Text("Salir") } },
            dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Cancelar") } }
        )
    }
}
