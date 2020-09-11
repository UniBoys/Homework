package assignments.hangman;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
// Hangman TEMPLATE
// Homework Assignment 2 2ip90 

/**
 * @name(s) Jort van Driel, Thijs Aarnoudse
 * @id(s) 1579584, 1551159
 * @group 52
 * @date 11-09-2020
 */
class Hangman {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Hangman().play();
    }

    void play() {
        //Secret words
        String[] bagOfWords = new String[]{"the", "walrus", "and", "carpenter", "were", "walking", "close", "at", "hand"};

        //Initialize the random number generator
        System.out.println("Type an arbitrary number");
        int seed = scanner.nextInt();
        Random random = new Random(seed);

        //Select a secret word
        String secret = bagOfWords[random.nextInt(bagOfWords.length)];
        List<Character> guestLetters = Collections.nCopies(secret.length(), '_');

        int points = 10;

        while (points > 0) {
            char guestLetter = scanner.next().charAt(0);

            if (!secret.contains("" + guestLetter) || guestLetters.contains(guestLetter)) {
                points--;
                continue;
            }

            for (int i = 0; i < secret.length(); i++) {
                char c = secret.charAt(i);

                if (guestLetter == c) {
                    guestLetters.set(i, guestLetter);
                }

                System.out.print(secret.charAt(i));
            }

            System.out.print("\n");

            if (!guestLetters.contains('_')) {
                System.out.println("Well done, you won!");
                return;
            }
        }

        System.out.println("Unlucky, you lost!");
        System.out.format("The secret word was: %s", secret);
    }
}
