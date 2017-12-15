package model;
import java.util.ArrayList;

public class Adder extends AtomicComponent{
	
	private double x0;
	private double k0;
	private double x1;
	private double k1;
	private double x2;
	private double k2;
	private double x3;
	private double k3;
	
	private double x;
	
	
	public Adder(String name){
		super(name);
		outputs.add("adder_s0");
		inputs.add("step1_s0");
		inputs.add("step2_s0");
		inputs.add("step3_s0");
		inputs.add("step4_s0");
	}
	
	public void init() {
		x0=0;
		k0=1;
		x1=0;
		k1=1;
		x2=0;
		k2=1;
		x3=0;
		k3=1;
		
		x=0;
		//real_varnames_var.put("x", x);
		
		current_state = 0;
	}

	public void delta_int(){
		if(current_state == 1){
			next_state = 0;
		}
		current_state = next_state;
	}
	//*******************************************************************************
	public void delta_ext(ArrayList<String> inputs)
	{
		if(current_state == 0 && (inputs.contains("step1_s0") || inputs.contains("step2_s0") || inputs.contains("step3_s0") || inputs.contains("step4_s0")) )
		{
			if(real_inputnames_input.containsKey("step1_s0") == true)
				x0 = real_inputnames_input.get("step1_s0");
			if(real_inputnames_input.containsKey("step2_s0") == true)
				x1 = real_inputnames_input.get("step2_s0");
			if(real_inputnames_input.containsKey("step3_s0") == true)
				x2 = real_inputnames_input.get("step3_s0");
			if(real_inputnames_input.containsKey("step4_s0") == true)
				x3 = real_inputnames_input.get("step4_s0");
			x =  x0*k0+x1*k1+x2*k2+x3*k3;
			real_varnames_var.put("x", x);
			next_state = 1;
			
		}
		else if(current_state == 1 && (inputs.contains("step1_s0") || inputs.contains("step2_s0") || inputs.contains("step3_s0") || inputs.contains("step4_s0")) )
		{
			if(real_inputnames_input.containsKey("step1_s0") == true)
				x0 = real_inputnames_input.get("step1_s0");
			if(real_inputnames_input.containsKey("step2_s0") == true)
				x1 = real_inputnames_input.get("step2_s0");
			if(real_inputnames_input.containsKey("step3_s0") == true)
				x2 = real_inputnames_input.get("step3_s0");
			if(real_inputnames_input.containsKey("step4_s0") == true)
				x3 = real_inputnames_input.get("step4_s0");
			x =  x0*k0+x1*k1+x2*k2+x3*k3;
			//real_varnames_var.put("x", x);
			next_state = 1;
		}
		current_state = next_state;
	}
	//*******************************************************************************
	public void delta_con(ArrayList<String> inputs){
		if(current_state == 1 && (inputs.contains("step1_s0") || inputs.contains("step2_s0") || inputs.contains("step3_s0") || inputs.contains("step4_s0")) ){
			if(real_inputnames_input.containsKey("step1_s0") == true)
				x0 = real_inputnames_input.get("step1_s0");
			if(real_inputnames_input.containsKey("step2_s0") == true)
				x1 = real_inputnames_input.get("step2_s0");
			if(real_inputnames_input.containsKey("step3_s0") == true)
				x2 = real_inputnames_input.get("step3_s0");
			if(real_inputnames_input.containsKey("step4_s0") == true)
				x3 = real_inputnames_input.get("step4_s0");
			x =  x0*k0+x1*k1+x2*k2+x3*k3;
			//real_varnames_var.put("x", x);
			next_state = 1;
		}
		current_state = next_state;
	}

	public ArrayList<String> lambda(){
		ArrayList<String> outputs = new ArrayList<>();
		if(current_state == 1){
			outputs.add("adder_s0");
			real_outputnames_output.put("adder_s0", x0*k0+x1*k1+x2*k2+x3*k3);
		}
		return outputs;
	}

	public double getTa(){

		if(current_state == 0){
			return Double.POSITIVE_INFINITY;
		}
		else if(current_state == 1){
			return 0;
		}
		return 0;
	}
}