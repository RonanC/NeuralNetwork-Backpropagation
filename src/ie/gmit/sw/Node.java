package ie.gmit.sw;

import java.util.ArrayList;

public class Node {
	private double value;
	private double error;
	
	private ArrayList<Connection> connectedFrom = new ArrayList<Connection>();
	private ArrayList<Connection> connectedTo = new ArrayList<Connection>();
	
	public Node(double value) {
		this.value = value;
	}
	
	public Node() {
		this.value = 0;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public void addConnectedTo(Connection connection){
		connectedTo.add(connection);
	}
	
	public void addConnectedFrom(Connection connection){
		connectedFrom.add(connection);
	}

	public ArrayList<Connection> getConnectedFrom() {
		return connectedFrom;
	}

	public ArrayList<Connection> getConnectedTo() {
		return connectedTo;
	}
	
	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
	
}
