package org.openntf.domino.xsp.script;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.domino.DateTime;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.jscript.InterpretException;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.JavaScriptException;
import com.ibm.jscript.types.FBSDefaultObject;
import com.ibm.jscript.types.FBSObject;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.jscript.types.FBSValueVector;
import com.ibm.jscript.types.GeneratedWrapperObject;
import com.ibm.jscript.types.GeneratedWrapperObject.MethodMap;
import com.ibm.jscript.types.IWrapperFactory;
import com.ibm.jscript.types.Registry;

public class WrapperOpenDomino {

	/*
	 * First block extends IBM's baseline GeneratedWrapper classes. Why? Because as you can see with the OpenFunction.call() method, we want
	 * to establish better error handling in the case of implementation problems. This is also very handy for adding trace code by
	 * overriding methods and putting logging around the super. calls.
	 */

	public static class OpenFunction extends GeneratedWrapperObject.Function {
		private final Class<?> clazz_;
		// private final Method crystal_;
		private Map<String, Method> crystals_;
		private Method defCrystal_;

		private static String getSignature(final Method crystal) {
			StringBuilder sb = new StringBuilder();
			Class<?>[] params = crystal.getParameterTypes();
			if (params.length > 0) {
				sb.append('(');
				for (Class<?> clazz : params) {
					if (clazz.isArray()) {
						Class<?> cClazz = clazz.getComponentType();
						if (cClazz.equals(void.class)) {
							sb.append("voids:[V");
						} else if (cClazz.equals(String.class)) {
							sb.append("strings:[T");
						} else if (cClazz.equals(Character.class) || cClazz.equals(Character.TYPE)) {
							sb.append("chars:[C");
						} else if (cClazz.equals(Integer.class) || cClazz.equals(Integer.TYPE)) {
							sb.append("ints:[I");
						} else if (cClazz.equals(Double.class) || cClazz.equals(Double.TYPE)) {
							sb.append("doubles:[D");
						} else if (cClazz.equals(Short.class) || cClazz.equals(Short.TYPE)) {
							sb.append("shorts:[S");
						} else if (cClazz.equals(Float.class) || cClazz.equals(Float.TYPE)) {
							sb.append("floats:[F");
						} else if (cClazz.equals(Byte.class) || cClazz.equals(Byte.TYPE)) {
							sb.append("bytes:[B");
						} else if (cClazz.equals(Long.class) || cClazz.equals(Long.TYPE)) {
							sb.append("longs:[J");
						} else if (cClazz.equals(DateTime.class)) {
							sb.append("DateTimes:[Y");
						} else if (cClazz.equals(Object.class)) {
							sb.append("Objects:[W");
						} else if (cClazz.equals(Boolean.class) || cClazz.equals(Boolean.TYPE)) {
							sb.append("booleans:[Z");
						} else {
							sb.append(cClazz.getSimpleName() + "s:[L" + cClazz.getCanonicalName());
						}
					} else if (clazz.equals(void.class)) {
						sb.append("void:V");
					} else if (clazz.equals(String.class)) {
						sb.append("str:T");
					} else if (clazz.equals(Character.class) || clazz.equals(Character.TYPE)) {
						sb.append("char:C");
					} else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
						sb.append("int:I");
					} else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
						sb.append("double:D");
					} else if (clazz.equals(Short.class) || clazz.equals(Short.TYPE)) {
						sb.append("short:S");
					} else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
						sb.append("float:F");
					} else if (clazz.equals(Byte.class) || clazz.equals(Byte.TYPE)) {
						sb.append("byte:B");
					} else if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
						sb.append("long:J");
					} else if (clazz.equals(DateTime.class)) {
						sb.append("DateTime:Y");
					} else if (clazz.equals(Object.class)) {
						sb.append("Object:W");
					} else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
						sb.append("boolean:Z");
					} else {
						sb.append(clazz.getSimpleName() + ":L" + clazz.getCanonicalName());
					}
					sb.append(';');
				}
				if (crystal.isVarArgs()) {
					sb.append("args:N;");
				}
				sb.append(')');
			} else {
				sb.append("()");
			}
			Class<?> ret = crystal.getReturnType();
			if (ret.isArray()) {
				Class<?> cClazz = ret.getComponentType();
				if (cClazz.equals(void.class)) {
					sb.append(":[V");
				} else if (cClazz.equals(String.class)) {
					sb.append(":[T");
				} else if (cClazz.equals(Character.class) || cClazz.equals(Character.TYPE)) {
					sb.append(":[C");
				} else if (cClazz.equals(Integer.class) || cClazz.equals(Integer.TYPE)) {
					sb.append(":[I");
				} else if (cClazz.equals(Double.class) || cClazz.equals(Double.TYPE)) {
					sb.append(":[D");
				} else if (cClazz.equals(Short.class) || cClazz.equals(Short.TYPE)) {
					sb.append(":[S");
				} else if (cClazz.equals(Float.class) || cClazz.equals(Float.TYPE)) {
					sb.append(":[F");
				} else if (cClazz.equals(Byte.class) || cClazz.equals(Byte.TYPE)) {
					sb.append(":[B");
				} else if (cClazz.equals(Long.class) || cClazz.equals(Long.TYPE)) {
					sb.append(":[J");
				} else if (cClazz.equals(DateTime.class)) {
					sb.append(":[Y");
				} else if (cClazz.equals(Object.class)) {
					sb.append(":[W");
				} else if (cClazz.equals(Boolean.class) || cClazz.equals(Boolean.TYPE)) {
					sb.append(":[Z");
				} else {
					sb.append(":[L" + ret.getCanonicalName());
				}
			} else if (ret.equals(void.class)) {
				sb.append(":V");
			} else if (ret.equals(String.class)) {
				sb.append(":T");
			} else if (ret.equals(Character.class) || ret.equals(Character.TYPE)) {
				sb.append(":C");
			} else if (ret.equals(Integer.class) || ret.equals(Integer.TYPE)) {
				sb.append(":I");
			} else if (ret.equals(Double.class) || ret.equals(Double.TYPE)) {
				sb.append(":D");
			} else if (ret.equals(Short.class) || ret.equals(Short.TYPE)) {
				sb.append(":S");
			} else if (ret.equals(Float.class) || ret.equals(Float.TYPE)) {
				sb.append(":F");
			} else if (ret.equals(Byte.class) || ret.equals(Byte.TYPE)) {
				sb.append(":B");
			} else if (ret.equals(Long.class) || ret.equals(Long.TYPE)) {
				sb.append(":J");
			} else if (ret.equals(DateTime.class)) {
				sb.append(":Y");
			} else if (ret.equals(Object.class)) {
				sb.append(":W");
			} else if (ret.equals(Boolean.class) || ret.equals(Boolean.TYPE)) {
				sb.append(":Z");
			} else {
				sb.append(":L" + ret.getCanonicalName());
			}

			return sb.toString();
		}

		private Method getMethodFromVector(final FBSValueVector vec) throws JavaScriptException {
			if (vec == null || vec.size() == 0)
				return defCrystal_;
			Method result = null;
			if (crystals_ != null) {
				int size = vec.size();
				for (Method crystal : crystals_.values()) {
					Class<?>[] params = crystal.getParameterTypes();
					if (params.length == vec.size()) {
						switch (size) {
						case 1:
							if (canCall(vec, params[0])) {
								result = crystal;
							}
							break;
						case 2:
							if (canCall(vec, params[0], params[1])) {
								result = crystal;
							}
							break;
						case 3:
							if (canCall(vec, params[0], params[1], params[2])) {
								result = crystal;
							}
							break;
						case 4:
							if (canCall(vec, params[0], params[1], params[2], params[3])) {
								result = crystal;
							}
							break;
						case 5:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4])) {
								result = crystal;
							}
							break;
						case 6:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5])) {
								result = crystal;
							}
							break;
						case 7:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6])) {
								result = crystal;
							}
							break;
						case 8:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7])) {
								result = crystal;
							}
							break;
						case 9:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7],
									params[8])) {
								result = crystal;
							}
							break;
						case 10:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7],
									params[8], params[9])) {
								result = crystal;
							}
							break;
						case 11:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7],
									params[8], params[9], params[10])) {
								result = crystal;
							}
							break;
						case 12:
							if (canCall(vec, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7],
									params[8], params[9], params[10], params[11])) {
								result = crystal;
							}
							break;
						}
					}
					if (result != null)
						break;
				}

			} else {
				// TODO NTF - deal with some giant exception 'cause this is all messed up
				String type = this.getTypeAsString();
				String message = "Function " + getClass().getName() + " which wraps methods for the class " + getWrappedClass().getName()
						+ " as is type " + type + " does not have a defined call() method for its implementation.";
				throw new InterpretException(null, message, new Object[0]);
			}
			return result;
		}

		private Object[] toJavaArguments(final Method crystal, final FBSValueVector vec) throws InterpretException {
			Object[] result = new Object[vec.size()];
			Class<?>[] params = crystal.getParameterTypes();
			for (int i = 0; i < vec.size(); i++) {
				FBSValue val = vec.get(i);
				result[i] = val.toJavaObject(params[i]);
			}
			return result;
		}

		/*
		 * The OpenFunction object extends the SSJS function object. It is a single class that encapsulates 1 to n methods on a Java class.
		 * For each method in your Java class, you'll need to register a function object with a known signature and give it a corresponding
		 * index. This index on the individual function object is then used as the switch in both the call() and getCallParameters()
		 * methods.
		 * 
		 * It implements 4 critical methods: 1) getCallParameters() which defines the expected parameters for the method 2) getMethodMap()
		 * which returns the method map that is supposed to provide this method 3) getWrappedClass() which identifies the Java interface
		 * that will be used in the call() method 4) call(FBSValueVector, FBSObject) which actually unwraps the SSJS objects and executes
		 * the explicit Java methods defined
		 */
		protected OpenFunction(final JSContext context, final Method crystal) {
			super(context, crystal.getName(), 1);
			addMethod(crystal);
			clazz_ = crystal.getDeclaringClass();
		}

		protected void addMethod(final Method crystal) {
			if (crystals_ == null) {
				crystals_ = new HashMap<String, Method>();
			}
			crystals_.put(getSignature(crystal), crystal);
			if (crystal.getParameterTypes().length == 0) {
				defCrystal_ = crystal;
			}
		}

		// @Override
		// public FBSValue call(FBSValueVector valueVector, FBSObject object) throws JavaScriptException {
		// String type = this.getTypeAsString();
		// String message = "Function " + getClass().getName() + " which wraps methods for the class " + getWrappedClass().getName()
		// + " as is type " + type
		// + " does not have a defined call() method for its implementation. Therefore it cannot execute against the object "
		// + object.getTypeAsString();
		// throw new InterpretException(null, message, new Object[0]);
		// }

		@Override
		protected MethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(null, getWrappedClass());
		}

		@Override
		protected Class<?> getWrappedClass() {
			return clazz_;
		}

		@Override
		protected String[] getCallParameters() {
			if (crystals_ == null || crystals_.isEmpty()) {
				return new String[] { "" };
			}
			return crystals_.keySet().toArray(new String[0]);
		}

		@Override
		public FBSValue call(final FBSValueVector valueVector, final FBSObject object) throws JavaScriptException {
			Object base = clazz_.cast(object.toJavaObject(clazz_));
			try {
				if (base != null) {
					Method crystal = getMethodFromVector(valueVector);
					if (crystal.getParameterTypes().length == 0) {
						Object jResult = crystal.invoke(base, (Object[]) null);
						FBSValue result = FBSUtility.wrap(getJSContext(), jResult);
						return result;
					} else {
						Object[] args = toJavaArguments(crystal, valueVector);
						Object jResult = crystal.invoke(base, args);
						FBSValue result = FBSUtility.wrap(getJSContext(), jResult);
						return result;
					}
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
			return object;
		}

	}

	public static class OpenObject extends GeneratedWrapperObject {
		private final String uiName_;
		private final Class<?> clazz_;

		/*
		 * The OpenObject is base wrapper for the Java classes that defines their names, method maps and unlying Java classes. These objects
		 * are registered with the WrapperFactory with integer indexes so that the SSJS interpretter can find the appropriate wrapper for
		 * Java objects at runtime.
		 */

		protected OpenObject(final JSContext paramJSContext, final Object paramObject, final Class<?> clazz) {
			super(paramJSContext, paramObject, "Open" + clazz.getSimpleName());
			uiName_ = "Open" + clazz.getSimpleName();
			clazz_ = clazz;
		}

		// protected OpenObject(JSContext paramJSContext, Object paramObject, String paramString) {
		// super(paramJSContext, paramObject, paramString);
		// uiName_ = paramString;
		// clazz_ = null;
		// }

		protected OpenObject(final JSContext paramJSContext, final Object paramObject, final String paramString, final Class<?> clazz) {
			super(paramJSContext, paramObject, paramString);
			uiName_ = paramString;
			clazz_ = clazz;
		}

		@Override
		public String getTypeAsString() {
			return uiName_;
		}

		@Override
		public Class<?> getWrappedClass() {
			return clazz_;
		}

		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(getJSContext(), getWrappedClass());
		}
	}

	public static class OpenMethodMap extends GeneratedWrapperObject.MethodMap {
		/*
		 * The MethodMap is a simple static HashMap that binds a String, like "getItemValue" or "isBefore" to an OpenFunction object
		 * 
		 * This way when SSJS encounters an org.openntf.domino.DateTime, and it sees .isBefore(DateTime) called on it, it can resolve the
		 * string to a particular fct_OpenDateTime object, with a specific index value. It then invokes the call() method on that particular
		 * function object, passing in the DateTime argument from SSJS, as well as the DateTime object on which the .isBefore was invoked.
		 */
		public OpenMethodMap(final JSContext arg0, final Class<?> arg1, final Class<?>[] arg2) {
			super(arg0, arg1, arg2);
		}

	}

	private final static Map<Class<?>, OpenMethodMap> classMap_ = new HashMap<Class<?>, OpenMethodMap>();

	private static Map<Class<?>, OpenMethodMap> getClassMap() {
		return Collections.unmodifiableMap(classMap_);
	}

	private static OpenMethodMap generateMethodMap(final JSContext context, final Class<?> clazz) {
		OpenMethodMap methodMap = new OpenMethodMap(context, clazz, null);
		for (Method crystal : clazz.getMethods()) {
			if (Modifier.isPublic(crystal.getModifiers()) && !Modifier.isStatic(crystal.getModifiers())) {
				String name = crystal.getName();
				Object cur = methodMap.get(name);
				if (cur == null) {
					methodMap.put(crystal.getName(), new OpenFunction(context, crystal));
				}
				if (cur instanceof OpenFunction) {
					((OpenFunction) cur).addMethod(crystal);
				} else {
					// TODO NTF - Huh? How'd that happen?
				}
			}
		}
		synchronized (WrapperOpenDomino.class) {
			classMap_.put(clazz, methodMap);
		}
		return methodMap;
	}

	private static OpenMethodMap getMethodMap(final JSContext context, final Class<?> clazz) {
		OpenMethodMap result = getClassMap().get(clazz);
		if (result == null) {
			result = generateMethodMap(context, clazz);
			// result = getClassMap().get(clazz);
		}
		return result;
	}

	// private static OpenMethodMap methodMap_OpenDocument; // 0
	//
	// private static OpenMethodMap getMap_OpenDocument(JSContext context) {
	// if (methodMap_OpenDocument == null) {
	// synchronized (WrapperOpenDomino.class) {
	// if (methodMap_OpenDocument == null) {
	// OpenMethodMap methodMap = new OpenMethodMap(context, Document.class, null);
	// // methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
	// // methodMap.put("getLastModifiedDate", new fct_OpenDocument(context, "getLastModifiedDate", 1));
	// // methodMap.put("getInitiallyModifiedDate", new fct_OpenDocument(context, "getInitiallyModifiedDate", 2));
	// // methodMap.put("getLastAccessedDate", new fct_OpenDocument(context, "getLastAccessedDate", 3));
	// methodMap_OpenDocument = methodMap;
	// }
	// }
	// }
	// return methodMap_OpenDocument;
	// }
	//
	// public static final class _OpenDocument extends OpenObject {
	// public _OpenDocument(JSContext context, Object object) {
	// super(context, object, "OpenDocument");
	// }
	//
	// @Override
	// public OpenMethodMap getMethodMap() {
	// return WrapperOpenDomino.getMap_OpenDocument(getJSContext());
	// }
	//
	// @Override
	// public Class<?> getWrappedClass() {
	// if (super.getWrappedClass() == null) {
	// return Document.class;
	// } else {
	// return super.getWrappedClass();
	// }
	// }
	//
	// }

	private static final class OpenConstructor extends GeneratedWrapperObject.EmptyConstructor {
		@SuppressWarnings("unused")
		private final String uiName_;
		private final Class<?> clazz_;

		private OpenConstructor(final JSContext context, final Class<?> clazz) {
			super(context, "Open" + clazz.getSimpleName());
			uiName_ = "Open" + clazz.getSimpleName();
			clazz_ = clazz;
		}

		@Override
		public Class<?> getWrappedClass() {
			return clazz_;
		}

		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(getJSContext(), getWrappedClass());
		}
	}

	// private static final class ctor_OpenDocument extends GeneratedWrapperObject.EmptyConstructor {
	// private ctor_OpenDocument(JSContext context) {
	// super(context, "OpenDocument");
	// }
	//
	// @Override
	// protected OpenMethodMap getMethodMap() {
	// return WrapperOpenDomino.getMap_OpenDocument(getJSContext());
	// }
	//
	// @Override
	// protected Class<?> getWrappedClass() {
	// return Document.class;
	// }
	//
	// }

	// private static OpenMethodMap getMap_OpenDateTime(JSContext context) {
	// if (methodMap_OpenDateTime == null) {
	// synchronized (WrapperOpenDomino.class) {
	// if (methodMap_OpenDateTime == null) {
	// OpenMethodMap methodMap = new OpenMethodMap(context, DateTime.class, null);
	// methodMap.put("isBefore", new fct_OpenDateTime(context, "isBefore", 0));
	// methodMap.put("isAfter", new fct_OpenDateTime(context, "isAfter", 1));
	// methodMap.put("toJavaCal", new fct_OpenDateTime(context, "toJavaCal", 2));
	// methodMap.put("equals", new fct_OpenDateTime(context, "equals", 3));
	// methodMap.put("equalsIgnoreDate", new fct_OpenDateTime(context, "equalsIgnoreDate", 4));
	// methodMap.put("equalsIgnoreTime", new fct_OpenDateTime(context, "equalsIgnoreTime", 5));
	// methodMap.put("isAnyDate", new fct_OpenDateTime(context, "isAnyDate", 6));
	// methodMap.put("isAnyTime", new fct_OpenDateTime(context, "isAnyTime", 7));
	// methodMap_OpenDateTime = methodMap;
	// }
	// }
	// }
	// return methodMap_OpenDateTime;
	// }

	// public static final class _OpenDateTime extends OpenObject {
	// public _OpenDateTime(JSContext context, Object object) {
	// super(context, object, "OpenDateTime");
	// }
	//
	// @Override
	// public OpenMethodMap getMethodMap() {
	// return WrapperOpenDomino.getMap_OpenDateTime(getJSContext());
	// }
	//
	// @Override
	// public Class<?> getWrappedClass() {
	// if (super.getWrappedClass() == null) {
	// return DateTime.class;
	// } else {
	// return super.getWrappedClass();
	// }
	// }
	//
	// }

	// private static final class ctor_OpenDateTime extends GeneratedWrapperObject.EmptyConstructor {
	// private ctor_OpenDateTime(JSContext context) {
	// super(context, "OpenDateTime");
	// }
	//
	// @Override
	// protected OpenMethodMap getMethodMap() {
	// return WrapperOpenDomino.getMap_OpenDateTime(getJSContext());
	// }
	//
	// @Override
	// protected Class<?> getWrappedClass() {
	// return DateTime.class;
	// }
	// }

	private static class OpenWrapperFactory implements IWrapperFactory {
		private final Class<?> clazz_;

		private OpenWrapperFactory(final Class<?> clazz) {
			clazz_ = clazz;
			// System.out.println("Registering OpenNTF SSJS object " + clazz.getName());
		}

		@Override
		public OpenMethodMap getMethodMap(final JSContext context) {
			return WrapperOpenDomino.getMethodMap(context, clazz_);
		}

		@Override
		public OpenObject wrap(final JSContext context, final Object object) {
			return new OpenObject(context, object, clazz_);
		}

	}

	private static boolean registered = false;

	public static void register(final JSContext context) {
		if (registered) {
			return;
		}

		try {
			Registry registry = context.getRegistry();
			registry.registerPackage("OpenNTFDomino", 1337);
			FBSDefaultObject defaultObject = registry.getRegistryObject();

			for (Class<?> clazz : WRAPPED_CLASSES) {
				registry.registerWrapper(clazz, new OpenWrapperFactory(clazz));
				defaultObject.createProperty("Open" + clazz.getSimpleName(), 1338, new OpenConstructor(context, clazz));
			}

		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		registered = true;
	}

	public static final List<Class<?>> WRAPPED_CLASSES = new ArrayList<Class<?>>();

	static {
		WRAPPED_CLASSES.add(org.openntf.domino.Document.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Database.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Session.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DateTime.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Item.class);
		WRAPPED_CLASSES.add(org.openntf.domino.View.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DocumentCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewEntryCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewColumn.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextItem.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Name.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DateRange.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DbDirectory.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Form.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Outline.class);
		WRAPPED_CLASSES.add(org.openntf.domino.OutlineEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Agent.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ACL.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ACLEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.EmbeddedObject.class);
		// WRAPPED_CLASSES.add(org.openntf.domino.Formula.class);
		WRAPPED_CLASSES.add(org.openntf.domino.International.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Log.class);
		WRAPPED_CLASSES.add(org.openntf.domino.MIMEEntity.class);
		WRAPPED_CLASSES.add(org.openntf.domino.MIMEHeader.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Newsletter.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NoteCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewNavigator.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Stream.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextTab.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextTable.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextStyle.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextSection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextRange.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextNavigator.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextParagraphStyle.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextDoclink.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Replication.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ReplicationEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Registration.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DxlExporter.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DxlImporter.class);
	}

}
