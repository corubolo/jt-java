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

import java.util.PriorityQueue;

import uk.ac.liv.jt.debug.DebugInfo;
import uk.ac.liv.jt.debug.DebugJTReader;

public class HuffTreeNode implements Comparable<HuffTreeNode> {

    /* A node of the Huffman tree */

    HuffTreeNode left;
    HuffTreeNode right;
    HuffCodeData data;
    int symCount;

    public static boolean oob = false;


    public HuffTreeNode() {
        super();
        left = null;
        right = null;
        data = null;
        symCount = 0;
    }

    public HuffTreeNode(int symCount, HuffCodeData data) {
        super();
        left = null;
        right = null;
        this.data = data;
        this.symCount = symCount;
    }

    public HuffTreeNode(HuffTreeNode left, HuffTreeNode right, int symCount,
            HuffCodeData data) {
        super();
        this.left = left;
        this.right = right;
        this.data = data;
        this.symCount = symCount;
    }

    public int compareTo(HuffTreeNode o2) {

        int k = symCount - o2.symCount;
        //        if (k<0)
        //            return -1;
        //        else return 1;
        if (k == 0) {
           // System.out.println("***********");

            //            if (data != null && o2.data != null) {
            int a = (o2.data.symbol - data.symbol);
            int b  =(o2.data.assValue - data.assValue);
            int c  =(int)((o2.data.assValue & 0xFFFFFFFFL) - (data.assValue & 0xFFFFFFFFL));
            int d = data.index - o2.data.index;
            int de = getDesscendantMaxDepth() - o2.getDesscendantMaxDepth();
            boolean e = (Math.abs(o2.data.assValue) == Math.abs(data.assValue) );
            if (e)
                k = -a;

            //            // System.out.println("***" + k + " "+ o2.getDesscendantMaxDepth() +
            //            // " " + getDesscendantMaxDepth());
            //            else
            //                k = a;
            if (k == 0 ){
                if (isLeaf() && !o2.isLeaf())
                    k = -1;
                else if (o2.isLeaf() && !isLeaf())
                    k = +1;
                else if (!isLeaf() && !o2.isLeaf()) 
                    k = -de;
            }
            if (o2.equals(this)){
                k = 0;
                System.out.println("equal");} 
            if (DebugInfo.debugCodec){
                System.out.print( sign(k));
                System.out.print(" " +sign(a));
                System.out.print(" " + sign(b));
                System.out.print(" " + sign(c));
                System.out.print(" " + sign(d));
                System.out.print(" " + (e?1:0));
                System.out.print(" " + (isLeaf()?1:0));
                System.out.print(" " + (o2.isLeaf()?1:0));
                System.out.print(" " + de);
                System.out.print(" " + (oob?1:0));
                System.out.println(" " + (o2.isLeaf()?1:0) +" " + data.index +" " +  o2.data.index);

            }
            /* 
             * 
             * conrod.jt 
             * broken 
             * {5F17BA06-DFDF-11D7-80-0-93-C1-31-E9-C7-D7}
             * - + + + - 1 1 1 7 6
             * fixed
             * + + + + - 1 1 1 7 6
             * - - - - + 1 1 1 6 7
             * cam.jt   
             * broken
             * {9C05674C-D286-11D7-80-0-82-45-5E-C4-6F-BD}
             * + + + + - 1 1 1 2 1
             * - - - - + 1 1 1 1 2
             * fixed 
             * - + + + - 1 1 1 2 1
             * + - - - + 1 1 1 1 2
             */
            //    
        }
        return k;
        //            } else {
        //                System.out.println("something strange");
        //            }
        //            else if (isLeaf() && !o2.isLeaf())
        //                k = -1;
        //            else if (o2.isLeaf() && !isLeaf())
        //                k = 1;
        //            else if (!isLeaf() && !o2.isLeaf()) {
        //                if (!left.equals(o2.left)) {
        //                    return left.compareTo(o2.left);
        //                } else {
        //                    return right.compareTo(o2.right);
        //                }
        //            }
        //        }
        //            System.out.println("S " + o2.data.symbol + " " + data.symbol);
        //            System.out.println("A " + o2.data.symbol + " " + data.symbol);


        // System.out.println("S "+ o2.data.symbol+ " " + data.symbol );
        //                System.out.println("A " + o2.data.assValue + " "
        //                        + data.assValue);


        // if (!(o2.data.symbol == -2 || data.symbol == -2) )

        // k =(int) ((o2.data.assValue & 0xFFFFFFFFL) - (data.assValue & 0xFFFFFFFFL));
        // k = o2.data.symbol - index;

        //else
        //k = o2.index - index;
        //           
        //            // case of both non leafs

        //                }
        //            }


        //        if (false) {
        //
        //            // case of one leaf and one not leaf
        //            if (isLeaf() && !o2.isLeaf())
        //                k = -1;
        //            else if (o2.isLeaf() && !isLeaf())
        //                k = 1;
        //            // case of both non leafs
        //            else if (!isLeaf() && !o2.isLeaf()) {
        //                if (!left.equals(o2.left)) {
        //                    return left.compareTo(o2.left);
        //                } else {
        //                    return right.compareTo(o2.right);
        //                }
        //                // case of both leafs:
        //            } else {
        //                // both elements from the tree
        //                // if (data.symbol != -2 && o2.data.symbol != -2) {
        //                k = o2.data.assValue - data.assValue;
        //                // } else
        //                k = index - o2.index;
        //
        //            }
        //            // k = (getDesscendantMaxDepth() - o2.getDesscendantMaxDepth());
        //            // System.out.println("***" + k + " "+ o2.getDesscendantMaxDepth() +
        //            // " " + getDesscendantMaxDepth());
        //        }
        // if (k == 0) {

        // if (k == 0 && data!=null && o2.data!=null){
        // k = (int)(o2.data.codeLen - data.codeLen);
        // System.out.println("***" + k + " "+ data.codeLen + o2.data.codeLen);
        // }
        // if (k == 0)
        // System.out.println("***" + k + " "+ o2.data.codeLen +" "+
        // data.codeLen);
        // if (k == 0 && data!=null && o2.data!=null) {
        // if ((data.assValue + o2.data.assValue) !=0)
        // k = data.symbol - o2.data.symbol;
        //
        // System.out.println("***" + k + " "+ data.symbol +" " +
        // o2.data.symbol);
        // System.out.println("***" + k + " "+ data.assValue +" " +
        // o2.data.assValue);
        // }
        // if (k< 0)
        // return -1;
        // if (k >0 )
        // return 1;
        //        

    }

    private String sign(int a) {
        if (a ==0)
            return "0";
        if (a < 0)
            return "+";
        else 
            return "-";
    }

    public int getSymCount() {
        return symCount;
    }

    public HuffCodeData getData() {
        return data;
    }

    public HuffTreeNode getLeft() {
        return left;
    }

    public int getSymbol() {
        return data.getSymbol();
    }

    public int getAssociatedValue() {
        return data.getAssValue();
    }

    public void setCode(long code) {
        data.setBitCode(code);
    }

    public void setCodeLen(int codeLen) {
        data.setCodeLen(codeLen);
    }

    public HuffTreeNode getRight() {
        return right;
    }

    // Transform a table in a tree
    public static HuffTreeNode buildHuffmanTree(Int32ProbCtxts probCtxt) {

        PriorityQueue<HuffTreeNode> nodeList = new PriorityQueue<HuffTreeNode>();

        Int32ProbCtxtTable[] probCtxtTables = probCtxt.getProbContextTables();

        if (probCtxtTables.length != 1) {
            System.out
            .println("The probability context has 0 or more than 1 table"
                    + " => Not implemented");
            return new HuffTreeNode();
        }

        Int32ProbCtxtTable probTable = probCtxtTables[0];

        // Initialize all the nodes and add them to the heap.

        Int32ProbCtxtEntry[] entries = probTable.getEntries();
        int i = 0;
        for (; i < entries.length; i++) {
            Int32ProbCtxtEntry entry = entries[i];
            HuffCodeData data = new HuffCodeData(
                    (int) entry.getSymbol(),
                    (int) (entry.getAssociatedValue() + probTable.getMinValue()));

            HuffTreeNode node = new HuffTreeNode((int) entry.getOccCount(),
                    data);
            node.data.index = i;
            nodeList.add(node);
            // System.out.println("Symbol "+ node.data.symbol +" Index " +
            // node.index);

        }
        // System.out.println();
        // System.out.println(nodeList);
        // System.out.println();

        while (nodeList.size() > 1) {
            i++;
            // Get the two lowest-frequency nodes.
            HuffTreeNode node1 = nodeList.poll();
            HuffTreeNode node2 = nodeList.poll();
            HuffCodeData data = new HuffCodeData(0xdeadbeef, -2);

            // Combine the low-freq nodes into one node.
            HuffTreeNode newNode = new HuffTreeNode(node1, node2,
                    node1.getSymCount() + node2.getSymCount(), data);

            // Add the new node to the heap.
            nodeList.add(newNode);
            newNode.data.index = i;

            // System.out.println();
            // System.out.println(nodeList);
            // System.out.println();
        }
        // System.out.println(nodeList.size());
        return nodeList.poll();
    }

    // Tree traversal of the tree to assign a code to each leaf. The codes
    // are binary codes, they corresponds to the sequence of bits to decode.
    // For instance if you read 110011, you search in the tree if the code
    // corresponds to a leaf, if it is the case you output the symbol associated
    // to the leaf.
    public static void assignCodeToTree(HuffTreeNode node,
            HuffCodecContext ctnxt) {

        if (node.getLeft() != null) {
            ctnxt.leftShift();
            ctnxt.bitOr(1); // left => 1
            ctnxt.incLength();
            assignCodeToTree(node.getLeft(), ctnxt);
            ctnxt.decLength();
            ctnxt.rightShift();
        }

        if (node.getRight() != null) {
            ctnxt.leftShift();
            // right => 0
            ctnxt.incLength();
            assignCodeToTree(node.getRight(), ctnxt);
            ctnxt.decLength();
            ctnxt.rightShift();
        }

        if (node.getRight() != null)
            return;

        // Set the code and its length for the node.
        node.setCode(ctnxt.getCode());
        node.setCodeLen(ctnxt.getCodeLen());

        ctnxt.add(node.getData());

        return;

    }

    /*
     * public boolean lowerThan2(HuffTreeNode o) { if (symCount < o.symCount)
     * return true; else if (symCount == o.symCount) { return data.assValue >
     * o.data.assValue; } else return false; }
     */

    public boolean lowerThan(HuffTreeNode o) {
        if (symCount < o.symCount)
            return true;
        else
            return false;
    }

    public String toString() {
        return data + " " + symCount;
    }

    public void print() {
        System.out.println(data + " " + symCount);
        if (left != null)
            left.print();

        if (right != null)
            right.print();
    }

    public boolean isLeaf() {
        return ((left == null) && (right == null));
    }

    public String dotString() {
        if (isLeaf())
            return "\"" + data.symbol + "_" + symCount + "\"";
        else
            return "X_" + symCount;
    }

    public int getDesscendantMaxDepth() {
        int l = 0, r = 0;
        if (left != null)
            l += left.getDesscendantMaxDepth();
        if (right != null)
            r += right.getDesscendantMaxDepth();
        return Math.max(l, r) + 1;
    }

    public void toDot(String code) {
        if (left != null) {
            System.out.println(dotString() + "->" + left.dotString()
                    + " [label=" + code + "1];");
            left.toDot(code + "1");
        }

        if (right != null) {
            System.out.println(dotString() + "->" + right.dotString()
                    + " [label=" + code + "0];");
            right.toDot(code + "0");
        }
    }

}
