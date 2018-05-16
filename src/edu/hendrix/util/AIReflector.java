package edu.hendrix.util;


import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@SuppressWarnings("rawtypes")
public class AIReflector<T> {
	private final static String suffix = ".class";
	
	private Map<String,Class> name2type;
	
	private FilenameFilter filter = (dir, name) -> name.endsWith(suffix);
		
	private void addFrom(Class superType, String packageName) throws UnsupportedEncodingException {
		String targetDirName = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String packageDir = targetDirName + packageName.replace('.', File.separatorChar);
		packageDir = URLDecoder.decode(packageDir, "UTF-8");
		File targetDir = new File(packageDir);
		if (!targetDir.isDirectory()) {throw new IllegalArgumentException(targetDir + " is not a directory");}
		for (File f: targetDir.listFiles(filter)) {
			String name = f.getName();
			name = name.substring(0, name.length() - suffix.length());
			try {
				Class type = Class.forName(packageName + "." + name);
				Object obj = type.newInstance();
				if (superType.isInstance(obj)) {
					name2type.put(name, type);
				}
				// If an exception is thrown, we omit the type.
				// Hence, ignore all three exceptions.
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}		
	}
		
	public AIReflector(Class superType, String... packageNames) {
		this.name2type = new TreeMap<String,Class>();
		for (String packageName: packageNames) {
			try {
				addFrom(superType, packageName);
			} catch (UnsupportedEncodingException e) {
				// Intentionally left blank
			}
		}
	}
	
	public ArrayList<String> getTypeNames() {
		return new ArrayList<String>(name2type.keySet());
	}
	
	public String toString() {
		String result = "Available:";
		for (String s: name2type.keySet()) {
			result += " " + s;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public T newInstanceOf(String typeName) throws InstantiationException, IllegalAccessException {
		return (T)name2type.get(typeName).newInstance();
	}
}
