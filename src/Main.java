import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    final static Random random = new Random();

    final static int ITERATIONS = 1000;
    final static double PORTION_TO_TRAIN = .75;

    final static double TRAINING_CONSTANT = .00001;

    final static double SOFT_RANGE = 5;

    final static double WEIGHT0 = random.nextDouble();
    final static double WEIGHT1 = random.nextDouble();
    final static double BIAS = -200;
    //final static double WEIGHT0 = 1;
    //final static double WEIGHT1 = 1;
    //final static double BIAS = -200;

    final static String DATA_FILE = "data.txt";


    public static void main(String[] args) {

        ArrayList<Student> students = null;

        // Read from file or generate new data
        try {
            if (args.length > 0 && args[0].equals("-generate")) {
                students = generateInputData();
                writeDataToFile(students);
            } else {
                students = readInputData();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }




        // Move some to test and some to train
        Random r = new Random();

        ArrayList<Student> dataTrain = new ArrayList<>();
        ArrayList<Student> dataTest = students;

        int totalSize = students.size();
        for(int i = 0; i < PORTION_TO_TRAIN * totalSize; i++) {
            int index = r.nextInt(students.size());
            dataTrain.add(students.remove(index));
        }

        System.out.println("Training on " + dataTrain.size() + " patterns.");
        System.out.println("Testing on " + dataTest.size() + " patterns\n");




        // Make two neurons to train using hard and soft respectively
        Neuron hardNeuron = new Neuron(2);
        hardNeuron.setWeight(0, WEIGHT0);
        hardNeuron.setWeight(1, WEIGHT1);
        hardNeuron.setBias(BIAS);

        Neuron softNeuron = new Neuron(2);
        softNeuron.setWeight(0, WEIGHT0);
        softNeuron.setWeight(1, WEIGHT1);
        softNeuron.setBias(BIAS);




        // Do the training
        for(int i = 0; i < ITERATIONS; i++) {
            trainNeuron(hardNeuron, dataTrain, dataTest, false);
            trainNeuron(softNeuron, dataTrain, dataTest, true);
        }

        System.out.println("HARD ACTIVATION FUNCTION");
        System.out.printf("w1 = %5.5f\n", hardNeuron.getWeight(0));
        System.out.printf("w2 = %5.5f\n", hardNeuron.getWeight(1));
        System.out.printf("w3 = %5.5f\n\n", hardNeuron.getBias());

        testNeuron(hardNeuron, dataTest);

        System.out.println("SOFT ACTIVATION FUNCTION");
        System.out.printf("w1 = %5.5f\n", softNeuron.getWeight(0));
        System.out.printf("w2 = %5.5f\n", softNeuron.getWeight(1));
        System.out.printf("w3 = %5.5f\n\n", softNeuron.getBias());

        testNeuron(softNeuron, dataTest);
    }










    public static ArrayList<Student> generateInputData() {

        System.out.println("Generating data...");

        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            students.add(new Male());
        }
        for (int i = 0; i < 2000; i++) {
            students.add(new Female());
        }

        return students;
    }

    public static void writeDataToFile(ArrayList<Student> students) throws IOException {

        System.out.println("Writing data to file...");

        BufferedWriter dataw = new BufferedWriter(new FileWriter(DATA_FILE));
        for (int i = 0; i < 4000; i++) {
            dataw.write(students.get(i).getHeight() + "," + students.get(i).getWeight() + ","
                    + (students.get(i).getClass() == Male.class ? 0 : 1) + "\n");
        }
    }

    public static ArrayList<Student> readInputData() throws IOException {

        System.out.println("Reading data in from file...\n");

        ArrayList<Student> students = new ArrayList<>();
        Scanner in = new Scanner(new File(DATA_FILE));
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] data = line.split(",");

            double height = Double.parseDouble(data[0].trim());
            double weight = Double.parseDouble(data[1].trim());
            if (data[2].trim().equals("0")) {
                students.add(new Male(height, weight));
            } else {
                Female f = new Female(height, weight);
                students.add(new Female(height, weight));
            }
        }
        return students;
    }

    public static Neuron trainNeuron(Neuron neuron, ArrayList<Student> dataTrain, ArrayList<Student> dataTest, boolean useSoftActivationFunction) {

        // Train neuron
        for(int i = 0; i < dataTrain.size(); i++) {

            if(testAgainstTotalError(neuron, dataTrain, useSoftActivationFunction)) {
                System.out.println("training quit early after " + i + " iterations, after reaching minimal error.");
            }

            Student inputStudent = dataTrain.get(i);

            double output = useSoftActivationFunction ? neuron.executeSoft(inputStudent.getHeight(), inputStudent.getWeight())
                    : neuron.executeHard(inputStudent.getHeight(), inputStudent.getWeight());
            double desired = inputStudent.getClass() == Male.class ? 1 : 0;

            double delta0 = TRAINING_CONSTANT * inputStudent.getHeight() * (desired - output);
            double delta1 = TRAINING_CONSTANT * inputStudent.getWeight() * (desired - output);
            double deltaBias = TRAINING_CONSTANT * 1 * (desired - output);

            neuron.setWeight(0, neuron.getWeight(0) + delta0);
            neuron.setWeight(1, neuron.getWeight(1) + delta1);
            neuron.setBias(neuron.getBias() + deltaBias);
        }

        return neuron;
    }

    public static void testNeuron(Neuron neuron, ArrayList<Student> dataTest) {
        int maleCorrect = 0;
        int maleWrong = 0;
        int femaleCorrect = 0;
        int femaleWrong = 0;

        Class guess;

        // run tests on dataTest
        for(int i = 0; i < dataTest.size(); i++) {

            // use hard or soft activation function based on useSoftActivationFunction parameter
            Student testStudent = dataTest.get(i);

            double output = neuron.executeHard(testStudent.getHeight(), testStudent.getWeight());

            if(output >= .5) guess = Male.class;
            else guess = Female.class;

            if(guess == dataTest.get(i).getClass()) {
                if(testStudent.getClass() == Male.class) maleCorrect++;
                else femaleCorrect++;
            } else {
                if(testStudent.getClass() == Male.class) maleWrong++;
                else femaleWrong++;
            }
        }

        double accuracy = ((maleCorrect + femaleCorrect) * 1.0/ (maleCorrect + maleWrong + femaleCorrect + femaleWrong));
        double error = 1.0 - accuracy;

        System.out.println("males correct: " + maleCorrect);
        System.out.println("males wrong: " + maleWrong);
        System.out.println("females correct: " + femaleCorrect);
        System.out.println("females wrong: " + femaleWrong);

        System.out.println();

        System.out.println("error: " + error);
        System.out.println("accuracy: " + accuracy);
        System.out.println();
    }

    public static boolean testAgainstTotalError(Neuron neuron, ArrayList<Student> dataTest, boolean useSoftActivationFunction) {
        double totalError = 0;
        for(int i = 0; i < dataTest.size(); i++) {
            double output = useSoftActivationFunction ? neuron.executeSoft(dataTest.get(i).getHeight(), dataTest.get(i).getWeight())
                    : neuron.executeHard(dataTest.get(i).getHeight(), dataTest.get(i).getWeight());
            double desired = dataTest.get(i).getClass() == Male.class ? 1 : 0;
            totalError += Math.pow(desired - output, 2);
            if(totalError > dataTest.size() * .00001) return false;
        }
        return true;
    }
}
