package Assignment_3;
/**
 *
 * @author Ine Vanhaverbeke
 */
public class Main {

    public static void main(String[] args) {
        Personnel myPersonnel = new Personnel();            // initalisation

        myPersonnel.initialize_random_number_generator(200);  //every time you run with the same argument the same random numbers
        //myPersonnel.initialize_random_number_generator();  // every time you run different random numbers

        // seed initialisation => fixed/variable
        // ((i3+1)*K-run); //? nog voor kijken!
        // Ensure you each time use a different seed to get IID replications
        // Given the same seed, a pseudo random number generator will produce the same sequence every time.
        // So it comes down to whether you want a different sequence of pseudo random numbers each time you run, or not.

        myPersonnel.procedure();                          // call of procedure
    }
}