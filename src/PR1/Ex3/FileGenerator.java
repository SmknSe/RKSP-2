package PR1.Ex3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class FileGenerator implements Runnable {
    private final BlockingQueue<File> queue;
    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(random.nextInt(901) + 100);
                int randomFileSize = random.nextInt(91) + 10;
                int randomCode = random.nextInt(1,5);

                File file = new File(
                        FileType.getFileTypeFromCode(Integer.toString(randomCode)),
                        randomFileSize
                );

                queue.put(file);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
