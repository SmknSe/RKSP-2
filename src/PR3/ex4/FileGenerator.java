package PR3.ex4;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class FileGenerator {
    private final BlockingQueue<File> queue;
    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    public Observable<File> run() {
        Random random = new Random();
        return Observable.create(emitter -> {
            while (!emitter.isDisposed()) {
                int size = random.nextInt(91) + 10;
                int randomCode = random.nextInt(1,5);
                FileType type = FileType.getFileTypeFromCode(Integer.toString(randomCode));

                File file = new File(type, size);
                emitter.onNext(file);

                int delay = random.nextInt(901) + 100;
                Thread.sleep(delay);
            }
        });
    }
}
