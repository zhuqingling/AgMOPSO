//  PropagationMachanism.java
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

package jmetal.operators.localSearch;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.crossover.SPXCrossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.ZDT.ZDT4;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;
import jmetal.util.wrapper.XReal;

import java.util.Comparator;
import java.util.HashMap;

/**
 * This class implements an local search operator based in the use of a 
 * mutation operator. An archive is used to store the non-dominated solutions
 * found during the search.
 */
public class PropagationMachanism extends LocalSearch {
    
  /**
   * Stores the problem to solve
   */
  private Problem problem_;
    
  /**
  * Stores a reference to the archive in which the non-dominated solutions are
  * inserted
  */
  private SolutionSet archive_;

  private int improvementRounds_ ; 

  /**
   * Stores comparators for dealing with constraints and dominance checking, 
   * respectively.
   */
  private Comparator constraintComparator_ ;
  private Comparator dominanceComparator_ ;
  
  /**
   * Stores the mutation operator 
   */
  private Operator mutationOperator_;
  
  /**
   * Stores the number of evaluations_ carried out
   */
  int evaluations_ ;  
  
  /**
  * Constructor. 
  * Creates a new local search object.
  * @param parameters The parameters

  */
  public PropagationMachanism(HashMap<String, Object> parameters) {
  	super(parameters) ;
  	if (parameters.get("problem") != null)
  		problem_ = (Problem) parameters.get("problem") ;  		
  	if (parameters.get("improvementRounds") != null)
  		improvementRounds_ = (Integer) parameters.get("improvementRounds") ;  		
  	if (parameters.get("mutation") != null)
  	  mutationOperator_ = (Mutation) parameters.get("mutation") ;  		

    evaluations_          = 0      ;
    archive_              = null;
    dominanceComparator_  = new DominanceComparator();
    constraintComparator_ = new OverallConstraintViolationComparator();
  } //Mutation improvement


  /**
  * Constructor. 
  * Creates a new local search object.
  * @param problem The problem to solve
  * @param mutationOperator The mutation operator 
  */
  //public MutationLocalSearch(Problem problem, Operator mutationOperator) {
  //  evaluations_ = 0 ;
  //  problem_ = problem;
  //  mutationOperator_ = mutationOperator;
  //  dominanceComparator_ = new DominanceComparator();
  //  constraintComparator_ = new OverallConstraintViolationComparator();
  //} // MutationLocalSearch
  
 /**
   * Executes the local search. The maximum number of iterations is given by 
   * the param "improvementRounds", which is in the parameter list of the 
   * operator. The archive to store the non-dominated solutions is also in the 
   * parameter list.
   * @param object Object representing a solution
   * @return An object containing the new improved solution
 * @throws JMException 
   */
  public Object execute(Object object) throws JMException {
    int size = improvementRounds_;//propagation size
    evaluations_ = 0;        
    SolutionSet front = (SolutionSet)object;
    SolutionSet Archive = new SolutionSet(size);//save propagation set

   
    //archive_ = (SolutionSet)getParameter("archive");
    int frontsize = front.size();
	if (frontsize <= 2)
	{   
    	//disturbense
    	while(Archive.size()<size)
    	{
	    	//1:random select a solution from front
	    	Solution newsolution = new Solution(front.get(PseudoRandom.randInt(0, frontsize-1)));
	    	//2:disturbe this solution
	    	//2.1:random select a dim of this solution and set to a value between bl and bu
	    	XReal help = new XReal(newsolution);
	    	int dim = PseudoRandom.randInt(0, help.getNumberOfDecisionVariables()-1);
	    	help.setValue(dim,PseudoRandom.randDouble(help.getLowerBound(dim),help.getUpperBound(dim)));
	    	problem_.evaluate(newsolution);evaluations_++;
	    	Archive.add(newsolution);
    	}
    	//System.out.println(evaluations_);
    	return Archive;
    }
    else
    {
    	//generate size solution according to crowdingDistance 
    
    	//1:allocate crowding distance to the boundery solution
    	double min_distance=0.0;
		double max_distance=1.0;
	    double sum_distance=0.0;
		//find max_distance and min_distance,and set the boundery solution to 2*max_distance
		//note that parents is sorted by crowding distance, this will easier to understand alg below
		for (int k = 0; k < front.size(); k++ ) 
		{
		    if(front.get(k).getCrowdingDistance()!=Double.POSITIVE_INFINITY)
		    {//k is the first not the boundery solution
		    	max_distance=2*front.get(k).getCrowdingDistance();
				min_distance=front.get(front.size()-1).getCrowdingDistance();
			    for(int l=0;l<k;l++)
			    {
			    	front.get(l).setCrowdingDistance(2*front.get(k).getCrowdingDistance());
			    }
			    break;
		   }
		} // for
		//this situation is that all the parents are boundery solution than set CD to 1.0
		if(front.get(0).getCrowdingDistance()==Double.POSITIVE_INFINITY)
		{
			for(int l=0;l<front.size();l++)
		    {
				front.get(l).setCrowdingDistance(1.0);
		    } 
		}//if all the points are in extreme region.
		//calculate the sum of crowding distance  
    	for (int k = 0; k < frontsize; k++ ) 
		{
		    sum_distance+=front.get(k).getCrowdingDistance();
		    front.get(k).setmaxDistance(max_distance);
		    front.get(k).setminDistance(min_distance);
		} // for
		
		double[] clones=new double[frontsize];//the probability to generate a solution
		for(int k=0;k<frontsize;k++)
		{
		    clones[k]= front.get(k).getCrowdingDistance()/sum_distance;
		    if(k>0)clones[k]+=clones[k-1];
			if(sum_distance==0)//this may not happen forever
			{//all individual are to one point
				clones[k]=(double)1.0/frontsize;
				System.out.print("zeros");
				System.out.print(clones[k]+" ");
			}
		}
		//begin to generate solution
		while(Archive.size()<size)
	    {
			double chaosnum = PseudoRandom.randDouble();
			int k;//the selected solution
			for (k=0;k<frontsize;k++)
			{//roulette select a solution
				if(chaosnum<clones[k]) 
					break;
			}
			Solution solution = front.get(k);
			Solution mutatedSolution = new Solution(solution);
	        mutationOperator_.execute(mutatedSolution);
	        problem_.evaluate(mutatedSolution);evaluations_++;
	        Archive.add(mutatedSolution);
	    }
    }
    return Archive;
  } // execute
  
   
  /** 
   * Returns the number of evaluations maded
   */
  public int getEvaluations() {
    return evaluations_;
  } // evaluations
  
  public static void main(String[] args) throws ClassNotFoundException, JMException, Exception {
		//对ZDT4生成popsize个个体
	    Problem problem = new ZDT4("Real",10);
	    int popsize = 100;
	    SolutionSet population = new SolutionSet (popsize);
	    Distance distance = new Distance();
	    for (int i=0;i<popsize;i++)
	    {
		    Solution newSolution = new Solution(problem);
		    problem.evaluate(newSolution);
		    population.add(newSolution);
	    }
	    //get population's first front
	    Ranking ranking = new Ranking(population);
	    SolutionSet front0 = ranking.getSubfront(0);
	    front0.Suppress();
	    //calculate crowdingDistance
		distance.crowdingDistanceAssignment(front0,
				problem.getNumberOfObjectives());
		//sort according to crowdingDistance
		front0.sort(new CrowdingComparator());
	    front0.printVariablesToFile("parentsvar");
	    front0.printObjectivesToFile("parentsobj");
	    
	    //spacify the  mutate operator
	    HashMap parameters = new HashMap();
	    parameters.put("probability", 0.5);
	    parameters.put("distributionIndex", 20.0);
	    Operator mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
	    
	    //add localsearch operator
	    int Archivesize = 20;//
	    parameters = new HashMap();
	    parameters.put("problem", problem);
	    parameters.put("improvementRounds", Archivesize);
	    parameters.put("mutation", mutation);
	    PropagationMachanism LocalSearch = new PropagationMachanism(parameters);
	    
	    SolutionSet Archive = new SolutionSet(Archivesize);
	    Archive = (SolutionSet)LocalSearch.execute(front0);
	    System.out.println("total evaluations is:" + LocalSearch.getEvaluations());
	    Archive.printVariablesToFile("childrenvar");
	    Archive.printObjectivesToFile("childrenobj");
	    System.out.println("propagation over");
	}
} // MutationLocalSearch
