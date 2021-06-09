/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.drivers;

import static ca.mcmaster.cltljune2021.Constants.DOUBLE_ZERO;
import static ca.mcmaster.cltljune2021.Constants.LOGGING_LEVEL;
import static ca.mcmaster.cltljune2021.Constants.LOG_FILE_EXTENSION;
import static ca.mcmaster.cltljune2021.Constants.LOG_FOLDER;
import static ca.mcmaster.cltljune2021.Constants.ONE;
import static ca.mcmaster.cltljune2021.Constants.SIXTY;
import static ca.mcmaster.cltljune2021.Parameters.PRESOLVED_MIP_FILENAME;
import ca.mcmaster.cltljune2021.collection.Collector;
import ca.mcmaster.cltljune2021.collection.LowerBoundConstraint;
import ca.mcmaster.cltljune2021.common.NoGood;
import static ca.mcmaster.cltljune2021.drivers.BaseDriver.logger;
import ca.mcmaster.cltljune2021.mohp.MOHP_BranchCallback;
import ca.mcmaster.cltljune2021.utilities.CplexUtilities;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author tamvadss
 */
public class MOHP_Driver  extends BaseDriver{

    
    static {
        logger=Logger.getLogger(MOHP_Driver.class);
        logger.setLevel(LOGGING_LEVEL);
        PatternLayout layout = new PatternLayout("%5p  %d  %F  %L  %m%n");     
        try {
            RollingFileAppender rfa =new  
                RollingFileAppender(layout,LOG_FOLDER+MOHP_Driver.class.getSimpleName()+ LOG_FILE_EXTENSION);
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
        
        init() ;
        
        branching_Heuristic_Callback= new MOHP_BranchCallback (noGoodMap) ;
        cplex.use( branching_Heuristic_Callback);
        
        solve ( );
        
    }//end main
    
}
