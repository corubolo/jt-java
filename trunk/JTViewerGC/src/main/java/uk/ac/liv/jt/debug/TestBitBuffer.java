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
package uk.ac.liv.jt.debug;

import java.nio.ByteBuffer;
import java.util.Arrays;

import uk.ac.liv.jt.format.BitBuffer;


public class TestBitBuffer {

	public static String toString32(long nb) {
		String s = Long.toBinaryString(nb);
	
		if (s.length() < 32) {
			int n = 32 - s.length();
			
			char[] fill = new char[n];
			Arrays.fill(fill, '0');
			String zeroes = new String(fill);
			
			s = zeroes + s;
			
		}
		
		return s;
	
	}

	public static void printLong(long nb) {
		System.out.println(toString32(nb));
	}
	
    public static void main(String[] args) {
    	
    	byte [] tmp = { 112, 127, 24, 114,
    					112, 127, 24, 114};

        BitBuffer encodedBits = new BitBuffer(ByteBuffer.wrap(tmp));
        
//		for (int i=0 ; i < 16 ; i++) { 
//        	long j = encodedBits.readAsInt(4);
//            System.out.print(j + " ");
//		}
//        System.out.println();
//        System.out.println(encodedBits.getBitBufBitSize());


    	long tst = 4294967295l;
    	long test = 1887377522;
    	printLong(test);
//    	System.out.println();    	
//    	
    //    BitBuffer encodedBits = new BitBuffer(test);
        
//		for (int i=0 ; i < 16 ; i++) { 
//        	long j = encodedBits.readAsInt(2);
//		}
//    	System.out.println(); 
//		for (int i=0 ; i < 32 ; i++) { 
//        	long j = encodedBits.readAsInt(1);
//		}   
//    	
 /*      	long j = encodedBits.readAsInt(25);
       	printLong(j);
       	j = encodedBits.readAsInt(12);
       	printLong(j);
       	*/
    	
    	long j = encodedBits.readAsInt(8, 16);
    	printLong(j);

    	
    }
}
