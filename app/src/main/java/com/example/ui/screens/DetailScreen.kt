package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.model.Hymn
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.OffWhiteBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

fun shareHymn(context: Context, hymn: Hymn) {
    val shareBody = """
TRIUNE HYMN — Hymn ${hymn.number}
${hymn.title}

${if (hymn.chorus.isNotBlank()) "CHORUS:\n" + hymn.chorus + "\n\n" else ""}
${hymn.lyrics}

Category: ${hymn.category}
Shared from Triune Embassy Hymnal App
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Hymn ${hymn.number}: ${hymn.title}")
        putExtra(Intent.EXTRA_TEXT, shareBody)
    }
    context.startActivity(Intent.createChooser(intent, "Share Hymn via"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    hymn: Hymn,
    isFavorite: Boolean,
    fontSizeSp: Float,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onNextHymn: () -> Unit,
    onPrevHymn: () -> Unit,
    onIncreaseFontSize: () -> Unit,
    onDecreaseFontSize: () -> Unit,
    onResetFontSize: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hymn ${hymn.number}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("detail_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Toggle Favorite",
                            tint = if (isFavorite) GoldAccent else Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            clipboard.setText(AnnotatedString("Hymn ${hymn.number}: ${hymn.title}\n\n${hymn.lyrics}"))
                            Toast.makeText(context, "Hymn lyrics copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("detail_copy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { shareHymn(context, hymn) },
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalBlue
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalIconButton(
                        onClick = onPrevHymn,
                        modifier = Modifier.testTag("prev_hymn_button"),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = RoyalBlueSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Hymn",
                            tint = RoyalBlue
                        )
                    }

                    // Font Size quick controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = onDecreaseFontSize,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "Decrease Font Size",
                                tint = TextDark
                            )
                        }

                        Text(
                            text = "${fontSizeSp.toInt()} sp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )

                        IconButton(
                            onClick = onIncreaseFontSize,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Increase Font Size",
                                tint = TextDark
                            )
                        }
                    }

                    FilledTonalIconButton(
                        onClick = onNextHymn,
                        modifier = Modifier.testTag("next_hymn_button"),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = RoyalBlueSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Hymn",
                            tint = RoyalBlue
                        )
                    }
                }
            }
        },
        containerColor = OffWhiteBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Card with Title and Category
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = RoyalBlueSurface,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = hymn.category.uppercase(),
                            color = RoyalBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    Text(
                        text = hymn.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )

                    if (hymn.author.isNotBlank() || hymn.keySignature.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (hymn.author.isNotBlank()) {
                                Text(
                                    text = hymn.author,
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                            if (hymn.author.isNotBlank() && hymn.keySignature.isNotBlank()) {
                                Text(text = " • ", fontSize = 12.sp, color = TextMuted)
                            }
                            if (hymn.keySignature.isNotBlank()) {
                                Text(
                                    text = "Key: ${hymn.keySignature}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GoldDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Chorus card if separate
            if (hymn.chorus.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "CHORUS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GoldDark,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hymn.chorus,
                            fontSize = fontSizeSp.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            fontFamily = FontFamily.Serif,
                            lineHeight = (fontSizeSp * 1.45f).sp
                        )
                    }
                }
            }

            // Hymn Lyrics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = hymn.formattedLyrics(),
                        fontSize = fontSizeSp.sp,
                        color = TextDark,
                        lineHeight = (fontSizeSp * 1.55f).sp,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.testTag("hymn_lyrics_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ending Amen
            Text(
                text = "— AMEN —",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
