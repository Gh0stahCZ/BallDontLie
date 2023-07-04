package com.tomaschlapek.nba.core.ui.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tomaschlapek.nba.core.ui.BallersTheme
import com.tomaschlapek.nba.core.ui.R

/**
 * Error screen composable
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.error_title),
    description: String = stringResource(id = R.string.error_desc),
    iconDrawableRes: Int = R.drawable.warning,
    actionButtonText: String = stringResource(id = R.string.error_action),
    onActionButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(id = iconDrawableRes),
            contentDescription = "Error image",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Visible
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Visible
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(modifier = Modifier.fillMaxWidth(), onClick = { onActionButtonClick() }) {
            Text(actionButtonText)
        }
    }
}

@Preview(showBackground = true, widthDp = 480, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ErrorScreenPreview() {
    BallersTheme {
        ErrorScreen(
            modifier = Modifier,
            title = "Something went wrong",
            description = "Please try again later"
        )
    }
}

