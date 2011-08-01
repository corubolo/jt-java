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

public enum JTProperty {

    unit("JT_PROP_MEASUREMENT_UNITS"), name("JT_PROP_NAME"), partition(
            "PartitionType"), tristrip_layout("JT_PROP_TRISTRIP_DATA_LAYOUT"), simp_params(
            "JT_PROP_SIMP_PARAMS"), simp_name("JT_PROP_SIMP_NAME"), shape_type(
            "JT_PROP_SHAPE_DATA_TYPE"), shape_lloaded_reference(
            "JT_LLPROP_SHAPEIMPL"), shape_lloaded_pmi("JT_LLPROP_PMI"), shape_lloaded_metadata(
            "JT_LLPROP_METADATA"), BSphereCoverageFractionMax(
            "BSphereCoverageFractionMax"), EXTERNAL_REF("__EXTERNAL_REF"), nTrisLODs(
            "_nTrisLODs"), Angular("Angular"), Chordal("Chordal"),

    SEOccurrenceDisplayAsReference("SEOccurrenceDisplayAsReference::"), SEOccurrenceExcludeFromBOM(
            "SEOccurrenceExcludeFromBOM:"), SEOccurrenceExcludeFromInterference(
            "SEOccurrenceExcludeFromInterference::"), SEOccurrenceExcludeFromPhysicalProps(
            "SEOccurrenceExcludeFromPhysicalProps::"), SEOccurrenceHideInDrawing(
            "SEOccurrenceHideInDrawing:"), SEOccurrenceHideInSubAssembly(
            "SEOccurrenceHideInSubAssembly::"), SEOccurrenceID(
            "SEOccurrenceID::"), SEOccurrenceName("SEOccurrenceName:"), SEOccurrenceQuantity(
            "SEOccurrenceQuantity::"), TOOLKIT_CUSTOMER("TOOLKIT_CUSTOMER"), Translation_Date(
            "Translation Date::"), Translation_Version("Translator Version::"),

    AdvCompressLevel("AdvCompressLevel::"), AdvCompressLODLevel(
            "AdvCompressLODLevel::"), Angular2("Angular::"), CAD_Source(
            "CAD Source::"), Chordal2("Chordal::"), Name("Name::");

    private String key;

    private JTProperty(String key) {
        this.key = key;
    }

}
