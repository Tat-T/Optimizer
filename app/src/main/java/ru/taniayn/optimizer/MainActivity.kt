package ru.taniayn.optimizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private val csvFilePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->

            if (uri != null) {
                // Файл выбран.
                // Обработку CSV подключим следующим шагом.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OptimizerApp(onPickCsv = {
                csvFilePicker.launch(
                    arrayOf("text/csv", "text/comma-separated-values", "*/*")
                )
            })
        }
    }
}

@Composable
fun OptimizerApp(onPickCsv: () -> Unit) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🎯",
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ОПТИМИЗАТОР",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Анализатор Рапидо",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Загружено тиражей: 0",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPickCsv,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📂  ЗАГРУЗИТЬ CSV",
                    fontSize = 17.sp
                )
            }
        }
    }
}