/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.drivers;

import ca.mcmaster.cltljune2021.collection.LowerBoundConstraint;
import ca.mcmaster.cltljune2021.collection.Tuple;
import ca.mcmaster.cltljune2021.common.NoGood;
import ca.mcmaster.cltljune2021.mohp.MOHP_Heuristic;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvadss
 */
class TestDriver1 extends BaseDriver{
   
    public TestDriver1 () throws IloException{
        
    }
   
    public static void main(String[] args) throws Exception{
         
        LowerBoundConstraint lbc = new LowerBoundConstraint ();
        List<Tuple> tuples = new ArrayList<Tuple> ();
        Tuple t1 = new Tuple ();
        t1.coefficient = -8 ;
        t1.varName = "x8";
        
        Tuple t3 = new Tuple ();
        t3.coefficient = 12 ;
        t3.varName = "x3";
        
        Tuple t2 = new Tuple ();
        t2.varName = "x1";
        t2.coefficient = 1;
        tuples.add (t1);
        tuples.add (t3);
        tuples.add (t2);
        lbc.add (tuples) ;
         
        objectiveFunctionMap = new TreeMap<String, Double>   ();
        mapOfAllVariablesInTheModel.put ("x1", null) ;
        mapOfAllVariablesInTheModel.put ("x2", null) ;
        mapOfAllVariablesInTheModel.put ("x3", null) ;
        mapOfAllVariablesInTheModel.put ("x4", null) ;
        mapOfAllVariablesInTheModel.put ("x8", null) ;
        mapOfAllVariablesInTheModel.put ("x9", null) ;
        
        TreeMap<Double, Set<NoGood>> noGoodMap = new TreeMap<Double, Set<NoGood>> () ;
        
        Set<String> zeroFixedVars1=new HashSet <String>();
        Set<String> oneFixedVars1 =new HashSet <String>();
        zeroFixedVars1.add("x1" );
        oneFixedVars1.add ("x2");
        NoGood ng1 = new NoGood (zeroFixedVars1,oneFixedVars1 ) ;
        
        Set<String> zeroFixedVars2=new HashSet <String>();
        Set<String> oneFixedVars2 =new HashSet <String>();
        zeroFixedVars2.add("x3" );
        oneFixedVars2.add ("x2");
        NoGood ng2 = new NoGood (zeroFixedVars2,oneFixedVars2 ) ;
        
        Set<String> zeroFixedVars3=new HashSet <String>();
        Set<String> oneFixedVars3 =new HashSet <String>();
        zeroFixedVars3.add("x3" );
        oneFixedVars3.add ("x4");
        NoGood ng3 = new NoGood (zeroFixedVars3,oneFixedVars3 ) ;
                
        Set<String> zeroFixedVars4=new HashSet <String>();
        Set<String> oneFixedVars4 =new HashSet <String>();
        zeroFixedVars4.add("x9" );
        oneFixedVars4.add ("x8");
        NoGood ng4 = new NoGood (zeroFixedVars4,oneFixedVars4 ) ;
        
        Set<NoGood> s1 = new HashSet<NoGood> ();
        s1.add(ng4);
        s1.add(ng3);
        Set<NoGood> s2 = new HashSet<NoGood> ();
         s2.add(ng2);
        s2.add(ng1);
        
        noGoodMap.put (1.0, s1);
        noGoodMap.put (2.0, s2);
       
        Set<String>  fractionalVars = new HashSet<String> ();
        //fractionalVars.add ("x1") ;
        //fractionalVars.add ("x2") ;
        //fractionalVars.add ("x3") ;
        //fractionalVars.add ("x4") ;
        fractionalVars.add ("x5") ;
        fractionalVars.add ("x6") ;
        //fractionalVars.add ("x7") ;
        //fractionalVars.add ("x8") ;
        System.out.print(MOHP_Heuristic.getBranchingVariable(noGoodMap, fractionalVars)) ;
        
    }
    
    
    
}
