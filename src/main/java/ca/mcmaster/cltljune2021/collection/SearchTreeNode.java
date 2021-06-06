/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;
import static ca.mcmaster.cltljune2021.drivers.BaseDriver.objectiveFunctionMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tamvadss
 */
class SearchTreeNode {
    
    public LowerBoundConstraint lbc;
    public SearchTreeNode parent=null;
    public String branchingVar; 
    public Boolean amITheDownChild = null;
    
    public int depth =ZERO;
    public Double changeInValue_dueToBranches = DOUBLE_ZERO;
    
    public SearchTreeNode (LowerBoundConstraint lbc){
        this.lbc = lbc;
    }
    
    public List<SearchTreeNode>  branch (String var) {
        List<SearchTreeNode>  result = new ArrayList<SearchTreeNode>  ();
        
        LowerBoundConstraint down_lbc= this.lbc.createCopy();
        down_lbc.remove(var, ZERO);
        SearchTreeNode downchild = new SearchTreeNode (down_lbc);
        
        LowerBoundConstraint  up_lbc = this.lbc.createCopy();
        up_lbc .remove(var, ONE);
        SearchTreeNode upchild = new SearchTreeNode (up_lbc);
        
        result.add (upchild );
        result.add (downchild );
        
        this.branchingVar= var;
        
        downchild.parent = this;       
        downchild.amITheDownChild = true;
        downchild.depth= this.depth + ONE;
        downchild.changeInValue_dueToBranches =  this.changeInValue_dueToBranches ;
        
       
        upchild.parent = this;       
        upchild.amITheDownChild = false;
        upchild.depth = this.depth + ONE;
        upchild.changeInValue_dueToBranches =  this.changeInValue_dueToBranches + objectiveFunctionMap.get (var);
         
        return result;
    }
    
}
