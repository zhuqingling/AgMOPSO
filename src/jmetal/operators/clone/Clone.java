package jmetal.operators.clone;

import jmetal.core.Operator;

import java.util.HashMap;

/**
 * This class represents the super class of all the crossover operators
 */
public abstract class Clone extends Operator {

	public Clone(HashMap<String, Object> parameters) {
		super(parameters);
	}
} // Crossover