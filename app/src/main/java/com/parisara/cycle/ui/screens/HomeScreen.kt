package com.parisara.cycle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parisara.cycle.Screen
import com.parisara.cycle.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Parisara Cycle",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = GreenPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LightGreyAccent)
                    .padding(8.dp)
                    .clickable { navController.navigate(Screen.Profile.route) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Where to?", color = LightGreyText) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = SurfaceLight,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = GreenPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Map Preview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { navController.navigate(Screen.Routes.route) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreyAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for Map
                Icon(Icons.Default.Map, contentDescription = "Map Preview", tint = GreenTertiary, modifier = Modifier.size(64.dp))
                Text("Tap to open map", modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp), color = DarkGreyText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Ride Button
        Button(
            onClick = { navController.navigate(Screen.Routes.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Start Ride", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's CO2 Saved
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Eco, contentDescription = "Eco", tint = GreenPrimary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Today's CO2 Saved", color = DarkGreyText, fontSize = 14.sp)
                    Text("1.2 kg", color = GreenPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Feature Grid
        Text("Explore Features", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                title = "Safe Route",
                icon = Icons.Default.DirectionsBike,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Routes.route) }
            )
            FeatureCard(
                title = "Eco Stats",
                icon = Icons.Default.BarChart,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Stats.route) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                title = "Buddy System",
                icon = Icons.Default.Group,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Buddy.route) }
            )
            FeatureCard(
                title = "Pit Stops",
                icon = Icons.Default.Build, // Placeholder icon for Pit Stop
                modifier = Modifier.weight(1f),
                onClick = { /* TODO: Add Pit Stop Route */ }
            )
        }
    }
}

@Composable
fun FeatureCard(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = GreenPrimary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkGreyText)
        }
    }
}
