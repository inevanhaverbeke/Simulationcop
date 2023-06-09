package Assignment1;

import javax.management.relation.RelationNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
public class Procedure {

    // In six coin tosses, what is the probability of having a different side come up with
// each throw, that is, that you never get two tails or two heads in a row?

    String fileName = "/Users/elinevergauwe/Documents/Simulation Modelling and Analysis/Monte Carlo Estimates/Output_Monte_Carlo_Estimates.txt";

    void procedure() throws IOException {
        File file = new File(fileName);
        // if file doesn't exist, then create it
        if (!file.exists()) {
            file.createNewFile(); // create the file
        } else {
            PrintWriter writer = new PrintWriter(file); // empty the file
            writer.print("");
            writer.close();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true); // APPENDS the text file with anything printed to the file during the rest of the procedure
        PrintWriter printWriter = new PrintWriter(fileWriter); // OPEN OUTPUT FILE
        printWriter.println("Run\tcount_success\ttotal_success\tRunning_avg"); // PRINT RESULTS IN OUTPUT FILE (HEADING ROW)

        // initialise parameters

        int toss;   // head or tail
        int K = 10000;  // aantal runs
        int max_tosses = 6;
        int total_success = 0; // aantal keer dat volledig experiment is gelukt
        int count_success;
        int nr_tosses;
        int current_toss;
        int previous_toss;
        double running_average;

        Random r = new Random();

            for (int k = 1; k <= K; k++) //runnumber
            {
                count_success = 0;  // coming up a different side with each throw
                nr_tosses = 0;
                current_toss = 0;

                toss = Distributions.Bernouilli_distribution(0.5, r);           // Toss coin
                if (toss == 0) {
                    current_toss = 0;
                } // If heads
                else if (toss == 1) {
                    current_toss = 1;
                } // If Tails
                nr_tosses++; // Increase number of tosses
                do {
                    toss = Distributions.Bernouilli_distribution(0.5, r);// Toss coin
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
                if (count_success == max_tosses - 1) // If experiment = success
                    total_success++;
                running_average = (float) total_success / k;

                System.out.println("Run " + k + "Count_succes" + count_success + "Total_successes " + total_success + " Running Avg. " + running_average);  // PRINT THE CURRENT NUMBER OF OFFSPRING
                printWriter.println(k + "\t" + count_success + "\t" + total_success + "\t" + running_average);  // PRINT RESULTS IN OUTPUT FILE

            }
            double average = (float) total_success/K;
            printWriter.println("TOTAL AVERAGE\t" + average);   // PRINT RESULTS IN OUTPUT FILE
            printWriter.close();
        }
    }
