package ru.otus.hw01;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс решения квадратных уравнений QuadEquation должен:")
class QuadEquationTest {
    private final static double precision = 1e-7;
    private QuadEquation quadEquation;

    @BeforeEach
    void setUp() {
        quadEquation = new QuadEquation(precision);
    }


    @Test
    @DisplayName("Возвращать пустой массив, когда D<0")
    void shouldBeEmptyWithDLsZero() {
        double a = 1;
        double b = 3;
        double c = 4;
        double[] solutions = quadEquation.getSolutions(a, b, c);
        assertEquals(solutions.length, 0);
    }

    @Test
    @DisplayName("Возвращать массив из двух одинаковых корней, когда D около 0")
    void shouldHaveOneSolutionWithDNearZero() {
        double a = 3;
        double b = (2e-15) + 6;
        double c = 3;
        double[] solutions = quadEquation.getSolutions(a, b, c);
        assertEquals(solutions.length, 2);
        assertTrue(Math.abs(a * solutions[0] * solutions[0] + b * solutions[0] + c) < precision);
        assertTrue(Math.abs(solutions[0] - solutions[1]) < precision);
    }

    @Test
    @DisplayName("Возвращать массив из двух одинаковых корней, когда D отрицательный и около 0")
    void shouldHaveOneSolutionWithDLessAndNearZero() {
        double a = 3;
        double b = 6;
        double c = (2e-15) + 3;
        double[] solutions = quadEquation.getSolutions(a, b, c);
        assertEquals(solutions.length, 2);
        assertTrue(Math.abs(a * solutions[0] * solutions[0] + b * solutions[0] + c) < precision);
        assertTrue(Math.abs(solutions[0] - solutions[1]) < precision);
    }


    @Test
    @DisplayName("Возвращать массив из двух различных корней, когда D>0")
    void shouldHaveTwoSolutionWithDGtZero() {
        double a = 3;
        double b = 7;
        double c = 3;
        double[] solutions = quadEquation.getSolutions(a, b, c);
        assertEquals(solutions.length, 2);
        assertTrue(Math.abs(a * solutions[0] * solutions[0] + b * solutions[0] + c) < precision);
        assertTrue(Math.abs(a * solutions[1] * solutions[1] + b * solutions[1] + c) < precision);
        assertTrue(Math.abs(solutions[0] - solutions[1]) > precision);
    }

    @Test
    @DisplayName("Бросать исключение, если a=0")
    void shouldHaveExcpWithAeqZero() {
        double a = precision / 2;
        double b = 1;
        double c = 2;
        Exception e = assertThrows(IllegalArgumentException.class, () -> quadEquation.getSolutions(a, b, c));
        assertEquals("Constant \"a\" must be greater than zero", e.getMessage());
    }

    @Test
    @DisplayName("Бросать исключение, если один из коэффициентов равен бесконечности")
    void shouldHaveExcpWithInfinite() {
        double[] params = new double[3];
        for (int i = 0; i < 3; i++) {
            params[i] = Double.MAX_VALUE * 10;
            params[(i + 1) % 3] = 1;
            params[(i + 2) % 3] = 2;
            Exception e = assertThrows(IllegalArgumentException.class, () -> quadEquation.getSolutions(params[0], params[1], params[2]));
            assertEquals("One or more constant equations are infinite", e.getMessage());
        }
    }

    @Test
    @DisplayName("Бросать исключение, если один из коэффициентов неопределен")
    void shouldHaveExcpWithNaN() {
        double[] params = new double[3];
        for (int i = 0; i < 3; i++) {
            params[i] = Double.NaN;
            params[(i + 1) % 3] = 1;
            params[(i + 2) % 3] = 2;
            Exception e = assertThrows(IllegalArgumentException.class, () -> quadEquation.getSolutions(params[0], params[1], params[2]));
            assertEquals("One or more constant of equations are undefined", e.getMessage());
        }
    }

    @Test
    @DisplayName("Бросать исключение, если D равен бесконечности")
    void shouldHaveExcpWithDInfinite() {
        double a = Double.MAX_VALUE;
        double b = Double.MAX_VALUE;
        double c = Double.MIN_VALUE;
        Exception e = assertThrows(IllegalArgumentException.class, () -> quadEquation.getSolutions(a, b, c));
        assertEquals("One or more constant of equations is too large", e.getMessage());

    }
}