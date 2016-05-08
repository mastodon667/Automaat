import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.util.HashMap;
import automaton.Automaton;
import automaton.State;
import automaton.Transition;

public class Builder {

	private Automaton isp;
	private dk.brics.automaton.Automaton ispAlt;

	public Builder(String[] solutions, String[] variables) {
		//isp = new Automaton(build(0,solutions.length-1,0,solutions,variables,fState),fState);
		//dk.brics.automaton.State fStateAlt = new dk.brics.automaton.State();
		//fStateAlt.setAccept(true);
		//ispAlt = new dk.brics.automaton.Automaton();
		//ispAlt.setInitialState(buildAlt(0,solutions.length-1,0,solutions,variables,fStateAlt));
		try {
			FileInputStream in = new FileInputStream("src/files/automaton_min.txt");
			ispAlt = dk.brics.automaton.Automaton.load(in);
			isp = new Automaton(convert(0, variables, ispAlt.getInitialState(),new HashMap<dk.brics.automaton.State, State>()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private State build(int start, int stop, int pos, String[] solutions, String[] variables, State fState) {
		if (solutions[start].length()>pos) {
			State state = new State(variables[pos],1); //importance could be different than 1 depending on preference
			int i = start;
			char v = solutions[start].charAt(pos);
			while(i<stop) {
				if (v != solutions[i].charAt(pos)) {
					state.addoTransition(new Transition(v, build(start,i-1,pos+1,solutions,variables,fState)));
					start = i;
					v = solutions[start].charAt(pos);
				}
				i++;
			}
			state.addoTransition(new Transition(v, build(start,stop,pos+1,solutions,variables,fState)));
			return state;
		}
		else {
			return fState;
		}
	}
	
	public State convert(int pos, String[] variables, dk.brics.automaton.State stateAlt, HashMap<dk.brics.automaton.State, State> seen) {
		State state = null;
		if (seen.containsKey(stateAlt)) {
			state = seen.get(stateAlt);
		}
		else {
			if (pos < variables.length)
				state = new State(variables[pos], 1);
			else {
				state = new State("", 0);
			}
			seen.put(stateAlt, state);
			for (dk.brics.automaton.Transition t : stateAlt.getTransitions()) {
				Transition o = new Transition(t.getMax(),convert(pos+1,variables,t.getDest(),seen));
				state.addoTransition(o);
			}
		}
		return state;
	}
	
	private dk.brics.automaton.State buildAlt(int start, int stop, int pos, String[] solutions, String[] variables, dk.brics.automaton.State fState) {
		if (solutions[start].length()>pos) {
			dk.brics.automaton.State state = new dk.brics.automaton.State();
			int i = start;
			char v = solutions[start].charAt(pos);
			while(i<stop) {
				if (v != solutions[i].charAt(pos)) {
					state.addTransition(new dk.brics.automaton.Transition(v, buildAlt(start,i-1,pos+1,solutions,variables,fState)));
					start = i;
					v = solutions[start].charAt(pos);
				}
				i++;
			}
			state.addTransition(new dk.brics.automaton.Transition(v, buildAlt(start,stop,pos+1,solutions,variables,fState)));
			return state;
		}
		else {
			return fState;
		}
	}
	
	public Automaton getAutomaton() {
		return isp;
	}
}
