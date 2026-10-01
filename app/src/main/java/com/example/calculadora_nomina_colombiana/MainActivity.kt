package com.example.calculadora_nomina_colombiana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadora_nomina_colombiana.ui.components.CampoNumerico
import com.example.calculadora_nomina_colombiana.ui.components.FilaInterruptor
import com.example.calculadora_nomina_colombiana.ui.theme.ButtonWhite
import com.example.calculadora_nomina_colombiana.ui.theme.Calculadora_Nomina_ColombianaTheme
import com.example.calculadora_nomina_colombiana.ui.theme.ErrorAccent
import com.example.calculadora_nomina_colombiana.ui.theme.OnErrorContainerDark
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchPurple
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchPurpleLight
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchSurface
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchSurfaceElevated
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchTextSecondary
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchWhite
import java.text.NumberFormat
import java.util.Locale

private const val LIMITE_HORAS_EXTRAS = 48.0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculadora_Nomina_ColombianaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    CalculadoraNominaScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculadoraNominaScreen(modifier: Modifier = Modifier) {
    var salarioText by rememberSaveable { mutableStateOf("") }
    var horasDiurnasText by rememberSaveable { mutableStateOf("") }
    var horasNocturnasText by rememberSaveable { mutableStateOf("") }
    var esDominical by rememberSaveable { mutableStateOf(false) }
    var transporteEmpresa by rememberSaveable { mutableStateOf(false) }
    var resultado by rememberSaveable { mutableStateOf<ResultadoNomina?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var errorSalario by rememberSaveable { mutableStateOf(false) }
    var errorHoras by rememberSaveable { mutableStateOf(false) }

    val mensajeErrorSalario = stringResource(R.string.texto_error_salario, formatCurrency(SMMLV_2026))
    val mensajeErrorHoras = stringResource(R.string.texto_error_horas)

    fun calcular() {
        val salario = salarioText.toDoubleOrNull()
        val horasDiurnas = horasDiurnasText.toDoubleOrNull() ?: 0.0
        val horasNocturnas = horasNocturnasText.toDoubleOrNull() ?: 0.0

        when {
            salario == null || salario < SMMLV_2026 -> {
                errorSalario = true
                errorHoras = false
                resultado = null
                errorMessage = mensajeErrorSalario
            }

            horasDiurnas < 0.0 || horasNocturnas < 0.0 ||
                horasDiurnas + horasNocturnas > LIMITE_HORAS_EXTRAS -> {
                    errorSalario = false
                    errorHoras = true
                    resultado = null
                    errorMessage = mensajeErrorHoras
                }

            else -> {
                errorSalario = false
                errorHoras = false
                errorMessage = null
                resultado = calcularNomina(salario, horasDiurnas, horasNocturnas, esDominical, transporteEmpresa)
            }
        }
    }

    fun recalcularSiEsValido() {
        val salario = salarioText.toDoubleOrNull()
        val horasDiurnas = horasDiurnasText.toDoubleOrNull() ?: 0.0
        val horasNocturnas = horasNocturnasText.toDoubleOrNull() ?: 0.0

        if (salario != null && salario >= SMMLV_2026 && horasDiurnas >= 0.0 &&
            horasNocturnas >= 0.0 && horasDiurnas + horasNocturnas <= LIMITE_HORAS_EXTRAS
        ) {
            resultado = calcularNomina(salario, horasDiurnas, horasNocturnas, esDominical, transporteEmpresa)
        }
    }

    fun limpiar() {
        salarioText = ""
        horasDiurnasText = ""
        horasNocturnasText = ""
        esDominical = false
        transporteEmpresa = false
        resultado = null
        errorMessage = null
        errorSalario = false
        errorHoras = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_calculadora),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TwitchWhite
        )

        CampoNumerico(
            label = stringResource(R.string.etiqueta_salario_basico),
            value = salarioText,
            onValueChange = {
                salarioText = it
                errorSalario = false
                errorMessage = null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = errorSalario
        )

        CampoNumerico(
            label = stringResource(R.string.etiqueta_horas_diurnas),
            value = horasDiurnasText,
            onValueChange = {
                horasDiurnasText = it
                errorHoras = false
                errorMessage = null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = errorHoras
        )

        CampoNumerico(
            label = stringResource(R.string.etiqueta_horas_nocturnas),
            value = horasNocturnasText,
            onValueChange = {
                horasNocturnasText = it
                errorHoras = false
                errorMessage = null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = errorHoras
        )

        FilaInterruptor(
            label = stringResource(R.string.etiqueta_horas_dominical),
            checked = esDominical,
            onCheckedChange = {
                esDominical = it
                if (resultado != null) {
                    recalcularSiEsValido()
                }
            }
        )

        FilaInterruptor(
            label = stringResource(R.string.etiqueta_transporte_empresa),
            checked = transporteEmpresa,
            onCheckedChange = {
                transporteEmpresa = it
                if (resultado != null) {
                    recalcularSiEsValido()
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = TwitchSurface
                ),
                border = BorderStroke(1.dp, SolidColor(ErrorAccent)),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = OnErrorContainerDark,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { calcular() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TwitchPurple,
                    contentColor = ButtonWhite
                ),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.boton_calcular),
                    fontWeight = FontWeight.SemiBold
                )
            }
            OutlinedButton(
                onClick = { limpiar() },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = TwitchSurface,
                    contentColor = TwitchPurple
                ),
                border = BorderStroke(1.dp, SolidColor(TwitchPurple)),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.boton_limpiar),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        resultado?.let { nomina ->
            Spacer(modifier = Modifier.height(4.dp))

            SeccionRango(nomina.salarioBase())

            Spacer(modifier = Modifier.height(4.dp))

            DesgloseNomina(nomina)
        }
    }
}

@Composable
fun SeccionRango(salarioBasico: Double) {
    val rango = clasificarRango(salarioBasico)
    val imagen = when (rango) {
        RangoSalarial.RANGO_1 -> R.drawable.ic_range_1
        RangoSalarial.RANGO_2 -> R.drawable.ic_range_2
        RangoSalarial.RANGO_3 -> R.drawable.ic_range_3
    }
    val descripcion = when (rango) {
        RangoSalarial.RANGO_1 -> stringResource(R.string.rango_1_descripcion)
        RangoSalarial.RANGO_2 -> stringResource(R.string.rango_2_descripcion)
        RangoSalarial.RANGO_3 -> stringResource(R.string.rango_3_descripcion)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TwitchSurface),
        border = BorderStroke(1.dp, SolidColor(TwitchSurfaceElevated)),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imagen),
                contentDescription = stringResource(R.string.descripcion_imagen_rango),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TwitchTextSecondary
            )
        }
    }
}

@Composable
fun DesgloseNomina(resultado: ResultadoNomina) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TwitchSurface),
        border = BorderStroke(1.dp, SolidColor(TwitchSurfaceElevated)),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.titulo_desglose),
                style = MaterialTheme.typography.titleMedium,
                color = TwitchWhite
            )

            Text(
                text = stringResource(R.string.seccion_devengado),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            LineaDesglose(stringResource(R.string.linea_valor_hora), formatCurrency(resultado.valorHora))
            LineaDesglose(stringResource(R.string.linea_salario_basico), formatCurrency(resultado.salarioBase()))
            LineaDesglose(stringResource(R.string.linea_horas_extra), formatCurrency(resultado.totalHorasExtra))
            LineaDesglose(
                stringResource(R.string.linea_auxilio_transporte),
                formatCurrency(resultado.auxilioTransporte)
            )
            LineaDesglose(
                stringResource(R.string.linea_total_devengado),
                formatCurrency(resultado.totalDevengado),
                esTotal = true
            )

            HorizontalDivider(color = TwitchSurfaceElevated)

            Text(
                text = stringResource(R.string.seccion_deducciones),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            LineaDesglose(stringResource(R.string.linea_salud), formatCurrency(resultado.aporteSalud))
            LineaDesglose(stringResource(R.string.linea_pension), formatCurrency(resultado.aportePension))
            LineaDesglose(
                stringResource(R.string.linea_fondo_solidaridad),
                formatCurrency(resultado.fondoSolidaridad)
            )
            LineaDesglose(
                stringResource(R.string.linea_total_deducciones),
                formatCurrency(resultado.totalDeducciones),
                esTotal = true
            )

            HorizontalDivider(color = TwitchSurfaceElevated)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.linea_salario_neto),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TwitchWhite
                )
                Text(
                    text = formatCurrency(resultado.salarioNeto),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TwitchPurpleLight
                )
            }
        }
    }
}

@Composable
private fun LineaDesglose(label: String, valor: String, esTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (esTotal) FontWeight.SemiBold else FontWeight.Normal,
            color = if (esTotal) TwitchWhite else TwitchTextSecondary
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (esTotal) FontWeight.SemiBold else FontWeight.Normal,
            color = TwitchWhite
        )
    }
}

private fun ResultadoNomina.salarioBase(): Double =
    totalDevengado - auxilioTransporte - totalHorasExtra

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}

@Preview(showBackground = true)
@Composable
fun CalculadoraNominaPreview() {
    Calculadora_Nomina_ColombianaTheme {
        CalculadoraNominaScreen()
    }
}