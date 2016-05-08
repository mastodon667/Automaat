import java.lang.reflect.Array;
import java.util.ArrayList;

import automaton.State;
import automaton.Transition;


public class Minimizer {

	private ArrayList<ArrayList<State>> partitions;
	private ArrayList<Character> alphabet;
	
	public Minimizer(ArrayList<State> fStates, ArrayList<State> nStates, ArrayList<Character> alphabet) {
		this.alphabet = alphabet;
		partitions = new ArrayList<ArrayList<State>>();
		partitions.add(fStates);
		partitions.add(nStates);
	}
	
	private void step(int k) {
		ArrayList<ArrayList<State>> partitionsCopy = new ArrayList<ArrayList<State>>();
		boolean changed = false;
		for (ArrayList<State> states : partitions) {
			ArrayList<ArrayList<State>> resultStates = distinguish(states,k);
			for (ArrayList<State> result : resultStates) {
				partitionsCopy.add(result);
			}
			if (resultStates.size() > 1)
				changed = true;
		}
		partitions = partitionsCopy;
		if (changed)
			step(k++);
		else
			merge();
	}
	
	private void merge() {
		for (ArrayList<State> partition : partitions) {
			if (partition.size() > 1) {
				State nState = new State(partition.get(0).getVariable(), 1);
				for (State s : partition) {
					for (Transition i : s.getiTransitions()) {
						i.setTo(nState);
					}
					
				}
			}
		}
	}
	
	private boolean theSame(ArrayList<ArrayList<State>> partitionsCopy) {
		for (ArrayList<State> part : partitionsCopy) {
			
		}
		return false;
	}

	
	private ArrayList<ArrayList<State>> distinguish(ArrayList<State> states, int k) {
		ArrayList<ArrayList<State>> dPartitions = new ArrayList<ArrayList<State>>();
		for (State state : states) {
			boolean indistinguishable = false;
			for (ArrayList<State> dPart : dPartitions) {
				if (indistinguishable(dPart.get(0), state, k)) {
					dPart.add(state);
					indistinguishable = true;
					break;
				}
			}
			if (!indistinguishable) {
				ArrayList<State> n = new ArrayList<State>();
				n.add(state);
				dPartitions.add(n);
			}
		}
		return dPartitions;
	}
	
	private boolean indistinguishable(State s1, State s2, int k) {
		if (s1.getoTransitions().size() != s2.getoTransitions().size())
			return false;
		if (s1.getoTransitions().size() == 0)
			return s2.getoTransitions().size() == 0;
		if (s2.getoTransitions().size() == 0)
			return s1.getoTransitions().size() == 0;
		if (k > 0)
			for (char letter : alphabet) {
				Transition t1 = s1.getoTransitions(letter);
				Transition t2 = s2.getoTransitions(letter);
				if (t1 == null)
					if (t2 != null)
						return false;
				if (t2 == null)
					if (t1 != null)
						return false;
				if (!indistinguishable(t1.getTo(), t2.getTo(), k--))
					return false;
			}
		return true;
	}
}
