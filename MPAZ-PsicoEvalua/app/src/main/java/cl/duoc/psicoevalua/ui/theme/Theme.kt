package cl.duoc.psicoevalua.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AzulPrimario = Color(0xFF1F4E8C)
val VerdeAzulado = Color(0xFF2A9D8F)

val GradientePrincipal = Brush.linearGradient(listOf(AzulPrimario, VerdeAzulado))

private val Esquema = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E6FA),
    onPrimaryContainer = Color(0xFF0B2A4A),
    secondary = VerdeAzulado,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDEFEA),
    onSecondaryContainer = Color(0xFF06443D),
    tertiary = Color(0xFFF4A261),
    background = Color(0xFFF6F9FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EEF5),
    error = Color(0xFFC0392B)
)

@Composable
fun PsicoEvaluaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Esquema,
        shapes = Shapes(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
