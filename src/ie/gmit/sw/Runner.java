package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Random;

public class Runner {
	// nums
	private int inputNum = 2;
	private int hiddenNum = 3;
	private int outputNum = 1;
	private double target = 1;
	private double learningRate = 0.5;
	double errorThreshold = 0.0001;
	// 257113, 36825

	// lists
	ArrayList<Node> inputs = new ArrayList<Node>();
	ArrayList<Node> hidden = new ArrayList<Node>();
	ArrayList<Node> outputs = new ArrayList<Node>();
	ArrayList<Connection> inputConnections = new ArrayList<Connection>();
	ArrayList<Connection> hiddenConnections = new ArrayList<Connection>();
	ArrayList<TrainingSet> setAnd = new ArrayList<TrainingSet>();
	ArrayList<TrainingSet> setOr = new ArrayList<TrainingSet>();
	ArrayList<TrainingSet> setXor = new ArrayList<TrainingSet>();
	
	// misc
	private Random random = new Random();
	int nodeCount, connectionCount;
	int epochCount = 0;
	double sum, value, euler;
	double difference;
	double sumSquaredErrors;

	public Runner() {
		createNodes();
		createConnections();

		// set training sets
		genAndSet();
		genOrSet();
		genXorSet();

//		displayAll();
		// displayHiddenShort();

		// epoch currently has one set
		// a set consists of the input values and a target
		// in order to add more sets we just update the input node values and
		// target
		do {
			epochCount++;
			for (TrainingSet trainingSet : setAnd) {
				sumSquaredErrors = 0.0;
				int count = 0;
				for (Node node : inputs) {
					node.setValue(trainingSet.getInputs()[count]);
					count++;
				}
				target = trainingSet.getTarget();
				
				
				//// forward pass
				genAllValues();
				// displayHiddenShort();

				//// output error
				// error = (t - o)(1 - o)o
				// t = target, o = output
				genOutputError();

				//// output(weight) update
				updateOutputWeights();

				// System.out.println("Updated Output Weights");
				// displayHiddenShort();

				//// hidden errors
				genHiddenError();

				//// hidden(weight) update
				updateHiddenWeights();

				// System.out.println("Updated Hidden Weights");
				// displayHiddenShort();

				
				sumSquaredErrors += difference * difference;
				displayOutput();
			}
			
//			System.out.println("\ndifference: " + difference);
//			System.out.println("errorThreshold: " + errorThreshold);
//			System.out.println("difference > errorThreshold: " + (difference > errorThreshold));
		} while (sumSquaredErrors > errorThreshold || sumSquaredErrors < -errorThreshold);

		System.out.println();
//		displayAll();

		System.out.println("**Final epoch iteration**");
		displayOutput();

		// an epoch may contain a set of items to test
	}
	
	private void genXorSet() {
		setXor.add(new TrainingSet(1, 1, 0));
		setXor.add(new TrainingSet(1, 0, 1));
		setXor.add(new TrainingSet(0, 1, 1));
		setXor.add(new TrainingSet(0, 0, 0));
	}

	private void genOrSet() {
		setOr.add(new TrainingSet(1, 1, 1));
		setOr.add(new TrainingSet(1, 0, 1));
		setOr.add(new TrainingSet(0, 1, 1));
		setOr.add(new TrainingSet(0, 0, 0));
	}

	private void genAndSet() {
		// AND
		setAnd.add(new TrainingSet(1, 1, 1));
		setAnd.add(new TrainingSet(1, 0, 0));
		setAnd.add(new TrainingSet(0, 1, 0));
		setAnd.add(new TrainingSet(0, 0, 0));
	}

	private void displayOutput() {
		for (Node node : outputs) {
			difference = target - node.getValue();
			System.out.printf("Epoch #%d\n", epochCount);
			displayInputsShort();
			System.out.printf("Target Value:\t\t%f\n", target);
			System.out.printf("Output Node Value:\t%f\n", node.getValue());
//			System.out.printf("Difference:\t\t%f\n", difference);
			System.out.printf("sumSquaredErrors:\t%f\n", sumSquaredErrors);
		}
	}

	private void genHiddenError() {
		for (Node node : hidden) {
			double hiddenError = 0.0;
			for (Connection connection : node.getConnectedTo()) {
				hiddenError = connection.getToNode().getError() * connection.getWeight() * (1 - node.getValue())
						* node.getValue();
				node.setError(hiddenError);
			}
			// System.out.printf("hiddenError: %f\n", hiddenError);
		}
		System.out.println();
	}

	private void updateHiddenWeights() {
		updateWeight(hidden);
	}

	private void genOutputError() {
		for (Node node : outputs) {
			double error = 0.0;
			;
			for (Connection connection : node.getConnectedFrom()) {
				double output = node.getValue();
				error = (target - output) * (1 - output) * output;
				node.setError(error);
			}
			// System.out.printf("error: %f\n", error);
		}
	}

	private void updateOutputWeights() {
		updateWeight(outputs);
	}

	private void updateWeight(ArrayList<Node> nodes) {
		for (Node node : nodes) {
			for (Connection connection : node.getConnectedFrom()) {
				double oldWeight = connection.getWeight();
				double newWeight = oldWeight + (learningRate * node.getError() * connection.getFromNode().getValue());
				connection.setWeight(newWeight);
				// System.out.println("oldWeight: " + oldWeight + "\tnewWeight:
				// " + newWeight);
			}
		}
	}
	
	private void displayInputsShort(){
		System.out.print("Target: " + target + "\t\t");
		System.out.print("Inputs:\t\t");
		for (Node node : inputs) {
			System.out.print(node.getValue() + "\t\t");
		}
		System.out.println();
	}

	private void displayHiddenShort() {
		System.out.println("*Hidden Values/Weights");
		for (Node node : hidden) {
			System.out.print("value:\t" + node.getValue());
			System.out.print("\tweights_from:\t");
			for (Connection connection : node.getConnectedFrom()) {
				System.out.printf("%.5f\t\t", connection.getWeight());
			}

			System.out.print("\tweights_to:\t");
			for (Connection connection : node.getConnectedTo()) {
				System.out.printf("%.5f\t", connection.getWeight());
			}
			System.out.println();
		}
		System.out.println();
	}

	private void displayAll() {
		System.out.println("===DISPLAYING VALUES===");
		System.out.println("**inputs**");
		displayInputs();
		System.out.println("**hidden**");
		displayHidden();
		System.out.println("**outputs**");
		displayOutputs();
	}

	private void displayInputs() {
		// display values
		for (Node node : inputs) {
			System.out.println("value:\t\t" + node.getValue());

			System.out.print("weights to:\t");
			for (Connection connection : node.getConnectedTo()) {
				System.out.print(connection.getWeight() + "\t");
			}

			System.out.println("\n");
		}
	}

	private void displayHidden() {
		// display values
		for (Node node : hidden) {
			System.out.println("value:\t\t" + node.getValue());
			System.out.print("weights from:\t");
			for (Connection connection : node.getConnectedFrom()) {
				System.out.print(connection.getWeight() + "\t");
			}
			System.out.println();

			System.out.print("weights to:\t");
			for (Connection connection : node.getConnectedTo()) {
				System.out.print(connection.getWeight() + "\t");
			}
			System.out.println("\n");
		}
	}

	private void displayOutputs() {
		// display values
		for (Node node : outputs) {
			System.out.println("value:\t\t" + node.getValue());
			System.out.print("weights from:\t");
			for (Connection connection : node.getConnectedFrom()) {
				System.out.print(connection.getWeight() + "\t");
			}

			System.out.println("\n");
		}
	}

	private void genAllValues() {
		// System.out.println("*Generating hidden node values");
		genValues(hidden);

		// System.out.println("*Generating output node values");
		genValues(outputs);
	}

	private void genValues(ArrayList<Node> nodes) {
		// sigmoid of the sum of the inputs times the weights
		nodeCount = 0;
		for (Node node : nodes) {
			connectionCount = 0;
			nodeCount++;
			sum = 0;

			// System.out.printf("*node: #%d\n", nodeCount);
			for (Connection connectionFrom : node.getConnectedFrom()) {
				connectionCount++;
				value = connectionFrom.getFromNode().getValue() * connectionFrom.getWeight();

				sum += value;
				// System.out.printf("connection #%d\t\t", connectionCount);
				// System.out.printf("value: %.5f\t\t",
				// connectionFrom.getFromNode().getValue());
				// System.out.printf("weight: %.5f\t\t",
				// connectionFrom.getWeight());
				// System.out.printf("v * w: %.5f\n", value);
			}
			// System.out.printf("sum: %.5f\t\t", sum);
			euler = (double) (1 / (1 + Math.exp(-sum)));
			// System.out.printf("euler: %.5f\n\n", euler);
			node.setValue(euler);
		}
	}

	private void createConnections() {
		// create connections
		// connect inputs to hidden
		Connection tempConnection;
		for (Node inputNode : inputs) {
			for (Node hiddenNode : hidden) {
				tempConnection = new Connection(inputNode, hiddenNode, random.nextFloat());
				inputNode.addConnectedTo(tempConnection);
				hiddenNode.addConnectedFrom(tempConnection);
			}
		}

		// connect hidden to outputs
		for (Node hiddenNode : hidden) {
			for (Node outputNode : outputs) {
				tempConnection = new Connection(hiddenNode, outputNode, random.nextFloat());
				hiddenNode.addConnectedTo(tempConnection);
				outputNode.addConnectedFrom(tempConnection);
			}
		}
	}

	private void createNodes() {
		// create nodes
		for (int i = 0; i < inputNum; i++) {
			inputs.add(new Node(random.nextFloat()));
		}

		for (int i = 0; i < hiddenNum; i++) {
			hidden.add(new Node());
		}

		for (int i = 0; i < outputNum; i++) {
			outputs.add(new Node());
		}
	}

	public static void main(String[] args) {
		new Runner();
	}
}
