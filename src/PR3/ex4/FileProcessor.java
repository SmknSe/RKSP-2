package PR3.ex4;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.BlockingQueue;

public class FileProcessor {

    private final FileType fileType;
    private final BlockingQueue<File> fileQueue;

    public FileProcessor(FileType fileType, BlockingQueue<File> fileQueue) {
        this.fileType = fileType;
        this.fileQueue = fileQueue;
    }

    public Observable<File> processFiles() {
        return Observable.create(emitter -> {
            while (!emitter.isDisposed()) {
                try {
                    File file = fileQueue.take();
                    if (file != null) {
                        if (file.getFileType().equals(fileType)) {
                            Thread.sleep(file.getFileSize() * 7L);
                            emitter.onNext(file);
                            System.out.println("Processed file: " + file.getFileType() + " " + Thread.currentThread());
                        }

                    }
                    else {
                        System.out.println("Пусто...");
                    }
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
