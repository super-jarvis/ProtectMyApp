package io.jarvis.pma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.AppUtils.AppInfo
import com.blankj.utilcode.util.ImageUtils
import io.jarvis.pma.viewModel.AppListIntent
import io.jarvis.pma.viewModel.AppListIntent.LaunchApp
import io.jarvis.pma.viewModel.AppListViewModel
import io.jarvis.pma.viewModel.AppListViewState

@Composable
fun AppListScreen(viewModel: AppListViewModel) {
    val state by viewModel.state.collectAsState()


    when (val uiState = state) {
        is AppListViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AppListViewState.Success -> {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(uiState.apps) { appInfo ->
                    AppListItem(viewModel, appInfo) {
                        viewModel.onIntent(LaunchApp(appInfo.packageName))
                    }
                }
            }
        }

        is AppListViewState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Error: ${uiState.message}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.onIntent(AppListIntent.Refresh) }) {
                    Text(text = "重试")
                }
            }
        }
    }
}

@Composable
fun AppListItem(viewModel: AppListViewModel, appInfo: AppInfo, onClick: () -> Unit) {
    val protectPackage by viewModel.protectPackage.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = protectPackage == appInfo.packageName,
                onCheckedChange = {
                    viewModel.onIntent(AppListIntent.Protect(appInfo.packageName))
                })
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                bitmap = ImageUtils.drawable2Bitmap(appInfo.icon).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = appInfo.name, style = MaterialTheme.typography.titleMedium)
                Text(text = appInfo.versionName, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}