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
package uk.ac.liv.jt.format.elements;
/**
 * This package contains all the elements currently interpreted by the JT parser.
 * Classes in this packages should be named according to the object names as in the 
 * /JTViewer/src/main/resources/object_type_identifiers.txt
 * file. This way they will be automatically picked up and used for parsing. 
 * Each class contains its specific attributes, inheriting from the appropriate class, and 
 * overrides the {@link uk.ac.liv.jt.format.JTElement#read()} read method implementing 
 * the specific reading functionality.
 * Most segment types in JT have a single element, of one type; the LSGSegment actually contains all the 
 * logical graph structure and makes use of most of the JTNode types in this package.
 * <p>
 * See 
 * {@link uk.ac.liv.jt.format.JTElement} class for the static loading of the elements and default read method.
 * See 
 * {@link uk.ac.liv.jt.format.JTNode} class for the base of all the Graph nodes.

 */

