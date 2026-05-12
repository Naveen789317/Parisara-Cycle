package com.parisara.cycle.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parisara.cycle.ui.theme.*

@Composable
fun EcoStatsScreen() {
    val dailyCO2SavedKg = 1.2f
    val monthlyCO2SavedKg = 24.5f
    val distanceTravelled = 45.2f // km
    val caloriesBurned = 1250

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Eco Stats", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
        Spacer(modifier = Modifier.height(24.dp))

        // Large CO2 Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GreenPrimary),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.EnergySavingsLeaf, contentDescription = "Leaf", tint = Color.White, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Today's CO2 Saved", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                Text("${dailyCO2SavedKg} kg", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Monthly & Weekly Summary
        Text("Weekly Progress", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreyText, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp).fillMaxSize(), contentAlignment = Alignment.Center) {
                WeeklyBarChart()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Additional Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Monthly CO2",
                value = "$monthlyCO2SavedKg kg",
                icon = Icons.Default.Timeline,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Distance",
                value = "$distanceTravelled km",
                icon = Icons.Default.DirectionsBike,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Calories",
                value = "$caloriesBurned",
                icon = Icons.Default.LocalFireDepartment,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = GreenPrimary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
            Text(title, fontSize = 12.sp, color = LightGreyText)
        }
    }
}

@Composable
fun WeeklyBarChart() {
    val barHeights = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 1.0f, 0.3f)
    val days = listOf("M", "T", "W", "T", "F", "S", "S")

    Canvas(modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 24.dp)) {
        val barWidth = 24.dp.toPx()
        val spacing = (size.width - (barWidth * 7)) / 6
        
        barHeights.forEachIndexed { index, heightPercentage ->
            val xOffset = index * (barWidth + spacing)
            val barHeight = size.height * heightPercentage
            
            drawLine(
                color = GreenSecondary,
                start = Offset(xOffset + barWidth / 2, size.height),
                end = Offset(xOffset + barWidth / 2, size.height - barHeight),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
    
    // Labels
    Row(
        modifier = Modifier.fillMaxSize().padding(top = 110.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEach { day ->
            Text(day, fontSize = 12.sp, color = LightGreyText)
        }
    }
}
