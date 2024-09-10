package PR3.ex1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.javatuples.Pair;

public class Alarm implements Observer<Pair<String,Float>> {
    private final float CO2_LIMIT = 70;
    private final float TEMP_LIMIT = 25;
    private float temperature = 0;
    private float co2 = 0;


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println(d.hashCode() + " has been subscribed");
    }

    @Override
    public void onNext(@NonNull Pair<String,Float> pair) {
        if(pair.getValue0() == "CO2") co2 = pair.getValue1();
        if(pair.getValue0() == "TEMP") temperature = pair.getValue1();

        if(temperature > TEMP_LIMIT && co2 > CO2_LIMIT){
            System.out.println("ALARM!!!" +  "Temperature/CO2: " +temperature + "/" + co2);
        } else if (temperature > TEMP_LIMIT) {
            System.out.println("Temperature limit exceeded " + temperature);
        } else if (co2 > CO2_LIMIT) {
            System.out.println("CO2 limit exceeded " + co2);
        }

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Alarm completed");
    }
}
