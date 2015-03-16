# Huffman Algorithm #

Huffman compression is a lossless compression algorithm that uses a variable length codeword to encode a source symbol. The codes are assigned to the symbols based on the probabilities of the symbol occurring. Huffman coding builds an extended and complete binary tree of minimum weighted path length, the tree is stored in the probability context table.

The decoding algorithm is managed by the method `decompressByHuffman()` of the class `Int32Compression`. The first part of the algorithm is dedicated to the construction of the Huffman tree. This is accomplished by the methods `HuffTreeNode.buildHuffmanTree()` and `HuffTreeNode.assignCodeToTree()`. The algorithm is similar to the code given in the Appendix C of the specification. The idea is to create an ordered list of nodes, a node being a symbol to decode and the order is given by the frequency associated to the symbol. Then the algorithm creates recursively a tree, it creates a subtree with a new node as a root and the two most frequent nodes of the list being the leaves. These two nodes are then removed from the list while the new root node is added (its frequency is the sum of the two leaves' frequency)

There's an ambiguity in the implementation which occurs when two symbols have the same frequency, depending on the comparison that we use between the nodes, the order may be different and lead to an inversion of some branches of the tree. For the moment we didn't find a working comparison algorithm which always produces a correct tree.

Once the tree is built, it's straightforward to assign a code to each leaf of the tree. Each leaf is linked to a symbol and the associated code is given by the path from the root to the leaf. A left branch add a 1 to the code while a right branch adds a 0.

Our decoding implementation is simple, we start with a pointer on the root of the tree and read a bit from the stream. If it's a 1 we move the pointer to the left child, if it's a 0 we move it to the right child. If the pointer is on a leaf we output the associated symbol and position the pointer at the root again and we continuer to read bits.
