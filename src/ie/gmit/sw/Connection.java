package ie.gmit.sw;

public class Connection {
	private Node fromNode;
	private Node toNode;
	private double weight;
	
	public Connection(Node fromNode, Node toNode, double weight) {
		super();
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Node getFromNode() {
		return fromNode;
	}

	public Node getToNode() {
		return toNode;
	}	
	
	
}
