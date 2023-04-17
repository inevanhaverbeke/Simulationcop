package Assignment_2;

import javax.management.relation.RelationNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
public class Procedure {

//Determine the probability of the children winning the game via a simulation program

    String fileName = "/Users/elinevergauwe/Documents/Simulation Modelling and Analysis/Markov Chain Trajectories/Output_Markov_Chain_Trajectories.txt";

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
        printWriter.println("Run\tSuccess\tTotal_success\tRunning_avg"); // PRINT RESULTS IN OUTPUT FILE (HEADING ROW)


        // initialise parameters

        int K = 10000; // aantal keer dat we spel spelen
        int nr_fruits = 4;
        int total_success = 0;
        double running_average;

        for(int k = 1; k <= K; k++)
        {
            int success = 0;
            int position_raven = 0;
            int fruits[] = new int[6];
            int i1;
            int total_nr_fruits;

            for(i1 = 1; i1 <= nr_fruits; i1++ ) // het plaatsen van fruit in de bomen
            {
                fruits[i1-1] = 3;
            }
            do {
                Random r = new Random();
                int i2 = r.nextInt(6);
                if (i2 < nr_fruits) // 0,1,2,3 is een kleur
                {
                    if (fruits[i2] > 0)
                    {
                        fruits[i2]--;
                    }
                    else{
                        int i3;
                        do{
                            i3 = r.nextInt(4);
                        }
                        while (fruits[i3] == 0); // smijten met dobbelsteen tot volle boom
                        fruits[i3]--;
                    }
                }
                else {
                    position_raven++;
                    if (fruits[position_raven-1] > 0)
                    {
                            fruits[position_raven-1]--;
                    }
            }
                total_nr_fruits = 0;
                for (i1 = 0; i1 < nr_fruits; i1++)
                {
                    total_nr_fruits += fruits[i1];
                }
            } while ((total_nr_fruits > 0) && (position_raven < 5));

            if (position_raven < 5) // kinderen winnen
            {
                success = 1;
                total_success++;
            }

            running_average = (float) total_success/k;

            System.out.println("Run " + k + " Success " + success + " Total_successes " + total_success + " Running Avg. " + running_average);  // PRINT THE CURRENT NUMBER OF OFFSPRING
            printWriter.println(k + "\t" + success + "\t" + total_success + "\t" + running_average);  // PRINT RESULTS IN OUTPUT FILE

        }
        double average = (float) total_success/K;
        printWriter.println("TOTAL AVERAGE\t" + average);   // PRINT RESULTS IN OUTPUT FILE
        printWriter.close();
    }
}
