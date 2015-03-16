# BitLength Algorithm #

The BitLength algorithm runs an adaptive-width bit field encoding for each value. For each input value the number of bits needed to represent it is calculated and compared to the
current "field width". The current field width is then adjusted upwards or downwards, this
increment or decrement of the current field width is indicated for each encoded value by a prefix code stored with each value.

The implementation is quite straightforward. The loop is divided in two parts, the test `if (encodedBits.readAsInt(1) == 0)` is used to determine if the field width to use is the current one or if we need to adapt it, 0 means that we use the same. If the bit read is 1, we read adjustment bits until we read the termination bit (0 for an increment and 1 for a decrement) and modify the field width accordingly. Then we read "width" bits to obtain the decoded value.


