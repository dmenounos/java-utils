package com.example.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IOZipUtils {

	public static byte[] extractFile(byte[] bytes, String fileName) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ZipInputStream zis = new ZipInputStream(bais);
		ZipEntry ze = null;

		try {
			while ((ze = zis.getNextEntry()) != null) {
				if (fileName.equals(ze.getName())) {
					byte[] fileData = IOUtils.fromInStreamToBytes(zis);
					return fileData;
				}
			}
		}
		finally {
			zis.close();
		}

		return null;
	}

	public static Map<String, byte[]> extractFiles(byte[] bytes, List<String> fileNames) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ZipInputStream zis = new ZipInputStream(bais);
		ZipEntry ze = null;

		Map<String, byte[]> fileMap = new HashMap<String, byte[]>();

		try {
			while ((ze = zis.getNextEntry()) != null) {
				for (Iterator<String> it = fileNames.iterator(); it.hasNext();) {
					String fileName = it.next();

					if (fileName.equals(ze.getName())) {
						byte[] fileData = IOUtils.fromInStreamToBytes(zis);
						fileMap.put(fileName, fileData);
						it.remove();
					}
				}
			}
		}
		finally {
			zis.close();
		}

		return fileMap;
	}
}
