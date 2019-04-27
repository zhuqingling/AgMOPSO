//  PolynomialMutation.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class implements a polynomial mutation operator.
 */
public class iAdaptiveMutation extends Mutation {
	
	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays.asList(
			RealSolutionType.class, ArrayRealSolutionType.class);
	 public static final double mutaindvalue = 3;
	    
	  /**
	  * eta_c stores the index for mutation to use
	  */
	  private double mutaind;
	  private Double mutationProbability_ = null;
	   
	/**
	 * Constructor Creates a new instance of the polynomial mutation operator
	 */
	public iAdaptiveMutation(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("probability") != null)
			mutationProbability_ = (Double) parameters.get("probability");
		mutaind = mutaindvalue;
	} // PolynomialMutation

	/**
	 * Perform the mutation operation
	 * 
	 * @param probability
	 *            Mutation probability
	 * @param solution
	 *            The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability,int it, Solution solution)
			throws JMException {
		double rnd, delta1, delta2, mut_pow, deltaq;
	    double y, yl, yu, val, xy;
	    double chaosnum;
	    XReal x = new XReal(solution);
	    for (int var=0; var < solution.numberOfVariables(); var++)
	    {
	    	Random r=new Random();
	      if (PseudoRandom.randDouble() <= probability)
	      {
	        y      = x.getValue(var);
	        yl     = x.getLowerBound(var);
			yu     = x.getUpperBound(var);
	        delta1 = (y-yl)/(yu-yl);
	        delta2 = (yu-y)/(yu-yl);
	        rnd = PseudoRandom.randDouble(); 
	        //chaosnum=solution.getmutationscale();
	        chaosnum=PseudoRandom.randDouble();
	        //while(y<yl||y>yu)
	        //{
	        if (PseudoRandom.randDouble() <= 0.5)
	        {
	        	//*Math.exp(-1.5*Math.pow((double)solution.ev/solution.maxev,0.6)) 
	        	if(solution.getmaxDistance()-solution.getminDistance()!=0){
	        		//deltaq = -0.1*Math.pow(chaosnum,mutaindvalue)*Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance());
	        		//2.deltaq = -0.1*Math.pow(chaosnum,3.0)*Math.exp(solution.getFitness());
	        		//3.deltaq = -0.8*solution.getFitness();
	        		//4.
	        		deltaq = -0.05*Math.pow(1.0-(double)it/250.0,1.0/2.0)*Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance());
	        	}
	        	else{
	        		deltaq = -0.10*Math.pow(1.0-(double)it/250.0,1.0/2.0);//for ZDT4
	        		//deltaq=-0.15;
	        	}      
	        	//System.out.print(solution.getCrowdingDistance()+ "  "+ solution.getmaxDistance());
	        	//deltaq = -Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance())*solution.getmutationscale();       
	        }
	        else
	        {
	        	if(solution.getmaxDistance()-solution.getminDistance()!=0){
	        		//deltaq =  0.1*Math.pow(chaosnum, mutaindvalue)*Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance());
	        		//2.deltaq = 0.1*Math.pow(chaosnum,3.0)*Math.exp(solution.getFitness());
	        		//3. 
	        		deltaq = 0.05*Math.pow(1.0-(double)it/250.0,1.0/2.0)*Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance());
	        	}
	        	else{
	        		deltaq = 0.10*Math.pow(1.0-(double)it/250.0,1.0/2.0);//for ZDT4
	        		//deltaq=0.15;
	        	}
	        	//deltaq = Math.exp(solution.getCrowdingDistance()/solution.getmaxDistance())*solution.getmutationscale(); 
	        }
	        y = y + deltaq*r.nextGaussian()*(yu-yl);
	        //solution.age_=0;
	        //solution.setmutationscale(PseudoRandom.randDouble());
	       // System.out.print(solution.getmutationscale()+"  ");
	       // printGD("mudata37",deltaq);
	      //}
	        if (y<yl)
	          y = yl;
	        if (y>yu)
	          y = yu;
	       // solution.getDecisionVariables().variables_[var].setValue(y);    
	        x.setValue(var, y);
	      }
	    }  

	} // doMutation

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing a solution
	 * @return An object containing the mutated solution
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Object[] parameters = (Object[])object ;
		int gen = (Integer) parameters[0];
		Solution solution = (Solution) parameters[1];

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_
					.severe("PolynomialMutation.execute: the solution "
							+ "type " + solution.getType()
							+ " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		doMutation(mutationProbability_,gen, solution);
		return solution;
	} // execute

} // PolynomialMutation
