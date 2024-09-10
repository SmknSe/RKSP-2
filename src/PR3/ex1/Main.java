package PR3.ex1;

public class Main {
    public static void main(String[] args) {
        TempSensor t = new TempSensor();
        Alarm alarm = new Alarm();
        CO2Sensor c = new CO2Sensor();

        t.subscribeActual(alarm);
        c.subscribeActual(alarm);

        Thread t1 = new Thread(t);
        t1.start();

        Thread t2 = new Thread(c);
        t2.start();
    }
}
