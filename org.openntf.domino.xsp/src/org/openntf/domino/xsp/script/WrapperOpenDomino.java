package org.openntf.domino.xsp.script;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
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

	public static abstract class OpenFunction extends GeneratedWrapperObject.Function {
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
		protected OpenFunction(JSContext context, String methodName, int index) {
			super(context, methodName, index);
		}

		@Override
		public FBSValue call(FBSValueVector valueVector, FBSObject object) throws JavaScriptException {
			String type = this.getTypeAsString();
			String message = "Function " + getClass().getName() + " which wraps methods for the class " + getWrappedClass().getName()
					+ " as is type " + type
					+ " does not have a defined call() method for its implementation. Therefore it cannot execute against the object "
					+ object.getTypeAsString();
			throw new InterpretException(null, message, new Object[0]);
		}
	}

	public static abstract class OpenObject extends GeneratedWrapperObject {
		/*
		 * The OpenObject is base wrapper for the Java classes that defines their names, method maps and unlying Java classes. These objects
		 * are registered with the WrapperFactory with integer indexes so that the SSJS interpretter can find the appropriate wrapper for
		 * Java objects at runtime.
		 */

		protected OpenObject(JSContext paramJSContext, Object paramObject, String paramString) {
			super(paramJSContext, paramObject, paramString);
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
		public OpenMethodMap(JSContext arg0, Class arg1, Class[] arg2) {
			super(arg0, arg1, arg2);
		}

	}

	private static OpenMethodMap methodMap_OpenDocument; // 0

	private static OpenMethodMap getMap_OpenDocument(JSContext context) {
		if (methodMap_OpenDocument == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenDocument == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, Document.class, null);
					methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap.put("getLastModifiedDate", new fct_OpenDocument(context, "getLastModifiedDate", 1));
					methodMap.put("getInitiallyModifiedDate", new fct_OpenDocument(context, "getInitiallyModifiedDate", 2));
					methodMap.put("getLastAccessedDate", new fct_OpenDocument(context, "getLastAccessedDate", 3));
					methodMap_OpenDocument = methodMap;
				}
			}
		}
		return methodMap_OpenDocument;
	}

	public static final class _OpenDocument extends OpenObject {
		public _OpenDocument(JSContext context, Object object) {
			super(context, object, "OpenDocument");
		}

		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMap_OpenDocument(getJSContext());
		}

		@Override
		public Class<?> getWrappedClass() {
			return Document.class;
		}

		@Override
		public String getTypeAsString() {
			return "OpenDocument";
		}
	}

	private static final class ctor_OpenDocument extends GeneratedWrapperObject.EmptyConstructor {
		private ctor_OpenDocument(JSContext context) {
			super(context, "OpenDocument");
		}

		@Override
		protected OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMap_OpenDocument(getJSContext());
		}

		@Override
		protected Class<?> getWrappedClass() {
			return Document.class;
		}

	}

	static class fct_OpenDocument extends OpenFunction {
		protected fct_OpenDocument(JSContext context, String methodName, int index) {
			super(context, methodName, index);
		}

		@Override
		protected MethodMap getMethodMap() {
			return methodMap_OpenDocument;
		}

		@Override
		protected Class getWrappedClass() {
			return Document.class;
		}

		@Override
		protected String[] getCallParameters() {
			switch (index) {
			case 0:
			case 1:
			case 2:
			case 3:
				return new String[] { "():Y" };
			}
			return super.getCallParameters();
		}

		@Override
		public FBSValue call(FBSValueVector valueVector, FBSObject object) throws JavaScriptException {
			Document localDocument = (Document) object.toJavaObject(null);
			try {
				switch (this.index) {
				case 0:
					if ((localDocument != null) && (valueVector.size() == 0)) {
						java.util.Date d = localDocument.getCreatedDate();
						FBSValue result = FBSUtility.wrap(getJSContext(), d);
						return result;
					}
				case 1:
					if ((localDocument != null) && (valueVector.size() == 0)) {
						java.util.Date d = localDocument.getLastModifiedDate();
						FBSValue result = FBSUtility.wrap(getJSContext(), d);
						return result;
					}
				case 2:
					if ((localDocument != null) && (valueVector.size() == 0)) {
						java.util.Date d = localDocument.getInitiallyModifiedDate();
						FBSValue result = FBSUtility.wrap(getJSContext(), d);
						return result;
					}
				case 3:
					if ((localDocument != null) && (valueVector.size() == 0)) {
						java.util.Date d = localDocument.getLastAccessedDate();
						FBSValue result = FBSUtility.wrap(getJSContext(), d);
						return result;
					}
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
			}

			return object;
		}
	}

	private static OpenMethodMap methodMap_OpenSession; // 1
	private static OpenMethodMap methodMap_OpenDatabase;// 2

	private static OpenMethodMap methodMap_OpenDocumentCollection;// 3
	private static OpenMethodMap methodMap_OpenView;// 4
	private static OpenMethodMap methodMap_OpenViewEntryCollection;// 5
	private static OpenMethodMap methodMap_OpenViewEntry;// 6
	private static OpenMethodMap methodMap_OpenDbDirectory;// 7
	private static OpenMethodMap methodMap_OpenACL;// 8
	private static OpenMethodMap methodMap_OpenACLEntry;// 9

	private static OpenMethodMap methodMap_OpenDateTime;// 10

	private static OpenMethodMap getMap_OpenDateTime(JSContext context) {
		if (methodMap_OpenDateTime == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenDateTime == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, DateTime.class, null);
					methodMap.put("isBefore", new fct_OpenDateTime(context, "isBefore", 0));
					methodMap.put("isAfter", new fct_OpenDateTime(context, "isAfter", 1));
					methodMap.put("toJavaCal", new fct_OpenDateTime(context, "toJavaCal", 2));
					methodMap_OpenDateTime = methodMap;
				}
			}
		}
		return methodMap_OpenDateTime;
	}

	public static final class _OpenDateTime extends OpenObject {
		public _OpenDateTime(JSContext context, Object object) {
			super(context, object, "OpenDateTime");
		}

		@Override
		public OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMap_OpenDateTime(getJSContext());
		}

		@Override
		public Class<?> getWrappedClass() {
			return DateTime.class;
		}

		@Override
		public String getTypeAsString() {
			return "OpenDateTime";
		}
	}

	private static final class ctor_OpenDateTime extends GeneratedWrapperObject.EmptyConstructor {
		private ctor_OpenDateTime(JSContext context) {
			super(context, "OpenDateTime");
		}

		@Override
		protected OpenMethodMap getMethodMap() {
			return WrapperOpenDomino.getMap_OpenDateTime(getJSContext());
		}

		@Override
		protected Class<?> getWrappedClass() {
			return DateTime.class;
		}
	}

	static class fct_OpenDateTime extends OpenFunction {
		protected fct_OpenDateTime(JSContext context, String methodName, int index) {
			super(context, methodName, index);
		}

		@Override
		protected MethodMap getMethodMap() {
			return methodMap_OpenDateTime;
		}

		@Override
		protected Class getWrappedClass() {
			return DateTime.class;
		}

		@Override
		protected String[] getCallParameters() {
			switch (index) {
			case 0: // isBefore(DateTime)
				return new String[] { "(DateTime:Lorg.openntf.domino.DateTime;):Z" };
			case 1: // isAfter(DateTime)
				return new String[] { "(DateTime:Lorg.openntf.domino.DateTime;):Z" };
			case 2: // toJavaCal()
				return new String[] { "():Y" };
			}
			return super.getCallParameters();
		}

		@Override
		public FBSValue call(FBSValueVector valueVector, FBSObject object) throws JavaScriptException {
			DateTime dateTime = (DateTime) object.toJavaObject(null);
			try {
				switch (this.index) {
				case 0:
					if ((dateTime != null) && (valueVector.size() == 1)) {
						org.openntf.domino.DateTime dt = (org.openntf.domino.DateTime) valueVector.getFBSValue(0).toJavaObject();
						return FBSUtility.wrap(getJSContext(), dateTime.isBefore(dt));
					}
				case 1:
					if ((dateTime != null) && (valueVector.size() == 1)) {
						org.openntf.domino.DateTime dt = (org.openntf.domino.DateTime) valueVector.getFBSValue(0).toJavaObject();
						return FBSUtility.wrap(getJSContext(), dateTime.isAfter(dt));
					}
				case 2:
					if ((dateTime != null) && (valueVector.size() == 0)) {
						FBSValue result = FBSUtility.wrap(getJSContext(), dateTime.toJavaCal());
						return result;
					}
				}
			} catch (Exception e) {
				// TODO please massively improve!!!
				if (e instanceof InterpretException) {
					throw ((InterpretException) e);
				}
				throw new InterpretException(e);
			}
			throw new InterpretException(); // TODO please massively improve!!!
		}
	}

	private static OpenMethodMap getMap_OpenDocumentCollection(JSContext context) {
		if (methodMap_OpenDocumentCollection == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenDocumentCollection == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, DocumentCollection.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenDocumentCollection = methodMap;
				}
			}
		}
		return methodMap_OpenDocumentCollection;
	}

	private static OpenMethodMap getMap_OpenDatabase(JSContext context) {
		if (methodMap_OpenDatabase == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenDatabase == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, Database.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenDatabase = methodMap;
				}
			}
		}
		return methodMap_OpenDatabase;
	}

	private static OpenMethodMap getMap_OpenView(JSContext context) {
		if (methodMap_OpenView == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenView == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, View.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenView = methodMap;
				}
			}
		}
		return methodMap_OpenView;
	}

	private static OpenMethodMap getMap_OpenViewEntry(JSContext context) {
		if (methodMap_OpenViewEntry == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenViewEntry == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, ViewEntry.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenViewEntry = methodMap;
				}
			}
		}
		return methodMap_OpenViewEntry;
	}

	private static OpenMethodMap getMap_OpenViewEntryCollection(JSContext context) {
		if (methodMap_OpenViewEntryCollection == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenViewEntryCollection == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, ViewEntryCollection.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenViewEntryCollection = methodMap;
				}
			}
		}
		return methodMap_OpenViewEntryCollection;
	}

	private static OpenMethodMap getMap_OpenSession(JSContext context) {
		if (methodMap_OpenSession == null) {
			synchronized (WrapperOpenDomino.class) {
				if (methodMap_OpenSession == null) {
					OpenMethodMap methodMap = new OpenMethodMap(context, Session.class, null);
					// methodMap.put("getCreatedDate", new fct_OpenDocument(context, "getCreatedDate", 0));
					methodMap_OpenSession = methodMap;
				}
			}
		}
		return methodMap_OpenSession;
	}

	private static class WrapperFactory implements IWrapperFactory {
		private int idx;

		private WrapperFactory(int paramInt) {
			this.idx = paramInt;
		}

		@Override
		public OpenMethodMap getMethodMap(JSContext context) {
			switch (this.idx) {
			case 0:
				return WrapperOpenDomino.getMap_OpenDocument(context);
			case 10:
				return WrapperOpenDomino.getMap_OpenDateTime(context);
			}
			throw new IllegalStateException("OpenNTF Domino SSJS wrapper registration error. No case for index " + this.idx);
		}

		@Override
		public OpenObject wrap(JSContext context, Object object) {
			switch (this.idx) {
			case 0:
				return new WrapperOpenDomino._OpenDocument(context, object);
			case 10:
				return new WrapperOpenDomino._OpenDateTime(context, object);
			}
			throw new IllegalStateException("OpenNTF Domino SSJS wrapper registration error No case for index " + this.idx);
		}
	}

	private static boolean registered = false;

	public static void register(JSContext context) {
		if (registered) {
			return;
		}
		registered = true;
		try {
			Registry registry = context.getRegistry();
			registry.registerPackage("OpenNTFDomino", 1337);
			registry.registerWrapper(org.openntf.domino.Document.class, new WrapperFactory(0));
			registry.registerWrapper(org.openntf.domino.DateTime.class, new WrapperFactory(10));

			FBSDefaultObject defaultObject = registry.getRegistryObject();
			defaultObject.createProperty("OpenDocument", 1338, new ctor_OpenDocument(context));
			defaultObject.createProperty("OpenDateTime", 1338, new ctor_OpenDateTime(context));
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}
}
