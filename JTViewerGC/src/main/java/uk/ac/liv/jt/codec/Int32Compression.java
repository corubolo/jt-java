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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import uk.ac.liv.jt.codec.Predictors.PredictorType;
import uk.ac.liv.jt.debug.DebugInfo;
import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.format.BitBuffer;
import uk.ac.liv.jt.format.ByteReader;
import uk.ac.liv.jt.types.U32Vector;

public class Int32Compression {

    public enum CompressedDataPacket {
        Int32CDP, Float64CDP
    }

    public static final int NULL_CODEC = 0;
    public static final int BITLENGTH_CODEC = 1;
    public static final int HUFFMAN_CODEC = 2;
    public static final int ARITHMETIC_CODEC = 3;

    public static int[] read_Int32CDP(ByteReader reader) throws IOException {
        int codeTextLength = 0;
        int valueElementCount = 0;
        int numSymbolsToRead = 0;
        int codecType = reader.readU8();
        Int32ProbCtxts probContext = new Int32ProbCtxts(reader);

        /*
         * numSymbolsToRead – Refers to the total number of symbols to be
         * decoded (i.e. Value Element Count in section 8.1.1 when the number of
         * Probability Context Tables is equal to 1, or Symbol Count when the
         * number of Probability Context Tables is 2).
         */

        if (DebugInfo.debugCodec)
            System.out.println("Codec Type: " + codecType);

        if ((codecType == HUFFMAN_CODEC) || (codecType == ARITHMETIC_CODEC)) {

            probContext.read(codecType);

            int outOfBandValueCount = reader.readI32();

            if (outOfBandValueCount > 0) {

                if (DebugInfo.debugCodec) {
                    System.out.println();
                    System.out.println("** Out Of Band Values **");
                }
                HuffTreeNode.oob = true;
                int[] outOfBand = read_Int32CDP(reader);
                HuffTreeNode.oob = false;
                if (DebugInfo.debugCodec) {
                    System.out.println("Out Of Band Values (" + outOfBand.length + ") ");
                    //                    for (int element : outOfBand)
                    //                        System.out.print(element + " ");
                    //                    System.out.println();
                }

                probContext.setOutOfBandValues(outOfBand);
            }
        }

        if (codecType != NULL_CODEC) {
            codeTextLength = reader.readI32();
            valueElementCount = reader.readI32();

            if (DebugInfo.debugCodec) {
                System.out.println();
                System.out.println("Code Text Length: " + codeTextLength);
                System.out.println("Value Element Count: " + valueElementCount);
            }

            if (probContext.getContextTableCount() == 1
                    || probContext.getContextTableCount() == 0)
                numSymbolsToRead = valueElementCount;
            else if (probContext.getContextTableCount() == 2) {
                // Symbol Count
                numSymbolsToRead = reader.readI32();

                if (DebugInfo.debugCodec)
                    System.out.println("Symbol Count: " + numSymbolsToRead);
            } else
                System.err
                .println("Problem with number of tables in Prob Ctxt -"
                        + probContext.getContextTableCount()
                        + " tables");

        }



        // residualValues is the vector of decoded symbols
        int[] residualValues = getResidualValues(reader, codecType,
                codeTextLength, probContext, numSymbolsToRead, valueElementCount);

        if (DebugInfo.debugCodec) {
            System.out.println("Residual Values I32 (" + residualValues.length
                    + "): ");
            //            for (int residualValue : residualValues)
            //                System.out.print(residualValue + " ");
            //            System.out.println();
        }

        return residualValues;

    }

    public static int[] read_VecI32_Int32CDP(ByteReader reader,
            PredictorType predType) throws IOException {

        // residualValues is the vector of decoded symbols
        int[] residualValues = read_Int32CDP(reader);
        Predictors.unpackResidualsOverwrite(residualValues,
                predType);

        return residualValues;

    }

    public static U32Vector read_VecU32_Int32CDP(ByteReader reader,
            PredictorType predType) throws IOException {

        int[] primalValues = read_VecI32_Int32CDP(reader, predType);
//        long[] primalValuesU32 = new long[primalValues.length];
//
//        // Convert I32 to U32
//        for (int i = 0; i < primalValues.length; i++)
            //primalValuesU32[i] = primalValues[i] & 0xFFFFFFFFL;

        return new U32Vector(primalValues);
    }

    public static byte[] getCodeText(ByteReader reader, int codeTextLength)
    throws IOException {
        // Read the number of U32 in VecU32
        int count = reader.readI32();

        // Read count * U32
        byte[] codeText = reader.readBytes(count * 4);

        // Reorder the bytes if needed
        if (reader.getByteOrder() == ByteOrder.LITTLE_ENDIAN) {
            //System.out.println("LE");
            byte tmp;
            for (int i = 0; i < count; i++) {
                tmp = codeText[0 + i * 4];
                codeText[0 + i * 4] = codeText[3 + i * 4];
                codeText[3 + i * 4] = tmp;

                tmp = codeText[1 + i * 4];
                codeText[1 + i * 4] = codeText[2 + i * 4];
                codeText[2 + i * 4] = tmp;
            }
        }

        return codeText;
    }

    // decompress the codeText using Huffman/BitLength/Arithmetic(not yet)
    // algorithm
    public static int[] getResidualValues(ByteReader reader, int codecType,
            int codeTextLength, Int32ProbCtxts probContext,
            int numSymbolsToRead, int valueElementCount) throws IOException {

        int[] residualValues;
        // Get the array of U32 in a byte[] array (the byte order is taken into
        // account when this array is created)
        if (codecType == NULL_CODEC) {
            //System.out.println("NULLCODEC");
            codeTextLength = reader.readI32();
            int j = codeTextLength / 4;
            residualValues = new int[j];
            for (int i = 0; i < j; i++) {
                residualValues[i] = reader.readI32();
            }
        }
        else {
            try {
            byte[] codeText = getCodeText(reader, codeTextLength);
            if (codecType == BITLENGTH_CODEC)
                residualValues = decompressByBitLength(codeText, codeTextLength, numSymbolsToRead);
            else if (codecType == HUFFMAN_CODEC)
                residualValues = decompressByHuffman(codeText, codeTextLength,
                        probContext, numSymbolsToRead);
            else if (codecType == ARITHMETIC_CODEC) {
                ArithmeticCodec codec = new ArithmeticCodec();
                residualValues = codec.decode(codeText, codeTextLength,
                        probContext, numSymbolsToRead, valueElementCount);
            } else
                residualValues = null;
            } catch (ArrayIndexOutOfBoundsException x) {
                x.printStackTrace();residualValues = null;
            }
//        }else { // NULL_CODEC
//           
//            for (int i = 0; i < codeText.length / 4; i++) {
//                byte ch1 = codeText[i * 4 + 0];
//                byte ch2 = codeText[i * 4 + 1];
//                byte ch3 = codeText[i * 4 + 2];
//                byte ch4 = codeText[i * 4 + 3];
//
//                if (reader.getByteOrder() == ByteOrder.LITTLE_ENDIAN)
//                    residualValues[i] = ((ch4 << 24) | (ch3 << 16) | (ch2 << 8) | (ch1 << 0)) & 0x000000FF;
//                else
//                    residualValues[i] = ((ch1 << 24) | (ch2 << 16) | (ch3 << 8) | (ch4 << 0)) & 0x000000FF;
//            }

        }

        return residualValues;
    }

    /**
     * Decodes the given bytes by the bitlength algorithm.
     * 
     * @param encodedBytes
     *            List of encoded bytes
     * @param codeTextLength
     *            Total number of codetext bits expected
     * @param numSymbolsToRead 
     * @param asSigned
     *            Treat bits in signed mode
     * @return Decoded symbols
     */
    private static int[] decompressByBitLength(byte[] encodedBytes,
            int codeTextLength, int numSymbolsToRead) {
        BitBuffer encodedBits = new BitBuffer(
                ByteBuffer.wrap(encodedBytes));
        

        int bitFieldWith = 0;
        //ArrayList<Integer> decodedSymbols = new ArrayList<Integer>();
        int[] result = new int[numSymbolsToRead];
        int position = 0;
        while (encodedBits.getBitPos() < codeTextLength)
            if (encodedBits.readAsInt(1) == 0) {
                // Decode symbol with same bit field length
                int decodedSymbol = -1;
                if (bitFieldWith == 0)
                    decodedSymbol = 0;
                else {
                    decodedSymbol = encodedBits.readAsInt(bitFieldWith);
                    // Convert and sign-extend the symbol
                    decodedSymbol <<= (32 - bitFieldWith);
                    decodedSymbol >>= (32 - bitFieldWith);
                }
                result[position++] = decodedSymbol;
                //decodedSymbols.add(decodedSymbol);

            } else {
                // Adjust bit field length
                int adjustmentBit = encodedBits.readAsInt(1);
                do
                    if (adjustmentBit == 1)
                        bitFieldWith += 2;
                    else
                        bitFieldWith -= 2;
                while (encodedBits.readAsInt(1) == adjustmentBit);

                // Decode symbol with new bit field length
                int decodedSymbol = -1;
                if (bitFieldWith == 0)
                    decodedSymbol = 0;
                else {
                    decodedSymbol = encodedBits.readAsInt(bitFieldWith);
                    // Convert and sign-extend the symbol
                    decodedSymbol <<= (32 - bitFieldWith);
                    decodedSymbol >>= (32 - bitFieldWith);

                }
                result[position++] = decodedSymbol;
                //decodedSymbols.add(decodedSymbol);
            }

        //        int[] ret = new int[decodedSymbols.size()];
        //        for (int i = 0; i < ret.length; i++)
        //            ret[i] = decodedSymbols.get(i).intValue();
        return result;
    }

    private static int[] decompressByHuffman(byte[] encodedBytes,
            int codeTextLength, Int32ProbCtxts probCtxt, int numSymbolsToRead) {
        // ArrayList<Integer> tmpList = new ArrayList<Integer>();
        int[] result = new int[numSymbolsToRead];
        int position = 0;

        HuffTreeNode huffTree = HuffTreeNode.buildHuffmanTree(probCtxt);
        //        HuffTreeNode huffTree = HuffTreeNode.buildHuffmanTree2(probCtxt);

        HuffCodecContext codecContext = new HuffCodecContext();
        HuffTreeNode.assignCodeToTree(huffTree, codecContext);
//          huffTree.print();
//
//         huffTree.toDot("");

      //  codecContext.printCodes();
       // System.out.println();
        codecContext.makeCanonical();
      //  System.out.println();
        int[] outofBandValues = probCtxt.getOutOfBandValues();
        int cptOutOfBand = 0;

        int cpt=1;

        BitBuffer encodedBits = new BitBuffer(ByteBuffer.wrap(encodedBytes));
        
        HuffTreeNode node = huffTree;
        while (encodedBits.getBitPos() < codeTextLength) {
            int read = encodedBits.readAsInt(1);

            if (read == 1)
                node = node.getLeft();
            else
                node = node.getRight();

            // If the node is a leaf, output a symbol and restart
            if (node.isLeaf()) {
                int symbol = node.getSymbol();
                int outValue;
                cpt ++;

                if (symbol == -2) {
                    if (cptOutOfBand < outofBandValues.length) {
                        outValue = outofBandValues[cptOutOfBand];
                        cptOutOfBand++;
                        result[position++] = outValue;
                        //tmpList.add(outValue);
                    } else {
                        System.out.println("Error: out of out of band values!");
                    }
                } else {
                    outValue = node.getAssociatedValue();
                    result[position++] = outValue;
                    //tmpList.add(outValue);
                }

                node = huffTree;
            }
        }
        //System.out.println();
        //System.out.println("**" + (outofBandValues.length - cptOutOfBand));
        //        int[] res = new int[tmpList.size()];
        //        for (int i = 0; i < tmpList.size(); i++)
        //            res[i] = tmpList.get(i);

        return result;
    }

}
