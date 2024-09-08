package PR1;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class Ex2 {
    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(12)) {
            while (true) {
                try {
                    System.out.print("Введите число (или 'exit' для выхода): ");
                    Scanner scanner = new Scanner(System.in);
                    String userInput = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(userInput)) {
                        break;
                    }

                    int number = Integer.parseInt(userInput);

                    Future<Integer> future = executorService.submit(() ->
                            calculateSquare(number));

                    try {
                        int result = future.get();
                        System.out.println("Результат: " + result);
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Ошибка при выполнении запроса: " +
                                e.getMessage());
                    }
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
