package PR1;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class Ex2 {
    public static void main(String[] args) {

        ConcurrentLinkedQueue<Future<Integer>> queue = new ConcurrentLinkedQueue<>();

        try (ExecutorService executorService = Executors.newFixedThreadPool(12)) {
            executorService.submit(()->{
                while (true) {
                    for (var f : queue) {
                        if (f.isDone()) {
                            try {
                                var res = f.get();
                                System.out.println("Result: "+res);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                            queue.remove(f);
                        }
                    }
                }
            });
            while (true) {
                try {
                    System.out.print("Введите число (или 'exit' для выхода): ");
                    Scanner scanner = new Scanner(System.in);
                    String userInput = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(userInput)) {
                        break;
                    }

                    int number = Integer.parseInt(userInput);

                    queue.add(executorService.submit(() ->
                            calculateSquare(number)));

                } catch (NumberFormatException e) {
                    System.err.println("Неверный формат числа. Пожалуйста, введите целое число.");
                }
            }
        }
    }
    private static int calculateSquare(int number) {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * number;
    }
}
