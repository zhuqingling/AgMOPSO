package jmetal.operators.clone;

import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.util.HashMap;

/**
 * Class implementing a factory for crossover operators.
 */
public class CloneFactory {

	/**
	 * Gets a crossover operator through its name.
	 * 
	 * @param name
	 *            Name of the operator
	 * @return The operator
	 */
	public static Clone getClone(String name, HashMap parameters)
			throws JMException {
		if (name.equalsIgnoreCase("entireclone"))
			return new entireclone(parameters);
		if (name.equalsIgnoreCase("proportionalclone"))
			return new proportionalclone(parameters);
		if (name.equalsIgnoreCase("proportional2"))
			return new proportional2(parameters);
		if (name.equalsIgnoreCase("proportional5"))
			return new proportional5(parameters);
		else {
			Configuration.logger_.severe("CloneFactory.getCloneOperator. "
					+ "Operator '" + name + "' not found ");
			throw new JMException("Exception in " + name
					+ ".getCloneOperator()");
		} // else
	} // getClone
} // CloneFactory

