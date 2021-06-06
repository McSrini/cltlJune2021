/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tamvadss
 */
class SearchTreeNode {
    
    public LowerBoundConstraint lbc;
    public SearchTreeNode parent;
    public String branchingVar; 
    public Boolean amITheDownChild = null;
    
    public int depth =ZERO;
    public Double changeInValue_dueToBranches = DOUBLE_ZERO;
    
    public List<SearchTreeNode>  branch (String var) {
        List<SearchTreeNode>  result = new ArrayList<SearchTreeNode>  ();
        
        SearchTreeNode downchild = new SearchTreeNode ();
        SearchTreeNode upchild = new SearchTreeNode ();
        result.add (upchild );
        result.add (downchild );
        
        downchild.parent = this;
        downchild.branchingVar = var;
        downchild.amITheDownChild = true;
        downchild.depth= this.depth + ONE;
        LowerBoundConstraint down_lbc= this.lbc.createCopy();
        down_lbc.remove(var, ZERO);
        downchild.lbc =down_lbc;
        
        upchild.lbc = this.lbc.createCopy();
        upchild.lbc .remove(var, ONE);
        upchild.parent = this;
        upchild.branchingVar= var;
        upchild.amITheDownChild = false;
        upchild.depth = this.depth + ONE;
        upchild.changeInValue_dueToBranches +=  this.changeInValue_dueToBranches + this.lbc.lowerBound - upchild.lbc.lowerBound;
        
        return result;
    }
    
}
