package cl.duoc.psicoevalua.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.psicoevalua.data.DatabaseHelper
import cl.duoc.psicoevalua.data.Rol
import cl.duoc.psicoevalua.data.Usuario

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Psicólogo
    const val PSI_HOME = "psi_home"
    const val CATALOGO = "catalogo"
    const val DETALLE_INSTRUMENTO = "instrumento/{id}"
    const val CREAR = "crear/{instId}"
    const val CODIGO = "codigo"
    const val APLICACIONES = "aplicaciones/{modo}"
    const val DETALLE_APLICACION = "aplicacion/{id}"
    const val HISTORIAL = "historial"

    // Comunes
    const val PRIVACIDAD = "privacidad"
    const val PERFIL = "perfil"

    // Estudiante
    const val EST_CODIGO = "est_codigo"
    const val EST_INSTRUCCIONES = "est_instrucciones/{id}"
    const val EST_CUESTIONARIO = "est_cuestionario/{id}"
    const val EST_CONFIRMACION = "est_confirmacion"

    fun instrumento(id: Int) = "instrumento/$id"
    fun crear(instId: Int) = "crear/$instId"
    fun aplicaciones(modo: String) = "aplicaciones/$modo"
    fun aplicacion(id: Int) = "aplicacion/$id"
    fun estInstrucciones(id: Int) = "est_instrucciones/$id"
    fun estCuestionario(id: Int) = "est_cuestionario/$id"
}

/** Control de acceso por perfil: si el rol no corresponde, se vuelve al login. */
@Composable
private fun RutaProtegida(
    usuario: Usuario?,
    rolRequerido: Rol,
    alDenegar: () -> Unit,
    contenido: @Composable (Usuario) -> Unit
) {
    if (usuario != null && usuario.rol == rolRequerido) {
        contenido(usuario)
    } else {
        LaunchedEffect(Unit) { alDenegar() }
    }
}

@Composable
fun AppNav() {
    val contexto = LocalContext.current
    val db = remember { DatabaseHelper(contexto.applicationContext) }
    val nav = rememberNavController()
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    fun irALogin() {
        usuario = null
        nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
    }

    NavHost(
        navController = nav,
        startDestination = Rutas.SPLASH,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350)) + fadeIn(tween(350))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350)) + fadeOut(tween(250))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350)) + fadeIn(tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350)) + fadeOut(tween(250))
        }
    ) {
        // ---------------- Acceso ----------------
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                db = db,
                onExito = { u ->
                    usuario = u
                    val destino = if (u.rol == Rol.PSICOLOGO) Rutas.PSI_HOME else Rutas.EST_CODIGO
                    nav.navigate(destino) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                },
                onIrRegistro = { nav.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                db = db,
                onVolver = { nav.popBackStack() },
                onRegistrado = { nav.popBackStack() }
            )
        }

        // ---------------- Psicólogo ----------------
        composable(Rutas.PSI_HOME) {
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) { u ->
                PsiHomeScreen(
                    usuario = u,
                    onCatalogo = { nav.navigate(Rutas.CATALOGO) },
                    onNueva = { nav.navigate(Rutas.crear(0)) },
                    onSupervisar = { nav.navigate(Rutas.aplicaciones("supervisar")) },
                    onRevisar = { nav.navigate(Rutas.aplicaciones("revisar")) },
                    onHistorial = { nav.navigate(Rutas.HISTORIAL) },
                    onPrivacidad = { nav.navigate(Rutas.PRIVACIDAD) },
                    onPerfil = { nav.navigate(Rutas.PERFIL) }
                )
            }
        }
        composable(Rutas.CATALOGO) {
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                CatalogoScreen(
                    onVolver = { nav.popBackStack() },
                    onAbrir = { nav.navigate(Rutas.instrumento(it)) }
                )
            }
        }
        composable(
            Rutas.DETALLE_INSTRUMENTO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 1
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                DetalleInstrumentoScreen(
                    id = id,
                    onVolver = { nav.popBackStack() },
                    onCrear = { nav.navigate(Rutas.crear(it)) }
                )
            }
        }
        composable(
            Rutas.CREAR,
            arguments = listOf(navArgument("instId") { type = NavType.IntType })
        ) { entrada ->
            val instId = entrada.arguments?.getInt("instId") ?: 0
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                CrearAplicacionScreen(
                    instrumentoInicial = instId,
                    onVolver = { nav.popBackStack() },
                    onGenerarCodigo = { nav.navigate(Rutas.CODIGO) }
                )
            }
        }
        composable(Rutas.CODIGO) {
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                CodigoTemporalScreen(
                    onVolver = { nav.popBackStack() },
                    onSupervisar = { nav.navigate(Rutas.aplicaciones("supervisar")) },
                    onPanel = { nav.popBackStack(Rutas.PSI_HOME, inclusive = false) }
                )
            }
        }
        composable(
            Rutas.APLICACIONES,
            arguments = listOf(navArgument("modo") { type = NavType.StringType })
        ) { entrada ->
            val modo = entrada.arguments?.getString("modo") ?: "supervisar"
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                AplicacionesScreen(
                    modo = modo,
                    onVolver = { nav.popBackStack() },
                    onAbrir = { nav.navigate(Rutas.aplicacion(it)) }
                )
            }
        }
        composable(
            Rutas.DETALLE_APLICACION,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 1
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) { u ->
                DetalleAplicacionScreen(id = id, usuario = u, onVolver = { nav.popBackStack() })
            }
        }
        composable(Rutas.HISTORIAL) {
            RutaProtegida(usuario, Rol.PSICOLOGO, { irALogin() }) {
                HistorialScreen(
                    onVolver = { nav.popBackStack() },
                    onAbrir = { nav.navigate(Rutas.aplicacion(it)) }
                )
            }
        }

        // ---------------- Comunes ----------------
        composable(Rutas.PRIVACIDAD) {
            PrivacidadScreen(onVolver = { nav.popBackStack() })
        }
        composable(Rutas.PERFIL) {
            val u = usuario
            if (u != null) {
                PerfilScreen(
                    usuario = u,
                    onVolver = { nav.popBackStack() },
                    onCerrarSesion = { irALogin() }
                )
            } else {
                LaunchedEffect(Unit) { irALogin() }
            }
        }

        // ---------------- Estudiante ----------------
        composable(Rutas.EST_CODIGO) {
            RutaProtegida(usuario, Rol.ESTUDIANTE, { irALogin() }) { u ->
                EstCodigoScreen(
                    nombre = u.nombre,
                    onContinuar = { nav.navigate(Rutas.estInstrucciones(1)) },
                    onPrivacidad = { nav.navigate(Rutas.PRIVACIDAD) },
                    onSalir = { irALogin() }
                )
            }
        }
        composable(
            Rutas.EST_INSTRUCCIONES,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 1
            RutaProtegida(usuario, Rol.ESTUDIANTE, { irALogin() }) {
                EstInstruccionesScreen(
                    id = id,
                    onVolver = { nav.popBackStack() },
                    onComenzar = { nav.navigate(Rutas.estCuestionario(id)) }
                )
            }
        }
        composable(
            Rutas.EST_CUESTIONARIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 1
            RutaProtegida(usuario, Rol.ESTUDIANTE, { irALogin() }) {
                EstCuestionarioScreen(
                    id = id,
                    onFinalizar = {
                        nav.navigate(Rutas.EST_CONFIRMACION) {
                            popUpTo(Rutas.EST_CODIGO) { inclusive = false }
                        }
                    },
                    onSalir = { nav.popBackStack(Rutas.EST_CODIGO, inclusive = false) }
                )
            }
        }
        composable(Rutas.EST_CONFIRMACION) {
            RutaProtegida(usuario, Rol.ESTUDIANTE, { irALogin() }) {
                EstConfirmacionScreen(onSalir = { irALogin() })
            }
        }
    }
}
