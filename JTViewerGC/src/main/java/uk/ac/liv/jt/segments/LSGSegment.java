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
package uk.ac.liv.jt.segments;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.ac.liv.jt.format.JTElement;
import uk.ac.liv.jt.format.elements.BaseAttributeElement;
import uk.ac.liv.jt.format.elements.BaseNodeElement;
import uk.ac.liv.jt.format.elements.BasePropertyAtomData;
import uk.ac.liv.jt.format.elements.GeometricTransformAttributeElement;
import uk.ac.liv.jt.format.elements.GroupNodeElement;
import uk.ac.liv.jt.format.elements.InstanceNodeElement;
import uk.ac.liv.jt.format.elements.MaterialAttributeElement;
import uk.ac.liv.jt.format.elements.PartitionNodeElement;
import uk.ac.liv.jt.format.elements.TriStripSetShapeNodeElement;
import uk.ac.liv.jt.types.GUID;
import de.jreality.scene.Appearance;
import de.jreality.scene.SceneGraphComponent;
import de.jreality.shader.CommonAttributes;


/**
 * The Logical Scene Graph segment contains the main structural information for a JT file.
 * The Scene graph contain references to the metadata, attributes (material, transformation) 
 * and properties, plus all the references to the actual geometric data.
 * 
 * Use the standard {@link #read()} method to generate the internal representation of the graph.
 * Use the {@link #generateSceneGraph()} method to generate the JReality scene graph and load 
 * all the geometry from the JT file.  
 * 
 * @author fabio
 *
 */
public class LSGSegment extends JTSegment {

    /** the Graph structure holding the LSG representation */
    Map<BaseNodeElement, List <BaseNodeElement>> graph;

    /** The JReality graph holding the LSG representation, created on demand */
    SceneGraphComponent theWorld = null;

    /** the root of the JT Graph representation used for visiting the graph */
    GroupNodeElement start;

    /** specifies if rendering the scene graph is requested */
    public static boolean doRender = false;

    /** Generates and returns the {@link SceneGraphComponent} JReality graph for the current JT file 
     *  Please note the Graph is generated on demand. 
     *  
     * */
    public SceneGraphComponent generateSceneGraph() throws IOException {
        if (graph == null)
            read();
        if (theWorld == null) {
            createSceneGraph();
        }
        return theWorld;

    }

    /** creates the JReality scene graph if not previously generated */
    private void createSceneGraph() throws IOException {
        theWorld = new JTSceneGraphComponent("JT File");
        HashMap<TriStripSetShapeNodeElement, SceneGraphComponent> geomCache;
        geomCache = new HashMap<TriStripSetShapeNodeElement, SceneGraphComponent>(10);
        Appearance app = new Appearance();
        app.setAttribute(CommonAttributes.VERTEX_DRAW, false);
        app.setAttribute(CommonAttributes.EDGE_DRAW, false);
        theWorld.setAppearance(app);
        addChilds(start, theWorld, geomCache);
        geomCache.clear();
    }

    /** recursively visits the internal graph representation to create the JReality graph 
     *  the late loaded Geometry data is actually loaded here 
     * 
     * */
    private void addChilds(BaseNodeElement current, SceneGraphComponent sg,HashMap<TriStripSetShapeNodeElement, SceneGraphComponent> geomCache )
    throws IOException {
//        if (current.ignore()){
//            System.out.println("Ignore node");
//            return;
//        }
        JTSceneGraphComponent s = new JTSceneGraphComponent(current.toString());
        sg.addChild(s);
        
        if (current.properties != null)
            s.properties = current.properties;


        // the only case currently supported for geometry data:
        if (current instanceof TriStripSetShapeNodeElement) {
            TriStripSetShapeNodeElement t = (TriStripSetShapeNodeElement) current;
            try {
                // if the data is already used, reuse it 
                if (geomCache.containsKey(t)) {
                    s.addChild(geomCache.get(t));
                }
                // otherwise read the geometry data from the file and add it 
                else {
                    s.addChildren(t.getGemoetryComponents(this));
                    geomCache.put(t, s);
                }
         
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         if (current instanceof PartitionNodeElement) {
            PartitionNodeElement pne = (PartitionNodeElement) current;
            if (!pne.equals(start)){
                s.addChild(pne.getGeometry(this));
            }
                
        }
        
        // in case of attributes
        for (BaseAttributeElement bae : current.attributes) {
            if (bae instanceof GeometricTransformAttributeElement) {
                GeometricTransformAttributeElement gt = (GeometricTransformAttributeElement) bae;
                s.setTransformation(gt.getTransformation());
            }
            if (bae instanceof MaterialAttributeElement) {
                MaterialAttributeElement mae = (MaterialAttributeElement) bae;
                s.setAppearance(mae.getAppereance());
            }
        }
        // add all the children, navigating the structure
        Collection<BaseNodeElement> c = graph.get(current);
        for (BaseNodeElement b : c)
            addChilds(b, s, geomCache);

    }

    /** 
     * Method to get the internal graph representation.
     * This method does not read all the late loaded segments.
     * @return the internal graph representation.
     * @throws IOException
     */
    public Map<BaseNodeElement, List <BaseNodeElement>> getGraph() throws IOException{
        if (graph == null)
            read();
        return graph;
    }
    @Override
    /** reads the segment LSG and generates the internal representation */
    public void read() throws IOException {

        super.read();
        
        
        graph = new HashMap<BaseNodeElement, List <BaseNodeElement>>(100);
        HashMap<Integer, BaseNodeElement> nodes = new HashMap<Integer, BaseNodeElement>(100);
        HashMap<Integer, BaseAttributeElement> attributes = new HashMap<Integer, BaseAttributeElement>(100);
        HashMap<Integer, BasePropertyAtomData> properties = new HashMap<Integer, BasePropertyAtomData>(100);

        // read all the nodes and attributes
        while (true) {

            JTElement element2 = JTElement.createJTElement(reader);

            // *** SEE FIGURE 9: the ZLIB header is applied ONLY to the first
            // element in the Segment.
            element2.read(); 
            // End of elements
            if (element2.id.equals(GUID.END_OF_ELEMENTS))
                break;
            if (element2 instanceof PartitionNodeElement && start == null) {
                PartitionNodeElement pne = (PartitionNodeElement) element2;
                start = pne;
            }
            // we have an actual node
            if (element2 instanceof BaseNodeElement) {
                BaseNodeElement node = (BaseNodeElement) element2;
                nodes.put(node.getObjectID(), node);
             // we have an attribute
            } else if (element2 instanceof BaseAttributeElement) {
                BaseAttributeElement attr = (BaseAttributeElement) element2;
                attributes.put(attr.getObjectID(), attr);
            } else
                System.out
                .println("Should not happen : not node nor attribute");

        }

        //read all the Property Atom Elements

        while (true) {

            JTElement element2 = JTElement.createJTElement(reader);

            // *** SEE FIGURE 9: the ZLIB header is applied ONLY to the first
            // element in the Segment.

            element2.read();

            if (element2.id.equals(GUID.END_OF_ELEMENTS))
                break;
            if (element2 instanceof BasePropertyAtomData) {
                BasePropertyAtomData node = (BasePropertyAtomData) element2;
                properties.put(node.getObjectID(), node);

            } else
                System.out
                .println("Error: Non property element found in the Property atoms ");

        }


        // for each Node add attributes, childs and instances
        int edgeN = 0;
        for (BaseNodeElement b : nodes.values()) {
            // add all the attributes
            for (int i = 0; i < b.attObjectId.length; i++) {
                BaseAttributeElement baseAttributeElement = attributes
                .get(b.attObjectId[i]);
                b.attributes[i] = baseAttributeElement;
                if (baseAttributeElement == null)
                    System.out.println("Error: null attribute!");
            }
            // add the vertex to the graph
            List<BaseNodeElement> l = new LinkedList<BaseNodeElement>();
           // graph.addVertex(b);
            // if it has child, add them to the graph
            if (b instanceof GroupNodeElement) {
                GroupNodeElement g = (GroupNodeElement) b;
                for (int child : g.childNodeObjectId) {
                    BaseNodeElement to = nodes.get(child);
                    if (to == null)
                        System.out.println("Error: child is a null node!");
                    else
                        l.add(to);
                }
            }
            // if it is an instance node, add the referenced node to the instanced node
            if (b instanceof InstanceNodeElement) {
                InstanceNodeElement ine = (InstanceNodeElement) b;
                BaseNodeElement to = nodes.get(ine.instancedNodeObjectId);
                if (to == null)
                    System.out.println("Error: child is a null node!");
                else
                    l.add(to);
            }
            graph.put(b, l);
        }


        // Finally read the Property table, adding the properties to the nodes
        
        short versionNumber = reader.readI16();
        BasePropertyAtomData[][] dummy = new BasePropertyAtomData[0][0];
        int count = reader.readI32();
        for (int i = 0; i < count; i++) {
            int id = reader.readI32();
            int key, value;
            LinkedList<BasePropertyAtomData[]> pro = new LinkedList<BasePropertyAtomData[]>();
            // System.out.println("\nNode object id: " + id + "(" + (i + 1)
            // + " of " + count + ")");
            while ((key = reader.readI32()) != 0) {
                value = reader.readI32();
                // System.out.println(properties.get(key) + " = "
                // + properties.get(value) + ";");
                BasePropertyAtomData basePropertyAtomData = properties.get(key);
                BasePropertyAtomData basePropertyAtomData2 = properties
                .get(value);
                pro.add(new BasePropertyAtomData[] { basePropertyAtomData,
                        basePropertyAtomData2 });

            }
            nodes.get(id).properties = pro.toArray(dummy);
        }

        // Graph rendering: represents the graph to screen
        if (doRender)
            renderGraphRepresentation();

    }

    /** returns the JTSegment for  GUID */
    public JTSegment getSegment(GUID segmentid) {

        return file.getSegment(segmentid);
    }
    
    
    
    /** Method to render the graph to a graph diagram, for debugging*/

    public void renderGraphRepresentation() {
//
//        
//        Graph<BaseNodeElement, Integer> gg = new DirectedSparseGraph<BaseNodeElement, Integer>();
//        
//        final VisualizationViewer<BaseNodeElement, Integer> vv = new VisualizationViewer<BaseNodeElement, Integer>(
//                new FRLayout<BaseNodeElement, Integer>( graph, new Dimension(1600, 1000)));
//        final JFrame frame = new JFrame();
//        Container content = frame.getContentPane();
//        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
//        content.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer, Number>();
//        vv.setGraphMouse(gm);
//        vv.addGraphMouseListener(new GraphMouseListener<BaseNodeElement>() {
//
//            public void graphReleased(BaseNodeElement arg0, MouseEvent arg1) {
//                // TODO Auto-generated method stub
//
//            }
//
//            public void graphPressed(BaseNodeElement arg0, MouseEvent arg1) {
//                // TODO Auto-generated method stub
//
//            }
//
//            public void graphClicked(BaseNodeElement arg0, MouseEvent arg1) {
//                for (BaseAttributeElement a : arg0.attributes)
//                    System.out.print(" " + a);
//
//                String s = "";
//                if (arg0.properties != null)
//                    for (BasePropertyAtomData[] a : arg0.properties)
//                        s += "\n" + a[0] + " = " + a[1];
//
//                System.out.println(s);
//
//            }
//        });
//        final ScalingControl scaler = new CrossoverScalingControl();
//
//        JButton plus = new JButton("+");
//        plus.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scaler.scale(vv, 1.1f, vv.getCenter());
//            }
//        });
//        JButton minus = new JButton("-");
//        minus.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
//            }
//        });
//
//        JCheckBox lo = new JCheckBox("Show Labels");
//        lo.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                vv.repaint();
//            }
//        });
//        lo.setSelected(true);
//        vv.getRenderContext().setVertexLabelTransformer(
//                new Transformer<BaseNodeElement, String>() {
//
//                    public String transform(BaseNodeElement arg0) {
//                        return arg0.toString();
//                    }
//                });
//        vv.setVertexToolTipTransformer(new Transformer<BaseNodeElement, String>() {
//
//            public String transform(BaseNodeElement arg0) {
//                String s = "<html> <body>";
//                if (arg0.properties != null)
//                    for (BasePropertyAtomData[] a : arg0.properties)
//                        s += "<br>\n" + a[0] + " = " + a[1];
//                if (arg0.attributes != null)
//                    for (BaseAttributeElement a : arg0.attributes)
//                        s += "<br>\n" + a;
//
//                return s;
//            }
//        });
//        JPanel controls = new JPanel();
//        controls.add(plus);
//        controls.add(minus);
//        controls.add(lo);
//        controls.add(((DefaultModalGraphMouse<Integer, Number>) gm)
//                .getModeComboBox());
//        content.add(controls, BorderLayout.SOUTH);
//
//        frame.pack();
//        frame.setVisible(true);
    }


}
