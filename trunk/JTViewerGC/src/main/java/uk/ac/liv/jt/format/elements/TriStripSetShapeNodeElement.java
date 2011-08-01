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

import uk.ac.liv.jt.segments.LODSegment;
import uk.ac.liv.jt.segments.LSGSegment;
import uk.ac.liv.jt.segments.ShapeSegment;
import uk.ac.liv.jt.types.GUID;
import de.jreality.geometry.IndexedFaceSetFactory;
import de.jreality.scene.SceneGraphComponent;
/** 
 * This class contains strip set data reference (usually as a reference to a LOD segment) 
 * that constitutes the faceted geometry of the objects. 
 * @author fabio
 *
 */
public class TriStripSetShapeNodeElement extends VertexShapeNodeElement {

    public static  boolean OGL_FIX = false;

    @Override
    public void read() throws IOException {
        super.read();
    }

    /** 
     * Used to retrieve the segment LOD GUID for the shape implementation referenced. 
     * This is used to allow late loading of the actual face data. 
     * @return
     * @throws IOException
     */
    public GUID getLODSegmentId() throws IOException {
        LateLoadedPropertyAtomElement l = null;
        for (BasePropertyAtomData p[] : properties)
            if (p[1] instanceof LateLoadedPropertyAtomElement
                    && p[0] instanceof StringPropertyAtomElement) {
                StringPropertyAtomElement s = (StringPropertyAtomElement) p[0];
                if (s.value.equals("JT_LLPROP_SHAPEIMPL"))
                    l = (LateLoadedPropertyAtomElement) p[1];
            }

        if (l == null)
            throw new IOException("Missing LOD segment reference");
        return l.segmentId;

    }


    /**
     * Given the LSG segment, retrieves and parses the geometry for the current node.
     * The geometry returned is as a tree of @{link {@link SceneGraphComponent} for the JReality system.
     * The geometry is stored in a separate segment identified in this node's attributes.
     * 
     * @param lsgSegment The Logical Scene Graph for this model.
     * @return 
     * @throws IOException
     */
    public SceneGraphComponent[] getGemoetryComponents(LSGSegment lsgSegment)
    throws IOException {

        GUID segmentid = getLODSegmentId();
        //System.out.println(segmentid);
        LODSegment lod;
        TriStripSetShapeLODElement l;
        Object o = lsgSegment.getSegment(segmentid);
        if (o instanceof LODSegment) {
            lod = (LODSegment) o;
            lod.read();
            l= (TriStripSetShapeLODElement) lod.e;
        } else {
            System.err.println("Unsupported segment type: " + o.getClass());
            return null;
        }

        IndexedFaceSetFactory ifsf = new IndexedFaceSetFactory();
        ifsf.setGenerateEdgesFromFaces(false);

        ifsf.setGenerateFaceNormals(OGL_FIX);
        ifsf.setGenerateAABBTree(false);
        ifsf.setGenerateEdgeLabels(false);
        ifsf.setGenerateVertexLabels(false);
        ifsf.setGenerateVertexNormals(OGL_FIX);
        int nbdVertices=0;
        int nbdfaces=0;

        for (int i = 0; i < l.primitiveListIndices.length - 1; i++) {
            int start = l.primitiveListIndices[i];
            int end = l.primitiveListIndices[i + 1];
            nbdVertices += end - start;
            nbdfaces += end - start -2;
        }

        double[] vertices = new double[nbdVertices*3];
        double[] normals = new double[nbdVertices*3];

        int[] faceIndices = new int[nbdfaces*3];
        SceneGraphComponent[] ret = new SceneGraphComponent[1];//[l.primitiveListIndices.length - 1];

        if (l.uncompressed) {
            vertices = l.vertex;
            normals= l.normal;
            int k =0;
            for (int i = 0; i < l.primitiveListIndices.length - 1; i++) {
                int start = l.primitiveListIndices[i];
                int end = l.primitiveListIndices[i + 1];
                for (int f = start; f <end-2; f++){
                    if (f % 2 == 0) {
                        faceIndices[k+0] = f;
                        faceIndices[k+1] = f + 1;
                        faceIndices[k+2] = f + 2;
                    } else {
                        faceIndices[k+0] = f;
                        faceIndices[k+2] = f + 1;
                        faceIndices[k+1] = f + 2;
                    }
                    k+=3;
                }
            }
        }
        else {
            int k =0;
            for (int i = 0; i < l.primitiveListIndices.length - 1; i++) {
                int start = l.primitiveListIndices[i];
                int end = l.primitiveListIndices[i + 1];

                for (int v = start; v < end; v++) {

                    int j = v*3 ;
                    int idx_raw = l.vertexDataIndices[v];
                    vertices[j+0] = l.quantVertexCoord.getXValues()[idx_raw];
                    vertices[j+1] = l.quantVertexCoord.getYValues()[idx_raw];
                    vertices[j+2] = l.quantVertexCoord.getZValues()[idx_raw];

                    normals[j+0] = l.normals[idx_raw].getX();
                    normals[j+1] = l.normals[idx_raw].getY();
                    normals[j+2] = l.normals[idx_raw].getZ();
                }
                for (int f = start; f <end-2; f++){
                    if (f % 2 == 0) {
                        faceIndices[k+0] = f;
                        faceIndices[k+1] = f + 1;
                        faceIndices[k+2] = f + 2;
                    } else {
                        faceIndices[k+0] = f;
                        faceIndices[k+2] = f + 1;
                        faceIndices[k+1] = f + 2;
                    }
                    k+=3;
                }
            }
        }
        l = null;
        lod.e = null;

        ifsf.setFaceCount(faceIndices.length/3);
        ifsf.setFaceIndices(faceIndices);
        ifsf.setVertexCount(vertices.length/3);
        ifsf.setVertexCoordinates(vertices);
        //]
        if (!OGL_FIX)
            ifsf.setVertexNormals(normals);

        ifsf.update();


        SceneGraphComponent sgc = new SceneGraphComponent("GEOMETRY");
        sgc.setGeometry(ifsf.getIndexedFaceSet());

        ifsf = null;
        ret[0] = sgc;
        return ret;

    }

    private LODSegment readShape() {
        // TODO Auto-generated method stub
        return null;
    }

}
