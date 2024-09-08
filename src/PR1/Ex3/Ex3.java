package PR1.Ex3;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class Ex3 {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);

        List<Thread> processors = Arrays.stream(FileType.values())
                .map(type -> new Thread(
                        new FileProcessor(queue,type)
                ))
                .toList();

        Thread generatorThread = new Thread(new FileGenerator(queue));

        generatorThread.start();
        processors.forEach(Thread::start);
    }
}
