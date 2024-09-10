package PR3.ex1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.javatuples.Pair;

import java.util.Random;

public class TempSensor extends Observable<Pair<String,Float>> implements Runnable{
    private PublishSubject<Pair<String,Float>> subject = PublishSubject.create();

    @Override
    protected void subscribeActual(@NonNull Observer<? super Pair<String,Float>> observer) {
        subject.subscribe(observer);
    }


    @Override
    public void run() {
        while (true){
            float temp = new Random().nextFloat(15, 30);
            subject.onNext(new Pair<>("TEMP",temp));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
