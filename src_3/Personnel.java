package Assignment_3;

import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.Random;

/**
 *
 * @author Ine Vanhaverbeke
 */
public class Personnel {

    private Random generator;

    public void initialize_random_number_generator(long... seed) {
        if (seed.length > 0) {
            generator = new Random(seed[0]);    // fixed random numbers every time you run with the same seed
        } else {
            generator = new Random();          // different randoms numbers every time you run
        }
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }
    // Definition of static parameters to define memory requirements
    final int maxC = 20000;
    final int max_run = 10;
    final int max_S = 10;
    final int max_AS = 5;
    final int max_nr_stations = 10;
    final int max_nr_job_types = 10;

    //COUNTERS
    int i1, i2, i6, run, i3;                                // define counters (integers)

    double j1, j2, j3, l1;                                  // define float numbers
    int K, s0, L;
    double[] avg = new double[30];
    String naam;
    String sproblem;

    double left_var_Triangular, right_var_Triangular; // inputs for triangular distribution

    //INPUT DATA RELATED TO RADIOLOGY DPT
    int nr_stations; //Number of workstations
    int[] nr_servers = new int[max_nr_stations]; //Number of servers per workstation

    //INPUT DATA RELATED TO SYSTEM JOBS
    int nr_job_types; // Number of job types
    int[] nr_workstations_job = new int[max_nr_job_types]; // Number of workstations per job type
    int[][] route = new int[max_nr_job_types][max_nr_stations]; // Route to follow for each job type
    int[] current_station = new int[maxC]; // Matrix that denotes the current station of a scan (sequence number)
    // GENERAL DISCRETE EVENT SIMULATION PARAMETERS

    double t;                              // Simulation time
    int N;                                 // Max number of scans (Stop criterion)

    // VARIABLES RELATED TO system SCANS
    int n;                                 // Number of scans in the system
    int[] n_ws = new int[max_nr_stations]; // Number of scans at a particular workstation

    double[] mean_customers_system = new double[max_run];
    double[] tot_n = new double[max_run];                              // Number of customers in the system over time
    double[][] tot_n_ws = new double[max_run][max_nr_stations];      // Number of customers in a workstation over time

    // PARAMETERS RELATED TO ARRIVAL OF SCANS
    int nr_arrival_sources;                                 // Number of arrival sources
    double[] lambda = new double[max_AS];                    // Arrival rate
    double[][] cum_distr_scans = new double[max_AS][max_nr_job_types];   // Cumulative(!) Distribution of job types per source
    int n_a;                                                // Number of scans arrived to the system
    int[] n_a_ws = new int[max_nr_stations];                 // Number of scans arrived to a particular workstation
    double[] t_a = new double[max_AS];                       // Time of next arrival for each source
    double first_ta;                                        // First arrival time over all sources;
    int index_arr;                                          // Source of next arrival;
    double t_lambda;
    double[][] tot_lambda = new double[max_run][max_AS];
    int[] scan_type = new int[maxC];                        // Type of scan arriving
    double[][] time_arrival = new double[max_run][maxC];    // Time of arrival of the scan to the ancillary services
    double[][][] time_arrival_ws = new double[max_run][max_nr_stations][maxC];   // Time of arrival of a particular scan to a particular workstation
    double[] mean_interarrival_time = new double[max_run];

    // PARAMETERS RELATED TO Processing OF SCANS
    double[][] mu = new double[max_nr_stations][max_nr_job_types];   // Processing rate per station and job type
    double[][] var = new double[max_nr_stations][max_nr_job_types];   // Variance per station and job type
    double[][] sigma = new double[max_nr_stations][max_nr_job_types];   // Standard deviation per station and job type
    int n_d;                                        // Number of scans handled
    int[] n_d_ws = new int[max_nr_stations];                    // Number of scans handled in a particular workstation
    double[][] t_d = new double[max_nr_stations][max_S];             // Time of next departure for each server in each workstation
    double first_td;                                   // First departure time over all sources
    int index_dep_station;                          // Station with first departure
    int index_dep_server;                           // Server with first departure
    double[] mean_service_time = new double[max_run];              // Calculated average service time
    double t_mu;                                    // Generated service time
    double[] tot_mu = new double[max_run];                         // Total service time generated
    double[][][] time_service = new double[max_run][max_nr_stations][maxC];   // Service time per customer and workstation
    int[][] current_cust = new int[max_nr_stations][max_S];       // Customer handles by a particular workstation and server
    int[][] list_scan = new int[max_nr_stations][maxC];          // list of customers processed at a particular workstation on a particular point in time

    // PARAMETERS RELATED TO waiting OF SCANS
    double[] mean_waiting_time = new double[max_run];
    double[] waiting_time = new double[max_run];
    double[][][] waiting_time_job_ws = new double[max_run][max_nr_stations][maxC];

    double[] mean_customers_queue = new double[max_run];
    double[] tot_n_queue = new double[max_run];
    double[][] tot_n_queue_ws = new double[max_run][max_nr_stations];

    // VARIABLES RELATED TO Processed SCANS
    double[][] time_departure = new double[max_run][maxC];
    double[][][] time_departure_ws = new double[max_run][max_nr_stations][maxC];

    double[][] time_system = new double[max_run][maxC];
    double[][][] time_system_job_ws = new double[max_run][max_nr_stations][maxC];

    int[] order_out = new int[maxC];
    double[] mean_system_time = new double[max_run];

    // OTHER PARAMETERS
    int infinity;                                       // value for infinity
    double[][][] idle = new double[max_run][max_nr_stations][max_S]; // Idle time for server s at workstation w
    double[][] rho_ws_s = new double[max_nr_stations][max_S]; // utilisation of server s at workstation w
    double[] rho_ws = new double[max_nr_stations];             //utilisation of workstation w
    double rho;                                         // overall utilisation

    // VARIABLES RELATED TO CLOCK TIME
    double elapsed_time, time_subproblem;
    long start_time, inter_time, project_start_time; //Time measurements to compute run times

    public void procedure() {
        int L = 1;
        for (int i3 = 0; i3 < L; i3++) { // Count number of runs

            int K = 1;
            for (int run = 0; run < K; run++) { // Count number of replications per run
                init();
                radiologySystem();
                output();
            }
        }
        try {
            FileWriter RadiologyAvgRunsFile = new FileWriter("Radiology_avg_runs.txt");
            // PRINT HERE OUTPUT of Multiple runs
            // RadiologyAvgRunsFile.write("This is an example of how you write output to the file");

            RadiologyAvgRunsFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred." + e.toString());
        }
    }

    // This functions puts all variables to 0
    public void initializeFunctions() {

        // PUT ALL VARIABLES TO ZERO
        // INPUT DATA RELATED TO RADIOLOGY DPT
        int nr_stations = 0;

        for (int i1 = 0; i1 < max_nr_stations; i1++) {
            nr_servers[i1] = 0;
        }
        // INPUT DATA RELATED TO SYSTEM JOBS
        nr_job_types = 0;

        for (int i2 = 0; i2 < max_nr_job_types; i2++) {
            nr_workstations_job[i2] = 0;
            for (int i1 = 0; i1 < max_nr_stations; i1++) {
                route[i2][i1] = 0;
            }
        }
        for (int i1 = 0; i1 < maxC; i1++) {
            current_station[i1] = 0;
        }

        // GENERAL DISCRETE EVENT SIMULATION PARAMETERS
        t = 0;
        N = 0;

        // VARIABLES RELATED TO system SCANS
        n = 0;
        for (int i1 = 0; i1 < max_nr_stations; i1++) {
            n_ws[i1] = 0;
        }

        for (int i2 = 0; i2 < max_run; i2++) {
            mean_customers_system[i2] = 0;
            tot_n[i2] = 0;
            for (int i1 = 0; i1 < max_nr_stations; i1++) {
                tot_n_ws[i2][i1] = 0;
            }
        }

        // PARAMETERS RELATED TO ARRIVAL OF SCANS
        nr_arrival_sources = 0;
        n_a = 0;
        first_ta = 0;
        index_arr = 0;
        t_lambda = 0;

        for (int i1 = 0; i1 < max_nr_stations; i1++) {
            n_a_ws[i1] = 0;
        }

        for (int i2 = 0; i2 < max_run; i2++) {
            mean_interarrival_time[i2] = 0;
            for (int i6 = 0; i6 < max_AS; i6++) {
                tot_lambda[i2][i6] = 0;
            }

            for (int i3 = 0; i3 < maxC; i3++) {
                time_arrival[i2][i3] = 0;
                for (int i1 = 0; i1 < max_nr_stations; i1++) {
                    time_arrival_ws[i2][i1][i3] = 0;
                }
            }
        }

        for (int i3 = 0; i3 < maxC; i3++) {
            scan_type[i3] = 0;
        }

        for (int i6 = 0; i6 < max_AS; i6++) {
            t_a[i6] = 0;
            lambda[i6] = 0;
            for (int i3 = 0; i3 < max_nr_job_types; i3++) {
                cum_distr_scans[i6][i3] = 0;
            }
        }

        // PARAMETERS RELATED TO PROCESSING OF SCANS
        for (int i1 = 0; i1 < max_nr_stations; i1++) {
            n_d_ws[i1] = 0;
            for (int i3 = 0; i3 < max_nr_job_types; i3++) {
                mu[i1][i3] = 0;
                var[i1][i3] = 0;
                sigma[i1][i3] = 0;
            }
            for (int i6 = 0; i6 < max_S; i6++) {
                t_d[i1][i6] = 0;
                current_cust[i1][i6] = 0;
            }
            for (int i6 = 0; i6 < maxC; i6++) {
                list_scan[i1][i6] = -1;
            }
        }

        n_d = 0;
        first_td = 0;
        index_dep_station = 0;
        index_dep_server = 0;
        t_mu = 0;

        for (int i2 = 0; i2 < max_run; i2++) {
            mean_service_time[i2] = 0;
            tot_mu[i2] = 0;
            for (int i1 = 0; i1 < max_nr_stations; i1++) {
                for (int i3 = 0; i3 < maxC; i3++) {
                    time_service[i2][i1][i3] = 0;
                }
            }
        }
        // PARAMETERS RELATED TO WAITING OF SCANS

        for (int i2 = 0; i2 < max_run; i2++) {
            mean_waiting_time[i2] = 0;
            waiting_time[i2] = 0;
            mean_customers_queue[i2] = 0;
            tot_n_queue[i2] = 0;
            for (int i1 = 0; i1 < max_nr_stations; i1++) {
                tot_n_queue_ws[i2][i1] = 0;
                for (int i3 = 0; i3 < maxC; i3++) {
                    waiting_time_job_ws[i2][i1][i3] = 0;
                }
            }
        }

        // VARIABLES RELATED TO PROCESSED SCANS
        for (int i2 = 0; i2 < max_run; i2++) {
            mean_system_time[i2] = 0;
            for (int i3 = 0; i3 < maxC; i3++) {
                time_departure[i2][i3] = 0;
                time_system[i2][i3] = 0;
                for (int i1 = 0; i1 < max_nr_stations; i1++) {
                    time_departure_ws[i2][i1][i3] = 0;
                    time_system_job_ws[i2][i1][i3] = 0;
                }
            }
        }

        for (int i3 = 0; i3 < maxC; i3++) {
            order_out[i3] = 0;
        }
        /* OTHER PARAMETERS */
        infinity = 0;

        for (i2 = 0; i2 < max_run; i2++) {
            for (i3 = 0; i3 < max_nr_stations; i3++) {

                for (i1 = 0; i1 < max_S; i1++) {
                    idle[i2][i3][i1] = 0;
                }
            }
        }
        rho = 0;
        for (i3 = 0; i3 < max_nr_stations; i3++) {
            rho_ws[i3] = 0;

            for (i1 = 0; i1 < max_S; i1++) {
                rho_ws_s[i3][i1] = 0;
            }
        }

    }

    // Initialisation function
    // This functions assigns meaningful values to all variables
    public void init() {

        // PUT ALL VARIABLES TO ZERO
        initializeFunctions();

        // SET INPUT VALUES
        // seed is already set in main.java
        // initialize_random_number_generator(0);
        // ((i3+1)*K-run);
        // Ensure you each time use a different seed to get IID replications
        // INPUT RADIOLOGY DPT
        nr_stations = 5;                // Number of workstations

        nr_servers[0] = 3;              // Input number of servers per workstation
        nr_servers[1] = 2;
        nr_servers[2] = 4;
        nr_servers[3] = 3;
        nr_servers[4] = 1;

        // INPUT JOB TYPES
        nr_job_types = 4;               // Number of scans types
        nr_workstations_job[0] = 4;     // Number of workstations per job type
        nr_workstations_job[1] = 3;
        nr_workstations_job[2] = 5;
        nr_workstations_job[3] = 3;

        route[0][0] = 2;                // Route to follow for each job type (JOB = 1)
        route[0][1] = 0;                // Note: Workstation i in assignment corresponds to workstation i-1 in code as here we start counting from 0
        route[0][2] = 1;
        route[0][3] = 4;

        route[1][0] = 3;                // Route to follow for each job type (JOB = 2)
        route[1][1] = 0;
        route[1][2] = 2;

        route[2][0] = 1;                // Route to follow for each job type (JOB = 3)
        route[2][1] = 4;
        route[2][2] = 0;
        route[2][3] = 3;
        route[2][4] = 2;

        route[3][0] = 1;                            // Route to follow for each job type (JOB = 4)
        route[3][1] = 3;
        route[3][2] = 4;

        // INPUT ARRIVAL PROCESS
        nr_arrival_sources = 2;                     // Number of arrival sources
        // Arrival from radiology department
        lambda[0] = 1 / 0.25;                       // Input arrival rate = 1/mean interarrival time
        cum_distr_scans[0][0] = 0.2;                // Distribution scans (SOURCE = 1) - Cumulative distribution
        cum_distr_scans[0][1] = 0.4;
        cum_distr_scans[0][2] = 0.5;
        cum_distr_scans[0][3] = 1;

        // Arrival from other services
        lambda[1] = 1 / 1;                          // Input arrival rate = 1/mean interarrival time
        cum_distr_scans[1][0] = 0;                  // Distribution scans (SOURCE = 2) - Cumulative distribution
        cum_distr_scans[1][1] = 0.4;
        cum_distr_scans[1][2] = 0.4;
        cum_distr_scans[1][3] = 1;

        /* INPUT SERVICE PROCESS */
        mu[0][0] = 12;                               //Processing time per ws and job type (WS1, J1)
        mu[0][1] = 15;
        mu[0][2] = 15;
        mu[0][3] = 0;
        mu[1][0] = 20;                               //Processing time per ws and job type (WS2, J1)
        mu[1][1] = 0;
        mu[1][2] = 21;
        mu[1][3] = 18;
        mu[2][0] = 16;                               //Processing time per ws and job type (WS3, J1)
        mu[2][1] = 14;
        mu[2][2] = 10;
        mu[2][3] = 0;
        mu[3][0] = 0;                               //Processing time per ws and job type (WS4, J1)
        mu[3][1] = 20;
        mu[3][2] = 24;
        mu[3][3] = 13;
        mu[4][0] = 25;                               //Processing time per ws and job type (WS5, J1)
        mu[4][1] = 0;
        mu[4][2] = 20;
        mu[4][3] = 25;
        var[0][0] = 2;                               //Processing variance per ws and job type (WS1, J1)
        var[0][1] = 2;
        var[0][2] = 3;
        var[0][3] = 0;
        var[1][0] = 4;                               //Processing variance per ws and job type (WS2, J1)
        var[1][1] = 0;
        var[1][2] = 3;
        var[1][3] = 3;
        var[2][0] = 4;                               //Processing variance per ws and job type (WS3, J1)
        var[2][1] = 2;
        var[2][2] = 1;
        var[2][3] = 0;
        var[3][0] = 0;                               //Processing variance per ws and job type (WS4, J1)
        var[3][1] = 3;
        var[3][2] = 4;
        var[3][3] = 2;
        var[4][0] = 5;                               //Processing variance per ws and job type (WS5, J1)
        var[4][1] = 0;
        var[4][2] = 3;
        var[4][3] = 5;
        sigma[0][0] = sqrt(var[0][0]);               //Processing stdev per ws and job type (WS1, J1)
        sigma[0][1] = sqrt(var[0][1]);
        sigma[0][2] = sqrt(var[0][2]);
        sigma[0][3] = sqrt(var[0][3]);
        sigma[1][0] = sqrt(var[1][0]);               //Processing stdev per ws and job type (WS2, J1)
        sigma[1][1] = sqrt(var[1][1]);
        sigma[1][2] = sqrt(var[1][2]);
        sigma[1][3] = sqrt(var[1][3]);
        sigma[2][0] = sqrt(var[2][0]);               //Processing stdev per ws and job type (WS3, J1)
        sigma[2][1] = sqrt(var[2][1]);
        sigma[2][2] = sqrt(var[2][2]);
        sigma[2][3] = sqrt(var[2][3]);
        sigma[3][0] = sqrt(var[3][0]);               //Processing stdev per ws and job type (WS4, J1)
        sigma[3][1] = sqrt(var[3][1]);
        sigma[3][2] = sqrt(var[3][2]);
        sigma[3][3] = sqrt(var[3][3]);
        sigma[4][0] = sqrt(var[4][0]);               //Processing stdev per ws and job type (WS5, J1)
        sigma[4][1] = sqrt(var[4][1]);
        sigma[4][2] = sqrt(var[4][2]);
        sigma[4][3] = sqrt(var[4][3]);

        // STOP CRITERION
        N = 1000;                                   // Number of scans

        infinity = 999999999;


        /* 3. INITIALISE SYSTEM */
        /**
         * *********************
         */

        /* DETERMINE FIRST ARRIVAL + FIRST DEPARTURE */
        for (i2 = 0; i2 < max_nr_stations; i2++) {
            for (i1 = 0; i1 < max_S; i1++) {
                t_d[i2][i1] = infinity;             // Put all departure times for all servers to +infty (system is idle and no departures have been scheduled yet
            }
        }

        for (i1 = 0; i1 < nr_arrival_sources; i1++) {
            t_a[i1] = exponentialDistribution(lambda[i1]);  // Generate first arrival for all sources
        }
        index_arr = 0;                                      // Initialise arrival source indicator
        first_ta = infinity;
        for (i1 = 0; i1 < nr_arrival_sources; i1++) {       // Get next arrival = Smallest arrival time
            //System.out.print(t_a[i1]+"\t");
            if (first_ta > t_a[i1]) {
                first_ta = t_a[i1];
                index_arr = i1;
            }

        }
        System.out.print("\n");

        tot_lambda[run][index_arr] = first_ta;              // Add interarrival time to the counter for calculating the average interarrival time

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void radiologySystem() {
        while (n_d < N) {                         // Perform simulation until prespecified number of customers have departed
            first_td = infinity;                             //Identify next departure event = The customer that leaves the soonest (=minimum departure time)
            for (i2 = 0; i2 < nr_stations; i2++) {
                for (i1 = 0; i1 < nr_servers[i2]; i1++) // Loop over all servers and workstations
                {
                    if (first_td > t_d[i2][i1]) {
                        first_td = t_d[i2][i1];
                        index_dep_server = i1;
                        index_dep_station = i2;

                    }
                }
            }

            //System.out.print("First departure time " + first_td + "\n");// You may want to print the first departure time
            index_arr = 0;                                                // Identify next arrival event
            first_ta = infinity;
            for (i1 = 0; i1 < nr_arrival_sources; i1++) {
                if (first_ta > t_a[i1]) {
                    first_ta = t_a[i1];
                    index_arr = i1;
                }
            }

            if (t_a[index_arr] < t_d[index_dep_station][index_dep_server]) // Is first event an arrival or a departure?
            {
                arrivalEvent();                                            // Arrival event
            } else // Departure event
            {
                /* UPDATE STATISTICS FOR PERIOD [t,t_d]*/
                tot_n[run] += (t_d[index_dep_station][index_dep_server] - t) * n;   // Update statistics: continuous time statistics to count the average number of jobs in process for specific workstation and server
                for (i2 = 0; i2 < nr_stations; i2++) {
                    tot_n_ws[run][i2] += (t_d[index_dep_station][index_dep_server] - t) * n_ws[i2]; // Update statistics: continuous time statistics to count the average number of jobs in process for specific workstation
                }

                for (i2 = 0; i2 < nr_stations; i2++) {
                    if (n_ws[i2] >= nr_servers[i2]) {
                        tot_n_queue_ws[run][i2] += (t_d[index_dep_station][index_dep_server] - t) * (n_ws[i2] - nr_servers[i2]);
                        tot_n_queue[run] += (t_d[index_dep_station][index_dep_server] - t) * (n_ws[i2] - nr_servers[i2]);
                    }
                }// Update statistics: continuous time statistics to count the average number of jobs in queue for specific workstation and server

                // Calculate idle time servers
                for (i2 = 0; i2 < nr_stations; i2++) {
                    for (i1 = 0; i1 < nr_servers[i2]; i1++) {
                        if (t_d[i2][i1] == infinity) {
                            idle[run][i2][i1] += (t_d[index_dep_station][index_dep_server] - t);
                        }
                    }
                }   // If no departure is planned, the workstation/server is idle (continuous time statistic)

                /* Increment simulation time t = t_d*/
                t = t_d[index_dep_station][index_dep_server];

                time_departure_ws[run][index_dep_station][current_cust[index_dep_station][index_dep_server]] = t;   // Store departure time customer n_d in source center
                time_system_job_ws[run][index_dep_station][current_cust[index_dep_station][index_dep_server]] += (t - time_arrival_ws[run][index_dep_station][current_cust[index_dep_station][index_dep_server]]);                                        // Calculate system time in source center of served customer

                n_d_ws[index_dep_station]++;    // Count number of departures at a particular station
                n_ws[index_dep_station]--;      // Count number of scans at the station

                current_station[current_cust[index_dep_station][index_dep_server]]++;  //Selected scan should go to next station

                for (i3 = 0; i3 < n_ws[index_dep_station] + 1; i3++) // Selected customer is identified from scan list current station
                {
                    if (list_scan[index_dep_station][i3] == current_cust[index_dep_station][index_dep_server]) {
                        break;
                    }
                }

                list_scan[index_dep_station][i3] = -1;      // Selected customer is removed from scan list current station

                for (i2 = i3; i2 < n_ws[index_dep_station]; i2++) // Scan list should be updated
                {
                    list_scan[index_dep_station][i2] = list_scan[index_dep_station][i2 + 1];  // Move all customers one position up
                }
                list_scan[index_dep_station][i2] = -1;
                System.out.printf("Departure event\tJob %4d\tWorkstation %d\tServer %d\tTime %f\tCurrent station %d of %d\tReal %d\n",
                        current_cust[index_dep_station][index_dep_server],
                        index_dep_station,
                        index_dep_server,
                        t,
                        current_station[current_cust[index_dep_station][index_dep_server]],
                        nr_workstations_job[scan_type[current_cust[index_dep_station][index_dep_server]]],
                        route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]] - 1]);

                // Does the finished job has more tasks to be done on if route?
                // Evaluate number of tasks done versus required number
                if (current_station[current_cust[index_dep_station][index_dep_server]] < nr_workstations_job[scan_type[current_cust[index_dep_station][index_dep_server]]]) { // For this customer there are still jobs to do
                    n_ws[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]]++;   // Count number of scans at a particular workstation, defined by the route (scan type and current                                                            station)
                    n_a_ws[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]]++;// Count the number of arrivals at a particular workstation
                    time_arrival_ws[run][route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][current_cust[index_dep_station][index_dep_server]] = t;// Set the time of arrival at a particular workstation of a particular job

                    if (n_ws[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]] <= nr_servers[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]]) // Arrival to a system where one of the servers is idle?
                    {
                        for (i1 = 0; i1 < nr_servers[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]]; i1++) // Identify the system that was idle
                        {
                            if (t_d[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i1] == infinity) {
                                break;
                            }
                        }

                        for (i2 = 0; i2 < nr_servers[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]]; i2++) {
                            if (list_scan[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i2] == -1) {
                                list_scan[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i2] = current_cust[index_dep_station][index_dep_server];
                            }
                        }// change scan list

                        //list_scan[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i1] = current_cust[index_dep_station][index_dep_server];// CHANGE
                        t_mu = normalDistribution(mu[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][scan_type[current_cust[index_dep_station][index_dep_server]]], sigma[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][scan_type[current_cust[index_dep_station][index_dep_server]]]);                        // Derive service time of the system that was idle determine the departure time for the newly arrived scan
                        t_mu = t_mu / 60;             // Change service time to an hourly basis
                        time_service[run][route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][current_cust[index_dep_station][index_dep_server]] = t_mu;       // Store service time

                        waiting_time_job_ws[run][route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][current_cust[index_dep_station][index_dep_server]] = 0;
                        /* Tally a delay of 0 for this job*/
                        /* Make a machine for this workstation busy */
                        t_d[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i1] = t + t_mu;
                        tot_mu[run] += t_mu;// Calculate departure time
                        current_cust[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][i1] = current_cust[index_dep_station][index_dep_server];                                     // Track the current customer being served
                        t_d[index_dep_station][index_dep_server] = infinity;                                                // Set departure time of current station to infty
                    } else {
                        list_scan[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][n_ws[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]] - 1] = current_cust[index_dep_station][index_dep_server];// Add new scan to the last place n the scan list

                    }

                } else {
                    order_out[n_d] = current_cust[index_dep_station][index_dep_server];                             // Store the order in which scans leave the system

                    n_d++;                                                                                          // Increment the number of orders that have left the system

                    n--;                                                                                            // Decrease the number of items in the system
                    time_departure[run][current_cust[index_dep_station][index_dep_server]] = t;                     // Set time of departure
                    time_system[run][current_cust[index_dep_station][index_dep_server]] = t - time_arrival[run][current_cust[index_dep_station][index_dep_server]];// Calculate system time
                    System.out.printf("Job %4d has left\t\t\t\t\t\tTime %f\n", current_cust[index_dep_station][index_dep_server], t);
                }

                // Determine next scan in departure station or idle status
                if (n_ws[index_dep_station] >= nr_servers[index_dep_station]) {   // Compute next job by calculating the waiting time: Select the job with the largest waiting time
                    // Job with the largest waiting time = Next job on the scan list of workstation
                    current_cust[index_dep_station][index_dep_server] = list_scan[index_dep_station][nr_servers[index_dep_station] - 1];

                    // Make idle server busy with next job and generate next departure event
                    t_mu = normalDistribution(mu[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][scan_type[current_cust[index_dep_station][index_dep_server]]], sigma[route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][scan_type[current_cust[index_dep_station][index_dep_server]]]);                        // For the system that was idle determine the departure time for the newly arrived scan
                    t_mu = t_mu / 60;
                    time_service[run][route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][current_cust[index_dep_station][index_dep_server]] = t_mu;           // Store service time
                    /* Calculate the delay for this job and workstation*/
                    waiting_time_job_ws[run][route[scan_type[current_cust[index_dep_station][index_dep_server]]][current_station[current_cust[index_dep_station][index_dep_server]]]][current_cust[index_dep_station][index_dep_server]] = t - time_arrival_ws[run][index_dep_station][current_cust[index_dep_station][index_dep_server]];

                    /* Make a machine for this workstation busy */
                    t_d[index_dep_station][index_dep_server] = t + t_mu;
                    tot_mu[run] += t_mu;

                } else {   // Make server in this station idle
                    t_d[index_dep_station][index_dep_server] = infinity;

                }
            }
        }
    }

    public void arrivalEvent() {
        /* UPDATE STATISTICS FOR PERIOD [t,t_a]*/
        tot_n[run] += (t_a[index_arr] - t) * n;                                 // Number of scans in system (Continuous time statistic)
        for (i2 = 0; i2 < nr_stations; i2++) {
            tot_n_ws[run][i2] += (t_a[index_arr] - t) * n_ws[i2];               // Number of scans in workstation (Continuous time statistic)
        }
        for (i2 = 0; i2 < nr_stations; i2++) {
            if (n_ws[i2] >= nr_servers[i2]) // Number of scans in queues
            {
                tot_n_queue_ws[run][i2] += (t_a[index_arr] - t) * (n_ws[i2] - nr_servers[i2]);
                tot_n_queue[run] += (t_a[index_arr] - t) * (n_ws[i2] - nr_servers[i2]);
            }
        }

        // Calculate idle time servers
        for (i2 = 0; i2 < nr_stations; i2++) {
            for (i1 = 0; i1 < nr_servers[i2]; i1++) {
                if (t_d[i2][i1] == infinity) {
                    idle[run][i2][i1] += (t_a[index_arr] - t);
                }
            }
        }


        /* Increment simulation time */
        t = t_a[index_arr];


        /* Generate the job type*/
        j1 = generator.nextDouble();                                            // Use a random number to determine the scan type
        scan_type[n_a] = 0;
        while (j1 > cum_distr_scans[index_arr][scan_type[n_a]]) // Inversion method for discrete distribution
        {
            scan_type[n_a]++;                                                   // Determine scan type of arrival
        }
        /* Set task = 1 for this job */
        /* Determine the station for this job*/
        current_station[n_a] = 0;                                               // New arrival needs to go to first station

        for (i1 = 0; i1 < n_ws[route[scan_type[n_a]][current_station[n_a]]]; i1++) {
            if (list_scan[route[scan_type[n_a]][current_station[n_a]]][i1] == -1) {
                break;
            }

        }
        list_scan[route[scan_type[n_a]][current_station[n_a]]][i1] = n_a;       //add new arrival to the scan list
        time_arrival_ws[run][route[scan_type[n_a]][current_station[n_a]]][n_a] = t;// Set the time of arrival at a particular workstation of a particular job
        if (n_a <= N) {
            time_arrival[run][n_a] = t;                                         // store time of arrival
        }

        n_a_ws[route[scan_type[n_a]][current_station[n_a]]]++;                  // Count the number of arrivals at a particular workstation

        n_ws[route[scan_type[n_a]][current_station[n_a]]]++;                    // Count number of scans at a particular workstation, defined by the route (scan type and current station)

        /* Are all machine busy on this station?*/
        if (n_ws[route[scan_type[n_a]][current_station[n_a]]] <= nr_servers[route[scan_type[n_a]][current_station[n_a]]]) // Arrival to a system where one of the servers was idle
        {
            for (i1 = 0; i1 < nr_servers[route[scan_type[n_a]][current_station[n_a]]]; i1++) // Identify the server that was idle
            {
                if (t_d[route[scan_type[n_a]][current_station[n_a]]][i1] == infinity) {
                    break;
                }
            }
            t_mu = normalDistribution(mu[route[scan_type[n_a]][current_station[n_a]]][scan_type[n_a]], sigma[route[scan_type[n_a]][current_station[n_a]]][scan_type[n_a]]);                                                       // For the system that was idle determine the departure time for the newly arrived scan
            t_mu = t_mu / 60;
            time_service[run][route[scan_type[n_a]][current_station[n_a]]][n_a] = t_mu;
            /* Tally a delay of 0 for this job*/
            waiting_time_job_ws[run][route[scan_type[n_a]][current_station[n_a]]][n_a] = 0;
            /* Make a machine for this workstation busy */
            t_d[route[scan_type[n_a]][current_station[n_a]]][i1] = t + t_mu;
            tot_mu[run] += t_mu;
            current_cust[route[scan_type[n_a]][current_station[n_a]]][i1] = n_a;        // Track the current customer being served
        }
        System.out.printf("Arrival event\tJob %4d\tSource %d\t\t\tTime %f\tScan Type %d\n", n_a, index_arr, t, scan_type[n_a]);

        n++;                    // Increase number of jobs in system
        n_a++;                  // Increase the number of arrivals

        // Schedule next arrival
        t_lambda = exponentialDistribution(lambda[index_arr]);
        t_a[index_arr] = t + t_lambda;
        tot_lambda[run][index_arr] += t_lambda;

    }

    public void departure_event() {

    }

    public void output() {
        try {
            FileWriter outputFile = new FileWriter("Output_Radiology.txt");

            for (i1 = 0; i1 < nr_stations; i1++) {
                // PRINT Utilisation
                outputFile.write(String.format("Utilisation servers Station WS %d:\t", i1));
                for (i2 = 0; i2 < nr_servers[i1]; i2++) {
                    j1 = (idle[run][i1][i2] / t);
                    rho_ws_s[i1][i2] = 1 - j1;
                    outputFile.write(String.format("%f\t", rho_ws_s[i1][i2]));
                }
                outputFile.write("\n");
            }

            outputFile.write("\n");

            for (i1 = 0; i1 < nr_stations; i1++) {
                outputFile.write(String.format("Avg utilisation Station WS %d:\t", i1));
                for (i2 = 0; i2 < nr_servers[i1]; i2++) {
                    rho_ws[i1] += rho_ws_s[i1][i2];
                }
                rho_ws[i1] = rho_ws[i1] / nr_servers[i1];
                outputFile.write(String.format("%f\n", rho_ws[i1]));
            }

            outputFile.write("\n");

            rho = 0.0;
            for (i1 = 0; i1 < nr_stations; i1++) {
                rho += rho_ws[i1];
            }
            rho /= nr_stations;
            outputFile.write(String.format("Overall avg utilisation: %f\n", rho));

            outputFile.write("\n");

            for (i1 = 0; i1 < N; i1++) {
                // PRINT system time = cycle time (observations and running average)
                mean_system_time[run] += time_system[run][order_out[i1]];
            }
            outputFile.write("Cycle time\n\n");
            j1 = mean_system_time[run] / N;
            outputFile.write(String.format("Avg cycle time: %f\n\n", j1));

            mean_system_time[run] = 0;
            outputFile.write("Number\tObservation\tRunning Average\n");
            for (i1 = 0; i1 < N; i1++) {
                mean_system_time[run] += time_system[run][order_out[i1]];
                j1 = mean_system_time[run] / (i1 + 1);
                outputFile.write(String.format("%d\t%f\t%f\n", i1, time_system[run][order_out[i1]], j1));
            }

            outputFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred:" + e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 6 different distribution functions
    public double exponentialDistribution(double lambda) {
        j1 = generator.nextDouble();                                     // Random number
        if (j1 == 0) {                                                          // Random number should lie in interval ]0,1[
            j1 += 0.0001;
        }
        j2 = -Math.log(j1) / lambda;                                   // Inversion method
        return j2;
    }

    public int poissonDistribution(double lambda) {
        int p;
        j1 = generator.nextDouble();                                     // Random number
        double k = 0;
        double L = Math.exp(-lambda);
        j3 = 0;
        do {                                                                     // Inversion method
            j2 = L * Math.pow(lambda, k);
            p = 1;
            for (i6 = 0; i6 <= k; i6++) {
                if (i6 == 0) {
                    p = 1;
                } else {
                    p *= i6;
                }
            }
            j2 /= p;
            j3 += j2;
            k++;
        } while (j1 >= j3);
        return (int) (k - 1);
    }

    public int normalDistribution(double mean, double stdev) {
        // TO MODEL BASED ON CUMULATIVE DENSITY FUNCTION OF NORMAL DISTRIBUTION
        // BASED ON BOOK OF SHELDON ROSS, Simulation, The polar method, p80.

        double v1, v2, t;
        int x;
        do {
            v1 = generator.nextDouble() * 1.998 - 1;
            v2 = generator.nextDouble() * 1.998 - 1;
            t = v1 * v1 + v2 * v2;
        } while (t >= 1 || t == 0);
        double multiplier = Math.sqrt(-2 * Math.log(t) / t);
        x = (int) (v1 * multiplier * stdev + mean);
        return x;
    }

    public int bernoulliDistribution(double prob) {
        j1 = generator.nextDouble(); // random number
        if (j1 < prob) // Inversion method
        {
            return 0;
        } else {
            return 1;
        }
    }

    public int uniformDistribution(double a, double b) {
        j1 = generator.nextDouble();                                     // random number
        int x = (int) (a + (b - a) * j1);                                       // Inversion method
        return x;
    }

    public int triangularDistribution(int a, int b, int c) {

        double mean = (a + b + c) / 3;                                          //(!?! mean is never used?!)
        double stdev = (Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2) - a * b - a * c - b * c) / 18;
        stdev = Math.sqrt(stdev);                                             //(!?! stdev is never used?!)

        j1 = generator.nextDouble();
        double x = a;

        double L;
        do {
            if (x <= b) {
                L = Math.pow((x - a), 2) / ((c - a) * (b - a));
            } else {
                L = 1 - (Math.pow(c - x, 2) / ((c - a) * (c - b)));
            }
            x++;
        } while (j1 >= L);

        return (int) (x - 1);
    }

}