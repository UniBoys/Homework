package exercises.o3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Airplane {
    private Random random = new Random();

    public void run() {
        double max = 100000;
        double successfull = 0;

        for (int i = 0; i < max; i++) {
            if (testCase()) {
                successfull++;
            }
        }

        System.out.format("The chances are: %s (%s/%s)", (successfull / max) * 100, successfull, max);
    }

    public boolean testCase() {
        int seats = 100;
        List<Boolean> plane = emptyPlane(seats);
        List<Integer> passengers = generatePassengers(seats);
        Collections.shuffle(passengers);

        int passenger = random.nextInt(seats);
        plane.set(passenger, true);

        for (int i = 0; i < seats - 2; i++) {
            passenger = passengers.get(i);

            if (plane.get(passenger)) {
                int seat = findEmptySeat(plane);
                plane.set(seat, true);
            } else {
                plane.set(passenger, true);
            }
        }

        return !plane.get(passengers.get(seats - 1));
    }

    int findEmptySeat(List<Boolean> plane) {
        int seat = random.nextInt(plane.size());

        if (plane.get(seat)) {
            return findEmptySeat(plane);
        }

        return seat;
    }

    List<Boolean> emptyPlane(int seats) {
        return new ArrayList<>(Collections.nCopies(seats, false));
    }

    List<Integer> generatePassengers(int seats) {
        return IntStream.range(0, seats).boxed().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        new Airplane().run();
    }
}