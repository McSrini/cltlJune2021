/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.drivers;

import static ca.mcmaster.cltljune2021.Constants.*;
import static ca.mcmaster.cltljune2021.Parameters.*;
import ca.mcmaster.cltljune2021.collection.*; 
import ca.mcmaster.cltljune2021.common.NoGood;
import ca.mcmaster.cltljune2021.utilities.CplexUtilities;
import ca.mcmaster.cltljune2021.utilities.NoGoodFilter;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author tamvadss
 */
class TestDriver2  extends BaseDriver{

    public TestDriver2 () throws IloException{
    
    }
    static {
        logger=Logger.getLogger(TestDriver2.class);
        logger.setLevel(LOGGING_LEVEL);
        PatternLayout layout = new PatternLayout("%5p  %d  %F  %L  %m%n");     
        try {
            RollingFileAppender rfa =new  
                RollingFileAppender(layout,LOG_FOLDER+TestDriver2.class.getSimpleName()+ LOG_FILE_EXTENSION);
            rfa.setMaxBackupIndex(SIXTY);
            logger.addAppender(rfa);
            logger.setAdditivity(false);            
             
        } catch (Exception ex) {
            ///
            System.err.println("Exit: unable to initialize logging"+ex);       
            exit(ONE);
        }
    }
        
    public static void main(String[] args) throws Exception{
        
        IloCplex cplex = new IloCplex ();
        cplex.importModel( PRESOLVED_MIP_FILENAME);
        
        objectiveFunctionMap = CplexUtilities.getObjective(cplex);
        for ( IloNumVar var : CplexUtilities.getVariables(cplex)){
            mapOfAllVariablesInTheModel.put (var.getName(), var);
        }
        
        
        /*for (Map.Entry<String, Double> entry :objectiveFunctionMap .entrySet()){
            System.out.println(entry.getKey() + ","+ entry.getValue()) ;
        }
        System.out.println("\n variable names \n") ;
        for (String name : mapOfAllVariablesInTheModel.keySet()){
            System.out.println(name);
        }*/
        
        
        List<LowerBoundConstraint> lbcList = CplexUtilities.getConstraints(cplex);
        
        /*for (LowerBoundConstraint lbc: lbcList) {
            lbc.printMe();
        }*/
        
        TreeMap<Double, Set<NoGood>> allNoGoods = new TreeMap<Double, Set<NoGood>> ();
        allNoGoods.put (DOUBLE_ZERO, new HashSet<NoGood>  ());
        
        for (LowerBoundConstraint lbc: lbcList) {
            System.out.println("\n\n--------------------------------------\n") ;
            lbc.printMe();
            Set <NoGood> noGoods = (new Collector()).collect(lbc);
            
            int max = ZERO;
            for (NoGood ng: noGoods){
               if (max < ng.getSize()) max=ng.getSize();
            }
            
            System.out.println("Max is ="+ max );
            System.out.println("\n Printing nogoods with adjusted priority") ;
                
            
            for ( NoGood ng : noGoods) {
                ng.printMe();
                System.out.println("\n") ;
            }
            
            Set<NoGood> current = allNoGoods.get (DOUBLE_ZERO);
            current.addAll(noGoods );
            
        }
        
        System.out.println("===================================="  );
        TreeMap<String , Integer> fixedVars = new TreeMap<String , Integer> ();
        fixedVars.put ("x1", ZERO );
        //fixedVars.put ("x2", ONE );
        NoGoodFilter.filter(allNoGoods, fixedVars);
        
    }
    
}
