/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
class SearchTree {
    
    public SearchTreeNode root;
    //index of leafs
    //first key is distance from no fixes, second key is number of fixes
    public TreeMap <Double, TreeMap<Integer, List<SearchTreeNode>> >leafMap = new TreeMap <Double, TreeMap<Integer, List<SearchTreeNode>>>();
    
    public  SearchTree ( SearchTreeNode rootNode) {
        root =    rootNode;
        addLeaf (root) ;
    }
    
    public SearchTreeNode removeLeaf () {
        SearchTreeNode result = null;
        List<SearchTreeNode> candidates = leafMap.firstEntry().getValue().firstEntry().getValue();
        result = candidates.remove(ZERO);
        if (ZERO==candidates.size()) {
            TreeMap<Integer, List<SearchTreeNode>> innerMap = leafMap.firstEntry().getValue();
            innerMap.remove (innerMap.firstKey()) ;
            if (ZERO==leafMap.firstEntry().getValue().size()){
                leafMap.remove (leafMap.firstKey()) ;
            }
        }
        return result;
    }
    
    public void addLeaf (SearchTreeNode leaf) {
        //
        TreeMap<Integer, List<SearchTreeNode>> innerMap =  leafMap.get( leaf.changeInValue_dueToBranches);
        if (null == innerMap){
            innerMap =new TreeMap<Integer, List<SearchTreeNode>> ();
        }
        List<SearchTreeNode> currentSet = innerMap.get (leaf.depth) ;
        if (null==currentSet) currentSet = new ArrayList<SearchTreeNode> ();
        currentSet.add (leaf );
        innerMap.put (leaf.depth, currentSet);
        leafMap.put( leaf.changeInValue_dueToBranches,innerMap );
         
    }
    
    public boolean isEmpty  () {
        return (leafMap.size() == ZERO);
    }
    public int size (){
        int size = ZERO;
        for (TreeMap<Integer, List<SearchTreeNode>>  innermap : leafMap.values()){
            size += innermap.values().size();
        }
        return size;
    }
}
