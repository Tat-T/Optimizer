package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GeneratorProgressScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "🧪 ИДЁТ РАСЧЁТ",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Анализируем исторические тиражи…",
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Формируем и проверяем комбинации.",
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Пожалуйста, не закрывайте приложение.",
            fontSize = 14.sp
        )
    }
}