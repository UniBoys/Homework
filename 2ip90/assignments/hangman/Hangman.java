import java.util.ArrayList;
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
 * @date 14-09-2020
 */
class Hangman {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Hangman().play();
    }

    /**
     * Starts the game Hangman.
     * It lets players play Hangman using the standard input and output.
     */
    void play() {
        //Secret words
        String[] bagOfWords = new String[]{"the", "walrus", "and", "carpenter", "were", "walking", "close", "at", "hand"};

        //Initialize the random number generator
        System.out.println("Type an arbitrary number");
        int seed = scanner.nextInt();
        Random random = new Random(seed);

        //Select a secret word
        String secret = bagOfWords[random.nextInt(bagOfWords.length)];
        // The list of letters that have been guessed correctly. Letters that have not been guessed are underscores.
        List<Character> guestLetters = new ArrayList<>(Collections.nCopies(secret.length(), '_'));

        printWord(guestLetters);

        // The amount of points the player has left
        int points = 10;

        while (points > 0) {
            // The letter the player has guest
            char guestLetter = scanner.next().charAt(0);

            if (!isGuessCorrect(secret, guestLetter, guestLetters)) {
                points--;
                if (points > 0) {
                    printWord(guestLetters);
                }

                continue;
            }

            revealLettersInWord(guestLetters, secret, guestLetter);
            printWord(guestLetters);

            if (hasWon(guestLetters)) {
                System.out.println("Well done, you won!");

                return;
            }
        }

        System.out.println("Unlucky, you lost!");
        System.out.format("The secret word was: %s\n", secret);
    }

    /**
     * Returns true if the guessed letter is in the secret and has not been guessed before.
     */
    boolean isGuessCorrect(String secret, char guess, List<Character> guestLetters) {
        return secret.contains("" + guess) && !guestLetters.contains(guess);
    }

    /**
     * Returns true if all characters have been guessed and no underscores are left.
     */
    boolean hasWon(List<Character> guestLetters) {
        return !guestLetters.contains('_');
    }

    /**
     * For every occurrence of the guest letter in the secret, 
     * will be updated in the characters list.
     */
    void revealLettersInWord(List<Character> characters, String secret, char guestLetter) {
        for (int i = 0; i < secret.length(); i++) {
            // The character from position i in the secret
            char character = secret.charAt(i);

            if (guestLetter == character) {
                characters.set(i, guestLetter);
            }
        }
    }

    /**
     * Prints the guest word on a line.
     * Any characters that are not guest yet wil be a underscore.
     * Any guest letters wil be printed.  
     */
    void printWord(List<Character> characters) {
        characters.forEach(character -> System.out.print(character));
        System.out.print("\n");
    }
}
