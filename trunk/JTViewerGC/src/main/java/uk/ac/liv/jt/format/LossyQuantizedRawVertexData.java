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
package uk.ac.liv.jt.format;

import java.io.IOException;

import uk.ac.liv.jt.codec.DeeringNormalCodec;
import uk.ac.liv.jt.codec.Int32Compression;
import uk.ac.liv.jt.codec.Predictors.PredictorType;
import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.types.Vec3D;

/** The Lossy Quantized Raw Vertex Data collection contains all the 
 * per-vertex information (i.e. UV texture coordinates, color, normal 
 * vector, XYZ coordinate) stored in a “lossy” encoding/compression 
 * format for all primitives of the shape. p.237 */

public class LossyQuantizedRawVertexData {


    ByteReader reader;

    /* Quantization data/representation for a set of vertex coordinates */
    QuantizedVertexCoordArray quantVertexCoord;

    /* Quantization data/representation for a set of vertex normals */
    QuantizedVertexNormalArray quantVertexNorm;
    
    /* Vertex Data Indices is a vector of indices identifying each Vertex’s 
     * data */
    int[] vertexDataIndices;
    

    public LossyQuantizedRawVertexData(ByteReader reader) {
        super();
        this.reader = reader;

        quantVertexCoord = new QuantizedVertexCoordArray(reader);
        quantVertexNorm = new QuantizedVertexNormalArray(reader);
        vertexDataIndices = new int[0];
    }
    

    public void read(int normalBinding, int textureCoordBinding, 
    		int colorBinding) throws IOException {

        if (DebugJTReader.debugMode)
            System.out.println("** Lossy Quantized Raw Vertex Data **");

        quantVertexCoord.read();

        if (normalBinding != 0)
        	quantVertexNorm.read();

        if (textureCoordBinding != 0) {
            // this.readQuantizedVertexTextureCoordArray();

        }

        if (colorBinding != 0) {
            // this.readQuantizedVertexColorArray();

        }
        
        if (DebugJTReader.debugMode) {
            System.out.println("** Vertex Data Indices **");
        }

        /* Vertex Data Indices is a vector of indices (one per vertex) into 
         * the uncompressed/dequantized unique vertex data arrays (Vertex 
         * Coords, Vertex Normals, Vertex Texture Coords, Vertex Colors) 
         * identifying each Vertex’s data (i.e. for each Vertex there is an 
         * index identifying the location within the unique arrays of the 
         * particular Vertex’s data). The Compressed Vertex Index List uses 
         * the Int32 version of the CODEC to compress and encode data. */
        vertexDataIndices = Int32Compression.read_VecI32_Int32CDP(reader,
                PredictorType.StripIndex);

        if (DebugJTReader.debugMode) {
        	System.out.println();
            System.out.println(" => Vertex Data Indices (" + vertexDataIndices.length + ")");

//            for (int vertexDataIndice : vertexDataIndices)
//                System.out.print(vertexDataIndice + " ");
//            System.out.println();
        }

    }

    
    public int[] getVertexDataIndices() {
        return vertexDataIndices;
    }

    
    public QuantizedVertexNormalArray getQuantVertexNorm() {
		return quantVertexNorm;
	}


	public QuantizedVertexCoordArray getQuantVertex() {
        return quantVertexCoord;
    }

}
