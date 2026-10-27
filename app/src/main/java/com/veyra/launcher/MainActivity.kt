package com.veyra.launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class Page { HOME, APPS, TOOLS }
private data class LaunchableApp(val packageName: String, val label: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent { VeyraApp() }
    }
}

@Composable
private fun VeyraApp() {
    val context = LocalContext.current
    var page by remember { mutableStateOf(Page.HOME) }
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    var apps by remember { mutableStateOf(emptyList<LaunchableApp>()) }

    LaunchedEffect(Unit) {
        apps = context.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.MATCH_ALL
        ).mapNotNull {
            val pkg = it.activityInfo?.packageName ?: return@mapNotNull null
            if (pkg == context.packageName) null
            else LaunchableApp(pkg, it.loadLabel(context.packageManager).toString())
        }.distinctBy { it.packageName }.sortedBy { it.label.lowercase(Locale.getDefault()) }
    }
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(15_000)
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFFC5D0FF),
        background = Color(0xFF080D18),
        surface = Color(0xFF172239),
        onSurface = Color(0xFFF3F5FF)
    )) {
        Box(Modifier.fillMaxSize().background(
            Brush.linearGradient(listOf(Color(0xFF17223D), Color(0xFF080D18), Color(0xFF131629)))
        )) {
            Box(Modifier.offset(150.dp, 90.dp).size(280.dp).background(
                Brush.radialGradient(listOf(Color(0x443F6FE5), Color.Transparent)),
                RoundedCornerShape(200.dp)
            ))
            Column(Modifier.fillMaxSize().safeDrawingPadding().padding(20.dp)) {
                AnimatedContent(
                    targetState = page,
                    transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(140)) },
                    label = "page"
                ) { selected ->
                    when (selected) {
                        Page.HOME -> Home(
                            now = now,
                            apps = apps,
                            onApps = { page = Page.APPS },
                            onTools = { page = Page.TOOLS },
                            onLaunch = { app ->
                                context.packageManager.getLaunchIntentForPackage(app.packageName)?.let {
                                    runCatching { context.startActivity(it) }
                                }
                            }
                        )
                        Page.APPS -> AppLibrary(apps, { page = Page.HOME }) { app ->
                            context.packageManager.getLaunchIntentForPackage(app.packageName)?.let {
                                runCatching { context.startActivity(it) }
                            }
                        }
                        Page.TOOLS -> Tools({ page = Page.HOME }) {
                            runCatching { context.startActivity(Intent(SettingsAction).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Home(
    now: LocalDateTime,
    apps: List<LaunchableApp>,
    onApps: () -> Unit,
    onTools: () -> Unit,
    onLaunch: (LaunchableApp) -> Unit
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).clip(RoundedCornerShape(15.dp))
                    .background(Color.White.copy(.12f))
                    .border(1.dp, Color.White.copy(.19f), RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center) {
                    Text("V", fontSize = 23.sp, color = Color(0xFFE3E9FF), fontWeight = FontWeight.Light)
                }
                Column(Modifier.padding(start = 11.dp)) {
                    Text("VEYRA", fontSize = 15.sp, letterSpacing = 3.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("PERSONAL SPACE", fontSize = 9.sp, letterSpacing = 1.5.sp, color = Color(0xFFADB9D6))
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onTools) {
                    Icon(Icons.Filled.Tune, contentDescription = "Quick panel", tint = Color(0xFFE3E9FF))
                }
            }
            Spacer(Modifier.height(36.dp))
            Text("A LITTLE MORE ROOM", fontSize = 11.sp, letterSpacing = 2.sp, color = Color(0xFF9DAAC9))
            Text(now.format(DateTimeFormatter.ofPattern("HH:mm")), fontSize = 82.sp, letterSpacing = (-4).sp, color = Color.White, fontWeight = FontWeight.Light)
            Text(now.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())).uppercase(Locale.getDefault()),
                fontSize = 11.sp, letterSpacing = 1.4.sp, color = Color(0xFFB9C4DE))
            Spacer(Modifier.height(25.dp))
            GlassCard(Modifier.fillMaxWidth().clickable(onClick = onApps)) {
                Column(Modifier.padding(20.dp)) {
                    Text("YOUR SPACE", fontSize = 10.sp, letterSpacing = 2.sp, color = Color(0xFFBECBF0))
                    Spacer(Modifier.height(13.dp))
                    Text("Everything, in its place.", fontSize = 19.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(5.dp))
                    Text("Tap to open your app library", fontSize = 12.sp, color = Color(0xFFABB8D5))
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallCard("APP LIBRARY", "Find anything", Modifier.weight(1f), onApps)
                SmallCard("QUICK PANEL", "Your shortcuts", Modifier.weight(1f), onTools)
            }
        }
        GlassCard(
            Modifier.fillMaxWidth().pointerInput(Unit) {
                var distance = 0f
                detectVerticalDragGestures(
                    onVerticalDrag = { _, amount -> distance += amount },
                    onDragEnd = { if (distance < -50f) onApps(); distance = 0f },
                    onDragCancel = { distance = 0f }
                )
            }
        ) {
            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                apps.take(3).forEach { app ->
                    Text(app.label, modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                        .clickable { onLaunch(app) }.padding(8.dp), fontSize = 11.sp,
                        color = Color(0xFFE3E9FF), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text("ALL APPS", modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = onApps).padding(8.dp), fontSize = 11.sp, color = Color(0xFFC5D0FF))
            }
        }
    }
}

@Composable
private fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier.clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(Color.White.copy(.105f), Color.White.copy(.035f), Color(0x229DB9FF))))
            .border(1.dp, Brush.linearGradient(listOf(Color.White.copy(.2f), Color.White.copy(.05f))), RoundedCornerShape(26.dp)),
        content = content
    )
}

@Composable
private fun SmallCard(title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    GlassCard(modifier.clickable(onClick = onClick)) {
        Column(Modifier.padding(15.dp)) {
            Icon(Icons.Filled.Apps, contentDescription = null, tint = Color(0xFFC5D0FF), modifier = Modifier.size(18.dp))
            Spacer(Modifier.height(12.dp))
            Text(title, fontSize = 10.sp, letterSpacing = 1.sp, color = Color.White)
            Text(subtitle, fontSize = 10.sp, color = Color(0xFFABB8D5))
        }
    }
}

@Composable
private fun AppLibrary(apps: List<LaunchableApp>, onBack: () -> Unit, onLaunch: (LaunchableApp) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(apps, query) { apps.filter { it.label.contains(query.trim(), true) } }
    Column(Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("APP LIBRARY", fontSize = 20.sp, letterSpacing = 2.sp, color = Color.White)
        }
        androidx.compose.material3.OutlinedTextField(
            value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Search apps") }, singleLine = true
        )
        Spacer(Modifier.height(14.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.packageName }) { app ->
                GlassCard(Modifier.fillMaxWidth().clickable { onLaunch(app) }) {
                    Text(app.label, Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
                        color = Color.White, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun Tools(onBack: () -> Unit, onSystemSettings: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("QUICK PANEL", fontSize = 20.sp, letterSpacing = 2.sp, color = Color.White)
        }
        GlassCard(Modifier.fillMaxWidth().clickable(onClick = onSystemSettings)) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFFC5D0FF))
                Column(Modifier.padding(start = 14.dp)) {
                    Text("System settings", color = Color.White, fontSize = 15.sp)
                    Text("Open Android settings", color = Color(0xFFABB8D5), fontSize = 12.sp)
                }
            }
        }
        Text("Veyra shortcuts stay separate from protected Android controls.", color = Color(0xFFABB8D5), fontSize = 12.sp)
    }
}

private const val SettingsAction = "android.settings.SETTINGS"
