/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.mohp;

import static ca.mcmaster.cltljune2021.Constants.*;
import ca.mcmaster.cltljune2021.Parameters;
import ca.mcmaster.cltljune2021.common.NoGood;
import ca.mcmaster.cltljune2021.drivers.TestDriver1;
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
   
    public static String getBranchingVariable (TreeMap<Double, Set<NoGood>> noGoodMap){
         
        Map <String, Integer> frequencyMap = new HashMap <String, Integer>();
        List<String> highFreqVars = new ArrayList<String> ();
        highFreqVars.addAll (TestDriver1.mapOfAllVariablesInTheModel.keySet());
        
        for (Map.Entry<Double, Set<NoGood>> entry : noGoodMap.descendingMap().entrySet()){
            for ( NoGood nogud : entry.getValue()){
                Set<String> varsInThisNoGood = new HashSet<String> ( );
                varsInThisNoGood.addAll(nogud.getZeroFixedVars() );
                varsInThisNoGood.addAll(nogud.getOneFixedVars() );
                for (String var:varsInThisNoGood ){
                    if (! highFreqVars.contains(var))continue;
                    Integer currentFreq = frequencyMap.get (var);
                    if (null==currentFreq){
                        frequencyMap.put (var, ONE);
                    }else {
                        frequencyMap.put (var,currentFreq + ONE); 
                    }
                }
            }
            
            //check if there is already a winner
            highFreqVars = new ArrayList<String> ();
            int highFreq = getVarsWithHighestFreq (   frequencyMap, highFreqVars);
            if (highFreqVars.size()==ONE) break;
            
            //remove all vars from freqency map that are not inluded in highFreqVars
            frequencyMap.clear();
            for (String var :  highFreqVars){
                frequencyMap.put (var, highFreq);
            }
            
        }
                       
        Collections.shuffle(highFreqVars,  Parameters.PERF_VARIABILITY_RANDOM_GENERATOR) ;
        return highFreqVars.get(ZERO);
        
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
