package jmetal.problems.M2M;

import jmetal.core.*;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

public class MOP7 extends Problem {

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UF1 (30
	 * decision variables)
	 * 
	 * @param solutionType
	 *            The solution type must "Real" or "BinaryReal".
	 */
	public MOP7(String solutionType) throws ClassNotFoundException {
		this(solutionType, 10, 3);
	} // MOP7
	  
	public MOP7(String solutionType, Integer numberOfVariables, Integer numberOfObjectives)
			throws ClassNotFoundException {
		numberOfVariables_ = numberOfVariables.intValue();
		numberOfObjectives_ = numberOfObjectives.intValue();
		numberOfConstraints_ = 0;
		problemName_ = "MOP7";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for
		
		if (solutionType.compareTo("BinaryReal") == 0)
	    	solutionType_ = new BinaryRealSolutionType(this) ;
	    else if (solutionType.compareTo("Real") == 0)
	    	solutionType_ = new RealSolutionType(this) ;
	    else {
	    	System.out.println("Error: solution type " + solutionType + " invalid") ;
	    	System.exit(-1) ;
	    }
	} // MOP7

	/**
	 * Evaluates a solution.
	 * @param solution The solution to evaluate.
	 * @throws JMException
	 */
	public void evaluate(Solution solution) throws JMException {
		XReal x = new XReal(solution) ;
 		
	    double[] f = new double[numberOfObjectives_];
	    double[] t = new double[numberOfVariables_];
	    
	    t        = this.evalT(x);
	    double g = this.evalG(x, t);
	    
		f[0] = (1.0 + g) * Math.cos(Math.PI * x.getValue(0) / 2.0)
				* Math.cos(Math.PI * x.getValue(1) / 2.0);
	    f[1] = (1.0 + g) * Math.cos(Math.PI * x.getValue(0) / 2.0)
				* Math.sin(Math.PI * x.getValue(1) / 2.0);
	    f[2] = (1.0 + g) * Math.sin(Math.PI * x.getValue(0) / 2.0);
	    
	    
	    for (int i = 0; i < numberOfObjectives_; i++)
	    	solution.setObjective(i, f[i]);
	} // evaluate
	
	private double evalG(XReal x, double[] t) throws JMException {
		double g = 0.0;
		
		for (int i = 2; i < x.getNumberOfDecisionVariables(); i++)
			g += (-0.9 * t[i] * t[i] + Math.pow(Math.abs(t[i]), 0.6)); 
			
		g = 2.0 * Math.sin(Math.PI * x.getValue(0)) * g;
		
		return g;
	} // evalG
	
	public double[] evalT(XReal x) throws JMException {
		double[] t = new double[numberOfVariables_];
		
		double temp = x.getValue(0) * x.getValue(1);
		for (int i = 2; i < numberOfVariables_; i++)
			t[i] = x.getValue(i) - temp;
		
		return t;
	}
}
