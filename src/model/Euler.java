package model;
import java.util.ArrayList;

public class Euler extends AtomicComponent{
	
	private double x;
	private double dx;
	private double hstep;
	
	
	public Euler(String name){
		super(name);
		outputs.add("euler_s0");
		inputs.add("adder_s0");
	}
	
	public void init() {
		x=0;
		dx=0;
		hstep=0.0001;
		real_varnames_var.put("ref", x);
		current_state = 0;
	}

	public void delta_int(){
		if(current_state == 0){
			x=x+e*dx;
			real_varnames_var.put("ref", x);
			next_state = 0;
		}
		current_state = next_state;
	}

	public void delta_ext(ArrayList<String> inputs){
		if(current_state == 0 && inputs.contains("adder_s0")){
			dx = real_inputnames_input.get("adder_s0");
			x=x+e*dx;
			real_varnames_var.put("ref", x);
			next_state = 0;
		}
		current_state = next_state;
	}
	
	public void delta_con(ArrayList<String> inputs){
		if(current_state == 0 && inputs.contains("adder_s0")){
			dx = real_inputnames_input.get("adder_s0");
			x=x+e*dx;
			real_varnames_var.put("ref", x);
			next_state = 0;
		}
		current_state = next_state;
	}

	public ArrayList<String> lambda(){
		ArrayList<String> outputs = new ArrayList<>();
		if(current_state == 0){
			outputs.add("euler_s0");
			real_outputnames_output.put("euler_s0", x+e*dx);
		}
		return outputs;
	}

	public double getTa(){

		if(current_state == 0){
			return hstep;
		}
		return 0;
	}
}