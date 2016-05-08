package automaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class State {

	private List<Transition> iTransitions;
	private List<Transition> oTransitions;
	private int importance;
	private int lCost;
	private int rCost;
	private String variable;
	private Map<Character, Integer> counts;
	
	public State(String variable, int importance) {
		this.variable = variable;
		this.importance = importance;
		lCost = 0;
		rCost = 0;
		iTransitions = new ArrayList<Transition>();
		oTransitions = new ArrayList<Transition>();
		counts = new TreeMap<Character, Integer>();
	}
	
//	public void printRandomPath() {
//		if (oTransitions.size()>0) {
//			int r = (int)Math.random()*oTransitions.size();
//			Transition t = oTransitions.get(r);
//			System.out.print("("+variable+")--"+t.getVal()+"-->");
//			if (t.getTo().getVariable().equals(""))
//				System.out.println("END");
//			t.getTo().printRandomPath();
//		}
//	}
	
	public void addoTransition(Transition transition) {
		oTransitions.add(transition);
		transition.setFrom(this);
		int count = 1;
		if (counts.containsKey(transition.getVal()))
			count += counts.get(transition.getVal());
		counts.put(transition.getVal(),count);
	}
	
	public void addiTransition(Transition transition) {
		iTransitions.add(transition);
	}
	
	public List<Transition> getoTransitions() {
		return oTransitions;
	}
	
	public List<Transition> getiTransitions() {
		return iTransitions;
	}
	
	public String getVariable() {
		return variable;
	}

	public int getlCost() {
		return lCost;
	}

	public int getrCost() {
		return rCost;
	}
	
	public void update(char val) {
		for (Transition t : oTransitions) {
			if (val != t.getVal()) {
				t.setWeight(importance);
			}
			else {
				t.setWeight(0);
			}
			t.getTo().recalculatelCost();
		}
		recalculaterCost();
	}
	
	public void relax() {
		for (Transition t : oTransitions) {
			t.setWeight(0);
			t.getTo().recalculatelCost();
		}
		recalculaterCost();
	}
	
	public void recalculaterCost() {
		int r = calculaterCost();
		if (r != rCost) {
			//System.out.println("State: " + variable + " new rCost " + r);
			rCost = r;
			for (Transition t : iTransitions) {
				t.getFrom().recalculaterCost();
			}
		}
		updateCounts();
	}
	
	private void updateCounts() {
		for (char c : counts.keySet()) {
			int t = 0;
			for (Transition transition : oTransitions) {
				if (transition.getVal() == c)
					if (transition.getCost() == 0)
						t++;
			}
			counts.put(c, t);
		}
	}
	
	public void recalculatelCost() {
		int l = calculatelCost();
		if (l != lCost) {
			lCost = l;
			for (Transition t : oTransitions) {
				t.getTo().recalculatelCost();
			}
		}
		updateCounts();
	}
	
	private int calculatelCost() {
		int cost = Integer.MAX_VALUE;
		for (Transition t : iTransitions) {
			int c = t.getlCost();
			if (c < cost)
				cost = c;
		}
		return cost;
	}
	
	private int calculaterCost() {
		int cost = Integer.MAX_VALUE;
		for (Transition t : oTransitions) {
			int c = t.getrCost();
			if (c < cost)
				cost = c;
		}
		return cost;
	}
	
	public Transition getoTransitions(char letter) {
		for (Transition o : oTransitions)
			if (o.getVal() == letter)
				return o;
		return null;
	}
	
	public String getLine1() {
		return variable;
	}
	public String getLine2() {
		return lCost + " | " + rCost;
	}
	public String getLineX(char v) {
		String s = "";
		for (Transition t : oTransitions) {
			if (v == t.getVal())
				s += "--" + t.getVal()+ "(" + t.getWeight() + ")-->";
		}
		return s;
	}
}
