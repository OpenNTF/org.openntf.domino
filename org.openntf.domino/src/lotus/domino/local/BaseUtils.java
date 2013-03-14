package lotus.domino.local;

import lotus.domino.NotesException;

//NTF - I should have known this wouldn't work at runtime. The compiler was okay with it, but the JVM said it was an IllegalAccess :)

public class BaseUtils extends NotesBase {
	// private static Method getCppMethod;
	// private static Method isInvalidMethod;
	// static {
	// try {
	// getCppMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
	// getCppMethod.setAccessible(true);
	// isInvalidMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("isInvalid", (Class<?>[]) null);
	// isInvalidMethod.setAccessible(true);
	// } catch (Exception e) {
	// DominoUtils.handleException(e);
	// }
	//
	// }

	public static boolean isRecycled(lotus.domino.local.NotesBase base) {
		return base.isInvalid();
	}

	public static long getCppId(lotus.domino.local.NotesBase base) {
		return base.GetCppObj();
	}

	public BaseUtils() throws NotesException {

	}
}
