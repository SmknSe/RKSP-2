package PR1;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import static java.lang.StringTemplate.STR;

public class Ex1 {

    private static long memoryUsed = 0L;

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        List<Integer> testList = setUp(10_000);

        long startTime = System.currentTimeMillis();
        int result = calculateSum(testList);
        long endTime = System.currentTimeMillis();
        printTimeAndMemoryUsage(
                STR."Single Thread: \{ result }",
                startTime,
                endTime
        );
        startTime = System.currentTimeMillis();
        result = calculateSumMultiThread(testList);
        endTime = System.currentTimeMillis();
        printTimeAndMemoryUsage(
                STR."Multi Thread: \{ result }",
                startTime,
                endTime
        );
        startTime = System.currentTimeMillis();
        result = calculateSumFork(testList);
        endTime = System.currentTimeMillis();
        printTimeAndMemoryUsage(
                STR."ForkJoinPool: \{ result }",
                startTime,
                endTime
        );
        startTime = System.currentTimeMillis();
        result = calculateVirtualThreadsSum(testList, 500);
        endTime = System.currentTimeMillis();
        printTimeAndMemoryUsage(
                STR."Virtual Thread: \{ result }",
                startTime,
                endTime
        );
    }
    private static List<Integer> setUp(int size) {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int randomNumber = random.nextInt(1,1000);
            list.add(randomNumber);
        }
        return list;
    }
    private static int calculateSum(List<Integer> list) throws InterruptedException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }
        int sum = 0;

        for (var el : list) {
            sum += el;
            Thread.sleep(1);
        }
        return sum;
    }
    private static int calculateSumMultiThread(List<Integer> list) throws
            InterruptedException, ExecutionException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }

        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        int sum = 0;
        try (ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)) {

            List<Callable<Integer>> tasks = new ArrayList<>();
            int batchSize = list.size() / numberOfThreads;

            for (int i = 0; i < numberOfThreads; i++) {
                final int startIndex = i * batchSize;
                final int endIndex = (i == numberOfThreads - 1) ? list.size() : (i +
                        1) * batchSize;
                tasks.add(() -> calculateSubListSum(list.subList(startIndex, endIndex)));
            }

            List<Future<Integer>> futures = executorService.invokeAll(tasks);

            for (Future<Integer> future : futures) {
                int subListSum = future.get();
                Thread.sleep(1);
                sum += subListSum;
            }
            executorService.shutdown();
        }

        return sum;
    }

    private static int calculateSubListSum(List<Integer> sublist) throws
            InterruptedException {
        int sum = 0;

        for (var el : sublist) {
            sum += el;
            Thread.sleep(1);
        }
        return sum;
    }

    private static int calculateVirtualThreadsSum(List<Integer> list, int numThreads)
            throws ExecutionException, InterruptedException {
        List<Future<Integer>> results;
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            int partitionSize = list.size() / numThreads;
            results = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                int start = i * partitionSize;
                int end = (i == numThreads - 1) ? list.size() : (i + 1) * partitionSize;
                List<Integer> sublist = list.subList(start, end);

                results.add(executorService.submit(() -> {
                    int sum = 0;
                    for (var el : sublist) {
                        sum += el;
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return sum;
                }));
            }
        }

        int total = 0;
        for (var f : results) {
            int s = f.get();
            total += s;
            Thread.sleep(1);
        }

        return total;
    }

    public static int calculateSumFork(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }

        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {

            SumCalculatingTask task = new SumCalculatingTask(list, 0, list.size());

            return forkJoinPool.invoke(task);
        }
    }

    private static void printTimeAndMemoryUsage(String method, long startTime,
                                                long endTime)
    {
        long elapsedTime = endTime - startTime;
        System.out.println("Метод " + method + ":");
        System.out.println("Время выполнения: " + elapsedTime + " мс");
        Runtime runtime = Runtime.getRuntime();
        long memoryOperationUsed = memoryUsed == 0 || runtime.freeMemory() > memoryUsed
        ? runtime.totalMemory() - runtime.freeMemory()
        : memoryUsed - runtime.freeMemory();
        memoryUsed = runtime.freeMemory();
        System.out.println("Использование памяти: " + memoryOperationUsed / (1024 * 1024)
                + " МБ");
        System.out.println();
    }

    static class SumCalculatingTask extends RecursiveTask<Integer> {
        private final List<Integer> list;
        private final int start;
        private final int end;
        SumCalculatingTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 50) {
                try {
                    return calculateSubListSum(list.subList(start, end));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            int middle = start + (end - start) / 2;

            SumCalculatingTask leftTask = new SumCalculatingTask(list, start, middle);
            SumCalculatingTask rightTask = new SumCalculatingTask(list, middle, end);

            leftTask.fork();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return rightTask.compute() + leftTask.join();
        }
    }
}