import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import automaton.Automaton;


public class Reader {

	private String file;
	private Map<String, ArrayList<Integer>> solutions;
	private int size = 0;

	public Reader(String file) {
		this.file = file;
		try {
			getNbOfSolutions();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		solutions = new TreeMap<String, ArrayList<Integer>>();
		ArrayList<String> vakken = null;
		try {
			vakken = (ArrayList<String>) getKeys();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (vakken != null)
			for (String vak : vakken) {
				solutions.put(vak, new ArrayList<Integer>());
			}
	}
	
	private void getNbOfSolutions() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (!line.contains("Number of models:")) {
			line = br.readLine();
		}
		line = line.replace("Number of models: ","");
		size = Integer.parseInt(line);
	}

	private List<String> getKeys() throws IOException {
		List<String> keys = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (!line.contains("Vak = {")) {
			line = br.readLine();
		}
		line = line.replace("Vak = {","").replace("\"","").replace(" ","").replace("}","");
		for (String vak : line.split(";")) {
			keys.add(vak);
		}
		return keys;
	}

	public void read() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (line != null) {
			if (line.contains("Geselecteerd") && !line.contains("Niet")) {
				line = line.replace("Geselecteerd = {","").replace("\"","").replace(" ","").replace("}","");
				TreeSet<String> keys = new TreeSet<String>(solutions.keySet());
				for (String item : line.split(";")) {
					String[] temp = item.split(",");
					solutions.get(temp[0]).add(Integer.parseInt(temp[1]));
					keys.remove(temp[0]);
				}
				for (String vak : keys) {
					solutions.get(vak).add(0);
				}
			}
			line = br.readLine();
		}
	}

	private String[] printResult() {
		String[] a = new String[size];
		for (String vak : solutions.keySet()) {
			ArrayList<Integer> t = solutions.get(vak);
			int i = 0;
			for (Integer v : t) {
				if (a[i] == null)
					a[i] = "";
				a[i] += v;
				i++;
			}
		}
		return a;
	}

	public String[] sort() {
		String[] a = printResult();
		int W = solutions.size();
		int R = 2; //may differ
		int N = a.length;
		String[] aux = new String[N];
		for (int d = W-1; d >= 0; d--)
		{ // Sort by key-indexed counting on dth char.
			int[] count = new int[R+1];     // Compute frequency counts.
			for (int i = 0; i < N; i++)
				count[Integer.parseInt(a[i].charAt(d)+"") + 1]++;
			for (int r = 0; r < R; r++)     // Transform counts to indices.
				count[r+1] += count[r];
			for (int i = 0; i < N; i++)     // Distribute.
				aux[count[Integer.parseInt(a[i].charAt(d)+"")]++] = a[i];
			for (int i = 0; i < N; i++)     // Copy back.
				a[i] = aux[i];
		}
		return a;
	}
	
	public String[] getVariables() {
		String[] v = new String[solutions.size()];
		int i = 0;
		for (String k : solutions.keySet()) {
			v[i] = k;
			i++;
		}
		return v;
	}

	public static void main(String[] args) {
		Reader r = new Reader("src/files/out.txt");
		//File niet aanwezig op github wegens te groot
		try {
			r.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Builder b = new Builder(r.sort(),r.getVariables());
		for (String v : r.getVariables())
			System.out.print(v + " ");
		System.out.println("");
		Automaton isp = b.getAutomaton();
		
		System.out.println("Valid: " + isp.isConsistent());
		isp.addSelection("G0Q55A", '0');
		System.out.println("DESELECTED: Fundamenten van Mens-machine interactie");
		isp.addSelection("H04K5A", '0');
		System.out.println("DESELECTED: Development of Secure Software");
		isp.addSelection("C07I6A", '1');
		System.out.println("SELECTED: ICT-Recht");
		System.out.println("Valid: " + isp.isConsistent());
		isp.addSelection("H02D2A", '0');
		System.out.println("DESELECTED: Uncertainty in Artificial Intelligence");
		System.out.println("Valid: " + isp.isConsistent());
		isp.develop(isp.getInitialState(), "", "");
		isp.showRestoration();
		isp.removeSelection("H02D2A");
		System.out.println("DESELECTION UNDONE: Uncertainty in Artificial Intelligence");
		System.out.println("Valid: " + isp.isConsistent());
		//isp.develop(isp.getInitialState(), "");
	}
}
