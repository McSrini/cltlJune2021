/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tamvadss
 */
public class Parameters {
    
    public static final String PRESOLVED_MIP_FILENAME =  
            
            System.getProperty("os.name").toLowerCase().contains("win") ?
            
            "F:\\temporary files here recovered\\knapsackTiny.lp":
            "PBO.pre.sav";
    
    public static final int MAX_INFEASIBLE_HYPERCUBE_SEARCH_BRANCH_LIMIT = 1000; 
      
    public static final int RAMP_UP_DURATION_HOURS=   2  ; 
    
    public static List<String> mipsWithBarrier = new ArrayList<String> (
        Arrays.asList( "seymour-disj-10.pre", "supportcase3.pre", "supportcase10.pre"));
    public static boolean USE_BARRIER_FOR_SOLVING_LP = mipsWithBarrier.contains(PRESOLVED_MIP_FILENAME);
         
    public static final int MAX_THREADS =  System.getProperty("os.name").toLowerCase().contains("win") ? 2 : 32;
    public static final int  FILE_STRATEGY= 3;  
    
    public static final long PERF_VARIABILITY_RANDOM_SEED = 18;
    public static final java.util.Random  PERF_VARIABILITY_RANDOM_GENERATOR =             
            new  java.util.Random  (PERF_VARIABILITY_RANDOM_SEED);   
        
}
