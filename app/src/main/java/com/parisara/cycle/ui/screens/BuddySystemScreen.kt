package com.parisara.cycle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.parisara.cycle.ui.theme.*

data class Buddy(val name: String, val distanceAway: String, val mutualRoute: Boolean)

@Composable
fun BuddySystemScreen() {
    val mockBuddies = listOf(
        Buddy("Alex (Student)", "200m away", true),
        Buddy("Sam", "500m away", true),
        Buddy("Jordan", "1.2km away", false)
    )

    val defaultLocation = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Map showing live locations
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(
                    state = MarkerState(position = LatLng(defaultLocation.latitude + 0.002, defaultLocation.longitude - 0.001)),
                    title = "Alex"
                )
                Marker(
                    state = MarkerState(position = LatLng(defaultLocation.latitude - 0.003, defaultLocation.longitude + 0.002)),
                    title = "Sam"
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Share Location Button
            Button(
                onClick = { /* Share Location */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InfoBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ShareLocation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share My Location", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Nearby Cyclists", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
            Spacer(modifier = Modifier.height(16.dp))

            // List of Users
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mockBuddies) { buddy ->
                    BuddyCard(buddy)
                }
            }
        }
    }
}

@Composable
fun BuddyCard(buddy: Buddy) {
    var isJoined by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightGreyAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = GreenPrimary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(buddy.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = LightGreyText)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(buddy.distanceAway, fontSize = 12.sp, color = LightGreyText)
                }
            }
            if (buddy.mutualRoute) {
                Button(
                    onClick = { isJoined = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isJoined) Color.Gray else GreenPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isJoined
                ) {
                    if (isJoined) {
                        Icon(Icons.Default.Check, contentDescription = "Joined", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Joined")
                    } else {
                        Icon(Icons.Default.GroupAdd, contentDescription = "Join Ride", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Join")
                    }
                }
            }
        }
    }
}
