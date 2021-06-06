/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.utilities;

import static ca.mcmaster.cltljune2021.Constants.*;
import ca.mcmaster.cltljune2021.collection.*;
import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloLinearNumExprIterator;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class CplexUtilities {
    
    
    public static List<LowerBoundConstraint> getConstraints (IloCplex cplex) throws IloException{
           
        IloLPMatrix lpMatrix = (IloLPMatrix)cplex.LPMatrixIterator().next();
        
        final int numConstraints = lpMatrix.getNrows();
         
        List<LowerBoundConstraint> result = new ArrayList<LowerBoundConstraint>( );
        
        
        int[][] ind = new int[ numConstraints][];
        double[][] val = new double[ numConstraints][];
        
        double[] lb = new double[numConstraints] ;
        double[] ub = new double[numConstraints] ;
        
        lpMatrix.getRows(ZERO,   numConstraints, lb, ub, ind, val);
        
        //IloRange[] ranges = lpMatrix.getRanges() ;
        
        
        //build up each constraint 
        for (int index=ZERO; index < numConstraints ; index ++ ){
            
            //String thisConstraintname = ranges[index].getName();
            //System.out.println("Constarint is : " + thisConstraintname + " lenght is " +ind[index].length);//k
            final int numVarsInConstraint =  ind[index].length;
            
            //if (ind[index].length > MAX_VARIABLES_PER_CONSTRAINT) continue;
                       
            boolean isUpperBound = Math.abs(ub[index])< BILLION ;
            boolean isLowerBound = Math.abs(lb[index])<BILLION ;
            boolean isEquality = ub[index]==lb[index];
            
            if  (isEquality)  {
                LowerBoundConstraint lbcUP =new LowerBoundConstraint();
                LowerBoundConstraint lbcDOWN =new LowerBoundConstraint( );
                
                List<Tuple> tuplesUp = new ArrayList <Tuple> ( );
                List<Tuple> tuplesDown = new ArrayList <Tuple> ( );
                 
                lbcUP .lowerBound= lb[index];
                lbcDOWN.lowerBound=-ub[index]; //ub portion
                
                for (  int varIndex = ZERO;varIndex< ind[index].length;   varIndex ++ ){
                    String var = lpMatrix.getNumVar(ind[index][varIndex]).getName() ;
                    Double coeff = val[index][varIndex];
                    tuplesUp.add(new Tuple(var,  coeff)) ;
                    tuplesDown.add(new Tuple(var, -coeff));
                }
                
                lbcUP.add (tuplesUp );
                lbcDOWN.add (tuplesDown);
                result.add(lbcUP);
                result.add(lbcDOWN);
                
            }else {
                
                //not an equailty constraint
                LowerBoundConstraint lbc =new LowerBoundConstraint();
                List<Tuple> tuples  = new ArrayList <Tuple> ( );
                 
                lbc.lowerBound=  (isUpperBound && ! isLowerBound )? -ub[index] : lb[index];
                for (  int varIndex = ZERO;varIndex< ind[index].length;   varIndex ++ ){
                    String var = lpMatrix.getNumVar(ind[index][varIndex]).getName() ;
                    Double coeff = val[index][varIndex];
                    tuples.add(new Tuple(var, (isUpperBound && ! isLowerBound )? -coeff: coeff)) ;                    
                    
                }
                
                lbc.add (tuples);
                result.add(lbc) ;
            }
            
        }
         
        return result;
     
          
    }
    
    
    public static TreeMap<String, Double> getObjective (IloCplex cplex) throws IloException {
        
        TreeMap<String, Double>  objectiveMap = new TreeMap<String, Double>();
        
        IloObjective  obj = cplex.getObjective();
       
        IloLinearNumExpr expr = (IloLinearNumExpr) obj.getExpr();
                 
        IloLinearNumExprIterator iter = expr.linearIterator();
        while (iter.hasNext()) {
           IloNumVar var = iter.nextNumVar();
           double val = iter.getValue();
           
           objectiveMap.put(var.getName(),   val   );
           
        }
        
        return  objectiveMap ;
        
         
    }
        
    public static List<IloNumVar> getVariables (IloCplex cplex) throws IloException{
        List<IloNumVar> result = new ArrayList<IloNumVar>();
        IloLPMatrix lpMatrix = (IloLPMatrix)cplex.LPMatrixIterator().next();
        IloNumVar[] variables  =lpMatrix.getNumVars();
        for (IloNumVar var :variables){
            result.add(var ) ;
        }
        return result;
    }
    
    
}
