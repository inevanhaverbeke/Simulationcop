package Assignment_2;
import javax.management.relation.RelationNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
public class Procedure {

//Determine the probability of the children winning the game via a simulation program

    String fileName = "Output_Markov_Chains.txt";

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

        int K = 10000;
        int nr_fruits = 4;
        int total_success = 0;  //Initialise parameters
        double running_average;

        Random r = new Random();

        for (int k = 1; k <= K; k++) { //runnumber
            int success = 0;
            int position_raven = 0;
            int[] fruits = new int[0];
            int total_nr_fruits = 0;
            for (int i1 = 1; i1 <= nr_fruits; fruits[i1 - 1] = 3)
            do {
                int i2 = Distributions.Uniform_distribution(1, 6, r);
                if (i2 < nr_fruits) {
                    int i3 = 0;
                    if (fruits[i2] > 0)
                        fruits[i2]--;
                    else
                        do {
                            i3 = Distributions.Uniform_distribution(1, 6, r) % nr_fruits;
                        }
                        while (fruits[i3] == 0);
                    fruits[i3]--;
                } else if (i2 >= nr_fruits) {
                    position_raven++;
                    if (fruits[position_raven - 1] > 0)   // Note: Raven starts at position 0; Position 1 corresponds to tree of fruit 0
                        fruits[position_raven - 1]--;
                    total_nr_fruits = 0;
                    for (i1 = 0; i1 < nr_fruits; i1++) {
                        total_nr_fruits += fruits[i1];
                    }
                }
            }
            while ((total_nr_fruits > 0) && (position_raven < 5));
            if (total_nr_fruits == 0) {
                success = 1;
                total_success++;
            }
            running_average = total_success / k;
        }
    }
}
