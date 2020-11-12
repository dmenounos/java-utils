package com.example.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtils {

	public static final String UTF_8 = "UTF-8";
	public static final int BUFFER_SIZE = 4096;

	public static boolean isEmpty(byte[] bytes) {
		return bytes == null || bytes.length == 0;
	}

	public static String fromInStreamToString(InputStream input, String encoding) throws IOException {
		return new String(fromInStreamToBytes(input), encoding);
	}

	public static byte[] fromInStreamToBytes(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copyBytes(input, output);
		return output.toByteArray();
	}

	public static void fromBytesToOutStream(byte[] bytes, OutputStream output) throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		copyBytes(input, output);
	}

	/**
	 * Copies all the bytes from the {@code source} to the {@code output}.
	 */
	public static long copyBytes(InputStream input, OutputStream output) throws IOException {
		return copyBytes(input, (buffer, offset, length, totalLength) -> {
			output.write(buffer, offset, length);
		});
	}

	/**
	 * Copies all the bytes from the {@code source} to the {@code target}.
	 */
	public static long copyBytes(InputStream source, BytesTarget target) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int readBytes = 0;
		long sumBytes = 0;

		while ((readBytes = source.read(buffer)) != -1) {
			target.write(buffer, 0, readBytes, sumBytes += readBytes);
		}

		return sumBytes;
	}

	@FunctionalInterface
	public interface BytesTarget {

		void write(byte[] buffer, int offset, int length, long totalLength) throws IOException;
	}

	/**
	 * Copies all the chars from the {@code source} to the {@code output}.
	 */
	public static long copyChars(Reader input, Writer output) throws IOException {
		return copyChars(input, (buffer, offset, length, totalLength) -> {
			output.write(buffer, offset, length);
		});
	}

	/**
	 * Copies all the chars from the {@code source} to the {@code target}.
	 */
	public static long copyChars(Reader input, CharsTarget target) throws IOException {
		char[] buffer = new char[BUFFER_SIZE];
		int readChars = 0;
		long sumChars = 0;

		while ((readChars = input.read(buffer)) != -1) {
			target.write(buffer, 0, readChars, sumChars += readChars);
		}

		return sumChars;
	}

	@FunctionalInterface
	public interface CharsTarget {

		void write(char[] buffer, int offset, int length, long totalLength) throws IOException;
	}

	/**
	 * Copies all the lines from the {@code source} to the {@code output}.
	 */
	public static void copyLines(Reader input, Writer output) throws IOException {
		BufferedReader bin = new BufferedReader(input);
		String str;

		while ((str = bin.readLine()) != null) {
			output.write(str);
			output.write("\n");
		}
	}

	public static void close(AutoCloseable closable) {
		try {
			if (closable != null) {
				closable.close();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
