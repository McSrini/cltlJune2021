/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.drivers;

import static ca.mcmaster.cltljune2021.Constants.*;
import static ca.mcmaster.cltljune2021.Parameters.*;
import ca.mcmaster.cltljune2021.collection.Collector;
import ca.mcmaster.cltljune2021.collection.LowerBoundConstraint;
import ca.mcmaster.cltljune2021.common.Base_BranchCallback;
import ca.mcmaster.cltljune2021.common.EmptyBranchHandler;
import ca.mcmaster.cltljune2021.common.NoGood;
import ca.mcmaster.cltljune2021.mohp.MOHP_BranchCallback;
import ca.mcmaster.cltljune2021.ted.TED_BranchCallback;
import ca.mcmaster.cltljune2021.utilities.CplexUtilities;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvadss
 */
public abstract class BaseDriver {
          
    protected static Logger logger;
    protected static  IloCplex cplex;
    protected  static Base_BranchCallback branching_Heuristic_Callback ;
    protected static TreeMap<Double, Set<NoGood>> noGoodMap=new TreeMap<Double, Set<NoGood>>   ();
 
    
    public static TreeMap<String, Double> objectiveFunctionMap =null;
    public static  TreeMap<String, IloNumVar> mapOfAllVariablesInTheModel = new TreeMap<String, IloNumVar> ();
    
    public static void init ( ) throws IloException{
        cplex = new IloCplex ();
        cplex.importModel( PRESOLVED_MIP_FILENAME);
        CplexUtilities.setCplexParameters(cplex) ;
        
        objectiveFunctionMap = CplexUtilities.getObjective(cplex);
        for ( IloNumVar var : CplexUtilities.getVariables(cplex)){
            mapOfAllVariablesInTheModel.put (var.getName(), var);
        }
       
        List<LowerBoundConstraint> lbcList = CplexUtilities.getConstraints(cplex);
        
        noGoodMap.put (DOUBLE_ZERO, new HashSet<NoGood> ()) ;
        for (LowerBoundConstraint lbc :  lbcList){
            Set <NoGood> noGoods = (new Collector()).collect(lbc);
            Set <NoGood> current = noGoodMap.get (DOUBLE_ZERO);
            current.addAll (noGoods) ;
            noGoodMap.put (DOUBLE_ZERO, current) ;
        }
             
    }
    
    protected static void solve () throws IloException {
        logger.info ("Solve invoked ..." );
        for (int hours = ONE; hours <= MAX_TEST_DURATION_HOURS ; hours ++){                
            cplex.solve();
            print_statistics (cplex, hours) ;
            
            
            if (hours == RAMP_UP_DURATION_HOURS){
                //restore default branching
                //mip.setParam(IloCplex.Param.MIP.Strategy.VariableSelect  , ZERO );

                //remove TED callback if any
                cplex.clearCallbacks();
                cplex.use (new EmptyBranchHandler() );
               
                logger.info ("Special callback removed" );
            }
            
            if (isHaltFilePresent()) break;

            if (cplex.getStatus().equals( IloCplex.Status.Infeasible)) break;
            if (cplex.getStatus().equals( IloCplex.Status.Optimal)) break;

        }
        cplex.end();
        logger.info ("Solve completed." );
    }
    
    protected static void print_statistics (IloCplex cplex, int hour) throws IloException {
        double bestSoln = BILLION;
        double relativeMipGap = BILLION;
        IloCplex.Status cplexStatus  = cplex.getStatus();
        if (cplexStatus.equals( IloCplex.Status.Feasible)  ||cplexStatus.equals( IloCplex.Status.Optimal) ) {
            bestSoln=cplex.getObjValue();
            relativeMipGap=  cplex.getMIPRelativeGap();
        };
        logger.info ("" + hour + ","+  bestSoln + ","+  
                cplex.getBestObjValue() + "," + cplex.getNnodesLeft64() +
                "," + cplex.getNnodes64() + "," + relativeMipGap ) ;
    }
    
    protected static boolean isHaltFilePresent (){
        File file = new File("haltfile.txt");         
        return file.exists();
    }
    
}
