package org.openntf.domino.xsp.script;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.types.GeneratedWrapperObject;
import com.ibm.jscript.types.GeneratedWrapperObject.MethodMap;
import com.ibm.jscript.types.IWrapperFactory;
import com.ibm.jscript.types.Registry;
import com.ibm.xsp.script.NotesFunctions;
import com.ibm.xsp.script.WrapperDomino;
import com.ibm.xsp.script.WrapperDominoEx;

public class OpenDomJavaScriptExtension implements JavaScriptProvider {

	static class fct_DocumentEx extends GeneratedWrapperObject.Function {
		// declared as an extension to match the existing pattern of structure. No particular reason it need to be done this way as far as I
		// can tell.

		MethodMap mm_;

		protected fct_DocumentEx(JSContext arg0, String arg1, int arg2, MethodMap mm) {
			super(arg0, arg1, arg2);
			mm_ = mm;
		}

		@Override
		protected MethodMap getMethodMap() {
			return mm_; // yes, this really should be a static reference, but it's hidden in WrapperDomino
		}

		@Override
		protected Class getWrappedClass() {
			return lotus.domino.Document.class;
		}

		@Override
		protected String[] getCallParameters() {
			switch (index) {
			case 89:
				return new String[] { "():Ljava.util.Date;" };
			}
			return super.getCallParameters();
		}
	}

	@Override
	public void registerWrappers(JSContext context) {
		WrapperDomino.register(context);
		WrapperDominoEx.register(context);
		context.getRegistry().registerGlobalPrototype("@Functions", new NotesFunctions(context));

		Registry registry = context.getRegistry();
		for (Registry.Package pack : registry.getRegisteredPackages()) {
			if (pack.getName().equals("Domino")) {
				// this is just here in case we want to try searching for it...
			}
		}
		// localRegistry.registerWrapper(DxlExporter.class, new WrapperFactory(18, null));
		IWrapperFactory factory = registry.getWrapper(lotus.domino.Document.class);
		// first get the factory that's responsible for the entire Document class
		MethodMap mm = factory.getMethodMap(context);
		Object protoReferenceO = mm.get("getCreated");
		if (protoReferenceO instanceof GeneratedWrapperObject.Function) {
			GeneratedWrapperObject.Function protoReference = (GeneratedWrapperObject.Function) protoReferenceO;
			// just here in case we want to learn more about what's going on.
		}
		// then get it's method map
		mm.put("getCreatedDate", new fct_DocumentEx(context, "getCreatedDate", 89, mm));
		// then add our new method to the map.
		// Note: overloads work differently. If there are multiple param signatures, they are established with a string[] like so...
		String[] example = { "():V", "(n:I):V", "(n:In:I):V" };
		// Therefore where we've overloaded existing functions, we're going to have to get the

	}

}
