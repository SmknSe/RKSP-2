package PR3.ex2;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;

public class Main {


    public static void main(String[] args) {
        Random random = new Random();
        var source = Observable
                .range(0, 1000)
                .map(number -> number * number)
                .subscribe(number -> System.out.print(number+" "));

        System.out.println();

        Observable<String> letters = Observable.range(0, 100)
                .map(i -> String.valueOf((char) ('A' + random.nextInt(26))));
        Observable<Integer> numbers = Observable.range(0, 100).map(i -> random.nextInt(10));
        var mergedStream = Observable.zip(
                letters, numbers, (letter, number) -> letter + number
        ).subscribe(s -> System.out.print(s+" "));

        System.out.println();

        var rand1 = Observable.range(0, 10).map(i -> random.nextInt(10));

        var result = rand1.skip(3)
                .subscribe(System.out::print);
    }



}
