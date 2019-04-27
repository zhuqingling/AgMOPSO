package jmetal.metaheuristics.AgMOPSO;

import jmetal.metaheuristics.AgMOPSO.Utils; //used for calculate neighbor individual
import jmetal.util.*;

import java.util.Vector;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.archive.CrowdingArchive;
import jmetal.core.*;
import jmetal.util.PseudoRandom;

public class AgMOPSO extends Algorithm {

	private static final long serialVersionUID = 2107684627645440737L;
	private Problem problem;

	int run;
	int T_;
	int[][] neighborhood_;
	public String curDir = System.getProperty("user.dir");

	private int populationSize;
	/**
	 * Stores the population
	 */
	private SolutionSet population,temppopulation,cpopulation;
	/**
	 * Z vector (ideal point)
	 */
	double[] idealPoint;
	/**
	 * Lambda vectors
	 */

	double[][] lamdaVectors;
	
	int[] leader_ind;

	/**
	 * Stores the velocity of each particle
	 */
	private double[][] velocity;
	/**
	 * Stores the personal best solutions found so far for each particle
	 */
	private Solution pbest_[];// _[];

	/**
	 * Stores the none dominated leaders
	 */
	private CrowdingArchive archive;

	int H_;

	Solution[] indArray_;

	// select the aggregation function to be used
	String functionType_;

	// store the number of the particles' evaluations
	int iteration;
	Operator cloneoperator;
	Operator mutationOperator;
	Operator crossoverOperator;
	
	int maxIterations;
	
	private Distance distance_;

	public AgMOPSO(Problem problem, int r) {
		super(problem);
		this.problem=problem;
		this.run = r;
	} // MOPSOD

	public SolutionSet execute() throws JMException, ClassNotFoundException {

		// to make the algo faster use archiveSize param instead of 100000, this
		// is used here to retrieve as much as possible non-dominated solutions
		
		

		functionType_ = ((String) getInputParameter("functionType"));
		maxIterations = ((Integer) this.getInputParameter("maxIterations"))
				.intValue();
		populationSize = ((Integer) this.getInputParameter("swarmSize"))
				.intValue();

		archive = new CrowdingArchive(populationSize, problem.getNumberOfObjectives());
		int clonesize = (int)populationSize/5;
		SolutionSet clonepopulation = new SolutionSet(clonesize);
		int evelations=0;
		int max_evelations=populationSize*maxIterations;

		iteration = 0;
		pbest_ = new Solution[this.populationSize];
		population = new SolutionSet(populationSize);
		cpopulation = new SolutionSet(populationSize);
		temppopulation=new SolutionSet(populationSize);
		indArray_ = new Solution[problem.getNumberOfObjectives()];

		// dominance_ = new DominanceComparator();
		distance_ = new Distance();
		
		cloneoperator = operators_.get("clone");
		mutationOperator = operators_.get("mutation");
		crossoverOperator = operators_.get("crossover");

		H_ = 23; // 23 for 300 and 33 for 595 to be used with 3 objective
					// problems

		T_ = 20;
		neighborhood_ = new int[populationSize][T_];
		velocity = new double[this.populationSize][problem
				.getNumberOfVariables()];

		idealPoint = new double[problem.getNumberOfObjectives()];

		lamdaVectors = new double[populationSize][problem
				.getNumberOfObjectives()];
		
		leader_ind = new int[populationSize];
		initUniformWeight();
		initNeighborhood();

		// initialize population
		population = initPopulation();

		// initialize the Ideal Point
		initIdealPoint(population);

		//this.createResultsFolders();

		// initialize velocity
		this.initVelocity();

		// assign lambda vector to each particle
		this.orderPopulation(population);

		distance_.crowdingDistanceAssignment(archive,
					problem.getNumberOfObjectives());
		archive.sort(new jmetal.util.comparators.CrowdingComparator());
		//get the clone population from the first front
		for (int k = 0; k < archive.size() && k < clonesize; k++) {
			clonepopulation.add(archive.get(k));
		} // for
			
		// STEP 2. Update
		while (evelations < max_evelations) {
			
			//1.CLONE POPULATION
			cpopulation = (SolutionSet) cloneoperator.execute(clonepopulation);
			
			temppopulation.clear();
			for(int i=0;i<cpopulation.size();i++){
				Solution[] particle2 = new Solution[2];
				int ran;
				particle2[0]= cpopulation.get(i);
				ran=PseudoRandom.randInt(0, cpopulation.size() - 1);
				particle2[1]= cpopulation.get(ran);
	        
				Solution[] offSpring=  (Solution[])crossoverOperator.execute(particle2);
				mutationOperator.execute(offSpring[0]);
		        problem.evaluate(offSpring[0]);
				if (problem.getNumberOfConstraints() != 0)
					problem.evaluateConstraints(offSpring[0]);
				updateReference(offSpring[0]);
				//offSpring[0].setsearch_type(1);
				temppopulation.add(offSpring[0]);
				evelations++;
			}
			for(int i=0;i<temppopulation.size();i++){
				archive.add(temppopulation.get(i));
			}
			
			find_leader();

			double speed[][] = this.computeSpeed();
			this.evaluatePopulation(speed);

			distance_.crowdingDistanceAssignment(archive,
					problem.getNumberOfObjectives());
			archive.sort(new jmetal.util.comparators.CrowdingComparator());
			//get the clone population from the first front
			clonepopulation.clear();
			for (int k = 0; k < archive.size() && k < clonesize; k++) {
				clonepopulation.add(archive.get(k));
			} // for
			iteration++;
			evelations+=populationSize;
		}
		return archive;
	}

	/**
	 * 
	 */

	public void find_leader(){
		int best_ind;
		double minFit,fitnesse;
		for(int i=0;i<this.populationSize;i++){
			best_ind=-1;
			minFit=Double.MAX_VALUE;
			for(int j=0;j<archive.size();j++){
				fitnesse=this.fitnessFunction(archive.get(j),this.lamdaVectors[i]);
				if(fitnesse<minFit){
					minFit=fitnesse;
					best_ind=j;
				}
			}
			leader_ind[i]=best_ind;
		}
	}
	
	public void orderPopulation(SolutionSet pop) {
		population = new SolutionSet(populationSize);

		double fitnesses[][] = new double[this.populationSize][this.populationSize];
		for (int i = 0; i < this.populationSize; i++)
			for (int j = 0; j < this.populationSize; j++)
				fitnesses[i][j] = this.fitnessFunction(pop.get(i),
						this.lamdaVectors[j]);
		for (int i = 0; i < this.populationSize; i++) {
			double minFit = Double.MAX_VALUE;
			int particleIndex = -1;
			for (int j = 0; j < this.populationSize; j++) {
				if (fitnesses[j][i] < minFit) {
					minFit = fitnesses[j][i];
					particleIndex = j;
				}
			}
			this.population.add(pop.get(particleIndex));
			for (int n = 0; n < this.populationSize; n++)
				fitnesses[particleIndex][n] = Double.MAX_VALUE;
			fitnesses[particleIndex][i] = Double.MAX_VALUE;
			this.pbest_[i] = new Solution(pop.get(particleIndex));
			//this.leaders_.add(pop.get(particleIndex));
			this.archive.add(pop.get(particleIndex));
		}

	}

	/**
	 * Update the position of each particle
	 * 
	 * @throws JMException
	 */
	private SolutionSet computeNewPositions(double[][] speed)
			throws JMException {

		SolutionSet pop = this.population;

		for (int n = 0; n < this.populationSize; n++) {

			// DecisionVariables particle = ;
			for (int var = 0; var < pop.get(n).getDecisionVariables().length; var++) {
				pop.get(n).getDecisionVariables()[var]
						.setValue(pop.get(n).getDecisionVariables()[var]
								.getValue() + speed[n][var]);
				if (pop.get(n).getDecisionVariables()[var].getValue() < problem
						.getLowerLimit(var)) {
					pop.get(n).getDecisionVariables()[var].setValue(problem
							.getLowerLimit(var));
					speed[n][var] = speed[n][var] * -1.0;
				}
				if (pop.get(n).getDecisionVariables()[var].getValue() > problem
						.getUpperLimit(var)) {
					pop.get(n).getDecisionVariables()[var].setValue(problem
							.getUpperLimit(var));
					speed[n][var] = speed[n][var] * -1.0;
				}
			}
			if(this.fitnessFunction(pop.get(n),this.lamdaVectors[n]) < this.fitnessFunction(this.pbest_[n],this.lamdaVectors[n]))
			{
				this.pbest_[n] = pop.get(n);
			}
		}
		return pop;
	} // computeNewPositions
		// //////////////////////////////////////////////////////////////////////////////////////

	public void evaluatePopulation(double[][] speed) throws JMException {

		SolutionSet pop = this.computeNewPositions(speed);
		for (int i = 0; i < this.populationSize; i++)

		{
			Solution particle = pop.get(i);
			// evaluate the new version of the population and update only the
			// particles with better fitness
			problem.evaluate(particle);
			if (problem.getNumberOfConstraints() != 0)
				problem.evaluateConstraints(particle);
			// Update the ideal point
			updateReference(particle);
			// Update of solutions
			updateProblem(particle, i, speed[i]);
			//this.leaders_.add(particle);
			this.archive.add(particle); 
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public void initUniformWeight() { // init lambda vectors
		int nw = 0;
		if (problem.getNumberOfObjectives() == 2) {
			for (int n = 0; n < populationSize; n++) {
				double a = 1.0 * n / (populationSize - 1);
				lamdaVectors[n][0] = a;
				lamdaVectors[n][1] = 1 - a;
				nw++;
			} // for
		} // if
		else {
			int i, j;
			for (i = 0; i <= H_; i++) {
				for (j = 0; j <= H_; j++) {
					if (i + j <= H_) {
						lamdaVectors[nw][0] = (double) (1.0 * i) / H_;
						lamdaVectors[nw][1] = (double) (1.0 * j) / H_;
						lamdaVectors[nw][2] = (double) (1.0 * (H_ - i - j) / H_);
						nw++;
					} // if
				} // for
			} // for
		} // else

		if (nw != populationSize) {
			System.out.println(nw + "---" + (populationSize));
			System.out.println("ERROR: population size <> #weights");
			System.exit(0);
		}
	} // initUniformWeight
		// ////////////////////////////////////////////////////////////////////////////////////////

	public void initNeighborhood() {
	    double[] x = new double[populationSize];
	    int[] idx = new int[populationSize];

	    for (int i = 0; i < populationSize; i++) {
	      // calculate the distances based on weight vectors
	      for (int j = 0; j < populationSize; j++) {
	        x[j] = Utils.distVector(lamdaVectors[i], lamdaVectors[j]);
	        //x[j] = dist_vector(population[i].namda,population[j].namda);
	        idx[j] = j;
	      //System.out.println("x["+j+"]: "+x[j]+ ". idx["+j+"]: "+idx[j]) ;
	      } // for

	      // find 'niche' nearest neighboring subproblems
	      Utils.minFastSort(x, idx, populationSize, T_);
	      //minfastsort(x,idx,population.size(),niche);

	        System.arraycopy(idx, 0, neighborhood_[i], 0, T_);
	    } // for
	  } // initNeighborhood
	
	public void matingSelection(Vector<Integer> list, int cid, int size, int type) {
	    // list : the set of the indexes of selected mating parents
	    // cid  : the id of current subproblem
	    // size : the number of selected mating parents
	    // type : 1 - neighborhood; otherwise - whole population
	    int ss;
	    int r;
	    int p;

	    ss = neighborhood_[cid].length;
	    while (list.size() < size) {
	      if (type == 1) {
	        r = PseudoRandom.randInt(0, ss - 1);
	        p = neighborhood_[cid][r];
	      //p = population[cid].table[r];
	      } else {
	        p = PseudoRandom.randInt(0, populationSize - 1);
	      }
	      boolean flag = true;
	      for (int i = 0; i < list.size(); i++) {
	        if (list.get(i) == p) // p is in the list
	        {
	          flag = false;
	          break;
	        }
	      }

	      //if (flag) list.push_back(p);
	      if (flag) {
	        list.addElement(p);
	      }
	    }
	  } // matingSelection
	
	public boolean updateProblem(Solution indiv, int id, double speed[]) {

		population.replace(id, new Solution(indiv)); // change position
		this.velocity[id] = speed; // update speed

		//
		return true;

	} // updateProblem
		// /////////////////////////////////////////////////////

	private double[][] computeSpeed() throws JMException {
		double r1, W, C1;
		double[][] speed = new double[this.populationSize][problem.getNumberOfVariables()];
		
		int l2;

		Variable[] pbest,lbest,gbest;
		for (int n = 0; n < this.population.size(); n++) {
			Variable[] particle = population.get(n).getDecisionVariables();
			double f;
			double sign1=1.0,sign2=1.0;
			Vector<Integer> p = new Vector<Integer>();
			l2 = leader_ind[n];
			pbest = archive.get(l2).getDecisionVariables();

			l2 = PseudoRandom.randInt(0, this.archive.size() - 1); // select random  leader
			gbest = archive.get(l2).getDecisionVariables();
			
			matingSelection(p, n, 1, 1); //Select a neighborhood sub-problem of n
			lbest = archive.get(leader_ind[p.get(0)]).getDecisionVariables();

			if (fitnessFunction(archive.get(leader_ind[p.get(0)]), lamdaVectors[n]) > fitnessFunction(population.get(n), lamdaVectors[n]))
			{
				sign1=-1.0;
			}
			if (fitnessFunction(archive.get(l2), lamdaVectors[n]) > fitnessFunction(population.get(n), lamdaVectors[n]))
			{
				sign2=-1.0;
			}

			for (int var = 0; var < particle.length; var++) {

				r1 = PseudoRandom.randDouble();
				C1 = PseudoRandom.randDouble(1.5, 2.0);
//				W = PseudoRandom.randDouble(0.1, 0.1);
//				f = PseudoRandom.randDouble(0.5, 0.5);
				W=0.1;
				f=0.5;

				speed[n][var] = (W * velocity[n][var])
							+ C1*r1*(pbest[var].getValue() - particle[var].getValue())
							+ sign1*f*(lbest[var].getValue() - particle[var].getValue())+ sign2*f*(gbest[var].getValue() - particle[var].getValue());

			}// end for
		}
		return speed;
	}

	// ///////////////////////////////////////////////////////////////////////
	private void initVelocity() {
		for (int i = 0; i < this.populationSize; i++)
			for (int j = 0; j < problem.getNumberOfVariables(); j++) {
				velocity[i][j] = 0.0;
			}
	}

	// ////////////////////////////////////////////////////////////////////////
	public SolutionSet initPopulation() throws JMException,
			ClassNotFoundException {
		SolutionSet pop = new SolutionSet(this.populationSize);
		for (int i = 0; i < populationSize; i++) {
			Solution newSolution = new Solution(problem);
			problem.evaluate(newSolution);
			if (this.problem.getNumberOfConstraints() != 0)
				problem.evaluateConstraints(newSolution);
			// evaluations++;
			pop.add(newSolution);
			archive.add(newSolution);
		}
		return pop;
	} // initPopulation
		// ///////////////////////////////////////////////////////////////////////////

	// ******************************************************************
	void initIdealPoint(SolutionSet pop) throws JMException,
			ClassNotFoundException {
		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			idealPoint[i] = 1.0e+30;
			indArray_[i] = new Solution(problem);
			problem.evaluate(indArray_[i]);
			// evaluations++;
		} // for

		for (int i = 0; i < populationSize; i++)
			updateReference(pop.get(i));

	} // initIdealPoint

	// ***************************************************************

	double fitnessFunction(Solution individual, double[] lamda) {

		if (functionType_.equals("_TCHE1")) {
			double maxFun = -1.0e+30;
			for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
				// double diff = Math.abs(individual.getObjective(n)
				// - this.idealPoint[n]);

				double diff = Math.abs(individual.getObjective(n)
						- idealPoint[n]);

				double feval;
				if (lamda[n] == 0) {
					feval = 0.0001 * diff;
				} else {
					feval = diff * lamda[n];
				}
				if (feval > maxFun) {
					maxFun = feval;
				}
			} // for
			return maxFun;
		} // if

		else if (functionType_.equals("_WSUM")) {

			double sum = 0;
			for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
				sum += (lamda[n]) * individual.getObjective(n);
			}
			return sum;

		} // if
		else if (functionType_.equals("_NBI")) {
			int i;
			double d1, d2, nl;
			double theta = 5.0;
			double fin;

			d1 = d2 = nl = 0.0;
			for (i = 0; i < problem.getNumberOfObjectives(); i++) {
				d1 += (individual.getObjective(i) - idealPoint[i]) * lamda[i];
				nl += Math.pow(lamda[i], 2.0);
			}
			d1 = Math.abs(d1) / Math.sqrt(nl);
			if (nl == 0.0) {
				System.out
						.println("ERROR: dived by zero(bad weihgted vector)\n");
				System.exit(0);
			}
			for (i = 0; i < problem.getNumberOfObjectives(); i++) {
				d2 += Math.pow((individual.getObjective(i) - idealPoint[i])
						- (d1 * lamda[i]), 2.0);
			}
			d2 = Math.sqrt(d2);
			fin = (d1 + theta * d2);
			return fin;
		}

		else {
			System.out.println("SDMOPSO.fitnessFunction: unknown type "
					+ functionType_);
			return 0;
		}

	} // fitnessEvaluation
	// *******************************************************************
	void updateReference(Solution individual) {
		for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
			if (individual.getObjective(n) < idealPoint[n]) {
				idealPoint[n] = individual.getObjective(n);

				indArray_[n] = individual;
			}
		}
	} // updateReference
} // MOPSOD