package PR3.ex1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.javatuples.Pair;

import java.util.Random;

public class CO2Sensor extends Observable<Pair<String,Float>> implements Runnable{
    private PublishSubject<Pair<String,Float>> subject = PublishSubject.create();

    @Override
    protected void subscribeActual(@NonNull Observer<? super Pair<String,Float>> observer) {
        subject.subscribe(observer);
    }


    @Override
    public void run() {
        while (true){
            float co2 = new Random().nextFloat(30, 100);
            subject.onNext(new Pair<>("CO2", co2));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
