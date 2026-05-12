package com.parisara.cycle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.parisara.cycle.ui.viewmodels.AuthViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parisara.cycle.ui.theme.*

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, onLogout: () -> Unit) {
    val currentUser by authViewModel.currentUser.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Picture",
            tint = GreenPrimary,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(LightGreyAccent)
                .padding(16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentUser?.name ?: "Guest", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
        Text(currentUser?.email ?: "No email", fontSize = 16.sp, color = LightGreyText)
        Text(currentUser?.mobile ?: "No mobile", fontSize = 14.sp, color = LightGreyText)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Dashboard Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(icon = Icons.Default.DirectionsBike, title = "Distance", value = "120.5 km")
            StatCard(icon = Icons.Default.Eco, title = "CO2 Saved", value = "25.2 kg")
            StatCard(icon = Icons.Default.EmojiEvents, title = "Total Rides", value = "34")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = GreenPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Settings", fontSize = 18.sp, color = DarkGreyText)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, title: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.size(105.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = GreenPrimary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreyText)
            Text(title, fontSize = 12.sp, color = LightGreyText)
        }
    }
}
