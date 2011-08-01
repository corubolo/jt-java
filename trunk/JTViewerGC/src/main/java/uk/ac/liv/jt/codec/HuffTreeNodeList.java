/*******************************************************************************
 * This Library is :
 * 
 *     Copyright © 2010 Jerome Fuselier and Fabio Corubolo - all rights reserved
 *     jerome.fuselier@gmail.com ; corubolo@gmail.com
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * see COPYING.LESSER.txt
 * 
 * ---
 * 
 * This library used data structures from the JT Specification, that are subject to the JT specification license: 
 * JT_Specification_License.txt
 * 
 ******************************************************************************/
package uk.ac.liv.jt.codec;

import java.util.ArrayList;

public class HuffTreeNodeList extends ArrayList<HuffTreeNode> {
	
	/* Correspond to the VectorHeap in the appendix C of the specification */
	
	// Add a node, keeping the ordering (higher frequency first)
	public void addOrder(HuffTreeNode node) {
    	int idx = 0;
    	
    	while ( idx < size() && node.lowerThan(get(idx)) )
    		idx++;

    	add(idx, node);
		
	}
	
	public void addLast(HuffTreeNode node) {
		add(size(), node);		
	}

  /*  // Get the minimum element and remove it
     public HuffTreeNode getMin2() {

        if (size() < 1)
            return null;
        else if (size() == 1) {
            HuffTreeNode node = get(0);
            remove(0);
            return node;
        }

        int idxMin = 0;
        HuffTreeNode min = get(idxMin);

        for (int j = 1; j < size(); j++) {
            HuffTreeNode el = get(j);

            if (el.lowerThan(min)) {
                idxMin = j;
                min = el;
            }
        }

        remove(idxMin);
        

        return min;
    }*/
	
	
	// Get the minimum element and remove it
     public HuffTreeNode getMin() {

    	 int idxMin = size()-1;
    	 HuffTreeNode min = get(idxMin);
    	 remove(idxMin);

    	 return min;
     }

     
    public String toString() {
    	String res = "[";

        for (int j = 0; j < size(); j++) {
            HuffTreeNode el = get(j);
            res += String.format("%d(%d)(%d),", el.getSymbol(), el.getSymCount(), el.data.assValue);
        }
        
        res += "]";
        return res;
    }

    }
