package com.example.calculadora_nomina_colombiana

/**
 * Salario mínimo legal mensual vigente para 2026.
 * Fuente: Decreto 1469 de 2025 (Ministerio de Trabajo).
 */
const val SMMLV_2026 = 1_750_905.0

/**
 * Auxilio de transporte máximo vigente para 2026.
 * Fuente: Decreto 1470 de 2025 (Ministerio de Trabajo).
 */
const val AUX_TRANSPORTE_2026 = 249_095.0

/**
 * Número de horas ordinarias laboradas al mes.
 * Fuente: Ley 2101 de 2021 (art. 3).
 */
const val HORAS_ORDINARIAS_MES = 210.0

/**
 * Porcentaje de aporte a salud del trabajador.
 * Fuente: Ley 100 de 1993 (art. 204).
 */
const val PORCENTAJE_SALUD = 0.04

/**
 * Porcentaje de aporte a pensión del trabajador.
 * Fuente: Ley 100 de 1993 (art. 204).
 */
const val PORCENTAJE_PENSION = 0.04

/**
 * Porcentaje del fondo de solidaridad pensional.
 * Fuente: Ley 797 de 2003 (art. 19).
 */
const val PORCENTAJE_FONDO_SOLIDARIDAD = 0.01

private const val FACTOR_EXTRA_DIURNO_DIA_NORMAL = 1.25
private const val FACTOR_EXTRA_NOCTURNO_DIA_NORMAL = 1.75
private const val FACTOR_EXTRA_DIURNO_DOMINICAL = 2.15
private const val FACTOR_EXTRA_NOCTURNO_DOMINICAL = 2.65

private const val MULTIPLICADOR_SALUD_PENSION = 2.0
private const val MULTIPLICADOR_FONDO_SOLIDARIDAD = 4.0

enum class RangoSalarial {
    RANGO_1,
    RANGO_2,
    RANGO_3
}

data class ResultadoNomina(
    val valorHora: Double,
    val totalHorasExtra: Double,
    val auxilioTransporte: Double,
    val totalDevengado: Double,
    val aporteSalud: Double,
    val aportePension: Double,
    val fondoSolidaridad: Double,
    val totalDeducciones: Double,
    val salarioNeto: Double
) : java.io.Serializable

/**
 * Calcula la nómina de un trabajador colombiano para el año 2026.
 *
 * @param salarioBasico salario base mensual en pesos.
 * @param horasDiurnas horas extra diurnas.
 * @param horasNocturnas horas extra nocturnas.
 * @param esDominical indica si las horas extras se laboraron en domingo o festivo.
 * @param transporteEmpresa indica si la empresa paga el auxilio de transporte.
 */
fun calcularNomina(
    salarioBasico: Double,
    horasDiurnas: Double,
    horasNocturnas: Double,
    esDominical: Boolean,
    transporteEmpresa: Boolean
): ResultadoNomina {
    val valorHora = salarioBasico / HORAS_ORDINARIAS_MES

    val factorDiurno = if (esDominical) FACTOR_EXTRA_DIURNO_DOMINICAL else FACTOR_EXTRA_DIURNO_DIA_NORMAL
    val factorNocturno = if (esDominical) FACTOR_EXTRA_NOCTURNO_DOMINICAL else FACTOR_EXTRA_NOCTURNO_DIA_NORMAL

    val pagoExtrasDiurnas = horasDiurnas * valorHora * factorDiurno
    val pagoExtrasNocturnas = horasNocturnas * valorHora * factorNocturno
    val totalHorasExtra = pagoExtrasDiurnas + pagoExtrasNocturnas

    val ibc = salarioBasico + totalHorasExtra

    val auxilioTransporte =
        if (salarioBasico <= MULTIPLICADOR_SALUD_PENSION * SMMLV_2026 && !transporteEmpresa) {
            AUX_TRANSPORTE_2026
        } else {
            0.0
        }

    val totalDevengado = ibc + auxilioTransporte

    val aporteSalud = ibc * PORCENTAJE_SALUD
    val aportePension = ibc * PORCENTAJE_PENSION
    val fondoSolidaridad =
        if (ibc >= MULTIPLICADOR_FONDO_SOLIDARIDAD * SMMLV_2026) {
            ibc * PORCENTAJE_FONDO_SOLIDARIDAD
        } else {
            0.0
        }

    val totalDeducciones = aporteSalud + aportePension + fondoSolidaridad
    val salarioNeto = totalDevengado - totalDeducciones

    return ResultadoNomina(
        valorHora = valorHora,
        totalHorasExtra = totalHorasExtra,
        auxilioTransporte = auxilioTransporte,
        totalDevengado = totalDevengado,
        aporteSalud = aporteSalud,
        aportePension = aportePension,
        fondoSolidaridad = fondoSolidaridad,
        totalDeducciones = totalDeducciones,
        salarioNeto = salarioNeto
    )
}

fun clasificarRango(salarioBasico: Double): RangoSalarial = when {
    salarioBasico <= MULTIPLICADOR_SALUD_PENSION * SMMLV_2026 -> RangoSalarial.RANGO_1
    salarioBasico < MULTIPLICADOR_FONDO_SOLIDARIDAD * SMMLV_2026 -> RangoSalarial.RANGO_2
    else -> RangoSalarial.RANGO_3
}