/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;
import static ca.mcmaster.cltljune2021.Parameters.*;
import ca.mcmaster.cltljune2021.common.NoGood;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class Collector {
    
    private int numBranchesAlreadyMade = ZERO;
    
   
    
    public Set<NoGood> collect (LowerBoundConstraint lbc){
        
        SearchTree tree = new SearchTree( new SearchTreeNode (lbc));
        
        Set<NoGood>  noGoods = new HashSet<NoGood> ();
        
        //insert root node into  map of leaf nodes
        while (numBranchesAlreadyMade<=MAX_INFEASIBLE_HYPERCUBE_SEARCH_BRANCH_LIMIT && !tree.isEmpty()){
            
            //System.out.println("NUm leafs in serach tree = "+ tree.size()) ;
            
            SearchTreeNode leaf = tree.removeLeaf();
            String varToBranchOn = leaf.lbc.getVariableToBranch();
            List<SearchTreeNode>  kids = leaf.branch( varToBranchOn);
            numBranchesAlreadyMade ++;
            
            //System.out.println("Branched on  "+varToBranchOn ) ;
            
            for (SearchTreeNode kid :  kids  ){
                
                //System.out.println("kid changeInValue_dueToBranches = " + kid.changeInValue_dueToBranches) ;
                
                if (kid.lbc.isGuaranteed_InFeasible()){
                    //collect it
                    NoGood ng = convertToNogood(kid);
                    noGoods.add (ng) ;
                    
                    //System.out.println("Collected no good ") ;
                    //ng.printMe( null);
                    
                    
                }else if (kid.lbc.isGuaranteed_Feasible()){
                    //discard kid
                    
                    //System.out.println("Feasible node ") ;
                    //kid.lbc.printMe();
                    
                }else {
                    //insert back into leaf index
                    tree.addLeaf(kid);
                }
            }
            
        }
        
        
        System.out.println("NOgood collection stopped after "+ numBranchesAlreadyMade + " branches.") ;
        return noGoods;
        
    }
    
    private NoGood convertToNogood (SearchTreeNode leaf ) {
        Set<String> zeroFixedVars = new HashSet<String> () ;
        Set<String> oneFixedVars = new HashSet<String> () ;
        
        SearchTreeNode current = leaf;
        SearchTreeNode parent = leaf.parent;
        while (parent !=null) {
            String branchVar = parent.branchingVar;
            if (current.amITheDownChild){
                zeroFixedVars.add (branchVar) ;
            }else {
                oneFixedVars.add (branchVar);
            }
            current = parent;
            parent = parent.parent;
        }
        
        return new NoGood (zeroFixedVars, oneFixedVars) ;
    }
 
}
