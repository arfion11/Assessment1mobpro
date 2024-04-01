package org.d3if0072.assessment1mobpro.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if0072.assessment1mobpro.R
import org.d3if0072.assessment1mobpro.ui.theme.Assessment1mobproTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) {padding ->
        ScreenContent(Modifier.padding(padding))

    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    val context = LocalContext.current

    var selectedGasType by remember { mutableStateOf(GasType.PERTALITE) }
    var payment by remember { mutableStateOf(0) }
    var result by remember { mutableStateOf(0) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(stringResource(id = R.string.select_gas_type))
        Spacer(modifier = Modifier.height(8.dp))
        RadioButtonGroup(
            options = GasType.values().toList(),
            selectedOption = selectedGasType,
            onOptionSelected = { gasType ->
                selectedGasType = gasType
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = payment.toString(),
            onValueChange = {payment = it.toIntOrNull() ?:0},
            label = { Text(stringResource(id = R.string.payment_label)) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { result = calculateGas (selectedGasType, payment)}
        ) {
            Text(stringResource(id = R.string.calculate_button))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text( "${stringResource(id = R.string.result_prefix)} $result ${stringResource(id = R.string.result_suffix)}")
    }
}

@Composable
fun RadioButtonGroup(
    options: List<GasType>,
    selectedOption: GasType,
    onOptionSelected: (GasType) -> Unit
) {
    Column {
        options.forEach { gasType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == gasType,
                    onClick = { onOptionSelected(gasType) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gasType.name)
            }
        }
    }
}

enum class GasType {
    PERTAMAX,
    PERTALITE
}

fun calculateGas(gasType: GasType, payment: Int): Int {
    return when (gasType) {
        GasType.PERTAMAX -> payment / 11000 // Harga per liter pertamax
        GasType.PERTALITE -> payment / 9500 // Harga per liter pertalite
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment1mobproTheme {
        MainScreen()
    }
}