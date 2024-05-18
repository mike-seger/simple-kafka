package com.net128.tool.generic.avro.client;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HexDumpUtil {

    public static String toHexDump(byte[] data) {
        final int lineLength = 16;
        return IntStream.range(0, data.length)
            .filter(i -> i % lineLength == 0)
            .mapToObj(i -> formatLine(data, i, lineLength))
            .collect(Collectors.joining());
    }

    @SuppressWarnings("SameParameterValue")
    private static String formatLine(byte[] data, int start, int lineLength) {
        var end = Math.min(start + lineLength, data.length);
        var hexString = IntStream.range(start, end)
            .mapToObj(i -> String.format("%02X ", data[i]))
            .collect(Collectors.joining());

        var padding = IntStream.range(hexString.length() / 3, lineLength)
            .mapToObj(i -> "   ")
            .collect(Collectors.joining());

        var asciiString = IntStream.range(start, end)
            .mapToObj(i -> formatAscii(data[i]))
            .collect(Collectors.joining());

        return String.format("%04X  %s%s |%s|\n", start, hexString, padding, asciiString);
    }

    private static String formatAscii(byte value) {
        var val = value & 0xFF;
        return (val >= 32 && val <= 126) ? String.valueOf((char) val) : ".";
    }
}
