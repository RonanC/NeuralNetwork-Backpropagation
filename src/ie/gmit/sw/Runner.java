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
	double errorThreshold = 0.001;
	// 257113, 36825

	// lists
	ArrayList<Node> inputs = new ArrayList<Node>();

	ArrayList<ArrayList<Node>> hiddenLayers = new ArrayList<ArrayList<Node>>();
	ArrayList<Node> hidden1 = new ArrayList<Node>();
	ArrayList<Node> hidden2 = new ArrayList<Node>();

	ArrayList<Node> outputs = new ArrayList<Node>();
	ArrayList<Connection> inputConnections = new ArrayList<Connection>();
	ArrayList<Connection> hiddenConnections = new ArrayList<Connection>();
	ArrayList<TrainingSet> setAnd = new ArrayList<TrainingSet>();
	ArrayList<TrainingSet> setOr = new ArrayList<TrainingSet>();
	ArrayList<TrainingSet> setXor = new ArrayList<TrainingSet>();
	ArrayList<TrainingSet> chosenSet;

	// misc
	private Random random = new Random();
	int nodeCount, connectionCount;
	int epochCount = 0;
	double sum, value, euler;
	double difference;
	double sumSquaredErrors;

	public Runner() {
		hiddenLayers.add(hidden1);
		hiddenLayers.add(hidden2);

		createNodes();
		createConnections();

		// set training sets
		genAndSet();
		genOrSet();
		genXorSet();
		
		chosenSet = setXor;

		displayAll();

		// epoch currently has one set
		// a set consists of the input values and a target
		// in order to add more sets we just update the input node values and
		// target
		do {
			epochCount++;
			sumSquaredErrors = 0.0;
			for (TrainingSet trainingSet : chosenSet) {
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

				calcSumSquaredErrors();
				// displayOutput();
				// System.out.printf("\nEpoch #%d\n", epochCount);
				// displayHiddenShort();
			}

			// System.out.println("\ndifference: " + difference);
			// System.out.println("errorThreshold: " + errorThreshold);
			// System.out.println("difference > errorThreshold: " + (difference
			// > errorThreshold));
		} while (sumSquaredErrors > errorThreshold || sumSquaredErrors < -errorThreshold);

		System.out.println("\n\n**FINISHED**");
		displayAll();

		// System.out.println("**SHORT HIDDEN**");
		// displayHiddenShort();

		System.out.println("\n**Final epoch iteration**");
		displayFinale();
		
		System.out.println("\n**Testing Chosen Set**");	// TODO: add name
		testSet();

		// an epoch may contain a set of items to test
	}

	private void testSet() {
		for (TrainingSet trainingSet : chosenSet) {
			int count = 0;
			for (Node node : inputs) {
				node.setValue(trainingSet.getInputs()[count]);
				count++;
			}
			target = trainingSet.getTarget();

			//// forward pass
			genAllValues();
			
			for (Node node : outputs) {
				displayInputsShort();
				System.out.printf("Output Node Value:\t%f\n", node.getValue());
			}
		}
	}

	private void genXorSet() {
		setXor.add(new TrainingSet(0, 0, 0));
		setXor.add(new TrainingSet(0, 1, 1));
		setXor.add(new TrainingSet(1, 0, 1));
		setXor.add(new TrainingSet(1, 1, 0));
	}

	private void genOrSet() {
		setOr.add(new TrainingSet(0, 0, 0));
		setOr.add(new TrainingSet(0, 1, 1));
		setOr.add(new TrainingSet(1, 0, 1));
		setOr.add(new TrainingSet(1, 1, 1));
	}

	private void genAndSet() {
		// AND
		setAnd.add(new TrainingSet(0, 0, 0));
		setAnd.add(new TrainingSet(0, 1, 0));
		setAnd.add(new TrainingSet(1, 0, 0));
		setAnd.add(new TrainingSet(1, 1, 1));

	}

	private void calcSumSquaredErrors() {
		sumSquaredErrors = 0.0;
		for (Node node : outputs) {
			difference = target - node.getValue();
			sumSquaredErrors += difference * difference;
		}
//		System.out.println("sumSquaredErrors: " + sumSquaredErrors);
	}

	private void displayFinale() {
		System.out.printf("Epoch #%d\n", epochCount);
		for (Node node : outputs) {
//			System.out.printf("Output Node Value:\t%f\n", node.getValue());
		}
		System.out.printf("sumSquaredErrors:\t%f\n", sumSquaredErrors);
	}

	private void displayOutput() {
		for (Node node : outputs) {
			System.out.printf("Epoch #%d\n", epochCount);
			displayInputsShort();
			// System.out.printf("Target Value:\t\t%f\n", target);
			System.out.printf("Output Node Value:\t%f\n", node.getValue());
			// System.out.printf("Difference:\t\t%f\n", difference);
		}
		System.out.printf("sumSquaredErrors:\t%f\n", sumSquaredErrors);
		// displayHiddenShort();
	}

	private void genHiddenError() {
		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			for (Node node : hiddenLayer) {
				double hiddenError = 0.0;
				for (Connection connection : node.getConnectedTo()) {
					hiddenError = connection.getToNode().getError() * connection.getWeight() * (1 - node.getValue())
							* node.getValue();
					node.setError(hiddenError);
				}
				// System.out.printf("hiddenError: %f\n", hiddenError);
			}
			// System.out.println();
		}
	}

	private void updateHiddenWeights() {
		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			updateWeight(hiddenLayer);
		}
		// updateWeight(hidden1);
	}

	private void genOutputError() {
		for (Node node : outputs) {
			double error = 0.0;

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

	private void displayInputsShort() {
		System.out.print("Target: " + target + "\t\t");
		System.out.print("Inputs:\t\t");
		for (Node node : inputs) {
			System.out.print(node.getValue() + "\t\t");
		}
		System.out.println();
	}

	private void displayHiddenShort() {
		int count = 0;
		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			count++;
			System.out.println("Hidden Layer: " + count);
			for (Node node : hiddenLayer) {
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
		}
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
		int count = 0;
		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			count++;
			System.out.println("Hidden Layer " + count);
			for (Node node : hiddenLayer) {
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
		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			genValues(hiddenLayer);
		}
		// genValues(hidden1);
		//
		// genValues(hidden2);

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
			for (Node hiddenNode1 : hidden1) {
				tempConnection = new Connection(inputNode, hiddenNode1, random.nextFloat());
				inputNode.addConnectedTo(tempConnection);
				hiddenNode1.addConnectedFrom(tempConnection);
			}
		}

		// connect hidden1 nodes to hidden2 nodes
		for (Node hiddenNode1 : hidden1) {
			for (Node hiddenNode2 : hidden2) {
				tempConnection = new Connection(hiddenNode1, hiddenNode2, random.nextFloat());
				hiddenNode1.addConnectedTo(tempConnection);
				hiddenNode2.addConnectedFrom(tempConnection);
			}
		}

		// connect hidden to outputs
		for (Node hiddenNode2 : hidden2) {
			for (Node outputNode : outputs) {
				tempConnection = new Connection(hiddenNode2, outputNode, random.nextFloat());
				hiddenNode2.addConnectedTo(tempConnection);
				outputNode.addConnectedFrom(tempConnection);
			}
		}
	}

	private void createNodes() {
		// create nodes
		for (int i = 0; i < inputNum; i++) {
			inputs.add(new Node(random.nextFloat()));
		}

		for (ArrayList<Node> hiddenLayer : hiddenLayers) {
			for (int i = 0; i < hiddenNum; i++) {
				hiddenLayer.add(new Node());
			}
		}

		// for (int i = 0; i < hiddenNum; i++) {
		// hidden1.add(new Node());
		// }
		//
		// for (int i = 0; i < hiddenNum; i++) {
		// hidden2.add(new Node());
		// }

		for (int i = 0; i < outputNum; i++) {
			outputs.add(new Node());
		}
	}

	public static void main(String[] args) {
		new Runner();
	}
}
