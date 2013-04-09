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

		protected fct_DocumentEx(JSContext arg0, String arg1, int arg2) {
			super(arg0, arg1, arg2);
		}

		@Override
		protected MethodMap getMethodMap() {
			return null; // TODO not sure yet...
		}

		@Override
		protected Class getWrappedClass() {
			return lotus.domino.Document.class;
		}

		@Override
		protected String[] getCallParameters() {
			switch (index) {
			case 89:
				return new String[] { "(str:Tstr:T):Ljava.util.Vector;" };
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

			}
		}
		// localRegistry.registerWrapper(DxlExporter.class, new WrapperFactory(18, null));
		IWrapperFactory factory = registry.getWrapper(lotus.domino.Document.class);
		MethodMap mm = factory.getMethodMap(context);
		mm.put("getItemValue", new fct_DocumentEx(context, "getItemValue", 89));
	}

}
