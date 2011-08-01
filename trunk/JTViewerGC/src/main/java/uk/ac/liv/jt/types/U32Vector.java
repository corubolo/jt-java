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
package uk.ac.liv.jt.types;
/**
 * Classes that wraps an int vector, used to represent a Unsigned int32 vector (unsigned int does not exist in Java); 
 * this class will only allow access using the get method that will perform the required conversion on demand and return a long. 
 * If the data needs to be accessed multiple times, call the convertAllData method; that will perform the long conversion once. 
 * 
 * @author fabio
 *
 */
public class U32Vector {

    
    private int[] content;
    private long[] contentLong;
    
    public U32Vector(int[] data) {
        content = data;
    }
    
    public long get(int index) {
        return content[index] & 0xFFFFFFFFL;
    }
    
    public int length() { return content.length;}
    
    public long[] convertAllData () {
        if (contentLong==null){
            contentLong = new long[content.length];
            for (int i=0;i< content.length;i++){
                contentLong[i] = content[i] & 0xFFFFFFFFL;
            }
        }
        return contentLong;
    }
}

