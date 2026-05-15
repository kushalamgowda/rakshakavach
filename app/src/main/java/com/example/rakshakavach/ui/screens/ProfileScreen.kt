package com.example.rakshakavach.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rakshakavach.data.local.entities.Incident
import com.example.rakshakavach.data.local.entities.User
import com.example.rakshakavach.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val incidents by viewModel.userIncidents.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    // Edit state
    var editName by remember(user) { mutableStateOf(user?.name ?: "") }
    var editJobRole by remember(user) { mutableStateOf(user?.jobRole ?: "") }
    var editEmployeeId by remember(user) { mutableStateOf(user?.employeeId ?: "") }
    var editCompany by remember(user) { mutableStateOf(user?.companyName ?: "") }
    var editSupervisor by remember(user) { mutableStateOf(user?.supervisorPhone ?: "") }
    var editFamily by remember(user) { mutableStateOf(user?.familyPhone ?: "") }
    var profileUri by remember(user) { mutableStateOf(user?.profilePictureUri) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileUri = uri?.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            user?.let {
                                val updatedUser = it.copy(
                                    name = editName,
                                    jobRole = editJobRole,
                                    employeeId = editEmployeeId,
                                    companyName = editCompany,
                                    supervisorPhone = editSupervisor,
                                    familyPhone = editFamily,
                                    profilePictureUri = profileUri
                                )
                                viewModel.updateProfile(updatedUser)
                            }
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8F9FA))
        ) {
            // Header: Profile Image and Basic Info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 40.dp, top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable(enabled = isEditing) { launcher.launch("image/*") }
                    ) {
                        if (profileUri != null) {
                            AsyncImage(
                                model = profileUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                tint = Color.White
                            )
                        }
                        if (isEditing) {
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isEditing) {
                        ProfileEditFields(
                            name = editName, onNameChange = { editName = it },
                            role = editJobRole, onRoleChange = { editJobRole = it },
                            empId = editEmployeeId, onEmpIdChange = { editEmployeeId = it },
                            company = editCompany, onCompanyChange = { editCompany = it }
                        )
                    } else {
                        Text(
                            text = if (user?.name?.isNotBlank() == true) user!!.name else "Kushala M",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (user?.jobRole?.isNotBlank() == true) user!!.jobRole else "Welder",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "Employee ID: ${if (user?.employeeId?.isNotBlank() == true) user!!.employeeId else "RK1023"}",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = if (user?.companyName?.isNotBlank() == true) user!!.companyName else "Raksha Sites Ltd.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Safety Score Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-25).dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(85.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { (user?.safetyScorePercentage ?: 92) / 100f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 10.dp,
                            color = Color(0xFF4CAF50),
                            trackColor = Color(0xFFE8F5E9)
                        )
                        Text(
                            text = "${user?.safetyScorePercentage ?: 92}%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text("Safety Score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            "PPE Compliance | Quiz | Safety Days",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Row(modifier = Modifier.padding(top = 10.dp)) {
                            Badge(text = "${user?.incidentFreeDays ?: 12} Incident-Free Days", color = Color(0xFFE3F2FD), textColor = Color(0xFF1976D2))
                        }
                    }
                }
            }

            // Completed Trainings
            ProfileSection("Completed Safety Trainings", Icons.Default.Verified) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    TrainingBadge("✅ Welding Safety Certified", true)
                    TrainingBadge("✅ Electrical Safety Training", true)
                    TrainingBadge("✅ Fire Safety Awareness", true)
                }
            }

            // Quiz Performance
            ProfileSection("Quiz Performance", Icons.Default.AutoGraph) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatBox("Daily Streak", "${user?.quizStreak ?: 7} days 🔥", Color(0xFFFF5722))
                    StatBox("Quizzes Done", "${user?.completedQuizzes ?: 15}", Color(0xFF2196F3))
                    StatBox("Avg Score", "88%", Color(0xFFFFC107))
                }
            }

            // Incident History
            ProfileSection("Incident History", Icons.Default.History) {
                if (incidents.isEmpty() && !isEditing) {
                    IncidentItemMock("Reported loose wiring – 12 May", "Near Miss")
                    IncidentItemMock("Slippery floor in Zone B – 05 May", "Environment")
                } else {
                    incidents.take(5).forEach { incident ->
                        IncidentItem(incident)
                    }
                }
            }

            // Rewards / Badges
            ProfileSection("Rewards & Badges", Icons.Default.EmojiEvents) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    RewardBadge("🏅 Safety Champion", Color(0xFFFFF9C4))
                    RewardBadge("🏅 30 Accident-Free Days", Color(0xFFC8E6C9))
                    RewardBadge("🏅 Top Quizzer", Color(0xFFE1BEE7))
                }
            }

            // Emergency Contacts
            ProfileSection("Emergency Contacts", Icons.Default.Emergency) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editSupervisor,
                        onValueChange = { editSupervisor = it },
                        label = { Text("Supervisor Number") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editFamily,
                        onValueChange = { editFamily = it },
                        label = { Text("Family Contact") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    ContactItem("Supervisor", if (user?.supervisorPhone?.isNotBlank() == true) user!!.supervisorPhone else "+91 98765 43210", Icons.Default.SupportAgent)
                    ContactItem("Family Member", if (user?.familyPhone?.isNotBlank() == true) user!!.familyPhone else "+91 99887 76655", Icons.Default.Favorite)
                    ContactItem("Ambulance", "102", Icons.Default.LocalHospital)
                }
            }

            // Gear Compliance
            ProfileSection("Safety Gear Compliance", Icons.Default.HealthAndSafety) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Last 30 days PPE compliance: 95%", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        LinearProgressIndicator(
                            progress = { 0.95f },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(8.dp),
                            color = Color(0xFF4CAF50),
                            trackColor = Color(0xFFE8F5E9),
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    }
                }
            }

            // Settings & Preferences
            ProfileSection("Settings & Preferences", Icons.Default.Settings) {
                SettingsItem("Language Preference", user?.languagePreference ?: "English", Icons.Default.Translate)
                SettingsItem("Notification Settings", "Enabled", Icons.Default.NotificationsActive)
                SettingsItem("Edit Profile", "Click icon in top bar", Icons.Default.Edit)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Text("LOGOUT ACCOUNT", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileEditFields(
    name: String, onNameChange: (String) -> Unit,
    role: String, onRoleChange: (String) -> Unit,
    empId: String, onEmpIdChange: (String) -> Unit,
    company: String, onCompanyChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        TextField(
            value = name, onValueChange = onNameChange,
            placeholder = { Text("Worker Name", color = Color.White.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = role, onValueChange = onRoleChange,
            placeholder = { Text("Job Role (Welder, etc.)", color = Color.White.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = empId, onValueChange = onEmpIdChange,
            placeholder = { Text("Employee ID", color = Color.White.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = company, onValueChange = onCompanyChange,
            placeholder = { Text("Company Name", color = Color.White.copy(alpha = 0.6f)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileSection(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = Color(0xFF37474F))
        }
        Spacer(modifier = Modifier.height(14.dp))
        content()
    }
}

@Composable
fun TrainingBadge(text: String, certified: Boolean) {
    Card(
        modifier = Modifier.padding(end = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (certified) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)),
        border = if (certified) BorderStroke(1.dp, Color(0xFFA5D6A7)) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (certified) Color(0xFF2E7D32) else Color.Gray
        )
    }
}

@Composable
fun RewardBadge(text: String, bgColor: Color) {
    Card(
        modifier = Modifier.padding(end = 10.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Composable
fun StatBox(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier.width(105.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
            Text(text = label, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun IncidentItemMock(text: String, tag: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F1F1))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if(tag == "Near Miss") Color(0xFFFF9800) else Color(0xFF2196F3)))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(text = tag, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun IncidentItem(incident: Incident) {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dateStr = sdf.format(Date(incident.date))
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = incident.incidentType, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = incident.description, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
            }
            Text(text = dateStr, fontSize = 11.sp, color = Color.LightGray)
        }
    }
}

@Composable
fun ContactItem(label: String, number: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), modifier = Modifier.size(44.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(10.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = number, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        IconButton(onClick = { /* Call */ }, modifier = Modifier.background(Color(0xFFE8F5E9), CircleShape)) {
            Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SettingsItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF607D8B), modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, modifier = Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
        if (value.isNotEmpty()) {
            Text(text = value, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun Badge(text: String, color: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
