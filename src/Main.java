import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {

    final static Random random = new Random();

    final static int ITERATIONS = 1000000;

    final static double TRAINING_CONSTANT = .00000001;
    final static double BIAS_TRAINING_MODIFIER = 50;

    final static int MAX_POLY_DEGREE = 3;

    public static void main(String[] args) {


        ArrayList<double[]> trainData = readInputTrainData();
        ArrayList<double[]> testData = readInputTestData();


        System.out.println("Training on " + trainData.size() + " patterns.");
        System.out.println("Testing on " + testData.size() + " patterns\n");


        double[] initWeights = new double[MAX_POLY_DEGREE];
        double initBias = random.nextDouble();

        for (int i = 0; i < initWeights.length; i++) {
            initWeights[i] = random.nextDouble();
        }

        for (int i = 1; i <= MAX_POLY_DEGREE; i++) {
            // Make neuron
            Neuron neuron = new Neuron(i);
            for (int j = 0; j < initWeights.length; j++) {
                neuron.setWeight(j, initWeights[j]);
            }
            neuron.setBias(initBias);

            // Do the training
            for (int j = 0; j < ITERATIONS; j++) {
                trainNeuron(neuron, trainData);
            }



            // Do print outs
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("***********************************************");
            System.out.println();

            System.out.println("Neuron weights (Degree = " + i + "):");
            for (int j = 0; j < neuron.size(); j++) {
                System.out.printf("w" + j + " = %5.5f\n", neuron.getWeight(j));
            }
            System.out.printf("bias = %5.5f\n\n", neuron.getBias());

            System.out.println();

            ArrayList<double[]> day1 = new ArrayList<>();
            ArrayList<double[]> day2 = new ArrayList<>();
            ArrayList<double[]> day3 = new ArrayList<>();

            for(int k = 0; k < 16; k++) {
                day1.add(trainData.get(k));
            }
            for(int k = 16; k < 32; k++) {
                day2.add(trainData.get(k));
            }
            for(int k = 32; k < 48; k++) {
                day3.add(trainData.get(k));
            }
            System.out.println("DAY 1");
            testNeuron(neuron, day1);
            System.out.println("DAY 2");
            testNeuron(neuron, day2);
            System.out.println("DAY 3");
            testNeuron(neuron, day3);
            System.out.println("DAY 4");
            testNeuron(neuron, testData);
        }
    }


    public static double[] normalize(double input, int size) {

        if (size == 1) {
            return new double[]{input};
        }

        double[] pattern = new double[size];

        for (int j = 0; j < size; j++) {
            pattern[j] = Math.pow(input, j + 1);
        }

        return pattern;
    }

    public static ArrayList<double[]> readInputTrainData() {

        ArrayList<double[]> data = new ArrayList<>();

        String[] files = new String[]{"Project3_data/train_data_1.txt", "Project3_data/train_data_2.txt",
                "Project3_data/train_data_3.txt"};

        for (int i = 0; i < files.length; i++) {
            try (Scanner sc = new Scanner(new File(files[i]))) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    data.add(new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static ArrayList<double[]> readInputTestData() {

        ArrayList<double[]> data = new ArrayList<>();

        String[] files = new String[]{"Project3_data/test_data_4.txt"};

        for (int i = 0; i < files.length; i++) {
            try (Scanner sc = new Scanner(new File(files[i]))) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    data.add(new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static Neuron trainNeuron(Neuron neuron, ArrayList<double[]> trainData) {

        // Train neuron
        for (int i = 0; i < trainData.size(); i++) {

            double input = trainData.get(i)[0];
            double desired = trainData.get(i)[1];


            double[] pattern = normalize(input, neuron.size());

            double output = neuron.execute(Neuron.ActivationFunction.NONE, pattern);

            for (int j = 0; j < neuron.size(); j++) {
                double delta = TRAINING_CONSTANT * pattern[j] * (desired - output);
                neuron.setWeight(j, neuron.getWeight(j) + delta);
            }
            double deltaBias = TRAINING_CONSTANT * 1 * BIAS_TRAINING_MODIFIER * (desired - output);
            neuron.setBias(neuron.getBias() + deltaBias);
        }

        return neuron;
    }

    public static void testNeuron(Neuron neuron, ArrayList<double[]> testData) {

        double totalError = 0;

        System.out.println("Input        Desired        Output");

        for (int i = 0; i < testData.size(); i++) {
            double input = testData.get(i)[0];
            double desired = testData.get(i)[1];

            double[] pattern = normalize(input, neuron.size());
            double output = neuron.execute(Neuron.ActivationFunction.NONE, pattern);

            totalError += Math.pow(desired - output, 2);

            System.out.printf("%f        %f        %f\n", testData.get(i)[0], desired, output);
        }

        totalError = Math.sqrt(totalError);
        System.out.println("\nTotal Error: " + totalError);
    }
}
