package com.example.rakshakavach.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rakshakavach.ui.viewmodel.MainViewModel

data class DashboardItem(
    val title: String, 
    val icon: ImageVector, 
    val route: String,
    val colors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    val items = listOf(
        DashboardItem("Start Check", Icons.Default.CheckCircle, "task_selection", listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))),
        DashboardItem("Safety Quiz", Icons.Default.Quiz, "safety_quiz", listOf(Color(0xFF2196F3), Color(0xFF1565C0))),
        DashboardItem("Report", Icons.Default.Report, "incident_report", listOf(Color(0xFFFF9800), Color(0xFFE65100))),
        DashboardItem("My Score", Icons.AutoMirrored.Filled.TrendingUp, "safety_score", listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A))),
        DashboardItem("SOS", Icons.Default.Warning, "sos", listOf(Color(0xFFF44336), Color(0xFFB71C1C))),
        DashboardItem("Profile", Icons.Default.Person, "profile", listOf(Color(0xFF607D8B), Color(0xFF37474F)))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("RAKSHA KAVACH", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color(0xFFE3F2FD),
                            Color(0xFFF3E5F5)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // User Greeting Header
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Box(modifier = Modifier.background(
                        Brush.horizontalGradient(listOf(Color.White, Color(0xFFE1F5FE), Color(0xFFF1F8E9)))
                    )) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                if (user?.profilePictureUri != null) {
                                    AsyncImage(
                                        model = user?.profilePictureUri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                }
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Hello, ${user?.name ?: "Worker"}!",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1C1E)
                                )
                                val statusColor = if((user?.totalQuizScore ?: 0) > 50) Color(0xFF2E7D32) else Color(0xFFE65100)
                                val statusBg = if((user?.totalQuizScore ?: 0) > 50) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                val statusBorder = if((user?.totalQuizScore ?: 0) > 50) Color(0xFF81C784) else Color(0xFFFFB74D)
                                
                                Surface(
                                    color = statusBg,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.border(1.dp, statusBorder, RoundedCornerShape(8.dp))
                                ) {
                                    Text(
                                        text = "Status: ${if((user?.totalQuizScore ?: 0) > 50) "Excellent" else "Stay Alert"}",
                                        fontSize = 12.sp,
                                        color = statusColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Safety Dashboard",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(items) { item ->
                        DashboardCard(item) {
                            if (item.route == "sos") {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${user?.supervisorPhone ?: "102"}")
                                }
                                context.startActivity(intent)
                            } else {
                                onNavigate(item.route)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(item: DashboardItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(item.colors))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(60.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.padding(14.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
