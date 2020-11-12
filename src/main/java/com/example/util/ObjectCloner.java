package com.example.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectCloner {

	@SuppressWarnings("unchecked")
	public static <T> T copy(T obj) {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();

			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);
			return (T) ois.readObject();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.close(oos);
			IOUtils.close(ois);
		}
	}
}
