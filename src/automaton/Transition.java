package automaton;

public class Transition {
	
	private char val;
	private int weight;
	private State from;
	private State to;

	public Transition(char val, State to) {
		this.val = val;
		setTo(to);
		weight = 0;
	}
	
	public void setTo(State to) {
		this.to = to;
		to.addiTransition(this);
	}
	
	public void setFrom(State from) {
		this.from = from;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public State getTo() {
		return to;
	}
	
	public State getFrom() {
		return from;
	}
	
	public char getVal() {
		return val;
	}
	
	public int getlCost() {
		return from.getlCost()+weight;
	}
	
	public int getrCost() {
		return weight+to.getrCost();
	}
	
	public boolean isOptimal() {
		return from.getrCost() == (weight + to.getrCost());
	}
	
	public int getCost() {
		return from.getlCost()+weight+to.getrCost();
	}
}
