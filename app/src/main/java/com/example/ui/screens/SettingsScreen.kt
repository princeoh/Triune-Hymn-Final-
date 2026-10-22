package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChurchLogo
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.OffWhiteBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun SettingsScreen(
    fontSize: Float,
    onIncreaseFontSize: () -> Unit,
    onDecreaseFontSize: () -> Unit,
    onResetFontSize: () -> Unit,
    onSyncHymns: () -> Unit,
    isLoadingHymns: Boolean,
    onSeed400: () -> Unit,
    isUploading400: Boolean,
    uploadStatusMessage: String?,
    totalHymns: Int,
    onChurchAction: (String) -> Unit // "website", "blog", "donate", "about", "contact"
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
            .testTag("settings_screen")
    ) {
        // Settings Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = RoyalBlue,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "SETTINGS & HYMNAL SPEC",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Reading Preferences, 1,000 Hymns & Ministry Information",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reading Font Size Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "HYMN TEXT SIZE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalBlue,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reading Size: ${fontSize.toInt()} sp",
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 15.sp
                        )

                        Row {
                            IconButton(onClick = onDecreaseFontSize) {
                                Icon(
                                    imageVector = Icons.Default.ZoomOut,
                                    contentDescription = "Decrease",
                                    tint = RoyalBlue
                                )
                            }
                            IconButton(onClick = onIncreaseFontSize) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = "Increase",
                                    tint = RoyalBlue
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = OffWhiteBackground,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "“Holy, holy, holy! Lord God Almighty!\nEarly in the morning our song shall rise to Thee”",
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily.Serif,
                            color = TextDark,
                            lineHeight = (fontSize * 1.4f).sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onResetFontSize,
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reset Default (18 sp)", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cloud Database Sync & 400 Hymns Upload Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "FIRESTORE SYNC & 1,000 HYMNS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalBlue,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Project: triune-hymn--global",
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$totalHymns Hymns in Library (1–1000)",
                                color = GoldDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = onSyncHymns,
                            enabled = !isLoadingHymns && !isUploading400,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (isLoadingHymns) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = GoldAccent)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isLoadingHymns) "Syncing..." else "Fetch", fontSize = 13.sp)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = OffWhiteBackground)

                    Text(
                        text = "Seed 400 Additional Hymns to Firestore:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )
                    Text(
                        text = "Adds 601–750 Christmas, 751–850 Easter, and 851–1000 Others to collection 'hymns' while keeping existing 600 safe.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    Button(
                        onClick = onSeed400,
                        enabled = !isUploading400 && !isLoadingHymns,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isUploading400) {
                            CircularProgressIndicator(color = RoyalBlue, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUploading400) "Uploading to Firestore..." else "Add 400 Hymns to Firestore",
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    uploadStatusMessage?.let { msg ->
                        Text(
                            text = msg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (msg.contains("error", ignoreCase = true)) Color.Red else GoldDark,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Ministry & Church Pages
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "CHURCH PAGES & RESOURCES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalBlue,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.VolunteerActivism,
                        title = "Donate Page",
                        subtitle = "Tithes, Offerings & Accounts",
                        onClick = { onChurchAction("donate") }
                    )
                    Divider(color = OffWhiteBackground, thickness = 1.dp)
                    SettingsRow(
                        icon = Icons.Default.ContactPhone,
                        title = "Contact Page",
                        subtitle = "Address, Lines & Fellowship",
                        onClick = { onChurchAction("contact") }
                    )
                    Divider(color = OffWhiteBackground, thickness = 1.dp)
                    SettingsRow(
                        icon = Icons.Default.Info,
                        title = "About Page",
                        subtitle = "Vision, Mission & Apostolic Mandate",
                        onClick = { onChurchAction("about") }
                    )
                    Divider(color = OffWhiteBackground, thickness = 1.dp)
                    SettingsRow(
                        icon = Icons.Default.Language,
                        title = "Church Website",
                        subtitle = "triuneembassy.org",
                        onClick = { onChurchAction("website") }
                    )
                    Divider(color = OffWhiteBackground, thickness = 1.dp)
                    SettingsRow(
                        icon = Icons.Default.Description,
                        title = "Church Blog",
                        subtitle = "Devotionals & Teachings",
                        onClick = { onChurchAction("blog") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Church Logo & Scripture footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(62.dp),
                shape = CircleShape,
                color = RoyalBlueSurface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    ChurchLogo(modifier = Modifier.size(52.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "TRIUNE EMBASSY GLOBAL CHURCH",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GoldDark,
                letterSpacing = 1.sp
            )

            Text(
                text = "“O sing unto the Lord a new song: sing unto the Lord, all the earth.” — Psalm 96:1",
                fontSize = 12.sp,
                color = GoldDark,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "1,000 Hymns Edition • Global Apostolic Fellowship",
                fontSize = 11.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(8.dp),
            color = RoyalBlueSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = RoyalBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Text(text = subtitle, fontSize = 12.sp, color = TextMuted)
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextMuted
        )
    }
}
