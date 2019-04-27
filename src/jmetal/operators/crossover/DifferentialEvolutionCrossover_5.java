//  DifferentialEvolutionCrossover.java
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

package jmetal.operators.crossover;

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

/**
 * Differential evolution crossover operators
 * Comments:
 * - The operator receives two parameters: the current individual and an array
 *   of three parent individuals
 * - The best and rand variants depends on the third parent, according whether
 *   it represents the current of the "best" individual or a randon one. 
 *   The implementation of both variants are the same, due to that the parent 
 *   selection is external to the crossover operator. 
 * - Implemented variants:
 *   - rand/1/bin (best/1/bin)
 *   - rand/1/exp (best/1/exp)
 *   - current-to-rand/1 (current-to-best/1)
 *   - current-to-rand/1/bin (current-to-best/1/bin)
 *   - current-to-rand/1/exp (current-to-best/1/exp)
 */
public class DifferentialEvolutionCrossover_5 extends Crossover {
	/**
	 * DEFAULT_CR defines a default CR (crossover operation control) value
	 */
	private static final double DEFAULT_CR = 0.5;

	/**
	 * DEFAULT_F defines the default F (Scaling factor for mutation) value
	 */
	private static final double DEFAULT_F = 0.5;

	/**
	 * DEFAULT_K defines a default K value used in variants current-to-rand/1
	 * and current-to-best/1
	 */
	private static final double DEFAULT_K = 0.5;

	/**
	 * DEFAULT_VARIANT defines the default DE variant
	 */

	private static final String DEFAULT_DE_VARIANT = "rand/1/bin";

  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class,
  		                                            ArrayRealSolutionType.class) ;

	private double CR_  ;
	private double F_   ;
	private double K_   ;
	private String DE_Variant_ ; // DE variant (rand/1/bin, rand/1/exp, etc.)

	/**
	 * Constructor
	 */
	public DifferentialEvolutionCrossover_5(HashMap<String, Object> parameters) {
		super(parameters) ;
		
		CR_ = DEFAULT_CR ;
		F_  = DEFAULT_F  ;
		K_  = DEFAULT_K   ;
		DE_Variant_ = DEFAULT_DE_VARIANT ;
		
  	if (parameters.get("CR") != null)
  		CR_ = (Double) parameters.get("CR") ;  		
  	if (parameters.get("F") != null)
  		F_ = (Double) parameters.get("F") ;  		
  	if (parameters.get("K") != null)
  		K_ = (Double) parameters.get("K") ;  		
  	if (parameters.get("DE_VARIANT") != null)
  		DE_Variant_ = (String) parameters.get("DE_VARIANT") ;  		

	} // Constructor


	/**
	 * Constructor
	 */
	//public DifferentialEvolutionCrossover(Properties properties) {
	//	this();
	//	CR_ = (new Double((String)properties.getProperty("CR_")));
	//	F_  = (new Double((String)properties.getProperty("F_")));
	//	K_  = (new Double((String)properties.getProperty("K_")));
	//	DE_Variant_ = properties.getProperty("DE_Variant_") ;
	//} // Constructor

	/**
	 * Executes the operation
	 * @param object An object containing an array of three parents
	 * @return An object containing the offSprings
	 */
	public Object execute(Object object) throws JMException {
		if (parameters_.get("CR") != null)
	  		CR_ = (Double) parameters_.get("CR") ;  		
	  	if (parameters_.get("F") != null)
	  		F_ = (Double) parameters_.get("F") ;  		
	  	if (parameters_.get("K") != null)
	  		K_ = (Double) parameters_.get("K") ;  		
	  	if (parameters_.get("DE_VARIANT") != null)
	  		DE_Variant_ = (String) parameters_.get("DE_VARIANT") ;
		Object[] parameters = (Object[])object ;
		Solution current   = (Solution) parameters[0];
		Solution [] parent = (Solution [])parameters[1];

		Solution child ;
		
    if (!(VALID_TYPES.contains(parent[0].getType().getClass()) &&
          VALID_TYPES.contains(parent[1].getType().getClass()) &&
          VALID_TYPES.contains(parent[2].getType().getClass())) ) {

			Configuration.logger_.severe("DifferentialEvolutionCrossover.execute: " +
					" the solutions " +
					"are not of the right type. The type should be 'Real' or 'ArrayReal', but " +
					parent[0].getType() + " and " + 
					parent[1].getType() + " and " + 
					parent[2].getType() + " are obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		}

		int jrand ;

		child = new Solution(current) ;
		
		XReal xParent0 = new XReal(parent[0]) ;
		XReal xParent1 = new XReal(parent[1]) ;
		XReal xParent2 = new XReal(parent[2]) ;
		XReal xParent3 = new XReal(parent[3]) ;
		XReal xParent4 = new XReal(parent[4]) ;
		XReal xCurrent = new XReal(current) ;
		XReal xChild   = new XReal(child) ;

		int numberOfVariables = xParent0.getNumberOfDecisionVariables() ;
		jrand = PseudoRandom.randInt(0, numberOfVariables - 1);

		// STEP 4. Checking the DE variant
		if ((DE_Variant_.compareTo("rand/1/bin") == 0) || 
				(DE_Variant_.compareTo("best/1/bin") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
							                                  xParent1.getValue(j)) ;
					value = resetBoundedValue(value, xChild, j);
//					if (value < xChild.getLowerBound(j))
//						value =  xChild.getLowerBound(j) ;
//					if (value > xChild.getUpperBound(j))
//						value = xChild.getUpperBound(j) ;
          /*
					if (value < xChild.getLowerBound(j)) {
            double rnd = PseudoRandom.randDouble(0, 1) ;
            value = xChild.getLowerBound(j) + rnd *(xParent2.getValue(j) - xChild.getLowerBound(j)) ;
					}
          if (value > xChild.getUpperBound(j)) {
            double rnd = PseudoRandom.randDouble(0, 1) ;
            value = xChild.getUpperBound(j) - rnd*(xChild.getUpperBound(j)-xParent2.getValue(j)) ;
          }
          */
					xChild.setValue(j, value) ;
				}
				else {
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for
		} // if
		else if ((DE_Variant_.compareTo("rand/2/bin")==0)
				|| (DE_Variant_.compareTo("best/2/bin")==0)){
			for (int j=0; j<numberOfVariables; j++){
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value;
					value = xParent2.getValue(j) + F_
							* (xParent0.getValue(j) - xParent1.getValue(j))
							+ F_
							* (xParent3.getValue(j) - xParent4.getValue(j));
					value = resetBoundedValue(value, xChild, j);
					xChild.setValue(j, value);
				} else {
					double value;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value);
				} // else
			}
		}
		else if ((DE_Variant_.compareTo("rand/1/exp") == 0) || 
				     (DE_Variant_.compareTo("best/1/exp") == 0)) {
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
							xParent1.getValue(j)) ;

					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					CR_ = 0.0 ;
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
			  } // else
			} // for		
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/1") == 0) || 
             (DE_Variant_.compareTo("current-to-best/1") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				double value ;
				K_ = PseudoRandom.randDouble(0, 1);
//				K_ = F_;
				value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
					    xCurrent.getValue(j)) +					
						  F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;

				xChild.setValue(j, value) ;
			} // for		
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/1/bin") == 0) ||
				     (DE_Variant_.compareTo("current-to-best/1/bin") == 0)) { 
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					K_ = PseudoRandom.randDouble(0, 1);
//					K_ = F_;
//					value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
//							xCurrent.getValue(j)) +					
//							F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

					value = xCurrent.getValue(j) + 				
							F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;
							
					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for
		} // if
		else if ((DE_Variant_.compareTo("current-to-rand/2") == 0)
				|| (DE_Variant_.compareTo("current-to-best/2") == 0)) {
			for (int j = 0; j < numberOfVariables; j++) {
				double value;
				K_ = PseudoRandom.randDouble(0, 1);
//				K_ = F_;
				value = xCurrent.getValue(j) + K_
						* (xParent2.getValue(j) - xCurrent.getValue(j))
						+ F_
						* (xParent0.getValue(j) - xParent1.getValue(j))
						+ F_
						* (xParent3.getValue(j) - xParent4.getValue(j));
				value = resetBoundedValue(value, xChild, j);
				xChild.setValue(j, value);
			} // for
		}
		else if ((DE_Variant_.compareTo("current-to-rand/2/bin") == 0)
				|| (DE_Variant_.compareTo("current-to-best/2/bin") == 0)) {
			for (int j = 0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value;
					K_ = PseudoRandom.randDouble(0, 1);
//					K_ = F_;
					value = xCurrent.getValue(j) + K_
							* (xParent2.getValue(j) - xCurrent.getValue(j))
							+ F_
							* (xParent0.getValue(j) - xParent1.getValue(j))
							+ F_
							* (xParent3.getValue(j) - xParent4.getValue(j));
					value = resetBoundedValue(value, xChild, j);
					xChild.setValue(j, value);
				} else {
					double value;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value);
				}
			} // for
		} 
		else if ((DE_Variant_.compareTo("current-to-rand/1/exp") == 0) || 
				(DE_Variant_.compareTo("current-to-best/1/exp") == 0)) {
			for (int j=0; j < numberOfVariables; j++) {
				if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
					double value ;
					value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
							xCurrent.getValue(j)) +					
							F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

					if (value < xChild.getLowerBound(j))
						value =  xChild.getLowerBound(j) ;
					if (value > xChild.getUpperBound(j))
						value = xChild.getUpperBound(j) ;

					xChild.setValue(j, value) ;
				}
				else {
					CR_ = 0.0 ;
					double value ;
					value = xCurrent.getValue(j);
					xChild.setValue(j, value) ;
				} // else
			} // for		
		} // if		
		else {
			Configuration.logger_.severe("DifferentialEvolutionCrossover.execute: " +
					" unknown DE variant (" + DE_Variant_ + ")");
			Class<String> cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		} // else
		return child ;
	}
	
	
private double resetBoundedValue(double value, XReal xChild, int j){
		
		try {
			if (value < xChild.getLowerBound(j))
				value = xChild.getLowerBound(j);			
			if (value > xChild.getUpperBound(j))
				value = xChild.getUpperBound(j);			
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;		
	}
	
	private double resetMiddleValue(double value, XReal xChild, int j){
		
		try {
			double lb = xChild.getLowerBound(j);
			double ub = xChild.getUpperBound(j);
			
			if (value < lb){
				if(ub < 2 * lb - value){
					value = ub;
				}else{
					value = 2 * lb - value;
				}			
			}
			
			if (value > ub){
				if(lb > 2 * ub - value){
					value = lb;
				}else{
					value = 2 * ub - value;
				}
			}
							
			
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;		
	}
} // DifferentialEvolutionCrossover
