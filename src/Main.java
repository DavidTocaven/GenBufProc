import java.util.ArrayList;
import java.util.HashMap;

import chart.*;
import model.*;
// M2 ISTR - 2017/2018 -
// Groupe Etudians : 
//					Lucien RAKOTOMALALA
//					David TOCAVEN





////////////////////////////////////////////////////////////////////
// H�berg� sur GitHub : https://github.com/DavidTocaven/GenBufProc
////////////////////////////////////////////////////////////////////

public class Main {
/****************************** Main *********************************************/
	public static void main(String args[])
	{
		
		//Attention
		//Les ports sont connectés par leur nom: pour connecter deux ports, ils doivent avoir le même nom
		//Pour l'affichage des charts, deux composants ne peuvent pas avoir le même nom de variable
		
		//
		ArrayList<AtomicComponent> atomicArray = new ArrayList<>();
		
		//******Instantiation des composants atomiques ****** INSERER ICI VOS COMPOSANTS
		
		Step1 step1 = new Step1("step1");
		Step2 step2 = new Step2("step2");
		Step3 step3 = new Step3("step3");
		Step4 step4 = new Step4("step4");
		Adder adder = new Adder("adder");
		Euler euler = new Euler("euler");
		
		//Qss qss = new Qss("qss");  
		//Buf  buf  = new Buf("Buf");
		//Gen  gen  = new Gen("Gen");
		//Proc proc = new Proc("Proc");
		// 
		// ajout des atomic dans le mod�le
		atomicArray.add(step1);
		atomicArray.add(step2);
		atomicArray.add(step3);
		atomicArray.add(step4);
		atomicArray.add(adder);
		atomicArray.add(euler);
		//atomicArray.add(qss); 
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
		
		double currentTime = 0; // t : le temps pr�sent
		double tfin = 2; // Dur�e de la simulation
		
		//*************************** BOUCLE DE SIMULATION *********************************
		while(currentTime < tfin)
		{	
			//Envoie des données aux charts ******NE PAS MODIFIER********
			for(String var : integer_variables)
				vars_chart.get(var).addDataToSeries(currentTime, vars_atom.get(var).getIntegerValue(var));
			
			for(String var : real_variables)
				vars_chart.get(var).addDataToSeries(currentTime, vars_atom.get(var).getRealValue(var));

			// *********************************************************************************
			ArrayList<AtomicComponent> imminentComponent = new ArrayList<AtomicComponent>(); // liste des composants imminents
			ArrayList<String> outputs = new ArrayList<String>(); // liste des sorties 
			double tmin = Double.POSITIVE_INFINITY;		// tmin =  + l'infini
			
			
			
			/*  Parcours de tous Les atomics components*/
			for(AtomicComponent elem : atomicArray)// Pour tous les composants de la simulation 
			{	
				if(elem.getTr()<tmin)// Si le Tr de l'atomic est inf�rieur � tmin  
				{
						tmin = elem.getTr()	;			// mise � jour de tmin 
						imminentComponent.clear(); 		// remplacement du/des composant(s) le plus imminent par celui-ci  		
						imminentComponent.add(elem)	;	
				}
				else if(elem.getTr()==tmin)// si des composants imminents ont le m�me tmin
				{
					imminentComponent.add(elem); // Alors j'ajoute l'�l�ment aux composants iminents
				}
			}
			
			for(AtomicComponent elem : atomicArray)// Pour tous les �l�ments de la simulation
			{
				elem.setE(currentTime - elem.getTl());	 // Temps du point interm�diaire = temps d�but de l'it�ration - temps actuel
			}
			HashMap<String, AtomicComponent> output_atom = new HashMap<>();   	// hasmap contenant les sorties des composants imminents et le 
																				// composant atomque auquel elle appartient
			ArrayList<String> output_of_component = new ArrayList<>();	// les sorties du composant
			/* Ex�cution sortie des �l�ments imminents  */
			for(AtomicComponent i : imminentComponent) // pour tous les composants imminuents
			{
				outputs.addAll(i.lambda()); 	// Ajout de toutes les sorties du composants dans outputs 
				output_of_component= i.lambda();  // les sorties du composants valent le lambda de l'imminent
				for(String s : output_of_component) // pour tous les lambda de l'imminent
				{
					output_atom.put(s, i); // ajout dans output_atom pour lambda du composant imminent 
				}
			}
			
			/* Boucle pour delta --> TRANSITIONS */
			for(AtomicComponent b : atomicArray) 	// pour tous les �l�ments	
			{
				Boolean imputFree=true; // true = pas d'entr�e(s) ; false = des entr�e(s)
				for (String s : outputs) // pour toutes les sorties
				{
					if(b.getInputs().contains(s)) // si au moins une entr�e de b est une sortie s active
					{
						b.writeRealInputValue(s,
											  output_atom.get(s).readRealOutputValue(s)); // ajout de la sortie
						imputFree = false;
					}
				}
				boolean hasEvolute = false; // true = a �volu� ; false = n'as pas �volu� 
				if(imminentComponent.contains(b))/*Si imminent*/
				{
					if( imputFree==true) /*si pas d'entr�e(s)*/ 
					{
						b.delta_int(); // transitions internes 

						hasEvolute=true;
					}
					else /* si entr�e(s)*/
					{
						b.delta_con(outputs); // cas de conflict, choix de la prio d�crit dans l'atomic component concern�

						hasEvolute=true;
					}
									
				}
				else // si non imminent
					{
						if(imputFree==false )/* si pas imminent && entr�e*/ 
						{
							b.delta_ext(outputs);
							hasEvolute=true;
						}
					}
				
				 if (hasEvolute) // si au moins une transition a �t� franchie
				 {
					 b.setTl(currentTime); // Temps de fin de l'it�ration de l'atomique = le temps courant
					 b.setTr(b.getTa());   // Temps d'attente restant de l'atomique = temps actuel de l'actomique 
				 }
				 else
				 {
					 b.setTr(b.getTr()-tmin); // Temps d'attente restant de l'atomique = Temps d'attente restant de l'atomique moins le temps �coul�  
				 }	
			}
			currentTime += tmin; // mise � jours du temps courant.
		}
		//*********************** FIN BOUCLE DE SIMULATION *********************************
	}
	
	
}
