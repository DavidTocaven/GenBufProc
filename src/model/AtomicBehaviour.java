package model;
import java.util.ArrayList;

public interface AtomicBehaviour {
	
	public void init();
	public void delta_int();
	public void delta_ext(ArrayList<String> inputs);
	public void delta_con(ArrayList<String> inputs);
	public ArrayList<String> lambda();
	public double getTa();

}
