//  SBXCrossover.java
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

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.problems.ZDT.ZDT4;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class allows to apply a SPX crossover operator using three or more parent solutions.
 * this was complete by zhu qing ling, April 24,2014.
 * Any question please fell free to connect me. my email is qlzhu1991@gmial.com
 */
/**
 * @author qlzhu
 *
 */
public class SPXCrossover extends Crossover {
  /**
   * EPS defines the minimum difference allowed between real values
   */
  private static final double EPS= 1.0e-14;
                                                                                      
  private static final double ETA_C_DEFAULT_ = 20.0;
  private Double crossoverProbability_ = 0.9 ;
  private double distributionIndex_ = ETA_C_DEFAULT_;
  private double e_ = 1.0;
  private int parentsize_ = 3;

  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class,
                                                  ArrayRealSolutionType.class) ;
  
  /** 
   * Constructor
   * Create a new SBX crossover operator whit a default
   * index given by <code>DEFAULT_INDEX_CROSSOVER</code>
   */
  public SPXCrossover(HashMap<String, Object> parameters) {
  	super (parameters) ;
  	
  	if (parameters.get("probability") != null)
  		crossoverProbability_ = (Double) parameters.get("probability") ;  		
  	if (parameters.get("distributionIndex") != null)
  		distributionIndex_    = (Double) parameters.get("distributionIndex") ; 
  	if (parameters.get("e") != null)
  		e_ =(Double) parameters.get("e");
  	if(parameters.get("parentsize")!=null)
  		parentsize_ = Integer.valueOf(parameters.get("parentsize").toString());//IS THIS CONVERT RIGHT?
  	
  } // SBXCrossover
    
  /**
   * Perform the crossover operation. 
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offsprings
   */
  public Solution doCrossover(double probability, 
                                Solution parent1, 
                                Solution parent2,Solution parent) throws JMException {
	
    double rand;
    double y1, y2, yL, yu;
    double c1, c2;
    double alpha, beta, betaq;
    double valueX1,valueX2,valueX3;//,center;
    
	//XReal p1 = new XReal(parent1) ;		
	//XReal p2 = new XReal(parent2) ;	
	//XReal p3 = new XReal(parent);//this the real parent
    XReal[] P = new XReal[3];
	P[0] = new XReal(parent1);P[1] = new XReal(parent2);P[2] = new XReal(parent);
	
	Solution offSpring = new Solution(parent);
	XReal offs = new XReal(offSpring);
	//XReal x1 = new XReal(parent1) ;		
	//XReal x2 = new XReal(parent2) ;	
	//XReal x3 = new XReal(parent);//this the mid valued
	Solution [] x = new Solution[3];
	for(int i=0;i<3;i++){x[i]=new Solution(parent1);}
	XReal[] X = new XReal[3];
	X[0] = new XReal(new Solution(parent1));X[1] = new XReal(new Solution(parent1));X[2] = new XReal(new Solution(parent1));
	
	//XReal C1 = new XReal(parent1) ;		
	//XReal C2 = new XReal(parent2) ;	
	//XReal C3 = new XReal(parent);//this the mid value
	Solution [] c = new Solution[3];
	for(int i=0;i<3;i++){c[i]=new Solution(parent1);}
	XReal[] C = new XReal[3];
	C[0] = new XReal(c[0]);C[1] = new XReal(c[1]);C[2] = new XReal(c[2]);
	Solution cent = new Solution(parent1);
	XReal center = new XReal(cent);
	
	int numberOfVariables = P[0].getNumberOfDecisionVariables() ;

    if (PseudoRandom.randDouble() <= probability)
    {//begin to SPX
    	//1:caculate the center of parents
    	for (int i=0;i<numberOfVariables;i++)
    	{
    		valueX1 = P[0].getValue(i);
            valueX2 = P[1].getValue(i);
            valueX3 = P[2].getValue(i);
            center.setValue(i, (valueX1+valueX2+valueX3)/3);//this will set center.varialble[i] and cent.varialble[i]
            for(int j=0;j<3;j++)
            {
            	X[j].setValue(i, center.getValue(i)+e_*(P[j].getValue(i)-center.getValue(i)));
            }
    	}
    	//2:move every parents and relevent r value
    	for (int j=0;j<3;j++)
    	{
    		if (j==0)
    		{
    			for(int i=0;i<C[j].size();i++)
    			{//c0
    				C[j].setValue(i, 0);
    			}
    		}
    		else
    		{//c1 and c2
    			double r =Math.pow( PseudoRandom.randDouble(0, 1), 1/((double)j) );
    			for(int i=0;i<C[j].size();i++)
    			{
    				C[j].setValue(i, r*(X[j-1].getValue(i)-X[j].getValue(i)+C[j-1].getValue(i)));
    			}
    		}
    	}
    	//3:caculate c value
    	
    	//4:generate the child
    	for(int i=0;i<X[2].size();i++)
		{
			offs.setValue(i, X[2].getValue(i)+C[2].getValue(i));
		}
    	//5:control the boundery
    	for(int i=0; i<numberOfVariables; i++)
    	{
    		yL = offs.getLowerBound(i);
    		yu = offs.getUpperBound(i);
    		if(offs.getValue(i) < yL)
    		{
    			offs.setValue(i, yL);
    		}
    		if(offs.getValue(i) > yu)
    		{
    			offs.setValue(i, yu);
    		}
    	}
    } // if probability            
    return offSpring;                                                                                      
  } // doCrossover
  
  
  /**
  * Executes the operation
  * @param object An object containing an array of two parents
  * @return An object containing the offSprings
  */
  public Object execute(Object object) throws JMException {
    Solution [] parents = (Solution [])object;    	
    	
    if (parents.length != 3) {
      Configuration.logger_.severe("SBXCrossover.execute: operator needs two " +
          "parents");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;      
    } // if

    if (!(VALID_TYPES.contains(parents[0].getType().getClass())  &&
          VALID_TYPES.contains(parents[1].getType().getClass())  &&
          VALID_TYPES.contains(parents[2].getType().getClass())) ) {
      Configuration.logger_.severe("SBXCrossover.execute: the solutions " +
					"type " + parents[0].getType() + " is not allowed with this operator");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
         
    Solution offSpring;
    offSpring = doCrossover(crossoverProbability_,
                            parents[0],
                            parents[1],parents[2]);
        
        
    //for (int i = 0; i < offSpring.length; i++)
    //{
    //  offSpring[i].setCrowdingDistance(0.0);
    //  offSpring[i].setRank(0);
    //} 
    return offSpring;
  } // execute 

  public static void main(String[] args) throws ClassNotFoundException, JMException, Exception 
  {
		//1:对ZDT4生成popsize个个体 and get it's first front
	    Problem problem = new ZDT4("Real",10);
	    int popsize = 100;
	    SolutionSet population = new SolutionSet (popsize);
	    for (int i=0;i<popsize;i++)
	    {
		    Solution newSolution = new Solution(problem);
		    problem.evaluate(newSolution);
		    population.add(newSolution);
	    }
	    Ranking ranking = new Ranking(population);
	    SolutionSet front0 = ranking.getSubfront(0);
	    front0.Suppress();
	    
	    //2:select 3 parent solution from front0
	    SolutionSet parents = new SolutionSet(3);
	    if(front0.size()<3)
	    {
	    	return;
	    }
	    for (int i=0;i<3;i++)
	    {
	    	parents.add(front0.get(i));
	    }
	    parents.printVariablesToFile("parentsvar");
	    parents.printObjectivesToFile("parentsobj");
	    
	    //3:new a SPX crossover operator
	    HashMap parameters = new HashMap();
	    parameters.put("e", 1.0);
	    parameters.put("parentsize", 3);
	    SPXCrossover SPX = new SPXCrossover(parameters);
	    
	    Solution[] p = new Solution[3];
	    p[0]=parents.get(0);p[1]=parents.get(1);p[2]=parents.get(2);
	    SolutionSet children = new SolutionSet(100);//to save the children population
	    for (int i=0;i<100;i++)
	    {//generate child
		    Solution child = (Solution)SPX.execute(p);
		    problem.evaluate(child);
		    children.add(child);
	    }
	    children.printVariablesToFile("childrenvar");
	    children.printObjectivesToFile("childrenobj");
	    System.out.print("please use:Frontshow(parentsobj,'or');Frontshow(childrenobj,'*k'); to see object space result in matlab\n" +
	    		"and use:plot(parentsvar(:,2),parentsvar(:,3),'or');plot(childrenvar(:,2),childrenvar(:,3),'*k'); to see variable space result in matlab.");
	}
} // SBXCrossover
