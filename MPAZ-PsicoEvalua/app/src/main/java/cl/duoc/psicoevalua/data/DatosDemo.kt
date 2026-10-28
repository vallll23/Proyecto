package cl.duoc.psicoevalua.data



enum class EstadoInstrumento(val etiqueta: String) {
    DISPONIBLE("Disponible"),
    NO_DISPONIBLE("No disponible"),
    PENDIENTE("Pendiente de autorización")
}

enum class EstadoAplicacion(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    EN_DESARROLLO("En desarrollo"),
    FINALIZADA("Finalizada"),
    REVISADA("Revisada")
}

data class Instrumento(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val rangoEdad: String,
    val tiempoMin: Int,
    val estado: EstadoInstrumento,
    val instrucciones: String,
    val escala: List<String>,
    val preguntas: List<String>,
    val puntuable: Boolean
)

data class Aplicacion(
    val id: Int,
    val estudiante: String,
    val curso: String,
    val instrumentoId: Int,
    val estado: EstadoAplicacion,
    val progreso: Float,
    val inicio: String,
    val fin: String?,
    val respuestas: List<Int>,
    val observacion: String
)

data class RegistroTraza(
    val fechaHora: String,
    val usuario: String,
    val accion: String
)

object DatosDemo {

    private val escalaFrecuencia = listOf("Nunca", "A veces", "Casi siempre", "Siempre")
    private val escalaAcuerdo = listOf(
        "Muy en desacuerdo",
        "En desacuerdo",
        "Neutral",
        "De acuerdo",
        "Muy de acuerdo"
    )

    val instrumentos: List<Instrumento> = listOf(
        Instrumento(
            id = 1,
            nombre = "Cuestionario de Hábitos de Estudio",
            tipo = "Cuestionario educativo (propio)",
            descripcion = "Cuestionario educativo no diagnóstico creado para la demostración. Recoge hábitos generales de estudio.",
            rangoEdad = "5.º a 8.º básico",
            tiempoMin = 10,
            estado = EstadoInstrumento.DISPONIBLE,
            instrucciones = "Lee cada afirmación con calma y elige la opción que mejor te describa. No hay respuestas correctas ni incorrectas.",
            escala = escalaFrecuencia,
            preguntas = listOf(
                "Reviso mis cuadernos antes de una evaluación.",
                "Tengo un lugar tranquilo para hacer mis tareas.",
                "Organizo mi tiempo para entregar los trabajos a tiempo.",
                "Pido ayuda cuando no entiendo una materia.",
                "Pongo atención durante las clases.",
                "Hago un resumen de lo que aprendo.",
                "Dejo lista mi mochila el día anterior.",
                "Termino mis tareas antes de jugar o usar el celular."
            ),
            puntuable = false
        ),
        Instrumento(
            id = 2,
            nombre = "Inventario de Bienestar Escolar (Simulado)",
            tipo = "Prueba psicométrica SIMULADA",
            descripcion = "Estructura e ítems simulados para representar el funcionamiento de una prueba psicométrica con puntaje. No es un instrumento real.",
            rangoEdad = "6.º a 8.º básico",
            tiempoMin = 12,
            estado = EstadoInstrumento.DISPONIBLE,
            instrucciones = "Indica qué tan de acuerdo estás con cada frase. Responde con sinceridad; nadie de tu curso verá tus respuestas.",
            escala = escalaAcuerdo,
            preguntas = listOf(
                "Me siento a gusto en mi sala de clases.",
                "Me gusta participar en las actividades de mi curso.",
                "Tengo compañeros con quienes conversar en los recreos.",
                "Siento que mis profesores me escuchan.",
                "Me siento seguro/a dentro del colegio.",
                "Disfruto aprender cosas nuevas.",
                "Sé a quién acudir si necesito ayuda.",
                "Llego tranquilo/a al colegio por las mañanas."
            ),
            puntuable = true
        ),
        Instrumento(
            id = 3,
            nombre = "Cuestionario de Convivencia Escolar",
            tipo = "Cuestionario educativo (público)",
            descripcion = "Cuestionario breve sobre convivencia en el curso, basado en normas generales de buen trato.",
            rangoEdad = "3.º a 8.º básico",
            tiempoMin = 8,
            estado = EstadoInstrumento.DISPONIBLE,
            instrucciones = "Marca la opción que mejor represente cómo te comportas con tus compañeros y profesores.",
            escala = escalaFrecuencia,
            preguntas = listOf(
                "Respeto los turnos para hablar.",
                "Ayudo a mis compañeros cuando lo necesitan.",
                "Resuelvo los conflictos conversando.",
                "Cuido los materiales de la sala.",
                "Respeto las opiniones distintas a la mía.",
                "Cumplo las normas de mi curso."
            ),
            puntuable = true
        ),
        Instrumento(
            id = 4,
            nombre = "Prueba Estandarizada Protegida (ejemplo)",
            tipo = "Prueba psicométrica estandarizada",
            descripcion = "Representa un instrumento con licencia. No se incorporan ítems, baremos ni pautas hasta contar con autorización institucional y licencia de uso.",
            rangoEdad = "Según manual",
            tiempoMin = 40,
            estado = EstadoInstrumento.PENDIENTE,
            instrucciones = "Instrumento pendiente de autorización.",
            escala = emptyList(),
            preguntas = emptyList(),
            puntuable = false
        ),
        Instrumento(
            id = 5,
            nombre = "Escala de Atención (en revisión)",
            tipo = "Cuestionario educativo",
            descripcion = "Instrumento en preparación. Se habilitará cuando esté validado para uso académico.",
            rangoEdad = "1.º a 4.º básico",
            tiempoMin = 15,
            estado = EstadoInstrumento.NO_DISPONIBLE,
            instrucciones = "Instrumento no disponible.",
            escala = emptyList(),
            preguntas = emptyList(),
            puntuable = false
        )
    )

    val aplicaciones: List<Aplicacion> = listOf(
        Aplicacion(
            id = 1, estudiante = "EST-001", curso = "5.º A ", instrumentoId = 1,
            estado = EstadoAplicacion.FINALIZADA, progreso = 1f,
            inicio = "20-10-2026 09:15", fin = "20-10-2026 09:31",
            respuestas = listOf(2, 3, 1, 2, 3, 3, 2, 1), observacion = ""
        ),
        Aplicacion(
            id = 2, estudiante = "EST-002", curso = "6.º B ", instrumentoId = 2,
            estado = EstadoAplicacion.REVISADA, progreso = 1f,
            inicio = "21-10-2026 10:00", fin = "21-10-2026 10:14",
            respuestas = listOf(3, 4, 2, 3, 4, 3, 2, 4),
            observacion = "Observación ficticia de prueba: el estudiante se mostró atento durante la aplicación."
        ),
        Aplicacion(
            id = 3, estudiante = "EST-003", curso = "7.º A ", instrumentoId = 3,
            estado = EstadoAplicacion.EN_DESARROLLO, progreso = 0.5f,
            inicio = "27-10-2026 08:50", fin = null,
            respuestas = listOf(1, 2, 3), observacion = ""
        ),
        Aplicacion(
            id = 4, estudiante = "EST-004", curso = "8.º B ", instrumentoId = 1,
            estado = EstadoAplicacion.PENDIENTE, progreso = 0f,
            inicio = "27-10-2026 09:30", fin = null,
            respuestas = emptyList(), observacion = ""
        ),
        Aplicacion(
            id = 5, estudiante = "EST-005", curso = "5.º B ", instrumentoId = 3,
            estado = EstadoAplicacion.FINALIZADA, progreso = 1f,
            inicio = "22-10-2026 11:20", fin = "22-10-2026 11:30",
            respuestas = listOf(3, 2, 2, 3, 3, 1), observacion = ""
        )
    )

    val trazabilidad: List<RegistroTraza> = listOf(
        RegistroTraza("27-10-2026 08:42", "psicologo@mpaz.cl", "Inicio de sesión"),
        RegistroTraza("27-10-2026 08:45", "psicologo@mpaz.cl", "Creación de aplicación #3 (EST-003)"),
        RegistroTraza("27-10-2026 08:46", "psicologo@mpaz.cl", "Generación de código temporal"),
        RegistroTraza("26-10-2026 15:10", "psicologo@mpaz.cl", "Consulta de respuestas de aplicación #1"),
        RegistroTraza("26-10-2026 15:18", "psicologo@mpaz.cl", "Consulta de puntaje de prueba de aplicación #2"),
        RegistroTraza("26-10-2026 15:25", "psicologo@mpaz.cl", "Registro de observación en aplicación #2"),
        RegistroTraza("26-10-2026 15:26", "psicologo@mpaz.cl", "Aplicación #2 marcada como revisada")
    )

    fun instrumento(id: Int): Instrumento = instrumentos.first { it.id == id }

    fun aplicacion(id: Int): Aplicacion = aplicaciones.first { it.id == id }

    fun puntaje(app: Aplicacion): Int? {
        val inst = instrumento(app.instrumentoId)
        val terminada = app.estado == EstadoAplicacion.FINALIZADA || app.estado == EstadoAplicacion.REVISADA
        if (!inst.puntuable || !terminada) return null
        return app.respuestas.sumOf { it + 1 }
    }

    fun puntajeMaximo(inst: Instrumento): Int = inst.preguntas.size * inst.escala.size
}
