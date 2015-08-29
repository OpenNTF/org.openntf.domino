package org.openntf.domino.rest.json;

import com.ibm.commons.util.io.json.JsonException;
// import com.ibm.domino.services.util.*;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JsonGraphFactory extends JsonJavaFactory {
	public static final JsonGraphFactory instance = new JsonGraphFactory();

	public JsonGraphFactory() {

	}

	@Override
	public Object createObject(Object paramObject, String paramString) throws JsonException {
		return new JsonJavaObject();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object createArray(Object paramObject, String paramString, Collection<Object> paramList)
			throws JsonException {
		if (paramList instanceof List) {
			return super.createArray(paramObject, paramString, (List) paramList);
		}
		return (paramList);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean isArray(Object arg0) throws JsonException {
		if (arg0 instanceof List) {
			return super.isArray(arg0);
		} else if (arg0 instanceof Collection) {
			return super.isArray(((Collection) arg0).toArray());
		}
		return super.isArray(arg0);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public int getArrayCount(Object paramObject) throws JsonException {
		if (paramObject instanceof List) {
			return super.getArrayCount(paramObject);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).size();
		}
		return super.getArrayCount(paramObject);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public Object getArrayItem(Object paramObject, int paramInt) throws JsonException {
		if (paramObject instanceof List) {
			return super.getArrayItem(paramObject, paramInt);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).toArray()[paramInt];
		}
		return super.getArrayItem(paramObject, paramInt);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator<Object> iterateArrayValues(Object paramObject) throws JsonException {
		if (paramObject instanceof List) {
			return super.iterateArrayValues(paramObject);
		} else if (paramObject instanceof Collection) {
			return ((Collection) paramObject).iterator();
		}
		return super.iterateArrayValues(paramObject);
	}
}
