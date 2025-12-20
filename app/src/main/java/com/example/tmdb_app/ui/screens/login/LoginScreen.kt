package com.example.tmdb_app.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tmdb_app.R
import com.example.tmdb_app.ui.helper.PreferenceManager
import com.example.tmdb_app.ui.theme.Red20
import com.example.tmdb_app.ui.theme.RedCED


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPassword by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var saveAccout by remember { mutableStateOf(false) }

    val preferenceManager = remember { PreferenceManager(context) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    fun clickRemember() {
        // Xử lý lưu tài khoản
        if(saveAccout && email != "" && password != ""){
            // Lưu tài khoản
            preferenceManager.saveEmailPassword(email, password)
        }
        else{
            // Xóa tài khoản
            preferenceManager.clearData()
        }
    }

    fun clickLogin() {
        // Xử lý đăng nhập
        if (email.isEmpty() || password.isEmpty()) {
            isError = true
        } else {
            isError = false
            // Thực hiện đăng nhập)
            // Điều hướng đến màn hình chính hoặc trang khác
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true } // remove splash screen from back stack
            }
        }
    }
    Scaffold {
        paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
                ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.height(100.dp)
                )
                Spacer(modifier = Modifier.height(50.dp))

                OutlinedTextField(
                    value = email, 
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.email),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },

                    shape = RoundedCornerShape(16.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,

                        focusedContainerColor = RedCED,
                        focusedBorderColor = Red20,
                        focusedLeadingIconColor = Red20,

                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedBorderColor = Color.Transparent, 
                        unfocusedLeadingIconColor = Color.Black,

                        errorContainerColor = Color(0xFFFFF0F1),
                        errorBorderColor = Color.Red,
                        errorLeadingIconColor = Color.Red
                    ),
                    isError = isError
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = password, 
                    onValueChange = { password = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.password),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        // --- CHÈN IconButton Ở ĐÂY ---
                        IconButton(onClick = {
                            isPassword = !isPassword // Đảo ngược giá trị true/false
                        }) {
                            Icon(
                                imageVector = if (isPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPassword) "Ẩn/Hiện" else "Hiện/Ẩn"
                            )
                        }
                    },

                    shape = RoundedCornerShape(16.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,


                        focusedContainerColor = RedCED,
                        focusedBorderColor = Red20,
                        focusedLeadingIconColor = Red20,

                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedBorderColor = Color.Transparent, 
                        unfocusedLeadingIconColor = Color.Black,

                        errorContainerColor = Color(0xFFFFF0F1),
                        errorBorderColor = Color.Red,
                        errorLeadingIconColor = Color.Red
                    ),
                    isError = isError,
                    visualTransformation = if (isPassword) VisualTransformation.None else PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = saveAccout,
                        onCheckedChange = { saveAccout = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Red20,
                            uncheckedColor = Color.Black
                        )

                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.remember_me),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /* Xử lý login */ clickLogin() },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors( // Dùng ButtonDefaults thay vì Outlined
                        containerColor = Red20, // Nền đỏ
                        contentColor = Color.White // Chữ trắng
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_button),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
    }

}
