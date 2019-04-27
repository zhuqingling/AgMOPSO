package jmetal.problems.M2M;

import jmetal.core.*;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

public class MOP4 extends Problem {

	/**
	 * Constructor. Creates a default instance of problem CEC2009_UF1 (30
	 * decision variables)
	 * 
	 * @param solutionType
	 *            The solution type must "Real" or "BinaryReal".
	 */
	public MOP4(String solutionType) throws ClassNotFoundException {
		this(solutionType, 10); // 30 variables by default
	} // MOP1

	public MOP4(String solutionType, Integer numberOfVariables)
			throws ClassNotFoundException {
		numberOfVariables_ = numberOfVariables.intValue();
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "MOP4";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		if (solutionType.compareTo("BinaryReal") == 0)
			solutionType_ = new BinaryRealSolutionType(this);
		else if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	} // MOP4

	/**
	 * Evaluates a solution.
	 * 
	 * @param solution
	 *            The solution to evaluate.
	 * @throws JMException
	 */
	public void evaluate(Solution solution) throws JMException {
		XReal x = new XReal(solution);

		double[] f = new double[numberOfObjectives_];
		double[] t = new double[numberOfVariables_];

		t = this.evalT(x);
		double g = this.evalG(x, t);

		f[0] = (1.0 + g) * x.getValue(0);
		f[1] = (1.0 + g)
				* (1.0 - Math.sqrt(x.getValue(0))
						* Math.cos(2.0 * Math.PI * x.getValue(0))
						* Math.cos(2.0 * Math.PI * x.getValue(0)));

		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
	} // evaluate

	private double evalG(XReal x, double[] t) throws JMException {
		double g = 0.0;

		for (int i = 1; i < x.getNumberOfDecisionVariables(); i++)
			g += (Math.abs(t[i]) / (1.0 + Math.exp(5.0 * Math.abs(t[i]))));

//		g = 1.0 + 10.0 * Math.sin(Math.PI * x.getValue(0)) * g;
		g = 10.0 * Math.sin(Math.PI * x.getValue(0)) * g;

		return g;
	} // evalG

	public double[] evalT(XReal x) throws JMException {
		double[] t = new double[numberOfVariables_];

		double temp = Math.sin(0.5 * Math.PI * x.getValue(0));
		for (int i = 1; i < numberOfVariables_; i++)
			t[i] = x.getValue(i) - temp;

		return t;
	}
}
