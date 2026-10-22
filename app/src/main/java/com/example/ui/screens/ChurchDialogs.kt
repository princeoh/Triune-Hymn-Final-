package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.OffWhiteBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open browser: $url", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurchInfoBottomSheet(
    type: String, // "website", "blog", "donate", "about"
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OffWhiteBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = RoyalBlueSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            com.example.ui.components.ChurchLogo(
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = when (type) {
                            "website" -> "Church Website"
                            "blog" -> "Church Blog & Articles"
                            "donate" -> "Giving & Support"
                            else -> "Triune Embassy Global Church"
                        },
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (type) {
                "website" -> {
                    Text(
                        text = "Welcome to the official online sanctuary of Triune Embassy. Join live services, listen to sermons, and stay connected with the global body of Christ.",
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoyalBlueSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Official Web Portal", fontWeight = FontWeight.Bold, color = RoyalBlue)
                            Text(text = "https://triuneembassy.org", fontSize = 14.sp, color = TextMuted)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { openUrl(context, "https://triuneembassy.org") },
                        modifier = Modifier.fillMaxWidth().testTag("visit_website_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Visit Website", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                "blog" -> {
                    Text(
                        text = "Explore inspired devotionals, theology articles, apostolic teachings, and worship resources published by Triune Embassy ministers.",
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GoldContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Weekly Devotionals & Articles", fontWeight = FontWeight.Bold, color = GoldDark)
                            Text(text = "Deepening your faith and understanding of the Triune God.", fontSize = 13.sp, color = TextDark)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { openUrl(context, "https://triuneembassy.org/blog") },
                        modifier = Modifier.fillMaxWidth().testTag("read_blog_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Read Blog Online", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                "donate" -> {
                    Text(
                        text = "“Give, and it shall be given unto you; good measure, pressed down, and shaken together, and running over.” — Luke 6:38",
                        fontSize = 14.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = GoldDark,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Partner with Triune Embassy in advancing the Gospel of Jesus Christ, church planting, community outreach, and global hymn ministry.",
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoyalBlueSurface),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Bank Giving Details", fontWeight = FontWeight.Bold, color = RoyalBlue)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Account Name: Triune Embassy Global", fontSize = 14.sp, color = TextDark)
                            Text(text = "Bank: Zenith Bank / Global Partner", fontSize = 14.sp, color = TextDark)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Account: 1019283746",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                                OutlinedButton(
                                    onClick = {
                                        clipboard.setText(AnnotatedString("1019283746"))
                                        Toast.makeText(context, "Account number copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { openUrl(context, "https://triuneembassy.org/give") },
                        modifier = Modifier.fillMaxWidth().testTag("online_giving_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = RoyalBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Online Giving Portal", color = RoyalBlue, fontWeight = FontWeight.Bold)
                    }
                }

                else -> { // About
                    Text(
                        text = "Triune Embassy is an apostolic, Christ-centered ministry devoted to revealing the fullness of the Father, Son, and Holy Spirit. We believe in vibrant worship, sound biblical doctrine, and spreading spiritual revival through sacred hymns and fellowship.",
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoyalBlueSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Our Hymnal Mission", fontWeight = FontWeight.Bold, color = RoyalBlue)
                            Text(
                                text = "Preserving the sacred rich hymnody of the church, equipping worshippers globally with 600 timeless hymns.",
                                fontSize = 13.sp,
                                color = TextDark,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Weekly Service Times:",
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        fontSize = 15.sp
                    )
                    Text(text = "• Sunday Celebration Service: 9:00 AM & 11:30 AM", fontSize = 14.sp, color = TextDark)
                    Text(text = "• Midweek Word & Prayer: Wednesday 6:30 PM", fontSize = 14.sp, color = TextDark)
                    Text(text = "• Night of Hymns & Glory: Last Friday of Month", fontSize = 14.sp, color = TextDark)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
