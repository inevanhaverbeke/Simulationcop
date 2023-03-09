import java.util.Random;

// In six coin tosses, what is the probability of having a different side come up with
// each throw, that is, that you never get two tails or two heads in a row?
public class Exercise_13 {
    public static void main(String[] args) {

        int K, max_tosses, total_success, count_success, nr_tosses, current_toss, previous_toss, toss; // Initialise parameters
        double running_average;
        K = 10000;
        max_tosses = 6;
        total_success = 0;

        for(int k = 1; k < K ; k++) {
            count_success = 0;
            nr_tosses = 0;
            current_toss = 0;
            toss = bernouilli_distribution             // Toss coin
            if (toss == 0) {current_toss = 0;} // If heads
            else if (toss == 1) {current_toss = 1;} // If Tails
            nr_tosses++; // Increase number of tosses
            do {
                toss = // Toss coin
                if (toss == 0) { // If heads
                    previous_toss = current_toss;
                    current_toss = 0;
                } else { // If tails
                    previous_toss = current_toss;
                    current_toss = 1;
                }
                nr_tosses++; // Increase number of tosses
                if (previous_toss != current_toss) // Heads should be followed by tails and vice versa
                    count_success++;
            }
            while (nr_tosses < max_tosses);
            if (count_success == max_tosses-1) // If experiment = success
                total_success++;
            running_average = total_success/k;
        }
    }
}
