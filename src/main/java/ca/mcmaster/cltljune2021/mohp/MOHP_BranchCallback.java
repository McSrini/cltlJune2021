/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.mohp;

import static ca.mcmaster.cltljune2021.Constants.*;
import ca.mcmaster.cltljune2021.common.Base_BranchCallback;
import ca.mcmaster.cltljune2021.common.NoGood;
import ca.mcmaster.cltljune2021.utilities.NoGoodFilter;
import ilog.concert.IloException;
import ilog.cplex.IloCplex.BranchCallback;
import static java.lang.System.exit;
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
public class MOHP_BranchCallback extends Base_BranchCallback {
    
    TreeMap<Double, Set<NoGood>> rootNode_noGoodMap;
    
    private static Logger logger=Logger.getLogger(MOHP_BranchCallback.class);
    static {
        logger.setLevel(LOGGING_LEVEL);
        PatternLayout layout = new PatternLayout("%5p  %d  %F  %L  %m%n");     
        try {
            RollingFileAppender appender = new  RollingFileAppender(layout,
                    LOG_FOLDER+MOHP_BranchCallback.class.getSimpleName()+ LOG_FILE_EXTENSION);
            appender.setMaxBackupIndex(SIXTY);
            logger.addAppender(appender);
            logger.setAdditivity(false);
        } catch (Exception ex) {
            ///
            System.err.println("Exit: unable to initialize logging"+ex);       
            exit(ONE);
        }
    } 
    
    
    public MOHP_BranchCallback (TreeMap<Double, Set<NoGood>> starting_noGoodMap) {
        rootNode_noGoodMap= starting_noGoodMap;
    }
 
    protected void main() throws IloException {
        
        // 
        if ( getNbranches()> ZERO ){  
           
                       
            boolean isMipRoot = ( getNodeId().toString()).equals( MIP_ROOT_ID);
                       
            //get the node attachment for this node, any child nodes will accumulate the branching conditions
            
            if (isMipRoot){
                //root of mip                
              
                setNodeData(rootNode_noGoodMap);                
            } 
            
            TreeMap<Double, Set<NoGood>>  nodeData = (TreeMap<Double, Set<NoGood>> ) getNodeData();
                         
            String branchingVar = null;
            TreeMap<Double, Set<NoGood>>  noGood_PriorityMap =null;
            if (nodeData!=null  && nodeData.size()>ZERO) {
                //get vars fixed by branching and BCP , and use them to get the no goods by priority
                TreeMap<String, Integer> fixedVars = new TreeMap<String, Integer>() ;    
                Set<String> fractionalVars = getFractionalAndFixedVars (   fixedVars);
                noGood_PriorityMap=NoGoodFilter.filter(nodeData, fixedVars); 
                branchingVar = MOHP_Heuristic.getBranchingVariable ( noGood_PriorityMap,  fractionalVars);                
                
            }
            
            if (branchingVar != null) {
                overruleCplexBranching(branchingVar, noGood_PriorityMap ); 
            }else {
                //take default cplex branching
                logger.warn("taking default cplex branching at node for lack of node data"+ getNodeId()) ;
                
            }
            
        }
    }
    
}
