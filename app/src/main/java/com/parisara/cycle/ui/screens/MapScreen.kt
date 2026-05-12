package com.parisara.cycle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.parisara.cycle.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val defaultLocation = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    var isRiding by remember { mutableStateOf(false) }
    var isSafestRoute by remember { mutableStateOf(true) }
    var showReportSheet by remember { mutableStateOf(false) }

    val dangerPins = remember { mutableStateListOf<LatLng>(
        LatLng(defaultLocation.latitude + 0.002, defaultLocation.longitude - 0.002)
    ) }
    val repairShops = remember { mutableStateListOf<LatLng>(
        LatLng(defaultLocation.latitude - 0.003, defaultLocation.longitude + 0.004)
    ) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Pinned Danger Zones
            dangerPins.forEach { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Danger Zone / Pothole",
                    snippet = "Avoid this area"
                )
            }
            // Repair Shops
            repairShops.forEach { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Repair Shop",
                    snippet = "Cycle Repair"
                )
            }
            // Mock Path (Polyline)
            Polyline(
                points = listOf(
                    LatLng(defaultLocation.latitude - 0.01, defaultLocation.longitude - 0.01),
                    defaultLocation,
                    LatLng(defaultLocation.latitude + 0.01, defaultLocation.longitude + 0.01)
                ),
                color = if (isSafestRoute) GreenPrimary else InfoBlue,
                width = 12f
            )
        }

        // Top Toggles (Safest vs Shortest)
        if (!isRiding) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceLight,
                    shadowElevation = 4.dp
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Button(
                            onClick = { isSafestRoute = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSafestRoute) GreenPrimary else Color.Transparent,
                                contentColor = if (isSafestRoute) Color.White else DarkGreyText
                            ),
                            elevation = null,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Safest Route")
                        }
                        Button(
                            onClick = { isSafestRoute = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isSafestRoute) GreenPrimary else Color.Transparent,
                                contentColor = if (!isSafestRoute) Color.White else DarkGreyText
                            ),
                            elevation = null,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Shortest")
                        }
                    }
                }
            }
        }

        // Bottom Sheet / Action Panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = { showReportSheet = true },
                    containerColor = DangerRed,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Report Issue")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (isRiding) {
                        // Ride Tracking State
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Distance", color = LightGreyText, fontSize = 12.sp)
                                Text("2.4 km", color = DarkGreyText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Time", color = LightGreyText, fontSize = 12.sp)
                                Text("12:05", color = DarkGreyText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Speed", color = LightGreyText, fontSize = 12.sp)
                                Text("18 km/h", color = DarkGreyText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { isRiding = false },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("End Ride", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Safe Route Preview State
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("To Central Park", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text("5.2 km", color = LightGreyText)
                                    Text("25 min", color = LightGreyText)
                                    Text(if (isSafestRoute) "High Safety" else "Avg Safety", color = GreenPrimary, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { isRiding = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Navigation", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showReportSheet) {
        ModalBottomSheet(onDismissRequest = { showReportSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Report Issue", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = true, onClick = { }, label = { Text("Pothole") })
                    FilterChip(selected = false, onClick = { }, label = { Text("Danger Zone") })
                    FilterChip(selected = false, onClick = { }, label = { Text("Traffic") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showReportSheet = false },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Submit Report")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
