    package org.d3if0072.assessment1mobpro.screen

    import android.content.Context
    import android.content.Intent
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
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.outlined.Info
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.RadioButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.rememberNavController
    import org.d3if0072.assessment1mobpro.R
    import org.d3if0072.assessment1mobpro.navigation.Screen
    import org.d3if0072.assessment1mobpro.ui.theme.Assessment1mobproTheme


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(navController: NavHostController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name))
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.About.route)}) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = stringResource(id = R.string.tentang_aplikasi),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        ) {padding ->
            ScreenContent(Modifier.padding(padding))
        }
    }

    @Composable
    fun ScreenContent(modifier: Modifier) {
        val context = LocalContext.current

        var selectedGasType by rememberSaveable { mutableStateOf(GasType.PERTALITE) }
        var payment by rememberSaveable { mutableIntStateOf(0) }
        var result by rememberSaveable { mutableIntStateOf(0) }
        var notificationMessage by rememberSaveable { mutableStateOf("") }
        var calculationPerformed by rememberSaveable { mutableStateOf(false) } // State untuk melacak apakah perhitungan telah dilakukan

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(80.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally

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
                onValueChange = {payment = it.toIntOrNull() ?: 0},
                label = { Text(stringResource(id = R.string.payment_label)) },
                isError = payment == 0
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (payment == 0) {
                    } else {
                        result = calculateGas(selectedGasType, payment)
                        notificationMessage = ""
                        calculationPerformed = true
                    }
                }
            ) {
                Text(stringResource(id = R.string.calculate_button))
            }
            Spacer(modifier = Modifier.height(16.dp))


            if (calculationPerformed) {
                Text(
                    text = notificationMessage,
                    color = Color.Red
                )

                val gasTypeText = when (selectedGasType) {
                    GasType.PERTAMAX -> context.getString(R.string.pertamax)
                    GasType.PERTALITE -> context.getString(R.string.pertalite)
                }
                Text(
                    text = "${context.getString(R.string.result_prefix)} $result ${context.getString(R.string.result_suffix)} ($gasTypeText)"
                )
                Button(
                    onClick = {
                        val outputData = "${context.getString(R.string.result_prefix)} $result ${context.getString(R.string.result_suffix)} ($gasTypeText)"
                        shareOutputData(context, outputData)
                    }
                ) {
                    Text(stringResource(id = R.string.share_button))
                }
            }
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
            GasType.PERTAMAX -> payment / 12950 // Harga per liter pertamax
            GasType.PERTALITE -> payment / 10000 // Harga per liter pertalite
        }
    }

    fun shareOutputData(context: Context, outputData: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, outputData)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }


    @Preview(showBackground = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun ScreenPreview() {
        Assessment1mobproTheme {
            MainScreen(rememberNavController())
        }
    }