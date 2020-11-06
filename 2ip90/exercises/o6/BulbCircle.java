package exercises.o6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BulbCircle {

    int AMOUNT_OF_BULBS = 50;
    List<Bulb> bulbStates;

    void run() {
        bulbStates = IntStream.range(0, AMOUNT_OF_BULBS).boxed().map(i -> new Bulb()).collect(Collectors.toList());
        long iterator;

        iterate(0);
        
        for(iterator = 1; bulbStates.stream().anyMatch(bulb -> !bulb.isOn()); iterator++) {
            iterate(iterator);

            if(iterator % 100000000 == 0) {
                System.out.format("%s steps\n", iterator);
            }
        }

        System.out.println(iterator);
    }

    void iterate(long iterator) {
        Bulb current = bulbStates.get((int)(iterator % AMOUNT_OF_BULBS));

        if(current.isOn()) {
            bulbStates.get((int)((iterator+1) % AMOUNT_OF_BULBS)).toggle();
        }
    }

    public static void main(String[] args) {
        new BulbCircle().run();
    }

    class Bulb {
        private boolean on;

        public Bulb() {
            this.on = true;
        }

        void toggle() {
            this.on = !this.on;
        }

        boolean isOn() {
            return this.on;
        }

        @Override
        public String toString() {
            return this.on ? "O" : "-";
        }
    }
}
