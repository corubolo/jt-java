# Decoding algorithms #


Our implementation is focused on the Int32 Compressed Data Packet format. It is used to encode/compress a collection of data into a series of Int32 based symbols. It is the only format used to encode the geometry of the different segments, for instance the X, Y, Z coordinates or the normals coordinates. All the decoding code is located in the package `uk.ac.liv.jt.codec`

This corresponds to data types like `VecU32{Int32CDP, Pred}` where Pred is a predictor algorithm which improves the compression. Depending on the data we want to decode, the decoded vector of U32 (or I32) values are "dequantized" to obtain float values. In our implementation, the entry point of the source code for the decoding algorithms is the class `Int32Compression`. More precisely the entry points are the methods `read_VecI32_Int32CDP()` and `read_VecU32_Int32CDP()`.

The `read_Int32CDP()` method is the implementation of the Int32 Compressed Data Packet data collection of figure 169. The first part is the decoding of the Out Of Band values for Huffman and Arithmetic. These values are also encoding using the BitLength algorithm. The second part is used to decode the probability context tables. The last part calls the `getResidualValues()` method which reads the encoded bits from the file and applies the correct decoding algorithm.

The encoded bits is called CodeText in the specification. It is encoded as a VecU32 meaning that we need to read chunk of 4 bytes, respecting the byte order (see `getCodeText()`). These encoded bits are then sent to the decoding algorithm specified in the header of the format, it returns a vector of U32 on which we apply the predictor algorithm `Predictors.unpackResidualsOverwrite()`.

The different algorithms are described on specific pages of the wiki :
  * [BitLength algorithm](BitLengthDecoding.md)
  * [Huffman algorithm](HuffmanDecoding.md)
  * [Arithmetic algorithm](ArithmeticDecoding.md)

The dequantization algorithms are easy to implement based on the information given from the specification.

  * The `UniformQuantisation` class is used to decode X, Y and Z values. The parameters for this transformation are found in the file and a simple formula is applied on each I32 value.

  * The `DeeringNormalCodec` class transforms the sextant, octant, theta, and psi values to a 3D Vector, a normal. The code is a bit more complex because it uses a set of precomputed cosinus and sinus tables to improve efficiency but the implementation should be sufficient to understand.


_All figures references are related to the JT File Format Reference version 8.1 revision D._