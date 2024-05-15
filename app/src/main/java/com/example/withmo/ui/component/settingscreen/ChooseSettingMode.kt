package com.example.withmo.ui.component.settingscreen

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.domain.model.SettingMode
import com.example.withmo.ui.component.settingscreen.home.FileAccessCheckDialog
import com.example.withmo.ui.theme.Typography
import com.example.withmo.until.getModelFile

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ChooseSettingMode(
    changeSettingMode: (SettingMode) -> Unit,
    showFileAccessCheckDialog: Boolean,
    setShowFileAccessCheckDialog: (Boolean) -> Unit,
    setModelFileList: (MutableList<ModelFile>) -> Unit
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (Environment.isExternalStorageManager()) {
                    setModelFileList(getModelFile(context))
                }
            }
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = {
                changeSettingMode(SettingMode.HOME)
            },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(0)
        ) {
            Text(
                text = "ホーム",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        TextButton(
            onClick = {
                if (Environment.isExternalStorageManager()) {
                    changeSettingMode(SettingMode.MODEL)
                    setModelFileList(getModelFile(context))
                } else {
                    setShowFileAccessCheckDialog(true)
                }
            },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(0)
        ) {
            Text(
                text = "モデル",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

    if (showFileAccessCheckDialog) {
        FileAccessCheckDialog(
            onDismissRequest = {
                setShowFileAccessCheckDialog(false)
            },
            onConfirm = {
                setShowFileAccessCheckDialog(false)
                launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
            },
            onDismiss = {
                setShowFileAccessCheckDialog(false)
            }
        )
    }
}