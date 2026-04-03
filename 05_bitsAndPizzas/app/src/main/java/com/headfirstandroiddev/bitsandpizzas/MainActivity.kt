package com.headfirstandroiddev.bitsandpizzas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headfirstandroiddev.bitsandpizzas.ui.theme.BitsAndPizzasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BitsAndPizzasTheme {
                PizzasApp()
            }
        }
    }
}

/**
 * Uses [PizzasAndExtrasViewModel] to handle
 * the backend logic for the app.
 */
@Composable
fun PizzasApp(
    modifier: Modifier = Modifier,
    viewModel: PizzasAndExtrasViewModel = viewModel(),
) {
    val pizzas = viewModel.pizzasState.value
    val extras = viewModel.extrasState.value
    val context = LocalContext.current

    LaunchedEffect(pizzas, extras) {
        val selectedPizzaStr = pizzas.filter { it.isSelected }.joinToString { it.name }
        val extrasSelectedStr = extras.filter { it.isSelected }.joinToString { it.name }
        if (selectedPizzaStr.isNotBlank() || extrasSelectedStr.isNotBlank()) {
            Toast.makeText(
                context,
                "Order Items: $selectedPizzaStr, (Extras: $extrasSelectedStr)",
                Toast.LENGTH_SHORT,
            ).show()
        } else if (extrasSelectedStr.isNotBlank() && selectedPizzaStr.isBlank()) {
            Toast.makeText(
                context,
                "Select a pizza first!",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(R.string.pizza_prompt),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 12.dp)
            )
            RadioButtons(
                pizzas,
                viewModel::onPizzaClick
            )
            Text(
                text = stringResource(R.string.extras_prompt),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                        .padding(start = 12.dp)
            )
            DisplayChips(
                extras,
                { extra ->
                    if (pizzas.none { it.isSelected }) {
                        Toast.makeText(
                            context,
                            "Select a pizza first!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        viewModel.onExtraClick(extra)
                    }
                },
                Modifier.padding(start = 12.dp)
            )
        }
    }
}

/**
 * Only one of the [options] can be selected.
 * Logic defined in [PizzasAndExtrasViewModel]
 */
@Composable
fun RadioButtons(
    options: List<Pizza>,
    onRadioSelect: (Pizza) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option.isSelected,
                    onClick = { onRadioSelect(option) }
                )
                Text(
                    text = option.name,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Multiple [extras] can be selected,
 * however a pizza needs to be selected first.
 * Logic specified in [PizzasApp]
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayChips(
    extras: List<Extra>,
    onChipSelect: (Extra) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        extras.forEach { extra ->
            FilterChip(
                label = { Text(extra.name) },
                selected = extra.isSelected,
                onClick = { onChipSelect(extra) },
                leadingIcon = { if (extra.isSelected) Icon(
                    Icons.Filled.Check,
                    null,
                    Modifier.size(FilterChipDefaults.IconSize)
                )}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PizzasAppPreview() {
    PizzasApp()
}