package jmetal.operators.clone;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.core.SolutionSet;

import java.util.HashMap;


public class proportional5 extends Clone {

	/**
	 * @param No
	 */
	  int clonesize;
	  public proportional5(HashMap<String, Object> parameters) {
		  //this.clonesize=clonesize;
		  super(parameters);
			if (parameters.get("clonesize") != null)
				//clonesize = (int) parameters.get("clonesize");
				clonesize = Integer.valueOf(parameters.get("clonesize").toString());
		  } // proportional clone
	  
	  public Object execute(Object parent) throws JMException {
			SolutionSet parents = (SolutionSet)parent;
			SolutionSet offSpring=new SolutionSet(clonesize);
			double min_distance=0.0;
			double max_distance=1.0;
		     double sum_distance=0.0;
			 for (int k = 0; k < parents.size(); k++ ) {
			 if(parents.get(k).getCrowdingDistance()!=Double.POSITIVE_INFINITY){
				 max_distance=2*parents.get(k).getCrowdingDistance();
				 min_distance=parents.get(parents.size()-1).getCrowdingDistance();
			    for(int l=0;l<k;l++){
			    	parents.get(l).setCrowdingDistance(2*parents.get(k).getCrowdingDistance());
			    }
			    break;
			    }
			  } // for
			 if(parents.get(0).getCrowdingDistance()==Double.POSITIVE_INFINITY){
				    for(int l=0;l<parents.size();l++){
				    	parents.get(l).setCrowdingDistance(1.0);
				    } 
			 }//if all the points are in extreme region.
			  for (int k = 0; k < parents.size(); k++ ) {
			      sum_distance+=parents.get(k).getCrowdingDistance();
			      parents.get(k).setmaxDistance(max_distance);
			      parents.get(k).setminDistance(min_distance);
			  } // for
			  double[] clones=new double[parents.size()];
			  for(int k=0;k<parents.size();k++){
			   clones[k]= Math.ceil(clonesize*parents.get(k).getCrowdingDistance()/sum_distance);
				  if(sum_distance==0){
					  clones[k]=Math.ceil((double)clonesize/parents.size());
					  System.out.print("zeros");
					  System.out.print(clones[k]+" ");
				  }
			  /* for (int l=0;l<clones;l++)
			      {
				     Solution Newsolution=new Solution(parents.get(k));
			       //if(remain>0){
			         offSpring.add(Newsolution);
			         //remain--;
			         //}
			    }*/
			  }
			  int remain=clonesize;
			  int i=0;
			  /* while(remain>0){
			    for(int k=parents.size()-1;k>-1;k--){
			    	if(remain>0&&clones[k]>0){
			    	   Solution Newsolution=new Solution(parents.get(k));
			    	   Newsolution.age_=Newsolution.age_-(int)clones[k]+1;
				       offSpring.add(Newsolution);
				       clones[k]--;
				       remain--;
			    	}
			    	i++;
			    }
			    if(i>400)
			    {
			     System.out.print("zeros400");
			    }
			  }*/

			   for(int k=0;k<parents.size();k++){
				    	int age=1;
				    	for(int l=0;l<clones[k];l++){		
				    	if(remain>0){
				    	   //Solution Newsolution=new Solution(parents.get(k));
				    	   //Newsolution.age_=Newsolution.age_+age;
					       offSpring.add(parents.get(k));
					       remain--;
					       age++;
				    	}
				    	i++;
				    	}
				    	if(remain==0)
				    		break;
				    	//parents.get(k).age_+=clones[k];
				    	//parents.get(k).setmutationscale(PseudoRandom.randDouble());
					    if(i>400)
					    {
					     System.out.print("zeros400");
					    }
				    }

			  /* for(int k=0;k<parents.size();k++){
			    	   Solution Newsolution=new Solution(parents.get(k));
				       offSpring.add(Newsolution);
			    	parents.get(k).age_+=1;
			  }*/
			  
		  return offSpring;//*/
		  } // execute 
	  /**
	  /**
	  * Executes the operation
	  * @param the parent population
	  * @return An object containing the offSprings
	  */
}