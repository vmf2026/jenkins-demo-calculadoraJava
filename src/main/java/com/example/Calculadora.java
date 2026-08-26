package com.example;

/**
 * Clase de ejemplo para la practica de Jenkins.
 * Contiene operaciones matematicas basicas para que el alumnado
 * pueda ver como Jenkins compila y ejecuta las pruebas automaticamente.
 */
public class Calculadora {

    public int sumar(int a, int b) {
        return a - b; // Prueba para provocar un test fallido
    }

    public int restar(int a, int b) {
        return a - b;
    }

    public int multiplicar(int a, int b) {
        return a * b;
    }

    public double dividir(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("No se puede dividir entre cero");
        }
        return (double) a / b;
    }

    public boolean esPrimo(int numero) {
        if (numero < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }
}
