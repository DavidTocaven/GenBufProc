import java.util.ArrayList;
import java.util.HashMap;

import chart.*;
import model.*;




public class Main {

	public static void main(String args[]){
		
		//Attention
		//Les ports sont connectÃ©s par leur nom: pour connecter deux ports, ils doivent avoir le mÃªme nom
		//Pour l'affichage des charts, deux composants ne peuvent pas avoir le mÃªme nom de variable
		
		
		ArrayList<AtomicComponent> atomicArray = new ArrayList<>();
		
		//******Instantiation des composants atomiques ****** INSERER ICI VOS COMPOSANTS
		
//		Step1 step1 = new Step1("step1");
//		Step2 step2 = new Step2("step2");
//		Step3 step3 = new Step3("step3");
//		Step4 step4 = new Step4("step4");
//		Adder adder = new Adder("adder");
//		Euler euler = new Euler("euler");
// 		Qss qss = new Qss("qss");  // TODO
		Buf  buf  = new Buf("Buf");
		Gen  gen  = new Gen("Gen");
		Proc proc = new Proc("Proc");
		//components.add(step1);
		//components.add(step2);
		//components.add(step3);
		//components.add(step4);
		//components.add(adder);
		//components.add(euler);
		
		//components.add(qss); // TODO
		atomicArray.add(buf);
		atomicArray.add(gen);
		atomicArray.add(proc);
		
		
		// CrÃ©ation du Frame Chart   METTRE UN TITRE
		ChartFrame chart = new ChartFrame("","test de l'Integrateur");	

		//**************************************************

		
		//******Initialisation des composants atomiques ****** NE PAS MODIFIER
		
		for(int i = 0; i < atomicArray.size() ; i++){
			atomicArray.get(i).init();
			atomicArray.get(i).setTr(atomicArray.get(i).getTa());
		}

		//**************************************************
			

		//******Gestion de l'affichage des rÃ©sultats*******   NE PAS MODIFIER
		
		// Cette HashMap associe un atomique Ã  chaque variable (utile pour l'envoi de la valeur pendant la simu)
		HashMap<String, AtomicComponent> vars_atom = new HashMap<>();
		ArrayList<String> integer_variables = new ArrayList<>();
		ArrayList<String> real_variables = new ArrayList<>();
		for(AtomicComponent a : atomicArray){
			for(String s : a.getIntegerVariables()){
				integer_variables.add(s);
				vars_atom.put(s, a);
			}
			
			for(String s : a.getRealVariables()){
				real_variables.add(s);
				vars_atom.put(s, a);
			}
		}

		// Cette HashMap associe une trajectoire (Chart) Ã  chaque variable
		HashMap<String, Chart> vars_chart = new HashMap<>();
		for(String var : integer_variables){
			Chart c = new Chart(var);
			chart.addToLineChartPane(c);
			vars_chart.put(var, c);
		}
		
		for(String var : real_variables){
			Chart c = new Chart(var);
			chart.addToLineChartPane(c);
			vars_chart.put(var, c);
		}
		
		//**************************************************
		
		double currentTime = 0; // t 
		double tn = 0;
		double tfin = 10;
		
		//BOUCLE DE SIMULATION
		while(currentTime < tfin)
		{

			System.out.println("****************");
			System.out.println("currentTime=" + currentTime);
			
			//Envoie des donnÃ©es aux charts ******NE PAS MODIFIER********
			for(String var : integer_variables)
				vars_chart.get(var).addDataToSeries(currentTime, vars_atom.get(var).getIntegerValue(var));
			
			for(String var : real_variables)
				vars_chart.get(var).addDataToSeries(currentTime, vars_atom.get(var).getRealValue(var));

			// *********************************************************************************
			// Provient de l'exo 1
			System.out.println("\n   Current time = "+ currentTime);
			ArrayList<AtomicComponent> imminentComponent = new ArrayList<AtomicComponent>();
			ArrayList<String> outputs = new ArrayList<String>();
			double tmin = Double.POSITIVE_INFINITY;		// tmin a l'infini
			
			
			
			/*  Parcours de Tous Les eléments*/
			for(AtomicComponent elem : atomicArray) {	// Pour tous les éléments dela simulation
				System.out.println(elem.getName() + ": Tr= " + elem.getTr());
				if(elem.getTr()<tmin)// Si le Ta de l'élément est inférieur a tmin, 
				{
						tmin = elem.getTr()	;							// Alors je met a jour tmin, et je regarde 
						imminentComponent.clear();												// si des composants imminents ont le meme tmin
						imminentComponent.add(elem)	;																	// Alors j'ajoute l'élément aux composant iminents
						
							
				}
				else if(elem.getTr()==tmin)// TODO Ta 
				{
					imminentComponent.add(elem);
				}
				
			}
			System.out.println("tmin("+tmin+")");
			System.out.println("Liste des éléments imminents : ");
			/* Exécution sortie des éléments imminents  */
			System.out.println("Nb component imminent = "+imminentComponent.size());
			for(AtomicComponent i : imminentComponent) 
			{
				System.out.println("\t"+i.getName() + ": lambda(S) : "+ i.lambda());
				outputs.addAll(i.lambda());
			}
			
			/* Boucle pour delta --> TRANSITIONS */
			for(AtomicComponent b : atomicArray) 		
			{
				String text = "début:"+b;
				
				ArrayList<String> c = new ArrayList<String>(b.getInputs()); // copies des entrées de b dans c
				c.retainAll(outputs); // garde dans c que les elements présent dans outputs
				boolean asEvolute = false;
				if(imminentComponent.contains(b))/*imminent*/
				{
					if( c.isEmpty()) /*si vide : pas d'entrées*/  /*pas d'entrée*/
					{
						b.delta_int();	
						asEvolute=true;
					}
					else /* entrée*/
					{
						b.delta_con(outputs);
						asEvolute=true;
					}
				
					
					// mise a jour du temps si imminent
				}
				else
					{
						if(!c.isEmpty() )/* pas imminent && entrée*/ 
						{
							b.delta_ext(outputs);
							asEvolute=true;
						}
						// mise a jour du tr de tout les élements
					}
				 if (asEvolute)
				 {
					 b.setTr(b.getTa());						 
				 }
				 else
				 {
						b.setTr(b.getTr()-tmin); 
				 }

				System.out.println(text+"\t fin:"+b);
	
			}
			currentTime += tmin;
			// fin depuis l'exo 1
			// *********************************************************************************
		}
	}
	
	
}
