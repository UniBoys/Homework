import java.util.Scanner;

public class Interest {

    private Scanner scanner = new Scanner(System.in);
    private double NEGATIVE_INTEREST = 0.005;
    private double MIN_BONUS = 0.005;
    private double NORMAL_INTEREST = 2.5;
    private double BONUS_INTEREST = 0.5;

    public void run() {
        System.out.println("What is your balance?");
        double balance = scanner.nextDouble();

        if (balance < 0) {
            balance -= balance * NEGATIVE_INTEREST;
        }
        else if (balance > MIN_BONUS) {
            balance += balance * (NORMAL_INTEREST + BONUS_INTEREST);
        }
        else {
            balance += balance * NORMAL_INTEREST;
        }

        System.out.format("Your balance next year will be: %.2f", balance);
    }
    
    public static void main(String args[]) {
        new Interest().run();
    }
}
