package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraTest {

    private final Calculadora calculadora = new Calculadora();

    @Test
    void sumarDosNumeros() {
        assertEquals(5, calculadora.sumar(2, 3));
    }

    @Test
    void restarDosNumeros() {
        assertEquals(1, calculadora.restar(3, 2));
    }

    @Test
    void multiplicarDosNumeros() {
        assertEquals(6, calculadora.multiplicar(2, 3));
    }

    @Test
    void dividirDosNumeros() {
        assertEquals(2.0, calculadora.dividir(4, 2));
    }

    @Test
    void dividirEntreCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.dividir(4, 0));
    }

    @Test
    void detectaNumeroPrimo() {
        assertTrue(calculadora.esPrimo(7));
        assertFalse(calculadora.esPrimo(8));
    }
}
