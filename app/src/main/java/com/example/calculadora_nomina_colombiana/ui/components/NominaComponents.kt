package com.example.calculadora_nomina_colombiana.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.calculadora_nomina_colombiana.ui.theme.ButtonWhite
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchPurple
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchSurface
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchSurfaceElevated
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchTextSecondary
import com.example.calculadora_nomina_colombiana.ui.theme.TwitchWhite

/**
 * Campo de entrada numérica reutilizable y sin estado interno.
 *
 * @param label texto visible sobre el campo.
 * @param value valor actual, controlada por el padre.
 * @param onValueChange notifica cada cambio de texto al padre.
 * @param keyboardOptions tipo de teclado y acciones asociadas.
 * @param isError marca el campo como error.
 * @param modifier modificador aplicado al campo.
 */
@Composable
fun CampoNumerico(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TwitchSurface,
            unfocusedContainerColor = TwitchSurface,
            disabledContainerColor = TwitchSurface,
            errorContainerColor = TwitchSurface,
            focusedBorderColor = TwitchPurple,
            unfocusedBorderColor = TwitchTextSecondary,
            disabledBorderColor = TwitchSurfaceElevated,
            focusedLabelColor = TwitchPurple,
            unfocusedLabelColor = TwitchTextSecondary,
            disabledLabelColor = TwitchSurfaceElevated,
            focusedTextColor = TwitchWhite,
            unfocusedTextColor = TwitchWhite,
            disabledTextColor = TwitchTextSecondary,
            errorTextColor = TwitchWhite,
            cursorColor = TwitchPurple,
            focusedPlaceholderColor = TwitchTextSecondary,
            unfocusedPlaceholderColor = TwitchTextSecondary
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Fila con etiqueta y interruptor reutilizable, sin estado interno.
 *
 * @param label texto que acompaña al interruptor.
 * @param checked estado actual, controlado por el padre.
 * @param onCheckedChange notifica el nuevo estado al padre.
 * @param modifier modificador aplicado a la fila.
 */
@Composable
fun FilaInterruptor(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TwitchWhite,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ButtonWhite,
                checkedTrackColor = TwitchPurple,
                checkedBorderColor = TwitchPurple,
                uncheckedThumbColor = TwitchTextSecondary,
                uncheckedTrackColor = TwitchSurfaceElevated,
                uncheckedBorderColor = TwitchTextSecondary
            )
        )
    }
}