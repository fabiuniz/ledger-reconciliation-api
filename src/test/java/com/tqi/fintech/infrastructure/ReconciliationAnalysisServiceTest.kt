package com.tqi.fintech.infrastructure

import com.tqi.fintech.application.output.AiAuditorEngine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ReconciliationAnalysisServiceTest {

    private val aiEngine: AiAuditorEngine = mockk()
    private val service = ReconciliationAnalysisService(aiEngine)

    @Test
    fun `deve executar auditoria complexa em paralelo retornando score de risco baixo`() = runTest {
        // Arrange
        val transactionId = "TRX-TEST-123"
        val amount = BigDecimal("50.00")
        val tipo = "CREDIT"

        // Act
        val resultado = service.executarAuditoriaComplexaEmParalelo(transactionId, amount, tipo)

        // Assert
        assertEquals("RISCO_BAIXO", resultado["riskScore"])
        assertEquals("COMPLIANT", resultado["complianceStatus"])
        assertEquals(transactionId, resultado["transactionId"])
    }

    @Test
    fun `deve encaminhar dados para o motor de ia chamando a porta da arquitetura hexagonal`() = runTest {
        // Arrange
        val transactionId = "TRX-IA-999"
        val amount = BigDecimal("0.05")
        val tipo = "DEBIT"
        val jsonEsperado = "{\"categoria_risco\": \"BAIXO\"}"

        coEvery { aiEngine.auditDiscrepancy(transactionId, amount, tipo) } returns jsonEsperado

        // Act
        val resultado = service.analisarDivergenciaFisica(transactionId, amount, tipo)

        // Assert
        assertEquals(jsonEsperado, resultado)
        coVerify(exactly = 1) { aiEngine.auditDiscrepancy(transactionId, amount, tipo) }
    }
}
