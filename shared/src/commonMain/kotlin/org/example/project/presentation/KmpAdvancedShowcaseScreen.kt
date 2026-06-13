package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.ads.AdBanner
import org.example.project.ads.AdManager
import org.example.project.ads.MockAdManager
import org.example.project.billing.BillingManager
import org.example.project.billing.ProductInfo
import org.example.project.billing.PurchaseResult
import org.example.project.storage.AppSettings
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KmpAdvancedShowcaseScreen(
    onBack: () -> Unit
) {
    // Inject managers via Koin
    val appSettings: AppSettings = koinInject()
    val adManager: AdManager = koinInject()
    val billingManager: BillingManager = koinInject()

    val scope = rememberCoroutineScope()

    // Storage state
    var userNameInput by remember { mutableStateOf("") }
    var premiumStatusToggle by remember { mutableStateOf(false) }
    var savedUserName by remember { mutableStateOf("") }
    var savedPremiumStatus by remember { mutableStateOf(false) }

    // Load saved settings on startup
    LaunchedEffect(Unit) {
        savedUserName = appSettings.getString("user_name", "None")
        savedPremiumStatus = appSettings.getBoolean("is_premium", false)
        userNameInput = savedUserName
        premiumStatusToggle = savedPremiumStatus
    }

    // WebView state
    var webUrlInput by remember { mutableStateOf("https://zenn.dev") }
    var webUrlToLoad by remember { mutableStateOf("https://zenn.dev") }
    var receivedWebMessages by remember { mutableStateOf("No messages received yet.") }

    // Ads state (cast to MockAdManager for simulation UI)
    val mockAdManager = adManager as? MockAdManager
    val showAdRequestState = mockAdManager?.showAdRequest?.collectAsState(initial = null)
    val showAdRequest = showAdRequestState?.value
    val isAdLoadedState = mockAdManager?.isAdLoaded?.collectAsState(initial = false)
    val isAdLoaded = isAdLoadedState?.value ?: false

    // Billing state
    var productList by remember { mutableStateOf<List<ProductInfo>>(emptyList()) }
    var isBillingInitializing by remember { mutableStateOf(true) }
    var isPurchasing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        billingManager.initialize()
        isBillingInitializing = false
        productList = billingManager.getProducts(setOf("premium_upgrade", "support_developer"))
    }

    // Collect IAP events
    LaunchedEffect(Unit) {
        billingManager.purchaseEvents.collect { result ->
            isPurchasing = false
            when (result) {
                is PurchaseResult.Success -> {
                    if (result.productId == "premium_upgrade") {
                        appSettings.putBoolean("is_premium", true)
                        savedPremiumStatus = true
                        premiumStatusToggle = true
                    }
                }
                is PurchaseResult.Failure -> {
                    // Handle failure if needed
                }
                PurchaseResult.UserCancelled -> {
                    // Handle cancellation
                }
            }
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E1E2C),
            Color(0xFF14141E)
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .safeContentPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C3C))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "カタログに戻る",
                        tint = Color(0xFFFF9800)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Advanced KMP",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = Color(0xFFFF9800)
                    )
                    Text(
                        text = "Platform Storage, WebViews, AdMob, & In-App Purchase",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // SECTION 1: Local Storage (AppSettings)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFFFF9800))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "1. Local Storage (AppSettings)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            TextField(
                                value = userNameInput,
                                onValueChange = { userNameInput = it },
                                label = { Text("Username") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF14141E),
                                    unfocusedContainerColor = Color(0xFF14141E),
                                    focusedIndicatorColor = Color(0xFFFF9800),
                                    unfocusedLabelColor = Color.Gray,
                                    focusedLabelColor = Color(0xFFFF9800),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Premium Member Status", color = Color.White, fontSize = 13.sp)
                                Switch(
                                    checked = premiumStatusToggle,
                                    onCheckedChange = { premiumStatusToggle = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFFFF9800),
                                        checkedTrackColor = Color(0xFFFF9800).copy(alpha = 0.5f)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        appSettings.putString("user_name", userNameInput)
                                        appSettings.putBoolean("is_premium", premiumStatusToggle)
                                        savedUserName = userNameInput
                                        savedPremiumStatus = premiumStatusToggle
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Save Settings", color = Color.Black)
                                }
                                Button(
                                    onClick = {
                                        appSettings.clear()
                                        savedUserName = appSettings.getString("user_name", "None")
                                        savedPremiumStatus = appSettings.getBoolean("is_premium", false)
                                        userNameInput = ""
                                        premiumStatusToggle = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C3C)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Clear All", color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = Color.White.copy(alpha = 0.05f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Stored Username: $savedUserName", color = Color.LightGray, fontSize = 12.sp)
                            Text("Stored Premium Status: ${if (savedPremiumStatus) "Active" else "Inactive"}", color = Color.LightGray, fontSize = 12.sp)
                        }
                    }
                }

                // SECTION 2: WebView (KmpWebView)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Web, contentDescription = null, tint = Color(0xFFFF9800))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "2. WebView & JS Bridge (Native)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = webUrlInput,
                                    onValueChange = { webUrlInput = it },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFF14141E),
                                        unfocusedContainerColor = Color(0xFF14141E),
                                        focusedIndicatorColor = Color(0xFFFF9800),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = { webUrlToLoad = webUrlInput },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                                ) {
                                    Text("Load", color = Color.Black)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            ) {
                                KmpWebView(
                                    url = webUrlToLoad,
                                    onMessageReceived = { msg ->
                                        receivedWebMessages = msg
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Bridge Received Message: $receivedWebMessages",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // SECTION 3: Ads (AdMob Simulator)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFFFF9800))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "3. Ads (Banner & Interstitial Mock)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Inline AdBanner Composable:", color = Color.LightGray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))

                            AdBanner(
                                adUnitId = "ca-app-pub-3940256099942544/2934735716",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { adManager.loadInterstitial("ca-app-pub-3940256099942544/1033173712") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C3C))
                                ) {
                                    Text("Load Fullscreen Ad", color = Color.White)
                                }
                                Button(
                                    onClick = {
                                        adManager.showInterstitial {
                                            // Handle ad dismissed callback
                                        }
                                    },
                                    enabled = isAdLoaded,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF9800),
                                        disabledContainerColor = Color(0xFF2C2C3C).copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text(
                                        text = if (isAdLoaded) "Show Fullscreen Ad" else "Ad Not Loaded",
                                        color = if (isAdLoaded) Color.Black else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                // SECTION 4: In-App Purchases (IAP Simulator)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFFF9800))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "4. In-App Purchases (IAP Billing Mock)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            if (isBillingInitializing) {
                                CircularProgressIndicator(color = Color(0xFFFF9800))
                            } else {
                                productList.forEach { product ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(product.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                            Text(product.priceText, color = Color.LightGray, fontSize = 11.sp)
                                        }
                                        Button(
                                            onClick = {
                                                isPurchasing = true
                                                scope.launch {
                                                    billingManager.launchBillingFlow(product)
                                                }
                                            },
                                            enabled = !isPurchasing,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                                        ) {
                                            Text("Buy", color = Color.Black, fontSize = 12.sp)
                                        }
                                    }
                                    Divider(color = Color.White.copy(alpha = 0.03f))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Current Status: ${if (savedPremiumStatus) "Premium Active" else "Free Account"}",
                                color = if (savedPremiumStatus) Color(0xFFFF9800) else Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Simulating full-screen overlay for Interstitial Ad
        if (showAdRequest != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .safeContentPadding(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "SPONSOR ADVERTISEMENT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFF9800), Color(0xFFAB47BC))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Beautiful KMP App\nShowcase Ad",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = { mockAdManager?.dismissAd() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Close Advertisement", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
