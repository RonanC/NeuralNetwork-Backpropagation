package ie.gmit.sw;

// batch
public class TrainingSet {
	private double[] inputs;
	private float target;

//	public TrainingSet(int inputNum, float target) {
//		super();
//		inputs = new double[inputNum];
//		this.target = target;
//	}
	
	public TrainingSet(float input1, float input2, float target) {
		super();
		inputs = new double[2];
		inputs[0] = input1;
		inputs[1] = input2;
		this.target = target;
	}

	public double[] getInputs() {
		return inputs;
	}

	public float getTarget() {
		return target;
	}
}
