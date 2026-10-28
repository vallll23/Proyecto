package cl.duoc.psicoevalua.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.psicoevalua.data.DatabaseHelper
import cl.duoc.psicoevalua.data.ResultadoRegistro
import cl.duoc.psicoevalua.data.Rol
import cl.duoc.psicoevalua.data.Usuario
import cl.duoc.psicoevalua.ui.theme.GradientePrincipal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    val escala = remember { Animatable(0.4f) }
    val alfa = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            escala.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        launch { alfa.animateTo(1f, tween(900)) }
        delay(2400)
        onTerminar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientePrincipal),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = alfa.value
                scaleX = escala.value
                scaleY = escala.value
            }
        ) {
            LogoAnimado()
            Spacer(Modifier.height(20.dp))
            Text(
                "MPAZ PsicoEvalúa",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Escuela Escritora Marcela Paz",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(28.dp))
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun LoginScreen(
    db: DatabaseHelper,
    onExito: (Usuario) -> Unit,
    onIrRegistro: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var verClave by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var intentosFallidos by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()


    val sacudida = remember { Animatable(0f) }
    LaunchedEffect(intentosFallidos) {
        if (intentosFallidos > 0) {
            repeat(3) {
                sacudida.animateTo(14f, tween(50))
                sacudida.animateTo(-14f, tween(50))
            }
            sacudida.animateTo(0f, tween(50))
        }
    }

    fun iniciarSesion() {
        if (correo.isBlank() || clave.isBlank()) {
            error = "Completa tu correo y contraseña."
            intentosFallidos++
        } else {
            cargando = true
            error = null
            scope.launch {
                val usuario = withContext(Dispatchers.IO) { db.autenticar(correo, clave) }
                cargando = false
                if (usuario != null) {
                    onExito(usuario)
                } else {
                    error = "Correo o contraseña incorrectos."
                    intentosFallidos++
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientePrincipal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Aparecer(0) { LogoAnimado() }
            Spacer(Modifier.height(12.dp))
            Aparecer(1) {
                Text(
                    "MPAZ PsicoEvalúa",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(20.dp))

            Aparecer(2) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { androidx.compose.ui.unit.IntOffset(sacudida.value.roundToInt(), 0) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Iniciar sesión",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        CampoTexto(
                            valor = correo,
                            alCambiar = { correo = it; error = null },
                            etiqueta = "Correo",
                            icono = Icons.Filled.Email,
                            teclado = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        CampoTexto(
                            valor = clave,
                            alCambiar = { clave = it; error = null },
                            etiqueta = "Contraseña",
                            icono = Icons.Filled.Lock,
                            teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visual = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                            trailing = {
                                IconButton(onClick = { verClave = !verClave }) {
                                    Icon(
                                        if (verClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = "Mostrar u ocultar contraseña"
                                    )
                                }
                            }
                        )
                        AnimatedVisibility(visible = error != null) {
                            Text(
                                error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        BotonPrincipal(
                            texto = "Ingresar",
                            onClick = { iniciarSesion() },
                            icono = Icons.AutoMirrored.Filled.Login,
                            cargando = cargando
                        )
                        TextButton(
                            onClick = onIrRegistro,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("¿No tienes cuenta? Regístrate")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Aparecer(3) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f))
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            "Cuentas de demostración (datos ficticios)",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Psicólogo: psicologo@mpaz.cl\nEstudiante: estudiante@mpaz.cl\nContraseña: Demo1234",
                            color = Color.White.copy(alpha = 0.92f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    db: DatabaseHelper,
    onVolver: () -> Unit,
    onRegistrado: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var rol by rememberSaveable { mutableStateOf(Rol.PSICOLOGO.name) }
    var verClave by remember { mutableStateOf(false) }
    var intento by remember { mutableStateOf(false) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var exito by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val errNombre = if (intento && nombre.isBlank()) "Ingresa un nombre (ficticio)." else null
    val errCorreo = if (intento && !android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches())
        "Ingresa un correo válido." else null
    val errClave = if (intento && clave.length < 6) "Mínimo 6 caracteres." else null
    val errConfirmar = if (intento && confirmar != clave) "Las contraseñas no coinciden." else null

    fun registrar() {
        intento = true
        errorGeneral = null
        val valido = errNombre == null && errCorreo == null && errClave == null && errConfirmar == null &&
            nombre.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() &&
            clave.length >= 6 && confirmar == clave
        if (valido) {
            cargando = true
            scope.launch {
                val resultado = withContext(Dispatchers.IO) {
                    db.registrarUsuario(nombre, correo, clave, Rol.valueOf(rol))
                }
                cargando = false
                when (resultado) {
                    is ResultadoRegistro.Exito -> exito = true
                    is ResultadoRegistro.Error -> errorGeneral = resultado.mensaje
                }
            }
        }
    }

    PantallaBase(titulo = "Crear cuenta", onVolver = onVolver) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Aparecer(0) {
                Text(
                    "Regístrate con datos ficticios. No uses nombres, RUT ni información real de estudiantes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Aparecer(1) {
                CampoTexto(nombre, { nombre = it }, "Nombre (ficticio)", icono = Icons.Filled.Person, error = errNombre)
            }
            Aparecer(2) {
                CampoTexto(
                    correo, { correo = it }, "Correo", icono = Icons.Filled.Email, error = errCorreo,
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }
            Aparecer(3) {
                CampoTexto(
                    clave, { clave = it }, "Contraseña", icono = Icons.Filled.Lock, error = errClave,
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visual = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                    trailing = {
                        IconButton(onClick = { verClave = !verClave }) {
                            Icon(
                                if (verClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Mostrar u ocultar contraseña"
                            )
                        }
                    }
                )
            }
            Aparecer(4) {
                CampoTexto(
                    confirmar, { confirmar = it }, "Confirmar contraseña", icono = Icons.Filled.Lock,
                    error = errConfirmar,
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visual = if (verClave) VisualTransformation.None else PasswordVisualTransformation()
                )
            }
            Aparecer(5) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Perfil", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = rol == Rol.PSICOLOGO.name,
                            onClick = { rol = Rol.PSICOLOGO.name },
                            label = { Text(Rol.PSICOLOGO.etiqueta) },
                            leadingIcon = { Icon(Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                        FilterChip(
                            selected = rol == Rol.ESTUDIANTE.name,
                            onClick = { rol = Rol.ESTUDIANTE.name },
                            label = { Text(Rol.ESTUDIANTE.etiqueta) },
                            leadingIcon = { Icon(Icons.Filled.School, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                    }
                    Text(
                        "En una versión real, el perfil de psicólogo lo asignaría la institución.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            AnimatedVisibility(visible = errorGeneral != null) {
                Text(errorGeneral ?: "", color = MaterialTheme.colorScheme.error)
            }
            Aparecer(6) {
                BotonPrincipal(
                    texto = "Crear cuenta",
                    onClick = { registrar() },
                    icono = Icons.Filled.PersonAdd,
                    cargando = cargando
                )
            }
        }
    }

    if (exito) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Cuenta creada!") },
            text = { Text("Tu cuenta se guardó en la base de datos local. Ya puedes iniciar sesión.") },
            confirmButton = {
                TextButton(onClick = onRegistrado) { Text("Ir a iniciar sesión") }
            }
        )
    }
}
