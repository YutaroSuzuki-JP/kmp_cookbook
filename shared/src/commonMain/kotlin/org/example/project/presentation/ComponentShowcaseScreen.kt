package org.example.project.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import org.example.project.theme.BrandTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentShowcaseScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E1E2C),
            Color(0xFF14141E)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .safeContentPadding()
    ) {
        // App Header
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
                    tint = Color(0xFFAB47BC)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Modern Components",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = Color(0xFFAB47BC)
                )
                Text(
                    text = "Glassmorphism UI, Sheets, and Text Fields",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.LightGray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section 1: Glassmorphism Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Simulated Glassmorphism Card",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "半透明のグラデーション、白い極薄の枠線、および背後にある光源（ぼかし付き円）を重ねることで、磨りガラス（グラスモルフィズム）風の質感をシミュレートしています。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Glass stack demonstration
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF14141E))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Blurred Glowing Orbs in Background
                        Box(
                            modifier = Modifier
                                .offset(x = (-40).dp, y = (-20).dp)
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFAB47BC).copy(alpha = 0.6f))
                                .blur(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .offset(x = 50.dp, y = 30.dp)
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF26A69A).copy(alpha = 0.5f))
                                .blur(25.dp)
                        )

                        // Translucent Glass Card Overlaid (using reusable GlassCard component)
                        GlassCard(
                            modifier = Modifier.fillMaxSize(0.85f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "GLASSMORPHIC CARD",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Translucent Overlay UI",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Bottom Sheet Drawer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Modal Bottom Sheet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Material 3標準のModalBottomSheetを使って、下部から展開するアニメーションドロワーを配置します。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { showBottomSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB47BC)),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ボトムシートを開く", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Section 3: Custom Text Fields with Count Meter
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Interactive Input & Character Meter",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "テキスト入力フィールドの文字数に応じて、カウント表示のカラーが動的にアニメーション変化します。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    var textInput by remember { mutableStateOf("") }
                    val maxCharLimit = 20

                    // Dynamic text counter color animation
                    val isLimitReached = textInput.length >= maxCharLimit
                    val isWarningState = textInput.length >= (maxCharLimit - 5)
                    val counterColor by animateColorAsState(
                        targetValue = when {
                            isLimitReached -> Color(0xFFEF5350) // Red
                            isWarningState -> Color(0xFFFFB74D) // Amber
                            else -> Color(0xFF26A69A) // Teal
                        }
                    )

                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {
                            if (it.length <= maxCharLimit) {
                                textInput = it
                            }
                        },
                        placeholder = { Text("文字を入力してください...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAB47BC),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF14141E),
                            unfocusedContainerColor = Color(0xFF14141E)
                        ),
                        singleLine = true,
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isLimitReached) "上限文字数に達しました" else "",
                                    color = Color(0xFFEF5350)
                                )
                                Text(
                                    text = "${textInput.length} / $maxCharLimit",
                                    color = counterColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                }
            }

            // Section 4: Login Form Validation & Keyboard Flow
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Reactive Input Form & Focus Flow",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "一方向データフロー(UDF)によるリアクティブ検証と、Next/Doneによるキーボードフォーカス制御の例です。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    val formState = remember { LoginFormState() }
                    val focusManager = LocalFocusManager.current
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val passwordFocusRequester = remember { FocusRequester() }
                    var passwordVisible by remember { mutableStateOf(false) }

                    // Email Input
                    var emailTouched by remember { mutableStateOf(false) }
                    val isEmailError = emailTouched && formState.email.isNotEmpty() && !formState.isEmailValid
                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = {
                            formState.email = it
                            emailTouched = true
                        },
                        label = { Text("メールアドレス") },
                        isError = isEmailError,
                        supportingText = {
                            if (isEmailError) {
                                Text("有効なメールアドレスを入力してください", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAB47BC),
                            unfocusedBorderColor = Color.DarkGray,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() }
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Input
                    var passwordTouched by remember { mutableStateOf(false) }
                    val isPasswordError = passwordTouched && formState.password.isNotEmpty() && !formState.isPasswordValid
                    OutlinedTextField(
                        value = formState.password,
                        onValueChange = {
                            formState.password = it
                            passwordTouched = true
                        },
                        label = { Text("パスワード") },
                        visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                            }
                        },
                        isError = isPasswordError,
                        supportingText = {
                            if (isPasswordError) {
                                Text("パスワードは8文字以上必要です", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAB47BC),
                            unfocusedBorderColor = Color.DarkGray,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var submittedMessage by remember { mutableStateOf("") }
                    if (submittedMessage.isNotEmpty()) {
                        Text(
                            text = submittedMessage,
                            color = Color(0xFF26A69A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Submit button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            submittedMessage = "送信完了: ${formState.email}"
                        },
                        enabled = formState.isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandTheme.colorScheme.goldAccent,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ログイン", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet Dialog
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E2C),
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeContentPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "設定・情報",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))

                // Sheet content details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("ダークテーマ", color = Color.LightGray)
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFAB47BC))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                var sliderVal by remember { mutableStateOf(0.5f) }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("表示倍率: ${(sliderVal * 100).toInt()}%", color = Color.LightGray)
                    Slider(
                        value = sliderVal,
                        onValueChange = { sliderVal = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFAB47BC),
                            activeTrackColor = Color(0xFFAB47BC)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showBottomSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB47BC)),
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("閉じる", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

class LoginFormState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    val isEmailValid: Boolean by derivedStateOf {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        email.matches(emailRegex)
    }
    val isPasswordValid: Boolean by derivedStateOf {
        password.length >= 8
    }
    val isFormValid: Boolean by derivedStateOf {
        isEmailValid && isPasswordValid
    }
}
