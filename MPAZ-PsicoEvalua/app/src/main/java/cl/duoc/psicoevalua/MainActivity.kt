package cl.duoc.psicoevalua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cl.duoc.psicoevalua.ui.AppNav
import cl.duoc.psicoevalua.ui.theme.PsicoEvaluaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PsicoEvaluaTheme {
                AppNav()
            }
        }
    }
}
