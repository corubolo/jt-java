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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.types.GUID;

/**
 * This class represents the basic JTElement parent to all the element types
 * used in JT. This class dynamically looks up for element implementations and
 * uses them to read specific element attributes. Classes extending this should
 * be named according to the object names as in the
 * /JTViewer/src/main/resources/object_type_identifiers.txt file. This way they
 * will be automatically picked up and used for parsing.
 * <p>
 * To create a new element, use the factory method
 * {@link #createJTElement(ByteReader)} that will instantiate the appropriate
 * class based on the GUID identifier for the element type. To actually read the
 * element contents, call the {@link #read()} method.
 * 
 * @author fabio
 * 
 */
public class JTElement {
    
    
    private static final String identifier_map = 
                "10dd1035-2ac8-11d1-9b-6b-00-80-c7-bb-59-97BaseNodeElement\n" + 
    		"10dd101b-2ac8-11d1-9b-6b-00-80-c7-bb-59-97GroupNodeElement\n" + 
    		"10dd102a-2ac8-11d1-9b-6b-00-80-c7-bb-59-97InstanceNodeElement\n" + 
    		"10dd102c-2ac8-11d1-9b-6b-00-80-c7-bb-59-97LODNodeElement\n" + 
    		"ce357245-38fb-11d1-a5-06-00-60-97-bd-c6-e1MetaDataNodeElement\n" + 
    		"d239e7b6-dd77-4289-a0-7d-b0-ee-79-f7-94-94NULLShapeNodeElement\n" + 
    		"ce357244-38fb-11d1-a5-06-00-60-97-bd-c6-e1PartNodeElement\n" + 
    		"10dd103e-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PartitionNodeElement\n" + 
    		"10dd104c-2ac8-11d1-9b-6b-00-80-c7-bb-59-97RangeLODNodeElement\n" + 
    		"10dd10f3-2ac8-11d1-9b-6b-00-80-c7-bb-59-97SwitchNodeElement\n" + 
    		"10dd1059-2ac8-11d1-9b-6b-00-80-c7-bb-59-97BaseShapeNodeElement\n" + 
    		"98134716-0010-0818-19-98-08-00-09-83-5d-5aPointSetShapeNodeElement\n" + 
    		"10dd1048-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PolygonSetShapeNodeElement\n" + 
    		"10dd1046-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PolylineSetShapeNodeElement\n" + 
    		"e40373c1-1ad9-11d3-9d-af-00-a0-c9-c7-dd-c2PrimitiveSetShapeNodeElement\n" + 
    		"10dd1077-2ac8-11d1-9b-6b-00-80-c7-bb-59-97TriStripSetShapeNodeElement\n" + 
    		"10dd107f-2ac8-11d1-9b-6b-00-80-c7-bb-59-97VertexShapeNodeElement\n" + 
    		"4cc7a521-0728-11d3-9d-8b-00-a0-c9-c7-dd-c2WireHarnessSetShapeNode\n" + 
    		"10dd1001-2ac8-11d1-9b-6b-00-80-c7-bb-59-97BaseAttributeElement\n" + 
    		"10dd1014-2ac8-11d1-9b-6b-00-80-c7-bb-59-97DrawStyleAttributeElement\n" + 
    		"ad8dccc2-7a80-456d-b0-d5-dd-3a-0b-8d-21-e7FragmentShaderAttributeElement\n" + 
    		"10dd1083-2ac8-11d1-9b-6b-00-80-c7-bb-59-97GeometricTransformAttributeElement\n" + 
    		"10dd1028-2ac8-11d1-9b-6b-00-80-c7-bb-59-97InfiniteLightAttributeElement\n" + 
    		"10dd1096-2ac8-11d1-9b-6b-00-80-c7-bb-59-97LightSetAttributeElement\n" + 
    		"10dd10c4-2ac8-11d1-9b-6b-00-80-c7-bb-59-97LinestyleAttributeElement\n" + 
    		"10dd1030-2ac8-11d1-9b-6b-00-80-c7-bb-59-97MaterialAttributeElement\n" + 
    		"10dd1045-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PointLightAttributeElement\n" + 
    		"8d57c010-e5cb-11d4-84-0e-00-a0-d2-18-2f-9dPointstyleAttributeElement\n" + 
    		"aa1b831d-6e47-4fee-a8-65-cd-7e-1f-2f-39-dbShaderEffectsAttributeElement\n" + 
    		"10dd1073-2ac8-11d1-9b-6b-00-80-c7-bb-59-97TextureImageAttributeElement\n" + 
    		"2798bcad-e409-47ad-bd-46-0b-37-1f-d7-5d-61VertexShaderAttributeElement\n" + 
    		"10dd104b-2ac8-11d1-9b-6b-00-80-c7-bb-59-97BasePropertyAtomElement\n" + 
    		"ce357246-38fb-11d1-a5-06-00-60-97-bd-c6-e1DatePropertyAtomElement\n" + 
    		"10dd102b-2ac8-11d1-9b-6b-00-80-c7-bb-59-97IntegerPropertyAtomElement\n" + 
    		"10dd1019-2ac8-11d1-9b-6b-00-80-c7-bb-59-97FloatingPointPropertyAtomElement\n" + 
    		"e0b05be5-fbbd-11d1-a3-a7-00-aa-00-d1-09-54LateLoadedPropertyAtomElement\n" + 
    		"10dd1004-2ac8-11d1-9b-6b-00-80-c7-bb-59-97JTObjectReferencePropertyAtom\n" + 
    		"10dd106e-2ac8-11d1-9b-6b-00-80-c7-bb-59-97StringPropertyAtomElement\n" + 
    		"873a70c0-2ac8-11d1-9b-6b-00-80-c7-bb-59-97JTBRepElement\n" + 
    		"ce357249-38fb-11d1-a5-06-00-60-97-bd-c6-e1PMIManagerMetaDataElement\n" + 
    		"ce357247-38fb-11d1-a5-06-00-60-97-bd-c6-e1PropertyProxyMetaDataElement\n" + 
    		"3e637aed-2a89-41f8-a9-fd-55-37-37-03-96-82NullShapeLODElement\n" + 
    		"98134716-0011-0818-19-98-08-00-09-83-5d-5aPointSetShapeLODElement\n" + 
    		"10dd109f-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PolygonSetShapeLODElement\n" + 
    		"10dd10a1-2ac8-11d1-9b-6b-00-80-c7-bb-59-97PolylineSetShapeLODElement\n" + 
    		"e40373c2-1ad9-11d3-9d-af-00-a0-c9-c7-dd-c2PrimitiveSetShapeElement\n" + 
    		"10dd10ab-2ac8-11d1-9b-6b-00-80-c7-bb-59-97TriStripSetShapeLODElement\n" + 
    		"10dd10b0-2ac8-11d1-9b-6b-00-80-c7-bb-59-97VertexShapeLODElement\n" + 
    		"4cc7a523-0728-11d3-9d-8b-00-a0-c9-c7-dd-c2WireHarnessSetShapeElement\n" + 
    		"873a70e0-2ac9-11d1-9b-6b-00-80-c7-bb-59-97XTBRepElement\n" + 
    		"873a70d0-2ac8-11d1-9b-6b-00-80-c7-bb-59-97WireframeRepElement";

    public static final int HEADER_LENGTH = 4 + 1 + GUID.LENGTH; // 21

    /**
     * Maps the existing, supported classed given the GUID
     */
    static protected Map<GUID, Class<? extends JTElement>> classes = new HashMap<GUID, Class<? extends JTElement>>();
    /**
     * Maps the unsupported classed given the GUID
     */
    static protected Map<GUID, String> unSupported = new HashMap<GUID, String>();

    protected ByteReader reader;

    protected int length;
    
    public GUID id;
    
    protected int objectBaseType;

    protected int bindingAttributes;

    protected JTElement() {

    };

    /*
     * the maps from identifier to class are built here.
     */
    static {
        BufferedReader b = new BufferedReader(new StringReader(identifier_map));
        String s;
        try {
            while ((s = b.readLine()) != null) {
                GUID g = GUID.fromString("{" + s.substring(0, 42) + "}");
                String className = s.substring(42, s.length());

                try {
                    Class<JTElement> c = (Class<JTElement>) Class
                            .forName("uk.ac.liv.jt.format.elements."
                                    + className);
                    // System.out.println("Registered class " + className +
                    // "for GUID " + g);
                    classes.put(g, c);

                } catch (ClassNotFoundException e) {
                    unSupported.put(g, className);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                b.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * convenience method
     * 
     * @param s
     * @return
     */
    public static final GUID idFor(String s) {
        return GUID.fromString(s);
    }

    /**
     * Factory method to create new JTElements of the appropriate type. This
     * method will read the element header from reader in order to create the
     * appropriate class.
     * 
     * 
     * @param reader
     *            The source file
     * @return A JTElement of the appropriate class
     * @throws IOException
     */
    public static JTElement createJTElement(ByteReader reader)
            throws IOException {

        /* read header */
        int length = reader.readI32();
        GUID id = reader.readGUID();
        int objectBaseType = 0;
        if (!id.equals(GUID.END_OF_ELEMENTS))
            objectBaseType = reader.readUChar();

        JTElement jt;
        Class<? extends JTElement> c = classes.get(id);
        if (c == null)
            jt = new JTElement();
        else
            try {
                jt = c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                jt = new JTElement();
            }
        jt.setReader(reader);
        jt.id = id;
        jt.length = length;
        jt.objectBaseType = objectBaseType;
        return jt;
    }

    /**
     * The read method should be overridden by the specific element types to
     * implement the actual data reading. The default implementation will simply
     * skip the appropriate number of bytes as indicated in the length.
     * 
     * @throws IOException
     */
    public void read() throws IOException {
        if (id.equals(GUID.END_OF_ELEMENTS))
            return;

        int dataSectionLength = getLength() - HEADER_LENGTH + 4;
        if (dataSectionLength > 0) {
            byte[] s = getReader().readBytes(dataSectionLength);
            if (DebugJTReader.debugMode) {
                String ss = "";
                for (byte b : s)
                    ss += (b & 0xff) + " ";
                System.out.println(" Skipped Content: " + ss);
            }
            System.out.println("TODO: implement "
                    + ((this.getClass().equals(JTElement.class)) ? unSupported
                            .get(getId()) : this.getClass().getName()));
            System.out.println("Skipped " + dataSectionLength + " bytes");
        }

    }

    @Override
    public String toString() {
        String s = new String();
        s += "Element ID:" + getId() + '\n';
        s += "Element Length:" + getLength() + '\n';
        s += "Object Base Type:" + getObjectBaseType() + '\n';

        return s;
    }

    public int getObjectBaseType() {
        return objectBaseType;
    }

    public void setObjectBaseType(int objectBaseType) {
        this.objectBaseType = objectBaseType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public GUID getId() {
        return id;
    }

    public void setId(GUID id) {
        this.id = id;
    }

    public int getBindingAttributes() {
        return bindingAttributes;
    }

    public void setBindingAttributes(int bindingAttributes) {
        this.bindingAttributes = bindingAttributes;
    }

    public final void read(ByteReader r) throws IOException {
        setReader(r);
        read();
    }

    public void setReader(ByteReader reader) {
        this.reader = reader;
    }

    public ByteReader getReader() {
        return reader;
    }

}
