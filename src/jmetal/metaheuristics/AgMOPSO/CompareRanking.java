package jmetal.metaheuristics.AgMOPSO;

import java.util.Comparator;

import jmetal.core.Solution;

public class CompareRanking implements Comparator<Solution> {

	@Override
	public int compare(Solution o1, Solution o2) {

		if ((o1.getRank() - o2.getRank()) < 0)
			return -1;
		else if ((o1.getRank() - o2.getRank()) > 0)
			return 1;
		else
	
			return 0;
	}

}
