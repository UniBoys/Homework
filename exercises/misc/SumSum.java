package exercises.misc;

public class SumSum {
    private Sum[] sums;

    public SumSum(Sum[] sums) {
        this.sums = sums;
    }

    int sum() {
        int sum = 0;

        for (int i = 0; i < this.sums.length; i++) {
            sum += this.sums[i].sum();
        }

        return sum;
    }
}