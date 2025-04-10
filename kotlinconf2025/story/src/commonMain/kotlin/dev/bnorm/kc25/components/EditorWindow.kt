package dev.bnorm.kc25.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.mac.MacWindow

@Composable
fun EditorWindow(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MacWindow(
        color = Color(0xFF3B3E40),
        modifier = modifier.fillMaxSize(),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // TODO switch to kotlin flag from IntelliJ
//                    Image(
//                        painter = painterResource(Res.drawable.kotlinGradleScript_dark),
//                        contentDescription = "",
//                        modifier = Modifier.size(18.dp),
//                    )
                Spacer(Modifier.width(8.dp))
                Text(text = title, fontSize = 14.sp, lineHeight = 14.sp)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF202124))
        ) {
            content()
        }
    }
}
