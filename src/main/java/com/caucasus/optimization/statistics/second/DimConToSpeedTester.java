package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.minfinder.SteepestDescent;
import com.caucasus.optimization.algos.entities.util.Domain;
import com.caucasus.optimization.algos.entities.util.QuadraticFunction;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to retrieve iterations to function dimension and conditionality number to iterations statistics.
 *
 * @author nkorzh
 */
public class DimConToSpeedTester {
    private final static int MAX_DIMENSION = 100_000;
    private final static int TESTS_AMOUNT = 5;
    private final static int CONDITIONAL_STEP = 100;
    private final static int CONDITIONAL_NUMBERS_AMOUNT = 100;

    /// DO NOT LAUNCH: USELESS BODY -> COMMENT BEFORE START
    public static void main(String[] args) {
        // TODO: for (n in 1..5) {
        //                  (n это степень размерности функции, нужен запуск для размерности от 10 до 10^5)
        //                L и l - наибольшее и наименьшее собственные значения матрицы (макс и мин на диагонали)
        //                launch 5 times -> запомнить числа обусловленности, подогнать чтобы под каждое запускалось
        //                по ~5 раз
        //       }

        // for each method
        List<List<Double>> dependency = new ArrayList<>();
        for (int dimension = 10; dimension <= MAX_DIMENSION; dimension *= 10) {
            List<Double> iterationsToK = new ArrayList<>();
            for (int conditionalNumber = 2; conditionalNumber <= CONDITIONAL_STEP * CONDITIONAL_NUMBERS_AMOUNT;
                 conditionalNumber += CONDITIONAL_STEP) {
                int iterationsCount = 0;
                for (int test = 0; test < TESTS_AMOUNT; test++) {
                    final QuadraticFunction function = generateFunction(dimension, conditionalNumber);
//                    final double eps = 0.1;
//                    final Domain domain = new Domain();
//                    GradientMethod method = new SteepestDescent(function, eps, domain);
//                    iterationsCount += method.getSolution().getIterations();
                }
                iterationsToK.add((double) iterationsCount / TESTS_AMOUNT);
                // need to save conditionalNumber and save to file the values
            }
        }
        // build MAX_DIMENSION charts with lines as iterationsToK
    }

    private static QuadraticFunction generateFunction(int dimension, int conditionalNumber) {
        // TODO: generate main diagonal only, others should be zero;
        //  numbers should be in ascending order
//        double startValue = random...
//        double endValue = conditionalNumber * startValue;

        return null;
    }

}
