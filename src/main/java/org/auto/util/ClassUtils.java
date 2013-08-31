package org.auto.util;

//import java.io.IOException;
//import java.io.InputStream;

//import org.objectweb.asm.Attribute;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.CodeVisitor;

/**
 * Class工具类
 * 
 * @author XiaohangHu
 * */
public class ClassUtils {

	private ClassUtils() {
		throw new AssertionError();
	}

	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
	 * class will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a
	 * scenario where you absolutely need a non-null ClassLoader reference: for
	 * example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
	 * reference as well).
	 * 
	 * @return the default ClassLoader (never <code>null</code>)
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtils.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * 从流中读取类
	 public static Class<?> getClassFromInputStream(InputStream inputStream)
			throws IOException {
		ClassReader classReader = new ClassReader(inputStream);
		SampleClassVisitor visitor = new SampleClassVisitor();
		classReader.accept(visitor, true);
		String className = visitor.getClassName();
		try {
			try {
				return getDefaultClassLoader().loadClass(className);
			} catch (Throwable e) {
				return Class.forName(className);
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	 * */
	

}

/***
class SampleClassVisitor implements ClassVisitor {

	private String className;

	public String getClassName() {
		return this.className;
	}

	private String convertResourcePathToClassName(String resourcePath) {
		return resourcePath.replace('/', '.');
	}

	public void visit(int version, int access, String name, String signature,
			String[] interfaces, String supername) {
		this.className = convertResourcePathToClassName(name);
	}

	public void visitAttribute(Attribute arg0) {
		// TODO Auto-generated method stub

	}

	public void visitField(int arg0, String arg1, String arg2, Object arg3,
			Attribute arg4) {
		// TODO Auto-generated method stub

	}

	public CodeVisitor visitMethod(int arg0, String arg1, String arg2,
			String[] arg3, Attribute arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	public void visitEnd() {
		// TODO Auto-generated method stub

	}

	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
*/