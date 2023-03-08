// In six coin tosses, what is the probability of having a different side come up with
// each throw, that is, that you never get two tails or two heads in a row?

public class Exercise_13 {
    public static void main(String[] args) {
        int K, max_tosses, total_succes, count_succes, nr_tosses, current_toss; // Initialise parameters
        boolean toss
        K = 10000;
        max_tosses = 6;
        total_succes = 0;

        for(int k = 1; k < K ; k++) {
            count_succes = 0;
            nr_tosses = 0;
            current_toss = 0;
            toss =               // Toss coin
            if (toss == 0) {current_toss = 0;} // If heads
            else if (toss == 1) {current_toss = 1;} // If Tails
            nr_tosses++; // Increase number of tosses
        }
    }
}
