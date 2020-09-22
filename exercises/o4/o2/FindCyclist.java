package exercises.o4.o2;

import exercises.o4.o2.CycleBubble;
import java.util.ArrayList;
import java.util.List;

class FindCyclist {
    public static void main(String[] args) {
        CycleBubble cb = new CycleBubble(4, 2);

        boolean tests[] = new boolean[cb.TESTCAP];

        for (int i = 0; i < tests.length; i++) {
            List<Integer> cyclists = new ArrayList<>();

            for (int n = 0; n < cb.BUBBLE_SIZE; n++) {
                if((n>>i) % 2 == 1) {
                    cyclists.add(n);
                }
            }

            tests[i] = cb.test(cyclists);
        }

        int index = 0;

        for (int i = 0; i < tests.length; i++) {
            index += tests[i] ? 1 : 0;
            index <<= 1;
        }

        cb.check(index);
    }
}