package com.example.workmanagertest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.workmanagertest.domain.usecase.UseCaseFactory
import com.example.workmanagertest.ui.theme.WorkManagerTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkManagerTestTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    val context = LocalContext.current
    Button(
        modifier = Modifier.padding(100.dp),
        onClick = { callTest(context) }
    ) {
        Text(text = "WorkerStart")
    }
}


private fun callTest(context: Context) {
    Log.d("test", "call TestWorkBuilder")
    val useCase = UseCaseFactory.createTestWorkerBuilder(context.applicationContext)
    useCase()
}