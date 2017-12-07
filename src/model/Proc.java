package model;
import java.util.ArrayList;

public class Proc extends AtomicComponent{
	
	public Proc(String name){
		super(name);
		outputs.add("done");
		inputs.add("req");
	}
	
	public void init() {
		current_state = 0;		
	}

	public void delta_int(){
		if(current_state == 1)
			next_state = 0;
		current_state = next_state;
	}

	public void delta_ext(ArrayList<String> inputs){
		if(current_state == 0 && inputs.contains("req"))
			next_state = 1;
		current_state = next_state;
	}
	
	public void delta_con(ArrayList<String> inputs){
		current_state = next_state;
	}

	public ArrayList<String> lambda(){
		ArrayList<String> outputs = new ArrayList<>();
		if(current_state == 1){
			outputs.add("done");
		}
		return outputs;
	}

	public double getTa(){
		if(current_state == 0){
			return Double.POSITIVE_INFINITY;
		}
		else if(current_state == 1){
			return 2.0;
		}
		return -1;
	}	
}