package PR1.Ex3;

import java.util.concurrent.BlockingQueue;

public class FileProcessor implements Runnable {
    private final BlockingQueue<File> queue;
    private final FileType allowedFileType;
    public FileProcessor(BlockingQueue<File> queue, FileType allowedFileType) {
        this.queue = queue;
        this.allowedFileType = allowedFileType;
    }
    @Override
    public void run() {
        while (true) {
            try {
                File file = queue.take();
                if (file.getFileType().equals(allowedFileType)) {
                    long processingTime = file.getFileSize() * 7L;
                    Thread.sleep(processingTime);
                    System.out.println("Обработан файл типа " +
                            file.getFileType() +
                            " с размером " + file.getFileSize() + ". Время обработки: " + processingTime + " мс.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}