/**
 * MOPSOD_main.java
 *
 * @author Noura Al Moubayed
 * 
 *
 */
package jmetal.metaheuristics.AgMOPSO;

import jmetal.core.*;
import jmetal.operators.clone.CloneFactory;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.*;
import jmetal.problems.DTLZ.*;
import jmetal.problems.WFG.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.cec2009Competition.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;


public class AgMOPSO_main {

	public static void printGD(String path,double[] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
	      BufferedWriter bw      = new BufferedWriter(osw)        ;               
	      for (int i = 0; i < GD.length; i++) {  
	        bw.write(GD[i]+" ");
	        bw.newLine();        
	      }
	      
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      Configuration.logger_.severe("Error acceding to the file");
	      e.printStackTrace();
	    }       
	  } // printGD
	
	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {
		TestStatistics sta = null;
		for(int fun=1;fun<=31;fun++){
			int runtimes=30;
			double[] IGDarray=new double[runtimes];
			
			Problem problem=null; // The problem to solve
			Algorithm algorithm; // The algorithm to use
			Operator clone = null; // Crossover operator
			Operator crossover ; // Crossover operator
			Operator mutation; // Mutation operator

			QualityIndicator indicators; // Object to get quality indicators

			indicators = null;

			for(int i=0;i<runtimes;i++){
			
			if (args.length == 1) {
				Object[] params = { "Real" };
				problem = (new ProblemFactory()).getProblem(args[0], params);
			} // if
			else if (args.length == 2) {
				Object[] params = { "Real" };
				problem = (new ProblemFactory()).getProblem(args[0], params);
				indicators = new QualityIndicator(problem, args[1]);
			} // if
			else { // Default problem
				if(fun==1){
			  	      problem = new ZDT1("Real");
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\ZDT1_501.txt" ) ;
			  	    	}//problem = new WFG1("Real");
			  	if(fun==2){
			  	      problem = new ZDT2("Real");
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\ZDT2_501.txt" ) ;
			  	    	}//problem = new WFG1("Real");
			  	if(fun==3){
			  	      problem = new ZDT3("Real");
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\ZDT3_269.txt" ) ;
			  	    	}
			  	if(fun==4){
				      problem = new ZDT4("Real",10);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\ZDT4_501.txt" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==5){
				      problem = new ZDT6("Real",10);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\ZDT6_774.txt" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==6){
				      problem = new DTLZ1("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ1.pf" ) ;
				    	}
			  	if(fun==7){
				      problem = new DTLZ2("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ2.pf" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==8){
				      problem = new DTLZ3("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ3.pf" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==9){
				      problem = new DTLZ4("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ4.pf" ) ;
				    	}
			  	if(fun==10){
				      problem = new DTLZ5("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ5.txt" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==11){
				      problem = new DTLZ6("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ6.txt" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==12){
				      problem = new DTLZ7("Real",10,3);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\DTLZ7.pf" ) ;
				    	}
				if(fun==13){
			  	      problem = new WFG1("Real",8,2,2);
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG1_605.txt" ) ;
			  	    	}//problem = new WFG1("Real");
			  	if(fun==14){
			  	      problem = new WFG2("Real",8,2,2);
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG2_111.txt" ) ;
			  	    	}//problem = new WFG1("Real");
			  	if(fun==15){
			  	      problem = new WFG3("Real",8,2,2);
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG3_301.txt" ) ;
			  	    	}
				if(fun==16){
				      problem = new WFG4("Real",8,2,2);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG4_1181.txt" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==17){
				      problem = new WFG5("Real",8,2,2);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG5.2D.pf" ) ;
				    	}
			  	if(fun==18){
			  	      problem = new WFG6("Real",8,2,2);
			  	      
			  	      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG6.2D.pf" ) ;
			  	    	}//problem = new WFG1("Real");
			  	if(fun==19){
				      problem = new WFG7("Real",8,2,2);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG7.2D.pf" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==20){
				      problem = new WFG8("Real",8,2,2);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG8.2D.pf" ) ;
				    	}
				if(fun==21){
				      problem = new WFG9("Real",8,2,2);
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\WFG9.2D.pf" ) ;
				    	}//problem = new WFG1("Real");
				if(fun==22){
				      problem = new UF1("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF1.DAT" ) ;//.txt
				    	}
				if(fun==23){
				      problem = new UF2("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF2.DAT" ) ;
				    	}
				if(fun==24){
				      problem = new UF3("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF3.DAT" ) ;
				    	}
				if(fun==25){
				      problem = new UF4("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF4.DAT" ) ;
				    	}
				if(fun==26){
				      problem = new UF5("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF5.DAT" ) ;
				    	}
				if(fun==27){
				      problem = new UF6("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF6.DAT" ) ;
				    	}
				if(fun==28){
				      problem = new UF7("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF7.DAT" ) ;
				    	}
				if(fun==29){
				      problem = new UF8("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF8.DAT" ) ;
				    	}
				if(fun==30){
				      problem = new UF9("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF9.DAT" ) ;
				    	}
				if(fun==31){
				      problem = new UF10("Real");
				      
				      indicators = new QualityIndicator(problem,"C:\\jMetal\\Pareto_front\\UF10.DAT" ) ;
				    	}
			} // else

			SolutionSet population = null;
			algorithm = new AgMOPSO(problem, 1);
			algorithm.setInputParameter("functionType", "_NBI");

			
			algorithm.setInputParameter("maxIterations", 300);
			if (problem.getNumberOfObjectives()==2){
				algorithm.setInputParameter("swarmSize", 100);
				// Clone operator
				HashMap<String, Integer> parameters = new HashMap<String, Integer>();
				parameters.put("clonesize", 100);
				clone = CloneFactory.getClone("proportionalclone", parameters);
			}else if(problem.getNumberOfObjectives()==3){
				algorithm.setInputParameter("swarmSize", 300);
				// Clone operator
				HashMap<String, Integer> parameters = new HashMap<String, Integer>();
				parameters.put("clonesize", 300);
				clone = CloneFactory.getClone("proportionalclone", parameters);
			}
			
			HashMap<String, Double> parameters = new HashMap<String, Double>();
		    parameters.put("probability", 0.9);
		    parameters.put("distributionIndex", 20.0);
		    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters); 

		    parameters = new HashMap<String, Double>();
		    parameters.put("probability", 1.0/problem.getNumberOfVariables());
		    parameters.put("distributionIndex", 20.0) ;
		    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters); 
			
		    // Add the operators to the algorithm
		    algorithm.addOperator("clone", clone);
		    algorithm.addOperator("crossover",crossover);
		    algorithm.addOperator("mutation",mutation);
		    
			population = algorithm.execute();

			population.printObjectivesToFile("AgMOPSO_"+problem.getName()+"_"+problem.getNumberOfObjectives()+"_"+"T"+(i+1));
		    IGDarray[i]=indicators.getCEC_IGD(population);
		}
		Arrays.sort(IGDarray);
		printGD("AgMOPSO_"+problem.getName()+"_"+problem.getNumberOfObjectives()+"_IGD",IGDarray);
		sta = new TestStatistics(IGDarray);
		System.out.println(sta.getAverage()+"\t"+sta.getStandardDiviation());
		} //runtimes
	} // main
} // MOPSOD_main
