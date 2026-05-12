package com.parisara.cycle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
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
import com.parisara.cycle.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PitStopScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var isMapView by remember { mutableStateOf(false) }

    val mockPlaces = listOf(
        PitStop("Raju Cycle Repair", "1.2 km away", Icons.Default.Build),
        PitStop("Public Water Point", "1.5 km away", Icons.Default.LocalDrink),
        PitStop("Kiran's Bike Shop", "2.8 km away", Icons.Default.Build)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Pit Stops", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Repair shops, water points...", color = LightGreyText) },
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

        // Toggle View
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("List View", fontSize = 14.sp, color = if (!isMapView) GreenPrimary else DarkGreyText)
            Switch(
                checked = isMapView,
                onCheckedChange = { isMapView = it },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenPrimary
                )
            )
            Text("Map View", fontSize = 14.sp, color = if (isMapView) GreenPrimary else DarkGreyText)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isMapView) {
            // Map Placeholder
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreyAccent)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Default.Map, contentDescription = "Map", tint = GreenTertiary, modifier = Modifier.size(64.dp))
                    Text("Interactive Map Placeholder", modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
                }
            }
        } else {
            // List View
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockPlaces) { place ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(place.icon, contentDescription = place.name, tint = GreenPrimary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(place.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
                                Text(place.distance, fontSize = 14.sp, color = LightGreyText)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { /* Navigate */ },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                            ) {
                                Text("Go")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PitStop(val name: String, val distance: String, val icon: ImageVector)
