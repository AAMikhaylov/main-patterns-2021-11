package ru.otus.hw01;

public class QuadEquation {
    private final double precision;

    public QuadEquation(double precision) {
        this.precision = precision;
    }

    public double[] getSolutions(double a, double b, double c) {
        if (Math.abs(a) < precision)
            throw new IllegalArgumentException("Constant \"a\" must be greater than zero");
        if (Double.isInfinite(a) || Double.isInfinite(b) || Double.isInfinite(c))
            throw new IllegalArgumentException("One or more constant equations are infinite");
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c))
            throw new IllegalArgumentException("One or more constant of equations are undefined");
        double D = b * b - 4 * a * c;
        if (Double.isInfinite(D) || Double.isNaN(D))
            throw new IllegalArgumentException("One or more constant of equations is too large");
        //when abs(x1-x2)<precision then abs(D) < a * a * precision * precision
        double epsD = a * a * precision * precision;
        if (Math.abs(D) < epsD) {
            return new double[]{-b / (2 * a), -b / (2 * a)};
        } else if (D <= -epsD) {
            return new double[0];
        } else {
            return new double[]{(-b + Math.sqrt(D)) / (2 * a), (-b - Math.sqrt(D)) / (2 * a)};
        }
    }
}
