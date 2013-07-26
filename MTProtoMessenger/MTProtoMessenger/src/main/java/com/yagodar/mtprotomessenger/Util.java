package com.yagodar.mtprotomessenger;

/**
 * Created by Yagodar on 26.07.13.
 */
public class Util {
    /**
     * Является ли число простым?
     */
    public static boolean isPrime(long N) {
        if (N < 2L) return false;
        for (long i = 2L; i*i <= N; i++)
            if (N % i == 0L) return false;
        return true;
    }
}
