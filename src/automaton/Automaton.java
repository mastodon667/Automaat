package automaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Automaton {

	private State initialState;
	private State finalState;
	private Map<String,List<State>> states;
	private Map<String, Character> selection;
	private List<String> interpretations;
	private Set<String> restorations;

	public Automaton(State initialState) {
		this.initialState = initialState;
		states = new TreeMap<String, List<State>>();
		selection = new TreeMap<String, Character>();
		interpretations = new ArrayList<String>();
		restorations = new TreeSet<String>();
		init(this.initialState);
	}

	private void init(State state) {
		if (!state.getVariable().equals(""))
			insertState(state);
		else 
			finalState = state;
		for (Transition t : state.getoTransitions()) {
			State s = t.getTo();
			init(s);
		}
	}

	private void insertState(State state) {
		String key = state.getVariable();
		if (!states.containsKey(key))
			states.put(key, new ArrayList<State>());
		if (!states.get(key).contains(state))
			states.get(key).add(state);
	}

	public void addSelection(String key, char val) {
		selection.put(key, val);
		for (State s : states.get(key))
			s.update(val);
	}

	public void removeSelection(String key) {
		selection.remove(key);
		for (State s : states.get(key))
			s.relax();
	}

	public State getFinalState() {
		return finalState;
	}

	public State getInitialState() {
		return initialState;
	}

	//		public boolean develop(int i, List<State> list, String e) {
	//			if (list.isEmpty())
	//				return false;
	//			if (i > states.size()) {
	//				interpretations.add(e);
	//				System.out.println(e);
	//				return false;
	//			}
	//			List<State> keep = new ArrayList<State>();
	//			List<State> relax = new ArrayList<State>();
	//			String h = "";
	//			for (State state : list) {
	//				h = state.getVariable();
	//				for (Transition transition : state.getoTransitions()) {
	//					if (transition.isOptimal())
	//						if (transition.getWeight() > 0)
	//							relax.add(transition.getTo());
	//						else {
	//							
	//							keep.add(transition.getTo());
	//						}
	//				}
	//			}
	//			List<String> _e = new ArrayList<String>(e);
	//			_e.add(h);
	//			if (develop(i+1,keep,e + " " + h + " - " + transition.getVal()))
	//				return true;
	//			else
	//				return develop(i+1,relax,e);
	//		}
	
	public void showRestoration() {
		int i = 1;
		for (String restoration : restorations) {
			System.out.println("Solution " + i);
			for (String key : restoration.split(" "))
				System.out.println("UNDO: " + key + " - " + selection.get(key));
			System.out.println("------------");
			i++;
		}
	}
	
	

	public void develop(State state, String e, String relax) {
		if (state == finalState) {
			interpretations.add(e.trim());
			restorations.add(relax.trim());
			//System.out.println(e);
		}
		String h = state.getVariable();;
		for (Transition transition : state.getoTransitions()) {
			if (transition.isOptimal()) {
				if (transition.getWeight() > 0)
					develop(transition.getTo(), e + " " + h + " - " + "/", relax + " " + h);
				else
					develop(transition.getTo(), e + " " + h + " - " + transition.getVal(), relax);
			}
		}
	}

	public boolean isConsistent() {
		return finalState.getlCost()==0;
	}

	public void printCounts() {
		int total = 1;
		for (List<State> s : states.values()) {
			System.out.println(s.size());
			total += s.size();
		}
		System.out.println(total);
	}

	//	public void printResult() {
	//		for (String key : states.keySet())
	//			printVarResult(key);
	//	}

	//	private void printVarResult(String key) {
	//		for (State state : states.get(key)) {
	//			System.out.printf("%15s",state.getLine1());
	//		}
	//		System.out.println("");
	//		for (State state : states.get(key)) {
	//			System.out.printf("%15s",state.getLine2());
	//		}
	//		System.out.println("");
	//		for (State state : states.get(key)) {
	//			System.out.printf("%15s",state.getLineX('0'));
	//		}
	//		System.out.println("");
	//		for (State state : states.get(key)) {
	//			System.out.printf("%15s",state.getLineX('1'));
	//		}
	//		System.out.println("");
	//		System.out.println("");
	//	}
}
