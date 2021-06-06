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
    
   
    
    public Set<NoGood> collect (SearchTree tree){
        
        Set<NoGood>  noGoods = new HashSet<NoGood> ();
        
        //insert root node into  map of leaf nodes
        while (numBranchesAlreadyMade<=MAX_INFEASIBLE_HYPERCUBE_SEARCH_BRANCH_LIMIT && !tree.isEmpty()){
            SearchTreeNode leaf = tree.removeLeaf();
            List<SearchTreeNode>  kids = leaf.branch( leaf.lbc.getVariableToBranch());
            
            for (SearchTreeNode kid :  kids  ){
                if (kid.lbc.isGuaranteed_InFeasible()){
                    //collect it
                    noGoods.add (convertToNogood(kid)) ;
                }else if (kid.lbc.isGuaranteed_Feasible()){
                    //discard kid
                }else {
                    //insert back into leaf index
                    tree.addLeaf(kid);
                }
            }
            
        }
        
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
