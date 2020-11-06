package exercises.o4.o2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class FindCyclist {
    static class CycleBubble {
        final int BUBBLE_SIZE;     // number of riders in the bubble
        final int TESTCAP;        // number of tests available
    
        private final int badguy;   //id of infected rider; 0 if no one is infected
        private int testcount = 0;  // number of tests performed so far
        private boolean hasCheated = false;   // has the user cheated?
        private Random random = new Random();  
    
        public CycleBubble(int size, int testcap) {
            if (size <= 0 || testcap <= 0) {
                BUBBLE_SIZE = 100;
                TESTCAP = 7;
            } else {
                BUBBLE_SIZE = size;
                TESTCAP = testcap;
            }
            badguy = random.nextInt(BUBBLE_SIZE + 1);
    
        }
        
        public CycleBubble() {
            this(100, 7);
        }
    
        public boolean test(List<Integer> batch) {
            return batch.contains(badguy);
        }
    
        public boolean test(int[] batch) {
            if (testcount >= TESTCAP) {
                throw new IllegalStateException("Number of available tests has been reached. We didn't see this coming. --The RIVM");
            }
            testcount++;
            // List<Integer> list = Arrays.asList(batch);
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int n : batch) {
                list.add(n);
            }
            return test(list);
        }
    
        public int cheat() {
            hasCheated = true;
            return badguy;
        }
    
        public String check(int id) {
            if (testcount > TESTCAP) {
                throw new IllegalStateException("Check already used.");
            }
            testcount = TESTCAP + 1; // to prevent checking further    
            if (id == badguy) {
                String solution = "Indeed, "+(badguy==0 ? "no one was infected," : badguy+" is infected. ");
                return solution+"You solved it, "+(hasCheated ? "with cheating, though." : "without cheating. Very good!");
            } else {
                return "Alas. You blamed the wrong guy.";
            }
        }
    }

    static String find() {
        CycleBubble cb = new CycleBubble();

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

        for (int i = tests.length-1; i >= 0; i--) {
            index += tests[i] ? 1 : 0;
            if(i > 0) {
                index <<= 1;
            }
        }

        if(index == 0) {
            index = 100;
        } 

        String check = cb.check(index);

        if(check.equals("Alas. You blamed the wrong guy.")) {
            System.out.format("%s, %s, %s\n", index, Arrays.toString(tests), cb.cheat());
        }

        return check;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 100; i++) {
            System.out.format("(%s) %s\n", i, find());
        }
    }
}