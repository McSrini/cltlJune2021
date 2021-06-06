/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.drivers;

import ilog.concert.IloNumVar;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvadss
 */
public abstract class BaseDriver {
          
    protected static Logger logger;
    
    public static TreeMap<String, Double> objectiveFunctionMap =null;
    public static  TreeMap<String, IloNumVar> mapOfAllVariablesInTheModel = new TreeMap<String, IloNumVar> ();
    
}
