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

import java.io.IOException;

import uk.ac.liv.jt.codec.Int32Compression;
import uk.ac.liv.jt.codec.Predictors.PredictorType;
import uk.ac.liv.jt.format.JTQuantizationParam;
import uk.ac.liv.jt.format.LossyQuantizedRawVertexData;
import uk.ac.liv.jt.format.QuantizedVertexCoordArray;
import uk.ac.liv.jt.format.QuantizedVertexNormalArray;
import uk.ac.liv.jt.types.Vec3D;
import de.jreality.geometry.IndexedFaceSetFactory;
import de.jreality.geometry.PointSetFactory;
import de.jreality.geometry.PointSetUtility;
import de.jreality.math.Pn;
import de.jreality.plugin.JRViewer;
import de.jreality.scene.Appearance;
import de.jreality.scene.Viewer;
import de.jreality.scene.proxy.scene.SceneGraphComponent;
import de.jreality.shader.CommonAttributes;
import uk.ac.liv.jt.debug.DebugInfo;

/** A Tri-Strip Set Shape LOD Element contains the geometric shape 
  definition data (e.g. vertices, polygons, normals, etc.) for a single 
  LOD of a collection of independent and unconnected triangle strips. 
  p.120 */ 
public class TriStripSetShapeLODElement extends ShapeLODElement {


    public int[] vertexDataIndices;
    /** Primitive List Indices is a vector of indices into the 
     uncompressed Raw Vertex Data marking the start/beginning of 
     primitives. */
    public int[] primitiveListIndices;
    public QuantizedVertexCoordArray quantVertexCoord;
    public QuantizedVertexNormalArray quantVertexNorm;

    public double[] vertex;
    public double[] normal;



    /* Normals for the vertices */
    Vec3D[] normals;
    private JTQuantizationParam quantParam;
    public boolean uncompressed;

    public static boolean testDisplay = false;
    public static boolean testDisplayNormals = false;

    @Override
    public void read() throws IOException {
        readVertexShapeLODData();

        /* Version Number is the version identifier for this Tri-Strip Set 
         * Shape LOD. Version number “0x0001” is currently the only valid 
         * value. p.120 */
        int versionNumber = getReader().readI16();

        readVertexBasedShapeCompressedRepData();
    }
    /** Vertex Shape LOD Data collection contains the bindings and 
     * quantization settings for all shape LODs defined by a collection of 
     * vertices. p.118
     */
    public void readVertexShapeLODData() throws IOException {


        /* Version Number is the version identifier for this Vertex Shape LOD 
         * Data. Version number “0x0001” is currently the only valid value.
         * p.119
         */
        int versionNumber = getReader().readI16();

        /* Binding Attributes is a collection of normal, texture coordinate, 
         * and color binding information encoded within a single I32 using the 
         * following bit allocation. p.119
         */
        int bindingAttributes = getReader().readI32();

        quantParam = new JTQuantizationParam(getReader());
        quantParam.read();

        if (DebugInfo.debugMode) {
            System.out.println();
            System.out.println("*** Vertex Shape LOD Data ***");
            System.out.println("Version Number: " + versionNumber);
            System.out
            .println("Binding Attributes: " + bindingAttributes);
        }
    }

    public void readVertexBasedShapeCompressedRepData() throws IOException {
        /* The Vertex Based Shape Compressed Rep Data collection is the 
         * compressed and/or encoded representation of the vertex coordinates, 
         * normal, texture coordinate, and color data for a vertex based 
         * shape. p.234 */

        /* Version Number is the version identifier for this Vertex Based 
         * Shape Rep Data. Version number “0x0001” is currently the only valid 
         * value. */
        int versionNumber = getReader().readI16();

        /* Normal Binding specifies how (at what granularity) normal vector 
         * data is supplied (“bound”) for the Shape Rep in either the Lossless 
         * Compressed Raw Vertex Data */
        int normalBinding = getReader().readU8();

        /* Texture Coord Binding specifies how (at what granularity) texture 
         * coordinate data is supplied (“bound”) for the Shape Rep in either 
         * the Lossless Compressed Raw Vertex Data or Lossy Quantized Raw 
         * Vertex Data collections. */
        int textureCoordBinding = getReader().readU8();

        /* Color Binding specifies how (at what granularity) color data is 
         * supplied (“bound”) for the Shape Rep in either the Lossless 
         * Compressed Raw Vertex Data or Lossy Quantized Raw Vertex Data 
         * collections. */
        int colorBinding = getReader().readU8();

        if (DebugInfo.debugMode) {
            System.out.println();
            System.out.println("*** Vertex Based Shape Compressed RepData ***");
            System.out.println("Version Number: " + versionNumber);
            System.out.println("Normal Binding: " + normalBinding);
            System.out.println("Texture Coord Binding: "
                    + textureCoordBinding);
            System.out.println("Color Binding: " + colorBinding);
        }

        JTQuantizationParam quantParam = new JTQuantizationParam(getReader());
        quantParam.read();


        if (DebugInfo.debugMode) {
            System.out.println();
            System.out.println("** Primitive List Indices **");
        }

        /* Primitive List Indices is a vector of indices into the 
         * uncompressed Raw Vertex Data marking the start/beginning of 
         * primitives. Primitive List Indices uses the Int32 version of the 
         * CODEC to compress and encode data. */
        primitiveListIndices = Int32Compression.read_VecI32_Int32CDP(
                getReader(), PredictorType.Stride1);

        if (DebugInfo.debugMode) {
            System.out.println();
            System.out.println(" => Primitive List Indices (" + primitiveListIndices.length + ") ");
            for (int primitiveListIndice : primitiveListIndices)
                System.out.print(primitiveListIndice + " ");
            System.out.println();
        }

        LossyQuantizedRawVertexData rawVertexData = new LossyQuantizedRawVertexData(
                reader);

        if (quantParam.getBitsPerVertex() == 0) {
            readLosslessCompressedRawVertexData(normalBinding, textureCoordBinding, colorBinding);
            uncompressed = true;
        }
        else {
            rawVertexData.read(normalBinding, textureCoordBinding, colorBinding);
            quantVertexCoord = rawVertexData.getQuantVertex();
            vertexDataIndices = rawVertexData.getVertexDataIndices();
            normals = rawVertexData.getQuantVertexNorm().getNormals();
            uncompressed = false;
        }

        if (testDisplay) {
            display();
            //displayVertices();
        }

    }

    /**
     * displays a single LOD element; used for debugging purposes
     */
    private void display() {
        SceneGraphComponent scene = new SceneGraphComponent();

        for (int i = 0; i < primitiveListIndices.length - 1; i++) {
            int start = primitiveListIndices[i];
            int end = primitiveListIndices[i + 1];

            int nbVertices = end - start;

            double[][] vertices = new double[nbVertices][3];
            double[][] normals = new double[nbVertices][3];

            for (int v = start; v < end; v++) {
                int idx_raw = vertexDataIndices[v];
                vertices[v - start][0] = quantVertexCoord.getXValues()[idx_raw];
                vertices[v - start][1] = quantVertexCoord.getYValues()[idx_raw];
                vertices[v - start][2] = quantVertexCoord.getZValues()[idx_raw];

                normals[v - start][0] = this.normals[idx_raw].getX();
                normals[v - start][1] = this.normals[idx_raw].getY();
                normals[v - start][2] = this.normals[idx_raw].getZ();
            }

            IndexedFaceSetFactory ifsf = new IndexedFaceSetFactory();

            int[][] faceIndices = new int[vertices.length - 2][3];

            for (int f = 0; f < faceIndices.length; f++)
                if (f % 2 == 0) {
                    faceIndices[f][0] = f;
                    faceIndices[f][1] = f + 1;
                    faceIndices[f][2] = f + 2;
                } else {
                    faceIndices[f][0] = f;
                    faceIndices[f][2] = f + 1;
                    faceIndices[f][1] = f + 2;
                }

            ifsf.setVertexCount(vertices.length);
            ifsf.setVertexCoordinates(vertices);
            ifsf.setFaceCount(faceIndices.length);
            ifsf.setFaceIndices(faceIndices);

            ifsf.setGenerateEdgesFromFaces(true);

            ifsf.setVertexNormals(normals);
            // In case you don't have normals
            //ifsf.setGenerateFaceNormals(true);

            ifsf.update();

            SceneGraphComponent sgc = new SceneGraphComponent();
            sgc.setGeometry(ifsf.getIndexedFaceSet());

            Appearance app = new Appearance();
            app.setAttribute(CommonAttributes.VERTEX_DRAW, false);
            app.setAttribute(CommonAttributes.EDGE_DRAW, false);
            sgc.setAppearance(app);

            scene.addChild(sgc);
            // Display Vertices Normals
            if (testDisplayNormals)
                scene.addChild(PointSetUtility.displayVertexNormals(
                        ifsf.getPointSet(), .1, Pn.EUCLIDEAN));
            // Display Faces Normals (in case they are generated by jReality)
            // scene.addChild(IndexedFaceSetUtility.displayFaceNormals(ifsf.getIndexedFaceSet(),
            // .1, Pn.EUCLIDEAN));

        }

        Viewer v = JRViewer.display(scene); 
    }

    private void displayVertices() {
        PointSetFactory psf = new PointSetFactory();

        double [][] vertices = new double[quantVertexCoord.getXValues().length][3];

        for (int v = 0; v < quantVertexCoord.getXValues().length; v++) {
            vertices[v][0] = quantVertexCoord.getXValues()[v];
            vertices[v][1] = quantVertexCoord.getYValues()[v];
            vertices[v][2] = quantVertexCoord.getZValues()[v];
        }        
        psf.setVertexCount( vertices.length );
        psf.setVertexCoordinates( vertices );
        psf.update();

        Viewer v = JRViewer.display(psf.getPointSet());
    }

    private void readLosslessCompressedRawVertexData(int normalBinding, int textureCoordBinding, int colorBinding) throws IOException {
        int uncompressedDataSize = getReader().readI32();
        int compressedDataSize = getReader().readI32();
        int len;
        if (compressedDataSize > 0) {
            getReader().setInflating(true, compressedDataSize);
            len = uncompressedDataSize;
        } else {
            len = Math.abs(compressedDataSize);

        }
        int numFaces = primitiveListIndices.length-1;
        int numVertices = primitiveListIndices[numFaces];
//        System.out.println(numFaces);
//        System.out.println(numVertices);
        for (int n = 0; n < len; n++)
            if (textureCoordBinding == 1) {

            }
        if (colorBinding == 1 ) {

        }
        normal = new double[numVertices*3];
        vertex = new double[numVertices*3];
        for (int i=0;i<numVertices;i++) {
            int j = i *3;
            if (normalBinding == 1) {
                
                normal[j+0] = reader.readF32();
                normal[j+1] = reader.readF32();
                normal[j+2] = reader.readF32();

            }
            vertex[j+0] = reader.readF32();
            vertex[j+1] = reader.readF32();
            vertex[j+2] = reader.readF32();  

        }


    }

}
