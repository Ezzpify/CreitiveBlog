package com.casper.creitive.Tasks;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by Desu on 2018-02-22.
 */

public class Tasks
{
    public void devOps()
    {
        for (int i = 1; i < 101; i++)
        {
            boolean dev = i % 3 == 0;
            boolean ops = i % 5 == 0;

            if (dev && ops)
            {
                Log.d("task", "DevOps");
            }
            else if (dev)
            {
                Log.d("task", "Dev");
            }
            else if (ops)
            {
                Log.d("task", "Ops");
            }
        }
    }

    public int factorial(int number)
    {
        if (number > 20)
            throw new IllegalArgumentException(number + " out of range");

        return (1 > number) ? 1 : number * factorial(number - 1);
    }

    public boolean isPrime(int number)
    {
        boolean prime = true;
        for(int div = 2; div <= number / 2; div++)
        {
            if (number % div == 0)
            {
                prime = false;
                break;
            }
        }

        return prime;
    }

    public Integer[] mergeSort(Integer[] a, Integer[] b)
    {
        Integer[] c = new Integer[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        Arrays.sort(c);

        Set<Integer> set = new HashSet<>(Arrays.asList(c));
        Integer[] unique = set.toArray(new Integer[set.size()]);

        return unique;
    }
}
