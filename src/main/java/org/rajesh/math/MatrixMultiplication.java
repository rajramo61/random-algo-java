package org.rajesh.math;

/*
  Challenge: Multiply two matrices
 */

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class MatrixMultiplication {
    /* helper function to generate MxN matrix of random integers */
    public static int[][] generateRandomMatrix(int M, int N) {
        System.out.format("Generating random %d x %d matrix...\n", M, N);
        Random rand = new Random();
        int[][] output = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                output[i][j] = rand.nextInt(100);
        return output;
    }

    /* evaluate performance of sequential and parallel implementations */
    public static void main(String[] args) throws InterruptedException {
        final int NUM_EVAL_RUNS = 5;
        /*
          For quick test run the sample with
          final int[][] A = generateRandomMatrix(200, 2000);
          final int[][] B = generateRandomMatrix(2000, 200);
         */
        final int[][] A = generateRandomMatrix(2000, 2000);
        final int[][] B = generateRandomMatrix(2000, 2000);

        System.out.println("Evaluating Sequential Implementation...");
        SequentialMatrixMultiplier smm = new SequentialMatrixMultiplier(A, B);
        int[][] sequentialResult = smm.computeProduct();
        double sequentialTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            smm.computeProduct();
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;

        System.out.println("sequentialTime..." + sequentialTime);
        System.out.println("Evaluating Parallel Implementation...");


        //final int length = A.length;
        final int length = A.length * B[0].length;
        CountDownLatch latch = new CountDownLatch(length);
        ParallelMatrixMultiplier pmm = new ParallelMatrixMultiplier(A, B);
        int[][] parallelResult = pmm.computeProduct(latch);
        latch.await(10, TimeUnit.MINUTES);
        if (!Arrays.deepEquals(sequentialResult, parallelResult))
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");

        double parallelTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            CountDownLatch rLatch = new CountDownLatch(length);
            long start = System.currentTimeMillis();
            pmm.computeProduct(rLatch);
            rLatch.await(10, TimeUnit.MINUTES);
            parallelTime += System.currentTimeMillis() - start;
        }
        pmm.shutdown();
        parallelTime /= NUM_EVAL_RUNS;

        // display sequential and parallel results for comparison
        System.out.format("Average Sequential Time: %.1f ms\n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms\n", parallelTime);
        System.out.format("Speedup: %.2f \n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%\n", 100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }
}



/* sequential implementation of matrix multiplication */
class SequentialMatrixMultiplier {

    private int[][] A, B;
    private final int numRowsA;
    private final int numColsA;
    private final int numColsB;

    public SequentialMatrixMultiplier(int[][] A, int[][] B) {
        this.A = A;
        this.B = B;
        this.numRowsA = A.length;
        this.numColsA = A[0].length;
        int numRowsB = B.length;
        this.numColsB = B[0].length;
        if (numColsA != numRowsB)
            throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d\n", numRowsA, numRowsB, numColsA, numColsB));
    }

    /* returns matrix product C = AB */
    public int[][] computeProduct() {
        int[][] C = new int[numRowsA][numColsB];
        for (int i = 0; i < numRowsA; i++) {
            for (int k = 0; k < numColsB; k++) {
                int sum = 0;
                for (int j = 0; j < numColsA; j++) {
                    sum += A[i][j] * B[j][k];
                }
                C[i][k] = sum;
            }
        }
        return C;
    }
}

/* parallel implementation of matrix multiplication */
class ParallelMatrixMultiplier {

    private int[][] A, B;
    private final int numRowsA;
    private final int numColsA;
    private final int numRowsB;
    private final int numColsB;
    private final ExecutorService executorService;

    public ParallelMatrixMultiplier(int[][] A, int[][] B) {
        this.A = A;
        this.B = B;
        this.numRowsA = A.length;
        this.numColsA = A[0].length;
        this.numRowsB = B.length;
        this.numColsB = B[0].length;
        if (numColsA != numRowsB)
            throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d\n", numRowsA, numRowsB, numColsA, numColsB));
        executorService = Executors.newWorkStealingPool();
    }

    /* returns matrix product C = AB */
    public int[][] computeProduct(CountDownLatch latch) {
        // YOUR CODE GOES HERE //
        int[][] result = new int[numRowsA][numColsB];
        for (int i = 0; i < numRowsA; i++) {
            for (int k = 0; k < numColsB; k++) {
                final int locali = i;
                final int localk = k;
                createCFuture(A[locali], localk).thenAccept(data -> {
                    result[locali][localk] = data;
                    latch.countDown();
                });
            }
        }
        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private CompletableFuture<Integer> createCFuture(int[] ints, int k) {
        return CompletableFuture.supplyAsync(() -> getSum(ints, k), executorService);
    }

    private int getSum(int[] ints, int k) {
        int sum = 0;
        for (int j = 0; j < numColsA; j++) {
            sum += ints[j] * B[j][k];
        }
        return sum;
    }
}
