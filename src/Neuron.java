
public class Neuron {

    double[] weights;
    double bias;

    static double SOFT_CONSTANT = .05;

    enum ActivationFunction {
        HARD,
        SOFT,
        NONE;
    }

    public Neuron(int numWeights) {

        weights = new double[numWeights];

        for(int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }

        bias = 1;
    }

    public int size() {
        return this.weights.length;
    }

    public void setWeight(int index, double weight) {
        if(index < weights.length & index >= 0) {
            weights[index] = weight;
        }
    }

    public double getWeight(int index) {
        if(index < weights.length & index >= 0) {
            return weights[index];
        }
        System.out.println("bad index used to access weights");
        return Integer.MAX_VALUE;
    }

    public void setBias(double weight) {
        bias = weight;
    }

    public double getBias() {
        return bias;
    }

    public double execute(ActivationFunction aFunc, double[] inputs) {
        if(inputs.length != weights.length) {
            System.out.println("bad inputs passed into neuron");
            return Double.MAX_VALUE;
        }
        double sum = 0;
        for(int i = 0; i < inputs.length; i++) {
            sum += weights[i] * inputs[i];
        }
        sum += bias;

        switch(aFunc) {
            case SOFT: return (1.0 / (1 + (Math.exp(-1.0 * sum * SOFT_CONSTANT))));
            case HARD: return (sum >= 0 ? 1 : 0);
            case NONE: return sum;
            default: return (sum >= 0 ? 1 : 0);
        }
    }
}
