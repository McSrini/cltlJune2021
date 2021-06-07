/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.common;

import static ca.mcmaster.cltljune2021.Constants.*; 
import static ca.mcmaster.cltljune2021.drivers.BaseDriver.objectiveFunctionMap;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author tamvadss
 */
public class NoGood {
    
    private Set<String> zeroFixedVars = new HashSet<String> () ;
    private Set<String> oneFixedVars = new HashSet<String> () ;
    
    private TreeSet<String> varsInThisCube_that_Are_included_in_objective = new TreeSet<String> () ;
    
    private double priority = DOUBLE_ZERO;
    
    
    public NoGood (Set<String> zeroFixedVars,  Set<String> oneFixedVars ) {
        
        this.addZeroFixings( zeroFixedVars);
        this.addOneFixings(oneFixedVars );
        
         
    }
    
   
    public Set<String> getZeroFixedVars (){
        //not a good idea to return a private collection
        return   unmodifiableSet (this.zeroFixedVars);
    }
    public Set<String> getOneFixedVars (){
        return unmodifiableSet (this.oneFixedVars);
    }
    
    
    public void addZeroFixings (Set<String> varList) {
        for (String str : varList){
            addZeroFixing (str );
        }
    }
    
    public void addOneFixings (Set<String> varList) {
        for (String str: varList){
            addOneFixing (str);
        }
    }
    
    public void addZeroFixing (String var) {
        this.zeroFixedVars.add(var);
        double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
        //if (objectiveCoeff < ZERO){
            priority += objectiveCoeff;
        //}
        if (DOUBLE_ZERO != objectiveCoeff){
            this.varsInThisCube_that_Are_included_in_objective.add (var);
        }
    }
    
    public void addOneFixing (String var) {
        this.oneFixedVars.add(var);
        double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
        //if (objectiveCoeff > ZERO){
            priority -= objectiveCoeff;
        //}
        if (DOUBLE_ZERO != objectiveCoeff){
            this.varsInThisCube_that_Are_included_in_objective.add (var);
        }
    }
    
    public void removeZeroFixedVar (String var) {
        if (zeroFixedVars.remove( var)) {
            //adjust priority
            
            double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
            //if (objectiveCoeff < ZERO){
                priority -= objectiveCoeff;
            //}

            this.varsInThisCube_that_Are_included_in_objective.remove(var);
            
        }        
    }
    
    public void removeOneFixedVar (String var) {
        if (this.oneFixedVars.remove( var)) {
            //adjust priority
            
            //double objectiveCoeff = objectiveFunctionMap.get(var );
            double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
            
            //if (objectiveCoeff >ZERO){
                priority += objectiveCoeff;
            //}
            this.varsInThisCube_that_Are_included_in_objective.remove(var);
        }        
    }
    
    public double getPriority () {
        
        return varsInThisCube_that_Are_included_in_objective.size() == ZERO ? -BILLION: this.priority;
    }
    
    //does this noggod render the best unconstrainted vertex infeasible?
    //i.e., every fixing in this nogood must lower the obj function value
    public boolean isBestUnconstraintedVertex_Passed () {
        boolean result = false ;
        //all the zero fixings must have positive  obj coeffs 
        //all the 1 fixings must have negative  obj coeffs 
        for (String var : this.zeroFixedVars){
            //
            double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
            if (objectiveCoeff<ZERO){
                result = true;
                break;
            }
        }
        for (String var : this.oneFixedVars){
            //
            double objectiveCoeff = objectiveFunctionMap.get(var )==null? DOUBLE_ZERO: objectiveFunctionMap.get(var ) ;
            if (objectiveCoeff>ZERO){
                result = true;
                break;
            }
        }
        return result;
    }
    
    //for small nogoods, keep adding next highest obj coeff variable until size equals largest size nogood
    //Tree map has aboslute value of obj func coeffs of free variables, and the count of variables with that obj value
    public double getAdjustedPriority (int largestSize, Set<String> fixedVars ) {
        
        TreeMap<Double, Integer > freeVariableCoeffs = new TreeMap<Double, Integer >   ();
        //prepare FreeVariableCoeffs 
        for (Map.Entry<String, Double> entry : objectiveFunctionMap.entrySet()){
            String thisVar = entry.getKey();
            if (this.zeroFixedVars.contains(thisVar) || this.oneFixedVars.contains(thisVar)|| fixedVars.contains(thisVar) ){
                //
            }else {
                double absoluteValue = Math.abs (entry.getValue());
                Integer current = freeVariableCoeffs.get (absoluteValue) ;
                if (null == current) current = ZERO;
                freeVariableCoeffs.put (absoluteValue , ONE + current);
            }
        }
        
        int mySize = getSize();
        int numOfAdjustmentsNeeded = largestSize- mySize;
        double adjustment = ZERO;
        
         
        for (Map.Entry<Double, Integer >  entry : freeVariableCoeffs.descendingMap().entrySet()){
            if (numOfAdjustmentsNeeded> entry.getValue()){
                numOfAdjustmentsNeeded -= entry.getValue();
                adjustment += entry.getValue() * entry.getKey();
            }else {
                adjustment += entry.getKey() *numOfAdjustmentsNeeded  ;
                numOfAdjustmentsNeeded= ZERO;
                
            }
            if (ZERO == numOfAdjustmentsNeeded) break;
        }
         
        
        return adjustment + priority;
    }
    
    public int getSize () {
        return oneFixedVars.size()+ zeroFixedVars.size();
    } 
    
    //can implement toString() 
    public void printMe () {
        System.out.println("Zero fixed vars") ;
        for (String str : zeroFixedVars){
            System.out.println(str) ;
        }
        System.out.println("One fixed vars") ;
        for (String str : oneFixedVars){
            System.out.println(str) ;
        }
        
        System.out.println("Priority = "+ this.priority + "Passes best vertex = "+this.isBestUnconstraintedVertex_Passed()      );
        
        
        
    } 
}
