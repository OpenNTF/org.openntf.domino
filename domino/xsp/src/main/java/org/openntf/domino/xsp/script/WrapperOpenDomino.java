/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp.script;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lotus.domino.DateTime;

import com.ibm.commons.extension.ExtensionManager;
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

@SuppressWarnings("nls")
public class WrapperOpenDomino implements WrapperRegistry {

	/*
	 * First block extends IBM's baseline GeneratedWrapper classes. Why? Because as you can see with the OpenFunction.call() method, we want
	 * to establish better error handling in the case of implementation problems. This is also very handy for adding trace code by
	 * overriding methods and putting logging around the super. calls.
	 */

	/**
	 * The OpenFunction object extends the SSJS function object. It is a single class that encapsulates 1 to n methods on a Java class. For
	 * each method in your Java class, you'll need to register a function object with a known signature and give it a corresponding index.
	 * This index on the individual function object is then used as the switch in both the call() and getCallParameters() methods.
	 * 
	 * It implements 4 critical methods:
	 * <ol>
	 * <li>getCallParameters() which defines the expected parameters for the method</li>
	 * <li>getMethodMap() which returns the method map that is supposed to provide this method</li>
	 * <li>getWrappedClass() which identifies the Java interface that will be used in the call() method</li>
	 * <li>call(FBSValueVector, FBSObject) which actually unwraps the SSJS objects and executes the explicit Java methods defined</li>
	 * </ol>
	 */
	public static class OpenFunction extends GeneratedWrapperObject.Function {
		private final Class<?> clazz_;
		// private final Method crystal_;
		private Map<String, Method> crystals_;
		private Method defCrystal_;

		/**
		 * Gets the signature, the parameter types converted to SSJS)
		 * 
		 * @param crystal
		 *            Meth(od)
		 * @return String of parameter types, e.g. "(void:V);"
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		private static String getSignature(final Method crystal) {
			StringBuilder sb = new StringBuilder();
			Class<?>[] params = crystal.getParameterTypes();
			if (params.length > 0) {
				sb.append('(');
				for (Class<?> clazz : params) {
					if (clazz.isArray()) {
						Class<?> cClazz = clazz.getComponentType();
						if (cClazz.equals(void.class)) {
							sb.append("voids:[V"); //$NON-NLS-1$
						} else if (cClazz.equals(String.class)) {
							sb.append("strings:[T"); //$NON-NLS-1$
						} else if (cClazz.equals(Character.class) || cClazz.equals(Character.TYPE)) {
							sb.append("chars:[C"); //$NON-NLS-1$
						} else if (cClazz.equals(Integer.class) || cClazz.equals(Integer.TYPE)) {
							sb.append("ints:[I"); //$NON-NLS-1$
						} else if (cClazz.equals(Double.class) || cClazz.equals(Double.TYPE)) {
							sb.append("doubles:[D"); //$NON-NLS-1$
						} else if (cClazz.equals(Short.class) || cClazz.equals(Short.TYPE)) {
							sb.append("shorts:[S"); //$NON-NLS-1$
						} else if (cClazz.equals(Float.class) || cClazz.equals(Float.TYPE)) {
							sb.append("floats:[F"); //$NON-NLS-1$
						} else if (cClazz.equals(Byte.class) || cClazz.equals(Byte.TYPE)) {
							sb.append("bytes:[B"); //$NON-NLS-1$
						} else if (cClazz.equals(Long.class) || cClazz.equals(Long.TYPE)) {
							sb.append("longs:[J"); //$NON-NLS-1$
						} else if (cClazz.equals(DateTime.class)) {
							sb.append("DateTimes:[Y"); //$NON-NLS-1$
						} else if (cClazz.equals(Object.class)) {
							sb.append("Objects:[W"); //$NON-NLS-1$
						} else if (cClazz.equals(Boolean.class) || cClazz.equals(Boolean.TYPE)) {
							sb.append("booleans:[Z"); //$NON-NLS-1$
						} else {
							sb.append(cClazz.getSimpleName() + "s:[L" + cClazz.getCanonicalName()); //$NON-NLS-1$
						}
					} else if (clazz.equals(void.class)) {
						sb.append("void:V"); //$NON-NLS-1$
					} else if (clazz.equals(String.class)) {
						sb.append("str:T"); //$NON-NLS-1$
					} else if (clazz.equals(Character.class) || clazz.equals(Character.TYPE)) {
						sb.append("char:C"); //$NON-NLS-1$
					} else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
						sb.append("int:I"); //$NON-NLS-1$
					} else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
						sb.append("double:D"); //$NON-NLS-1$
					} else if (clazz.equals(Short.class) || clazz.equals(Short.TYPE)) {
						sb.append("short:S"); //$NON-NLS-1$
					} else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
						sb.append("float:F"); //$NON-NLS-1$
					} else if (clazz.equals(Byte.class) || clazz.equals(Byte.TYPE)) {
						sb.append("byte:B"); //$NON-NLS-1$
					} else if (clazz.equals(Long.class) || clazz.equals(Long.TYPE)) {
						sb.append("long:J"); //$NON-NLS-1$
					} else if (clazz.equals(DateTime.class)) {
						sb.append("DateTime:Y"); //$NON-NLS-1$
					} else if (clazz.equals(Object.class)) {
						sb.append("Object:W"); //$NON-NLS-1$
					} else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
						sb.append("boolean:Z"); //$NON-NLS-1$
					} else {
						sb.append(clazz.getSimpleName() + ":L" + clazz.getCanonicalName()); //$NON-NLS-1$
					}
					sb.append(';');
				}
				if (crystal.isVarArgs()) {
					sb.append("args:N;"); //$NON-NLS-1$
				}
				sb.append(')');
			} else {
				sb.append("()"); //$NON-NLS-1$
			}
			Class<?> ret = crystal.getReturnType();
			if (ret.isArray()) {
				Class<?> cClazz = ret.getComponentType();
				if (cClazz.equals(void.class)) {
					sb.append(":[V"); //$NON-NLS-1$
				} else if (cClazz.equals(String.class)) {
					sb.append(":[T"); //$NON-NLS-1$
				} else if (cClazz.equals(Character.class) || cClazz.equals(Character.TYPE)) {
					sb.append(":[C"); //$NON-NLS-1$
				} else if (cClazz.equals(Integer.class) || cClazz.equals(Integer.TYPE)) {
					sb.append(":[I"); //$NON-NLS-1$
				} else if (cClazz.equals(Double.class) || cClazz.equals(Double.TYPE)) {
					sb.append(":[D"); //$NON-NLS-1$
				} else if (cClazz.equals(Short.class) || cClazz.equals(Short.TYPE)) {
					sb.append(":[S"); //$NON-NLS-1$
				} else if (cClazz.equals(Float.class) || cClazz.equals(Float.TYPE)) {
					sb.append(":[F"); //$NON-NLS-1$
				} else if (cClazz.equals(Byte.class) || cClazz.equals(Byte.TYPE)) {
					sb.append(":[B"); //$NON-NLS-1$
				} else if (cClazz.equals(Long.class) || cClazz.equals(Long.TYPE)) {
					sb.append(":[J"); //$NON-NLS-1$
				} else if (cClazz.equals(DateTime.class)) {
					sb.append(":[Y"); //$NON-NLS-1$
				} else if (cClazz.equals(Object.class)) {
					sb.append(":[W"); //$NON-NLS-1$
				} else if (cClazz.equals(Boolean.class) || cClazz.equals(Boolean.TYPE)) {
					sb.append(":[Z"); //$NON-NLS-1$
				} else {
					sb.append(":[L" + ret.getCanonicalName()); //$NON-NLS-1$
				}
			} else if (ret.equals(void.class)) {
				sb.append(":V"); //$NON-NLS-1$
			} else if (ret.equals(String.class)) {
				sb.append(":T"); //$NON-NLS-1$
			} else if (ret.equals(Character.class) || ret.equals(Character.TYPE)) {
				sb.append(":C"); //$NON-NLS-1$
			} else if (ret.equals(Integer.class) || ret.equals(Integer.TYPE)) {
				sb.append(":I"); //$NON-NLS-1$
			} else if (ret.equals(Double.class) || ret.equals(Double.TYPE)) {
				sb.append(":D"); //$NON-NLS-1$
			} else if (ret.equals(Short.class) || ret.equals(Short.TYPE)) {
				sb.append(":S"); //$NON-NLS-1$
			} else if (ret.equals(Float.class) || ret.equals(Float.TYPE)) {
				sb.append(":F"); //$NON-NLS-1$
			} else if (ret.equals(Byte.class) || ret.equals(Byte.TYPE)) {
				sb.append(":B"); //$NON-NLS-1$
			} else if (ret.equals(Long.class) || ret.equals(Long.TYPE)) {
				sb.append(":J"); //$NON-NLS-1$
			} else if (ret.equals(DateTime.class)) {
				sb.append(":Y"); //$NON-NLS-1$
			} else if (ret.equals(Object.class)) {
				sb.append(":W"); //$NON-NLS-1$
			} else if (ret.equals(Boolean.class) || ret.equals(Boolean.TYPE)) {
				sb.append(":Z"); //$NON-NLS-1$
			} else {
				sb.append(":L" + ret.getCanonicalName()); //$NON-NLS-1$
			}

			return sb.toString();
		}

		/**
		 * Gets the method from the FBSValueVector
		 * 
		 * @param vec
		 *            FBSValueVector
		 * @return Method
		 * @throws JavaScriptException
		 * @since org.openntf.domino.xsp 2.5.0
		 */
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

		/**
		 * Converts the SSJS parameters to a Java Object containing the parameters
		 * 
		 * @param crystal
		 *            Meth(od) being called
		 * @param vec
		 *            FBSValueVector
		 * @return Object containing parameters
		 * @throws InterpretException
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		private Object[] toJavaArguments(final Method crystal, final FBSValueVector vec) throws InterpretException {
			Object[] result = new Object[vec.size()];
			Class<?>[] params = crystal.getParameterTypes();
			for (int i = 0; i < vec.size(); i++) {
				FBSValue val = vec.get(i);
				result[i] = val.toJavaObject(params[i]);
			}
			return result;
		}

		/**
		 * Constructor
		 * 
		 * @param context
		 *            JSContext to which the methods are being registered
		 * @param crystal
		 *            Meth(od)
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		protected OpenFunction(final JSContext context, final Method crystal) {
			super(context, crystal.getName(), 1);
			addMethod(crystal);
			clazz_ = crystal.getDeclaringClass();
		}

		/**
		 * Adds the method to a Hash Map of methods
		 * 
		 * @param crystal
		 *            Meth(od)
		 * @since org.openntf.domino.xsp 2.5.0
		 */
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject.Function#getMethodMap()
		 */
		@Override
		protected MethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(null, getWrappedClass());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject.Function#getWrappedClass()
		 */
		@Override
		protected Class<?> getWrappedClass() {
			return clazz_;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.FBSObject#getCallParameters()
		 */
		@Override
		protected String[] getCallParameters() {
			if (crystals_ == null || crystals_.isEmpty()) {
				return new String[] { "" }; //$NON-NLS-1$
			}
			return crystals_.keySet().toArray(new String[0]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.BuiltinFunction#call(com.ibm.jscript.types.FBSValueVector, com.ibm.jscript.types.FBSObject)
		 */
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
				t.printStackTrace();
			}
			return object;
		}

	}

	/**
	 * The OpenObject is base wrapper for the Java classes that defines their names, method maps and unlying Java classes. These objects are
	 * registered with the WrapperFactory with integer indexes so that the SSJS interpretter can find the appropriate wrapper for Java
	 * objects at runtime.
	 */
	public static class OpenObject extends GeneratedWrapperObject {
		private final String uiName_;
		private final Class<?> clazz_;

		/**
		 * Constructor
		 * 
		 * @param paramJSContext
		 *            JSContext to which the methods are being registered
		 * @param paramObject
		 *            Object method map
		 * @param clazz
		 *            Class the methods belong to
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		protected OpenObject(final JSContext paramJSContext, final Object paramObject, final Class<?> clazz) {
			super(paramJSContext, paramObject, "Open" + clazz.getSimpleName()); //$NON-NLS-1$
			uiName_ = "Open" + clazz.getSimpleName(); //$NON-NLS-1$
			clazz_ = clazz;
		}

		// protected OpenObject(JSContext paramJSContext, Object paramObject, String paramString) {
		// super(paramJSContext, paramObject, paramString);
		// uiName_ = paramString;
		// clazz_ = null;
		// }

		/**
		 * Extended Constructor, not used
		 * 
		 * @param JSContext
		 *            to which the methods are being registered
		 * @param paramObject
		 *            Object method map
		 * @param paramString
		 *            String
		 * @param clazz
		 *            Class the methods belong to
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		protected OpenObject(final JSContext paramJSContext, final Object paramObject, final String paramString, final Class<?> clazz) {
			super(paramJSContext, paramObject, paramString);
			uiName_ = paramString;
			clazz_ = clazz;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.FBSObject#getTypeAsString()
		 */
		@Override
		public String getTypeAsString() {
			return uiName_;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject#getWrappedClass()
		 */
		@Override
		public Class<?> getWrappedClass() {
			return clazz_;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject#getMethodMap()
		 */
		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(getJSContext(), getWrappedClass());
		}
	}

	/**
		 * The MethodMap is a simple static HashMap that binds a String, like "getItemValue" or "isBefore" to an OpenFunction object
		 * 
		 * This way when SSJS encounters an org.openntf.domino.DateTime, and it sees .isBefore(DateTime) called on it, it can resolve the
		 * string to a particular fct_OpenDateTime object, with a specific index value. It then invokes the call() method on that particular
		 * function object, passing in the DateTime argument from SSJS, as well as the DateTime object on which the .isBefore was invoked.
		 */
	public static class OpenMethodMap extends GeneratedWrapperObject.MethodMap {
		public OpenMethodMap(final JSContext arg0, final Class<?> arg1, final Class<?>[] arg2) {
			super(arg0, arg1, arg2);
		}

	}

	private final static Map<Class<?>, OpenMethodMap> classMap_ = new HashMap<Class<?>, OpenMethodMap>();

	/**
	 * Gets a map where the key is the class and the value is its OpenMethodMap
	 * 
	 * @return Map<Class<?>, OpenMethodMap>
	 * @since org.openntf.domino 2.5.0
	 */
	private static Map<Class<?>, OpenMethodMap> getClassMap() {
		return Collections.unmodifiableMap(classMap_);
	}

	/**
	 * Generates the map of all methods in the class where the key is the method name and the value is a new OpenFunction for the method<br/>
	 * 
	 * @param context
	 *            JSContext in which to register the method
	 * @param clazz
	 *            Class from which to register methods
	 * @return OpenMethodMap, a hash map of methods from the class
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private static OpenMethodMap generateMethodMap(final JSContext context, final Class<?> clazz) {
		OpenMethodMap methodMap = new OpenMethodMap(context, clazz, null);
		for (Method crystal : clazz.getMethods()) {
			if (Modifier.isPublic(crystal.getModifiers()) && !Modifier.isStatic(crystal.getModifiers())) {
				String name = crystal.getName();
				Object cur = methodMap.get(name);
				if (cur == null) {
					// Method does not exist in map
					cur = new OpenFunction(context, crystal);
					methodMap.put(crystal.getName(), cur);
				}
				if (cur instanceof OpenFunction) {
					((OpenFunction) cur).addMethod(crystal);
				} else {
					// We get here if we add a method that's the same as an existing lotus.domino one
					try {
						if ("function:IBMJS built-in function".equals(cur.toString())) { //$NON-NLS-1$
							methodMap.put(crystal.getName(), new OpenFunction(context, crystal));
						} else {
							System.out.println("Something's gone wrong! " + name + ": " + cur.toString() + ">" + cur.getClass().getName());
						}
					} catch (Throwable t) {
						// System.out.println("Hit error on " + name);
					}
				}
			}
		}
		synchronized (WrapperOpenDomino.class) {
			classMap_.put(clazz, methodMap);
		}
		return methodMap;
	}

	/**
	 * Gets the map of all methods in a class
	 * 
	 * @param context
	 *            JSContext to which to register the methods
	 * @param clazz
	 *            Class from which to retrieve the methods
	 * @return OpenMethodMap, a hash map of methods from the class
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private static OpenMethodMap getMethodMap(final JSContext context, final Class<?> clazz) {
		OpenMethodMap result = getClassMap().get(clazz);
		if (result == null) {
			result = generateMethodMap(context, clazz);
			// result = getClassMap().get(clazz);
		}
		return result;
	}

	/**
	 * Classes allowing us to register all methods within the relevant OpenNTF extensions
	 * 
	 * @since org.openntf.domino 2.5.0
	 */
	private static final class OpenConstructor extends GeneratedWrapperObject.EmptyConstructor {
		@SuppressWarnings("unused")
		private final String uiName_;
		private final Class<?> clazz_;

		/**
		 * Constructor
		 * 
		 * @param context
		 *            JSContext in which to register the methods
		 * @param clazz
		 *            Class from which to register the methods
		 * @since org.openntf.domino 2.5.0
		 */
		private OpenConstructor(final JSContext context, final Class<?> clazz) {
			super(context, "Open" + clazz.getSimpleName()); //$NON-NLS-1$
			uiName_ = "Open" + clazz.getSimpleName(); //$NON-NLS-1$
			clazz_ = clazz;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject.Constructor#getWrappedClass()
		 */
		@Override
		public Class<?> getWrappedClass() {
			return clazz_;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.GeneratedWrapperObject.Constructor#getMethodMap()
		 */
		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMethodMap(getJSContext(), getWrappedClass());
		}
	}

	/**
	 * WrapperFactory used for registering classes
	 * 
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	private static class OpenWrapperFactory implements IWrapperFactory {
		private final Class<?> clazz_;

		/**
		 * Constructor
		 * 
		 * @param clazz
		 *            Class being registered
		 * @since org.openntf.domino.xsp 2.5.0
		 */
		private OpenWrapperFactory(final Class<?> clazz) {
			clazz_ = clazz;
			// System.out.println("Registering OpenNTF SSJS object " + clazz.getName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.IWrapperFactory#getMethodMap(com.ibm.jscript.JSContext)
		 */
		@Override
		public OpenMethodMap getMethodMap(final JSContext context) {
			return WrapperOpenDomino.getMethodMap(context, clazz_);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.jscript.types.IWrapperFactory#wrap(com.ibm.jscript.JSContext, java.lang.Object)
		 */
		@Override
		public OpenObject wrap(final JSContext context, final Object object) {
			return new OpenObject(context, object, clazz_);
		}

	}

	private static boolean registered = false;

	/**
	 * Registers the methods of all Java classes in {@link WrapperOpenDomino#WRAPPED_CLASSES}, prefixing them with "Open"
	 * 
	 * @param context
	 *            JSContext in which methods will be registered
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static void register(final JSContext context) {
		if (registered) {
			return;
		}

		try {
			Registry registry = context.getRegistry();
			registry.registerPackage("OpenNTFDomino", 1337); //$NON-NLS-1$
			FBSDefaultObject defaultObject = registry.getRegistryObject();

			List<Object> wregs = ExtensionManager.findServices(null, WrapperOpenDomino.class, WrapperRegistry.class.getName());
			// for (Class<?> clazz : WRAPPED_CLASSES) {
			for (Object wreg : wregs) {
				for (Class<?> clazz : ((WrapperRegistry) wreg).getWrapperClasses()) {
					registry.registerWrapper(clazz, new OpenWrapperFactory(clazz));
					defaultObject.createProperty("Open" + clazz.getSimpleName(), 1338, new OpenConstructor(context, clazz)); //$NON-NLS-1$
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		registered = true;
	}

	/**
	 * Java classes for which to register SSJS methods
	 * 
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public static final List<Class<?>> WRAPPED_CLASSES = new ArrayList<Class<?>>();

	static {
		WRAPPED_CLASSES.add(org.openntf.domino.ACL.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ACLEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.AdministrationProcess.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.Agent.class);
		WRAPPED_CLASSES.add(org.openntf.domino.AgentBase.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.AgentContext.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.AutoMime.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.Base.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.ColorObject.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.Database.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DateRange.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DateTime.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DbDirectory.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Directory.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.DirectoryNavigator.class); // Added 5.0.0
		WRAPPED_CLASSES.add(org.openntf.domino.Document.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DocumentCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DxlExporter.class);
		WRAPPED_CLASSES.add(org.openntf.domino.DxlImporter.class);
		WRAPPED_CLASSES.add(org.openntf.domino.EmbeddedObject.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Form.class);
		// WRAPPED_CLASSES.add(org.openntf.domino.Formula.class);
		WRAPPED_CLASSES.add(org.openntf.domino.International.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Item.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Log.class);
		WRAPPED_CLASSES.add(org.openntf.domino.MIMEEntity.class);
		WRAPPED_CLASSES.add(org.openntf.domino.MIMEHeader.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Name.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Newsletter.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NoteCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NotesCalendar.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NotesCalendarEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NotesCalendarNotice.class);
		WRAPPED_CLASSES.add(org.openntf.domino.NotesProperty.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Outline.class);
		WRAPPED_CLASSES.add(org.openntf.domino.OutlineEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Registration.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Replication.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ReplicationEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextDoclink.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextItem.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextNavigator.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextParagraphStyle.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextRange.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextSection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextStyle.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextTab.class);
		WRAPPED_CLASSES.add(org.openntf.domino.RichTextTable.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Session.class);
		WRAPPED_CLASSES.add(org.openntf.domino.Stream.class);
		WRAPPED_CLASSES.add(org.openntf.domino.View.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewColumn.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewEntry.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewEntryCollection.class);
		WRAPPED_CLASSES.add(org.openntf.domino.ViewNavigator.class);
	}
	@Override
	public List<Class<?>> getWrapperClasses() {
		return WRAPPED_CLASSES;
	}

}
