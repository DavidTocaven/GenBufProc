import java.util.ArrayList;
import java.util.HashMap;

import chart.*;
import model.*;




public class Main {

	public static void main(String args[]){
		
		//Attention
		//Les ports sont connectés par leur nom: pour connecter deux ports, ils doivent avoir le même nom
		//Pour l'affichage des charts, deux composants ne peuvent pas avoir le même nom de variable
		
		
		ArrayList<AtomicComponent> atomicArray = new ArrayList<>();
		
		//******Instantiation des composants atomiques ****** INSERER ICI VOS COMPOSANTS
		
		Step1 step1 = new Step1("step1");
		Step2 step2 = new Step2("step2");
		Step3 step3 = new Step3("step3");
		Step4 step4 = new Step4("step4");
		Adder adder = new Adder("adder");
		Euler euler = new Euler("euler");
// 		Qss qss = new Qss("qss");  // TODO
		//Buf  buf  = new Buf("Buf");
		//Gen  gen  = new Gen("Gen");
		//Proc proc = new Proc("Proc");
		
		atomicArray.add(step1);
		atomicArray.add(step2);
		atomicArray.add(step3);
		atomicArray.add(step4);
		atomicArray.add(adder);
		atomicArray.add(euler);
		//atomicArray.add(qss); // TODO
		//atomicArray.add(buf);
		//atomicArray.add(gen);
		//atomicArray.add(proc);
		
		
		// Création du Frame Chart   METTRE UN TITRE
		ChartFrame chart = new ChartFrame("","test de l'Integrateur");	

		//**************************************************

		
		//******Initialisation des composants atomiques ****** NE PAS MODIFIER
		
		for(int i = 0; i < atomicArray.size() ; i++){
			atomicArray.get(i).init();
			atomicArray.get(i).setTr(atomicArray.get(i).getTa());
		}

		//**************************************************
			

		//******Gestion de l'affichage des résultats*******   NE PAS MODIFIER
		
		// Cette HashMap associe un atomique à chaque variable (utile pour l'envoi de la valeur pendant la simu)
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

		// Cette HashMap associe une trajectoire (Chart) à chaque variable
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
		double tfin = 2;
		
		//BOUCLE DE SIMULATION
		while(currentTime < tfin)
		{

			System.out.println("****************");
			System.out.println("currentTime=" + currentTime);
			
			//Envoie des données aux charts ******NE PAS MODIFIER********
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
			
			
			
			/*  Parcours de Tous Les el�ments*/
			for(AtomicComponent elem : atomicArray)// Pour tous les �l�ments de la simulation 
			{	
				System.out.println(elem.getName() + ": Tr= " + elem.getTr());
				if(elem.getTr()<tmin)// Si le Tr de l'�l�ment est inf�rieur a tmin, 
				{
						tmin = elem.getTr()	;							// Alors je met a jour tmin, et je regarde 
						imminentComponent.clear();												// si des composants imminents ont le meme tmin
						imminentComponent.add(elem)	;																	// Alors j'ajoute l'�l�ment aux composant iminents
						
							
				}
				else if(elem.getTr()==tmin)// TODO Ta 
				{
					imminentComponent.add(elem);
				}
				
			}
			for(AtomicComponent elem : atomicArray)// Pour tous les �l�ments de la simulation
			{
				
				elem.setE(currentTime - elem.getTl());
				
			}
			System.out.println("tmin("+tmin+")");
			System.out.println("Liste des �l�ments imminents : ");
			HashMap<String, AtomicComponent> output_atom = new HashMap<>();
			ArrayList<String> output_of_component = new ArrayList<>();
			/* Ex�cution sortie des �l�ments imminents  */
			System.out.println("Nb component imminent = "+imminentComponent.size());
			for(AtomicComponent i : imminentComponent) 
			{
				System.out.println("\t"+i.getName() + ": lambda(S) : "+ i.lambda());
				outputs.addAll(i.lambda());
				output_of_component= i.lambda();
				for(String s : output_of_component)
				{
					output_atom.put(s, i);
				}
			}
			
			/* Boucle pour delta --> TRANSITIONS */
			for(AtomicComponent b : atomicArray) 		
			{
				Boolean imputFree=true;
				for (String s : outputs) // pour toutes les sorties
				{
					if(b.getInputs().contains(s)) // si au moins une entr�e de b est une sortie s active
					{
						b.writeRealInputValue(s,
											  output_atom.get(s).readRealOutputValue(s)); // ajout de la sortie
						imputFree = false;
						System.out.println(b.getName()+":"+s+" vient de " + output_atom.get(s).getName());

					}
				}
				String text = "d�but:"+b;
//				ArrayList<String> c = new ArrayList<String>(b.getInputs()); // copies des entr�es de b dans c
//				c.retainAll(outputs); // garde dans c que les elements pr�sent dans outputs
				boolean asEvolute = false;
				if(imminentComponent.contains(b))/*imminent*/
				{
					if( imputFree==true) /*si vide : pas d'entr�es*/  /*pas d'entr�e*/
					{
						b.delta_int();
						System.out.println("Delta int de "+b.getName());
						
						asEvolute=true;
					}
					else /* entr�e(s)*/
					{
						b.delta_con(outputs);
						System.out.println("Delta con de "+b.getName());

						asEvolute=true;
					}
				
					
					// mise a jour du temps si imminent
				}
				else
					{
						if(imputFree==false )/* pas imminent && entr�e*/ 
						{
							b.delta_ext(outputs);
							System.out.println("Delta ext de "+b.getName());

							asEvolute=true;
						}
						// mise a jour du tr de tout les �lements
					}
				
				// e = currentTime - tfin - b.tr
				// temps interd d'evo = tps courant - temps final de simu - tps restant du compo
				 if (asEvolute)
				 {
					//b.setE(0);
					b.setTl(currentTime);
					//elem.setTl(elem.getTl() - currentTime);
					 b.setTr(b.getTa());						 // ancienne version : b.setTr(  b.getTa());
				 }
				 else
				 {
						b.setTr(b.getTr()-tmin);
					//	b.setE( b.getTa() - b.getTr() ); //currentTime + b.getTl() 
				 }

				System.out.println(text+"\t fin:"+b);
	
			}
			currentTime += tmin;
			// fin depuis l'exo 1
			// *********************************************************************************
		}
	}
	
	
}
