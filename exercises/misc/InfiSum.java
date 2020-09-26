package exercises.misc;

public class InfiSum {

    private InfiSum[] sums;
    private int[] numbers;

    InfiSum(int[] numbers) {
        this.numbers = numbers;
    }

    InfiSum(InfiSum[] sums) {
        this.sums = sums;
    }

    int sumNumbers() {
        int sum = 0;

        for (int i = 0; i < this.numbers.length; i++) {
            sum += this.numbers[i];
        }

        return sum;
    }

    int sum() {
        if(this.sums == null) {
            return sumNumbers();
        }

        int sum = 0;

        for (int i = 0; i < this.sums.length; i++) {
            sum += this.sums[i].sum();
        }

        return sum;
    }

    public static void main(String[] args) {
        System.out.println(new InfiSum(new InfiSum[]{}).sum());
    }
}
