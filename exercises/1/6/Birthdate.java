package 1.6;

import static 1.6.BirthDate.ANSICode.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

class BirthDate {
    private static Scanner scanner;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private int calculateAge(final LocalDate dateOfBirth, final LocalDate currentDate) {
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    private LocalDate dateFromString(String date) {
        return LocalDate.parse(date, formatter);
    }

    private String getCurrentDate() {
        return LocalDate.now().format(formatter);
    }

    private LocalDate readDate(String message, String defaultValue) {
        LocalDate date = null;

        while (date == null) {
            System.out.format("%s %s(default is %s)%s: ", message, GRAY, defaultValue, RESET);
            String givenDate = scanner.nextLine();

            if (givenDate.isBlank()) {
                givenDate = defaultValue;
                System.out.format(
                        "%s%s%s %s(default is %s)%s: %s%s%s\n",
                        RESET, CURSOR_UP, message, GRAY, defaultValue, RESET, GRAY, defaultValue, RESET);
            }

            try {
                date = dateFromString(givenDate);
            } catch (DateTimeParseException e) {
                System.out.format(
                        "%s%s %s(default is %s)%s: %s %s(Invalid date)%s\n",
                        CURSOR_UP, message, GRAY, defaultValue, RESET, givenDate, RED, RESET);
            }
        }

        return date;
    }

    private void run() {
        LocalDate dateOfBirth = readDate("What is your birthdate? Format yyyyMMdd", "20020907");
        LocalDate currentDate = readDate("What is the current date?", getCurrentDate());

        int age = calculateAge(dateOfBirth, currentDate);

        if (age < 0) {
            System.out.println("You can't be a negative age");
            run();

            return;
        }

        System.out.format("Your age is: %s%s%s\n", age >= 18 ? GREEN : RED, age, RESET);
    }

    public static void main(final String[] args) {
        scanner = new Scanner(System.in);
        final BirthDate birthdate = new BirthDate();
        birthdate.run();
    }

    enum ANSICode {
        RED("\u001b[31m"),
        GREEN("\u001b[32m"),
        GRAY("\u001b[38;5;8m"),
        RESET("\u001b[0m"),
        CURSOR_UP("\033[1A"),
        ;

        private String code;

        private ANSICode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
