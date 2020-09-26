package exercises.misc;

class Sum {
    private int[] numbers;

    public Sum(int[] numbers) {
        this.numbers = numbers;
    }
    
    int sum() {
        int sum = 0;

        for (int i = 0; i < this.numbers.length; i++) {
            sum += this.numbers[i];
        }

        return sum;
    }
}