/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.utilities;

import static ca.mcmaster.cltljune2021.Constants.*;
import ca.mcmaster.cltljune2021.common.NoGood;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class NoGoodFilter {
    
    //fixed vars are the delta from the parent, which will include the branching var
    public static TreeMap<Double, Set<NoGood>> filter (TreeMap<Double, Set<NoGood>> noGoodMap, TreeMap<String , Integer> fixedVars){
        
        TreeMap<Double, Set<NoGood>> result = new TreeMap<Double, Set<NoGood>> ();
        
        Set<NoGood> newNoGoods = new HashSet <NoGood> ();
        int maxSize = ZERO;
        
        for (Map.Entry<Double, Set<NoGood>> entry : noGoodMap.entrySet()){
            for (NoGood ng : entry.getValue() ){
                NoGood  newNg =   getFilteredCube (ng, fixedVars) ;
                if (null!=newNg) {
                    newNoGoods.add (newNg );
                    int newSize =  newNg.getSize();
                    if ( maxSize < newSize) maxSize =newSize;
                } 
                
            }
        }
        
        //now arrange in result map
        for (NoGood newNg : newNoGoods){
            double priority = newNg.getAdjustedPriority(maxSize , fixedVars.keySet());
            Set<NoGood> current = result.get (priority);
            if (null==current) current = new HashSet<NoGood> ();
            current.add (newNg );
            result.put (priority, current) ;
            
            //System.out.println("\n\nCube and priority is: "+ priority) ;
            //newNg.printMe();
            
            
        }
        
        //print results
        /*for (Map.Entry<Double, Set<NoGood>> entry : result.entrySet()) {
            System.out.println("\nPriority is "+ entry.getKey()) ;
            for (NoGood ng : entry.getValue()){
                ng.printMe(  );
            }
        }*/
        
        return result;
        
    }

    
    private static boolean isConflict (NoGood hcube, TreeMap<String, Integer> varFixings) {
        boolean isConflict = false;
        
        for (String var : hcube.getZeroFixedVars()){
            if (isConflict) break;
            if (varFixings.keySet().contains(var) && varFixings.get(var)==ONE){
                isConflict=true;                 
            }
        }
        for (String var : hcube.getOneFixedVars()){
            if (isConflict) break;
            if (varFixings.keySet().contains(var) && varFixings.get(var)==ZERO){
                isConflict=true;                 
            }
        }
        
        return   isConflict;
    }
    
    private static NoGood    getFilteredCube      (NoGood cube,  TreeMap <String, Integer>  fixedVars) {
        NoGood result = cube;
        
        //System.out.println("\n\n Filetring this cube");
        //cube.printMe();
        
        if (isConflict (cube,  fixedVars)){
            result = null;
        } else {
            NoGood newcube = new NoGood(cube.getZeroFixedVars(), cube.getOneFixedVars());
            
            for ( Map.Entry <String, Integer> entry : fixedVars.entrySet()){
                if (entry.getValue()==ZERO){
                    newcube.removeZeroFixedVar ( entry.getKey());
                }else {
                    newcube.removeOneFixedVar ( entry.getKey());
                }
            }
            
            if (newcube.getSize()!=cube.getSize()){
                result= newcube;
            }
            
        }
        
        /*if (null== result){
            System.out.println("\n\n Cube dropped");
        } else         if (cube== result) {
            System.out.println("\n\n Cube retained as is");
        } else {
            System.out.println("\n\n New Cube ");
            result.printMe();
        }*/
        
        
        return result;    
    }
    
    
    
}
