package model;
import java.util.ArrayList;

public class Step4 extends AtomicComponent{
	
	private double sigma;
	private double xi;
	private double xf;
	private double ts;
		
	public Step4(String name){
		super(name);
		outputs.add("step4_s0");
	}
	
	public void init() {
		sigma = 0;
		xi = 0;
		xf = 4;
		ts = 1.5;
		current_state = 0;
	}

	public void delta_int(){
		if(current_state == 0){
			sigma=ts;
			next_state = 1;
		}
		if(current_state == 1){
			next_state = 2;
		}
		current_state = next_state;
	}

	public void delta_ext(ArrayList<String> inputs){
		current_state = next_state;
	}
	
	public void delta_con(ArrayList<String> inputs){
		current_state = next_state;
	}

	public ArrayList<String> lambda(){
		ArrayList<String> outputs = new ArrayList<>();
		if(current_state == 0){
			outputs.add("step4_s0");
			real_outputnames_output.put("step4_s0", xi);
		}
		else if(current_state == 1){
			outputs.add("step4_s0");
			real_outputnames_output.put("step4_s0", xf);
		}
		return outputs;
	}

	public double getTa(){

		if(current_state == 0){
			return 0;
		}
		else if(current_state == 1){
			return sigma;
		}
		else if(current_state == 2){
			return Double.POSITIVE_INFINITY;
		}
		
		return 0;
	}
}