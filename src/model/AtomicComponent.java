package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class AtomicComponent implements AtomicBehaviour{

	protected int current_state;
	protected int next_state;
	protected double tr;
	protected double e;
	protected double tl;
	
	private String name;
	protected HashMap<String, Integer> integer_outputnames_output;
	protected HashMap<String, Integer> integer_inputnames_input;
	protected HashMap<String, Integer> integer_varnames_var;
	protected HashMap<String, Double> real_outputnames_output;
	protected HashMap<String, Double> real_inputnames_input;
	protected HashMap<String, Double> real_varnames_var;
	protected ArrayList<String> outputs;
	protected ArrayList<String> inputs;
	
	public AtomicComponent(String name){
		this.name = name;
		
		integer_outputnames_output = new HashMap<>();
		integer_inputnames_input = new HashMap<>();
		integer_varnames_var = new HashMap<>();
		
		real_outputnames_output = new HashMap<>();
		real_inputnames_input = new HashMap<>();
		real_varnames_var = new HashMap<>();
		
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public double getTr(){
		return tr;
	}
	
	public void setTr(double tr){
		this.tr = tr;
	}
	
	public double getE(){
		return e;
	}
	
	public void setE(double e){
		this.e = e;
	}
	
	public double getTl(){
		return tl;
	}
	
	public void setTl(double tl){
		this.tl = tl;
	}
	
	
	public ArrayList<String> getInputs() {
		return inputs;
	}

	public int readIntegerOutputValue(String output){
		for(String s : integer_outputnames_output.keySet())
			if(s.equals(output))			
				return integer_outputnames_output.get(s);
			return 0;
	}

	public void writeIntegerInputValue(String input, int value){
		integer_inputnames_input.put(input,value);
	}
	
	public double readRealOutputValue(String output){
		for(String s : real_outputnames_output.keySet())
			if(s.equals(output))			
				return real_outputnames_output.get(s);
			return 0;
	}

	public void writeRealInputValue(String input, double value){
		real_inputnames_input.put(input,value);
	}
	
	public int getIntegerValue(String var){
		for(String s : integer_varnames_var.keySet())
			if(s.equals(var))			
				return integer_varnames_var.get(s);
			return 0;
	}

	public Set<String> getIntegerVariables() {
		return integer_varnames_var.keySet();
	}
	
	public double getRealValue(String var){
		for(String s : real_varnames_var.keySet())
			if(s.equals(var))			
				return real_varnames_var.get(s);
			return 0;
	}

	public Set<String> getRealVariables() {
		return real_varnames_var.keySet();
	}

}
