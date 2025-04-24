package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authenticatorapp.presentation.model.AccountTab
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabLayout(
    selectedTab: AccountTab,
    onTabSelected: (AccountTab) -> Unit
) {
    val tabs = AccountTab.values

    PrimaryTabRow(
        selectedTabIndex = AccountTab.toPage(selectedTab),
        contentColor = MainBlue,
        containerColor = Color.Transparent,
        divider = {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
        },
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = stringResource(tab.titleRez),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = if (selectedTab == tab) MainBlue else Gray6
                    )
                }
            )
        }
    }
}
