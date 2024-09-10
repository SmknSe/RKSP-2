package PR3.ex4;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        FileGenerator fileGenerator = new FileGenerator(queue);

        List<FileProcessor> processors = Arrays.stream(FileType.values())
                .map(type ->
                        new FileProcessor(type,queue)
                )
                .toList();

        List<Observable<File>> observables = processors.stream()
                .map(p -> {
                    return p.processFiles()
                            .subscribeOn(Schedulers.io());
                })
                .toList();

        Observable<File> FileObservable3 = fileGenerator.run().subscribeOn(
                Schedulers.io()
        );

        FileObservable3.subscribe(queue::add);

        observables.forEach(Observable::subscribe);




        // Ожидаем завершения всех задач

        System.out.println("Все задачи завершены.");


        Thread.sleep(100000000000L);
    }
}


