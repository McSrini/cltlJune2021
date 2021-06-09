/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.ted;

import ca.mcmaster.cltljune2021.common.NoGood;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class TED_BranchCallback extends IloCplex.BranchCallback {
    
    TreeMap<Double, Set<NoGood>> rootNode_noGoodMap;
    
    public TED_BranchCallback (TreeMap<Double, Set<NoGood>> starting_noGoodMap) {
        rootNode_noGoodMap= starting_noGoodMap;
    }
 
    protected void main() throws IloException {
        
    }
    
}
