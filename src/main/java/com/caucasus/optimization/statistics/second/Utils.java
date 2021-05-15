package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.util.QuadraticFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    public static QuadraticFunction generateFunction(int dimension, int conditionalNumber) {
        Random random = new Random();
        double first = random.nextDouble();
        double last = first * conditionalNumber;
        List<Double> list = random.doubles(dimension, first, last)
                .sorted().boxed().collect(Collectors.toList());
        List<List<Double>> a = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            a.add(new ArrayList<>(Collections.nCopies(dimension, 0.)));
            a.get(i).set(i, list.get(i));
        }
        a.get(0).set(0, first);
        a.get(dimension - 1).set(dimension - 1, last);
        return new QuadraticFunction(a, new ArrayList<>(Collections.nCopies(dimension, 0.)), 0.);
    }
}
