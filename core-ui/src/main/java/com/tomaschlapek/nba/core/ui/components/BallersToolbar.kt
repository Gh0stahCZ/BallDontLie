package com.tomaschlapek.nba.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tomaschlapek.nba.core.ui.BallersTheme

/**
 * Ballers Toolbar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BallersToolbar(
    @StringRes titleRes: Int?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    canPop: Boolean = false,
    onNavigationClick: () -> Unit = { /* Empty */ },
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = colors,
        title = {
            Text(text = titleRes?.let { stringResource(id = it) } ?: "", maxLines = 1)
        },
        navigationIcon = {
            if (canPop) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun BallersToolbarPreview() {
    BallersTheme {
        BallersToolbar(
            titleRes = android.R.string.untitled,
            canPop = false,
            onNavigationClick = { /* Empty */ },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar Back")
@Composable
private fun BallersToolbarBackPreview() {
    BallersTheme {
        BallersToolbar(
            titleRes = android.R.string.untitled,
            canPop = true,
            onNavigationClick = { /* Empty */ },
        )
    }
}

