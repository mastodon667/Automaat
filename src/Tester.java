import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.io.PrintWriter;

import dk.brics.automaton.Automaton;


public class Tester {

	public static void main(String[] args) {
		try {
			FileInputStream in = new FileInputStream("src/files/automaton_big.txt");
			Automaton isp = Automaton.load(in);
			in.close();
			isp.minimize();
			FileOutputStream out = new FileOutputStream("src/files/automaton_min.txt");
			isp.store(out);
			out.close();
			PrintWriter writer = new PrintWriter("src/files/automaton_dot.gv");
			writer.println(isp.toDot());
			writer.close();
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
}
