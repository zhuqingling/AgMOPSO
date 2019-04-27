//  CrossoverFactory.java
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

import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.util.HashMap;

/**
 * Class implementing a factory for crossover operators.
 */
public class CrossoverFactory {
    
  /**
   * Gets a crossover operator through its name.
   * @param name Name of the operator
   * @return The operator
   */
  public static Crossover getCrossoverOperator(String name, HashMap parameters) throws JMException {
    if (name.equalsIgnoreCase("SBXCrossover"))
      return new SBXCrossover(parameters);
    else if (name.equalsIgnoreCase("IMADE"))
        return new IMADECrossover(parameters);
    else if (name.equalsIgnoreCase("AHX"))
        return new AHX(parameters);
    else if (name.equalsIgnoreCase("NX"))
        return new NX(parameters);
    else if (name.equalsIgnoreCase("GHX"))
        return new GHX(parameters);
    else if (name.equalsIgnoreCase("HDE"))
        return new HDE(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover1"))
      return new SBXCrossover1(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover2"))
        return new SBXCrossover2(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover2_1"))
        return new SBXCrossover2_1(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover3"))
        return new SBXCrossover3(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover4"))
        return new SBXCrossover4(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover_adaptive"))
        return new SBXCrossover_adaptive(parameters);
    else if (name.equalsIgnoreCase("SBXCrossover5"))
        return new SBXCrossover5(parameters);
    else if (name.equalsIgnoreCase("SinglePointCrossover"))
      return new SinglePointCrossover(parameters);
    else if (name.equalsIgnoreCase("PMXCrossover"))
      return new PMXCrossover(parameters);
    else if (name.equalsIgnoreCase("TwoPointsCrossover"))
      return new TwoPointsCrossover(parameters);
    else if (name.equalsIgnoreCase("HUXCrossover"))
      return new HUXCrossover(parameters);
    else if (name.equalsIgnoreCase("DifferentialEvolutionCrossover"))
      return new DifferentialEvolutionCrossover(parameters);
    else if (name.equalsIgnoreCase("DifferentialEvolutionCrossoverFRRMAB"))
        return new DifferentialEvolutionCrossoverFRRMAB(parameters);
    else if (name.equalsIgnoreCase("BLXAlphaCrossover"))
      return new BLXAlphaCrossover(parameters);
    else if (name.equalsIgnoreCase("SPXCrossover"))
    	return new SPXCrossover(parameters);
    else {
      Configuration.logger_.severe("CrossoverFactory.getCrossoverOperator. " +
          "Operator '" + name + "' not found ");
      throw new JMException("Exception in " + name + ".getCrossoverOperator()") ;
    } // else        
  } // getCrossoverOperator
} // CrossoverFactory
