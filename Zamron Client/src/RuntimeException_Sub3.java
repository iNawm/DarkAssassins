import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* RuntimeException_Sub3 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class RuntimeException_Sub3 extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8188886043974819736L;
	static CacheIndex aClass243_6310;
	static Class479[] aClass479Array6311;

	RuntimeException_Sub3() {
		/* empty */
	}

	static void method50123(int x) {
		try {
			Class<ClassLoader> var_class = java.lang.ClassLoader.class;
			Field field = var_class.getDeclaredField("nativeLibraries");
			Class<AccessibleObject> var_class_124_ = java.lang.reflect.AccessibleObject.class;
			Method method = var_class_124_.getDeclaredMethod("setAccessible", (new Class[] { Boolean.TYPE }));
			method.invoke(field, new Object[] { Boolean.TRUE });
		} catch (Throwable throwable) {
			/* empty */
		}
	}
}
