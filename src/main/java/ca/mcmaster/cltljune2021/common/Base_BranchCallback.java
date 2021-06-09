/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.common;

import static ca.mcmaster.cltljune2021.Constants.*;
import static ca.mcmaster.cltljune2021.drivers.BaseDriver.mapOfAllVariablesInTheModel;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public abstract class Base_BranchCallback extends IloCplex.BranchCallback{
    
    protected void  overruleCplexBranching(String branchingVarName, TreeMap<Double, Set<NoGood>>  noGood_PriorityMap) throws IloException {
        IloNumVar[][] vars = new IloNumVar[TWO][] ;
        double[ ][] bounds = new double[TWO ][];
        IloCplex.BranchDirection[ ][]  dirs = new  IloCplex.BranchDirection[ TWO][];
        getArraysNeededForCplexBranching(branchingVarName, vars , bounds , dirs);

        //create both kids, pass on infeasible hypercubes from parent      

        double lpEstimate = getObjValue();
        IloCplex.NodeId zeroChildID =  makeBranch( vars[ZERO][ZERO],  bounds[ZERO][ZERO],
                                              dirs[ZERO][ZERO],  lpEstimate  , noGood_PriorityMap );
        IloCplex.NodeId oneChildID = makeBranch( vars[ONE][ZERO],  bounds[ONE][ZERO],
                                                 dirs[ONE][ZERO],   lpEstimate, noGood_PriorityMap );

    }
    
    protected void getArraysNeededForCplexBranching (String branchingVar,IloNumVar[][] vars ,
                                                   double[ ][] bounds ,IloCplex.BranchDirection[ ][]  dirs ){
        
        IloNumVar branchingCplexVar = mapOfAllVariablesInTheModel.get(branchingVar );
        
         
        //    System.out.println("branchingCplexVar is "+ branchingCplexVar);
         
        
        //get var with given name, and create up and down branch conditions
        vars[ZERO] = new IloNumVar[ONE];
        vars[ZERO][ZERO]= branchingCplexVar;
        bounds[ZERO]=new double[ONE ];
        bounds[ZERO][ZERO]=ZERO;
        dirs[ZERO]= new IloCplex.BranchDirection[ONE];
        dirs[ZERO][ZERO]=IloCplex.BranchDirection.Down;

        vars[ONE] = new IloNumVar[ONE];
        vars[ONE][ZERO]=branchingCplexVar;
        bounds[ONE]=new double[ONE ];
        bounds[ONE][ZERO]=ONE;
        dirs[ONE]= new IloCplex.BranchDirection[ONE];
        dirs[ONE][ZERO]=IloCplex.BranchDirection.Up;
    }
    
            
    protected  Set<String> getFractionalAndFixedVars (  Map <String, Integer> fixedVars) throws IloException {
        
        Set<String>  fractionalVars = new HashSet<String> ();
        
        IloNumVar[] allVariables = new  IloNumVar[mapOfAllVariablesInTheModel.size()] ;
        int index =ZERO;
        for  (Map.Entry <String, IloNumVar> entry : mapOfAllVariablesInTheModel.entrySet()) {
            //
            allVariables[index++] = entry.getValue();
        }
        IloCplex.IntegerFeasibilityStatus [] status =   getFeasibilities(allVariables);
        
        //double[] varValues = getValues (allVariables) ;
        
        index =-ONE;
        for (IloNumVar var: allVariables){
            index ++;
            //check if candidate is integer infeasible in the LP relax
            if (status[index].equals( IloCplex.IntegerFeasibilityStatus.Infeasible)) {
                fractionalVars.add(var.getName()  );
            }else {
                //var is fixed if its upper and lower bounds are the same
                Double ub = getUB(var) ;
                Double lb = getLB(var) ;
                if (ZERO==Double.compare( ub,  lb)){
                    fixedVars.put (var.getName(), (int) Math.round(lb)) ;
                }else {
                    //not fixed but integer
                    //integralVars.put (var.getName(), (int) Math.round(varValues[index])  ) ;
                }
            }
        }
        
        return fractionalVars;
        
    }
    
}
