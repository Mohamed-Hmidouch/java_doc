import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> cars = new ArrayList<String>();
        ArrayList<Integer>  années = new ArrayList<Integer>();
        années.add(2020);
        années.add(2012);
        cars.add("Mercedes");
        cars.add("Multsubtchi");
        cars.add("Renault");
        cars.set(0,"jjj");
        Collections.sort(cars);
        for(String car : cars){
            System.out.println(car);
        }
        for(Integer année : années){
            System.out.println(année);
        }
    }
}