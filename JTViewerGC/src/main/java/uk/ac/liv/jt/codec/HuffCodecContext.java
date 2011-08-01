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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class HuffCodecContext {
	
	/* Used to assign codes to the nodes of the Huffman tree */

    int length; // Used to tally up total encoded code length
    long code; // Code under construction
    int codeLength; // Length of Huffman code currently under construction.
    Hashtable<String, HuffCodeData> codes;

    public HuffCodecContext() {
        super();
        codes = new Hashtable<String, HuffCodeData>();
    }

    public void leftShift() {
        code = code << 1;
    }

    public void rightShift() {
        code = code >>> 1;
    }

    public void bitOr(int value) {
        code = code | value;
    }

    public void incLength() {
        codeLength++;
    }

    public void decLength() {
        codeLength--;
    }

    public long getCode() {
        return code;
    }

    public int getCodeLen() {
        return codeLength;
    }

    public void add(HuffCodeData data) {
        codes.put(data.codeToString(), data);
    }
    
    public void makeCanonical (){ 
        //long code = 0;
        LinkedList <HuffCodeData>e = new LinkedList<HuffCodeData>(codes.values());
        Collections.sort(e, new Comparator<HuffCodeData>() {public int compare(HuffCodeData o1, HuffCodeData o2) {
            int ret = (int)(o2.codeLen - o1.codeLen);
            if (ret == 0)
                 ret = (int)(o2.bitCode - o1.bitCode);
           return ret;
        }
        });
        code = e.get(0).bitCode;
        //int length = e.get(0).codeLen;
        for (int i=0;i<e.size();i++){
            HuffCodeData d1 = e.get(i);

        
//            String tmp = Long.toBinaryString(code);
//            if (tmp.length() != d.codeLen) {
//                long nb_0 = d.codeLen - tmp.length();
//                for (int k = 0; k < nb_0; k++)
//                    tmp = "0" + tmp;
//            }
//            System.out.println(tmp);

          //  int nextCL = (e.get(i+1<e.size()?i+1:i).codeLen);
            //code = (code  - 1 ) >> (nextCL  - ((int)d1.codeLen));
            if (i <e.size() -1 ) {
                HuffCodeData d2 = e.get(i+1);
                if (d1.index < d2.index && (Math.abs(d1.assValue) == Math.abs(d2.assValue))&& Math.abs(d2.assValue) == 2) {
                    int t;
                    t = d1.assValue;
                    d1.assValue = d2.assValue;
                    d2.assValue = t;
                    t = d1.symbol;
                    d1.symbol = d2.symbol;
                    d2.symbol = t;
                    t = d1.index;
                    d1.index = d2.index;
                    d2.index = t;
                    
              //      System.out.println("swap");
                }
            }
            //System.out.println(d1);
        }
        

            

    }

    public void printCodes() {
        Enumeration<HuffCodeData> e = codes.elements();

        while (e.hasMoreElements())
            System.out.println(e.nextElement());
    }

}
