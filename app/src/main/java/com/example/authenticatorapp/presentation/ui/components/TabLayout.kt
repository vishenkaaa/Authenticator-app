package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

@Composable
fun TabLayout(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
){
    val tabs = listOf(stringResource(R.string.time_based), stringResource(R.string.counter_based))
    val textWidths = remember { mutableStateListOf<Int>().apply { repeat(tabs.size) { add(0) } } }

    Column(
        Modifier.padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ){
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = if (isSelected) MainBlue else Gray6,
                        modifier = Modifier.padding(top = 10.dp, bottom = 7.dp)
                            .onGloballyPositioned { coordinates ->
                                textWidths[index] = coordinates.size.width
                            }
                    )

                    Box(
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textWidths[index].toDp() })
                            .height(3.dp)
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(if (isSelected) MainBlue else Color.Transparent)
                    )
                }
            }
        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
    }
}