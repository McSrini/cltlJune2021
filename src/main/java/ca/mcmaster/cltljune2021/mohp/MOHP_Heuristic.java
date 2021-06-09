/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.mohp;

import static ca.mcmaster.cltljune2021.Constants.*;
import ca.mcmaster.cltljune2021.Parameters;
import ca.mcmaster.cltljune2021.common.NoGood;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class MOHP_Heuristic {
    
    //given a treemap of nogood sets ,  return branching variable
   
    public static String getBranchingVariable (TreeMap<Double, Set<NoGood>> noGoodMap, Set<String> fractionalVariables){
         
        String result = null;
        int numOfFractionalVars = fractionalVariables.size();
        
        Map <String, Integer> frequencyMap = new HashMap <String, Integer>();
         
        //every fractional variable is a candidate
        for (String var :  fractionalVariables){
            frequencyMap.put (var, ONE);
        }
                
        
        for (Map.Entry<Double, Set<NoGood>> entry : noGoodMap.descendingMap().entrySet()){
            for ( NoGood nogud : entry.getValue()){
                
                if (nogud.isBestUnconstraintedVertex_Passed()) continue;
                
                Set<String> varsInThisNoGood = new HashSet<String> ( );
                varsInThisNoGood.addAll(nogud.getZeroFixedVars() );
                varsInThisNoGood.addAll(nogud.getOneFixedVars() );
                for (String var:varsInThisNoGood ){
                     
                    Integer currentFreq = frequencyMap.get (var);
                    if (null!=currentFreq){
                         
                        frequencyMap.put (var,currentFreq + ONE); 
                    }
                }
            }
            
            //reset high freq vars
            List<String>    highFreqVars = new ArrayList<String> ();
            int highFreq = getVarsWithHighestFreq (   frequencyMap, highFreqVars);
                                    
            //remove all vars from freqency map that are not inluded in highFreqVars
            frequencyMap.clear();
            for (String var :  highFreqVars){
                frequencyMap.put (var, ONE);
            }
            
            
            //check if there is already a winner
            if (frequencyMap.size()==ONE) break;
            
            
        }
        
        if (numOfFractionalVars != frequencyMap.size()){
            List<String> candidates = new ArrayList<String> () ;
            candidates.addAll(frequencyMap.keySet());

            Collections.shuffle(candidates,  Parameters.PERF_VARIABILITY_RANDOM_GENERATOR) ;
            result =  candidates.get(ZERO);
        }// else null result
        
        return result;
        
    }
    
    private static int getVarsWithHighestFreq ( Map <String, Integer> frequencyMap, List<String> highFreqVars ){
        
        int highest = - BILLION;
        
        for ( Map.Entry <String, Integer> entry : frequencyMap.entrySet()){
            if (entry.getValue()> highest){
                highest = entry.getValue();
                highFreqVars.clear();
                highFreqVars.add (entry.getKey());
            }else if (entry.getValue()== highest) {
                highFreqVars.add (entry.getKey());
            }
        }
        
        return highest;
    }
    
}
