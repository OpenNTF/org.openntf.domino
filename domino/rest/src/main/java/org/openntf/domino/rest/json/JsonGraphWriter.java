package org.openntf.domino.rest.json;

// import com.ibm.domino.services.util.JsonWriter;
import com.ibm.commons.util.AbstractIOException;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonReference;
import com.ibm.commons.util.io.json.util.JsonWriter;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import org.openntf.domino.DateTime;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.big.impl.NoteCoordinate;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.json.JsonGraphFactory.IJsonWriterAdapter;
import org.openntf.domino.rest.resources.frames.JsonFrameAdapter;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.rest.utils.RTtoHTMLUtil;

public class JsonGraphWriter extends JsonWriter {
	protected DFramedTransactionalGraph<?> graph_;
	protected ParamMap parameters_;
	protected JsonGraphFactory factory_;
	protected boolean isCollectionRoute_;

	private static ThreadLocal<SimpleDateFormat> ISO8601_UTC = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			TimeZone localTimeZone = TimeZone.getTimeZone("UTC");
			SimpleDateFormat result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			result.setTimeZone(localTimeZone);
			return result;
		}
	};
	private static ThreadLocal<SimpleDateFormat> ISO8601_DT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		};
	};

	private static ThreadLocal<SimpleDateFormat> ISO8601_DO = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};

	private static ThreadLocal<SimpleDateFormat> ISO8601_TO = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		};
	};

	private boolean forceLowerCaseKeys_ = false;

	public JsonGraphWriter(Writer arg1, DFramedTransactionalGraph<?> graph, ParamMap parameters, boolean arg2,
			boolean forceLowerCaseKeys, boolean isCollectionRoute) {
		super(JsonGraphFactory.instance, arg1, arg2);
		factory_ = JsonGraphFactory.instance;
		forceLowerCaseKeys_ = forceLowerCaseKeys;
		parameters_ = parameters;
		graph_ = graph;
		isCollectionRoute_ = isCollectionRoute;
	}

	public void outDateLiteral(Date paramDate) throws IOException {
		String str = dateToString(paramDate, true);
		outStringLiteral(str);
	}

	public void outDateLiteral(DateTime paramDateTime) throws IOException {
		String str = dateToString(paramDateTime, true);
		out(str);
	}

	private String dateOnlyToString(Date paramDate) {
		return ISO8601_DO.get().format(paramDate);
	}

	private String timeOnlyToString(Date paramDate) {
		return ISO8601_TO.get().format(paramDate);
	}

	private String dateToString(DateTime paramDateTime, boolean paramBoolean) throws IOException {
		return dateToString(paramDateTime.toJavaDate(), paramBoolean);
	}

	private String dateToString(Date paramDate, boolean paramBoolean) throws IOException {
		String str = null;

		if (paramBoolean) {
			str = ISO8601_UTC.get().format(paramDate);
		} else {
			str = ISO8601_DT.get().format(paramDate);
		}

		return str;
	}

	public void outRichTextItem(RichTextItem richText) throws IOException, JsonException {
		String html = RTtoHTMLUtil.getHTML(richText);
		outStringLiteral(html);
	}

	@Override
	public void outObject(Object paramObject) throws IOException, JsonException {
		// System.out.println("TEMP DEBUG Attempting to jsonify an object of "
		// + (paramObject == null ? "NULL" : paramObject.getClass().getName()));
		if (paramObject != null) {
			Class<?> objClass = paramObject.getClass();
			IJsonWriterAdapter adapter = factory_.getJsonWriterAdapter(objClass);
			if (adapter != null) {
				paramObject = adapter.toJson(paramObject);
			}
		}

		if (paramObject == null) {
			super.outNull();
		} else if (paramObject instanceof EdgeFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (EdgeFrame) paramObject, parameters_,
					isCollectionRoute_);
			super.outObject(adapter);
		} else if (paramObject instanceof VertexFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (VertexFrame) paramObject, parameters_,
					isCollectionRoute_);
			super.outObject(adapter);
		} else if (paramObject instanceof Class<?>) {
			String className = ((Class<?>) paramObject).getName();
			super.outStringLiteral(className);
		} else if (paramObject instanceof RichTextItem) {
			outRichTextItem((RichTextItem) paramObject);
		} else if (paramObject instanceof Enum) {
			String className = ((Enum<?>) paramObject).getClass().getName();
			String enumName = ((Enum<?>) paramObject).name();
		} else if (paramObject instanceof NoteCoordinate) {
			String nc = ((NoteCoordinate) paramObject).toString();
			super.outStringLiteral(nc);
		} else if (paramObject instanceof org.openntf.domino.impl.View.DominoColumnInfo) {
			String itemName = ((org.openntf.domino.impl.View.DominoColumnInfo) paramObject).getItemName();
			super.outStringLiteral(itemName);
		} else if (paramObject instanceof Set) {
			// System.out.println("TEMP DEBUG outObject received a Set");
			outArrayLiteral(((Set) paramObject).toArray());
		} else if (paramObject instanceof DateTime) {
			// System.out.println("TEMP DEBUG outObject received a Set");
			outArrayLiteral(paramObject);
		} else if (paramObject instanceof Throwable) {
			outException((Throwable) paramObject);
		} else {
			// Class<?> clazz = paramObject.getClass();
			// String name = clazz.getName();
			super.outObject(paramObject);
		}
	}

	public void outException(Throwable throwable) throws IOException, JsonException {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("exceptionType", throwable.getClass().getSimpleName());
		result.put("message", throwable.getMessage());
		StackTraceElement[] trace = throwable.getStackTrace();
		StringBuilder sb = new StringBuilder();
		int elemCount = 0;
		for (StackTraceElement elem : trace) {
			if (++elemCount > 15) {
				sb.append("...more");
				break;
			}
			sb.append("  at ");
			sb.append(elem.getClassName());
			sb.append("." + elem.getMethodName());
			sb.append("(" + elem.getFileName() + ":" + elem.getLineNumber() + ")");
			sb.append(System.getProperty("line.separator"));
		}
		result.put("stacktrace", sb.toString());
		if (throwable.getCause() != null) {
			result.put("causedby", throwable.getCause());
		}
		outObject(result);
	}

	@SuppressWarnings("rawtypes")
	public void outDominoValue(Object paramObject) throws IOException {
		try {
			if (paramObject == null) {
				outNull();
				return;
			}
			if (paramObject instanceof String) {
				outStringLiteral((String) paramObject);
				return;
			}
			if (paramObject instanceof Number) {
				outNumberLiteral(((Number) paramObject).doubleValue());
				return;
			}
			if (paramObject instanceof Boolean) {
				outBooleanLiteral(((Boolean) paramObject).booleanValue());
				return;
			}
			if (paramObject instanceof Date) {
				outStringLiteral(dateToString((Date) paramObject, true));
				return;
			}
			if (paramObject instanceof DateTime) {
				if (((DateTime) paramObject).getDateOnly().length() == 0) {
					outStringLiteral(timeOnlyToString(((DateTime) paramObject).toJavaDate()));
					return;
				}

				if (((DateTime) paramObject).getTimeOnly().length() == 0) {
					outStringLiteral(dateOnlyToString(((DateTime) paramObject).toJavaDate()));
					return;
				}

				outStringLiteral(dateToString(((DateTime) paramObject).toJavaDate(), true));
				return;
			}
			if (paramObject instanceof Vector) {
				startArray();
				Vector localVector = (Vector) paramObject;
				int i = localVector.size();
				for (int j = 0; j < i; ++j) {
					startArrayItem();
					outDominoValue(localVector.get(j));
					endArrayItem();
				}
				endArray();
				return;
			}

			outStringLiteral("???");
		} catch (JsonException localJsonException) {
			throw new AbstractIOException(localJsonException, "");
		}
	}

	@Override
	public void outPropertyName(String arg0) throws IOException {
		if (forceLowerCaseKeys_) {
			super.outPropertyName(arg0.toLowerCase());
		} else {
			super.outPropertyName(arg0);
		}
	}

	@Override
	public void outArrayLiteral(Object arg0, boolean paramBoolean) throws IOException, JsonException {
		// Class<?> clazz = arg0.getClass();
		// String name = clazz.getName();
		// System.out.println("DEBUG: Attempting to jsonify an array " + name);
		super.outArrayLiteral(arg0, paramBoolean);
	}

	@Override
	public void outArrayLiteral(Object arg0) throws IOException, JsonException {
		// Class<?> clazz = arg0.getClass();
		// String name = clazz.getName();
		// System.out.println("DEBUG: Attempting to jsonify an array " + name);
		super.outArrayLiteral(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void outLiteral(Object paramObject, boolean paramBoolean) throws IOException, JsonException {
		// System.out.println("TEMP DEBUG outputting a literal of "
		// + (paramObject == null ? "NULL VALUE" :
		// paramObject.getClass().getName()));
		if (paramObject != null) {
			Class<?> objClass = paramObject.getClass();
			IJsonWriterAdapter adapter = factory_.getJsonWriterAdapter(objClass);
			if (adapter != null) {
				paramObject = adapter.toJson(paramObject);
			}
		}

		if (this.getFactory().isNull(paramObject)) {
			outNull();
		} else if (paramObject instanceof VertexFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (VertexFrame) paramObject, parameters_,
					isCollectionRoute_);
			outObject(adapter);
		} else if (paramObject instanceof EdgeFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (EdgeFrame) paramObject, parameters_,
					isCollectionRoute_);
			outObject(adapter);
		} else if (paramObject instanceof Class<?>) {
			String className = ((Class) paramObject).getName();
			outStringLiteral(className);
		} else if (paramObject instanceof RichTextItem) {
			outRichTextItem((RichTextItem) paramObject);
		} else if (paramObject instanceof Enum) {
			String className = ((Enum) paramObject).getClass().getName();
			String enumName = ((Enum) paramObject).name();
			outStringLiteral(className + " " + enumName);
		} else if (paramObject instanceof CharSequence) {
			outStringLiteral(paramObject.toString());
		} else if (paramObject instanceof Set) {
			// System.out.println("TEMP DEBUG Got a set!");
			outArrayLiteral(((Set) paramObject).toArray());
		} else if (this.getFactory().isString(paramObject)) {
			outStringLiteral(this.getFactory().getString(paramObject));
		} else if (this.getFactory().isNumber(paramObject)) {
			outNumberLiteral(this.getFactory().getNumber(paramObject));
		} else if (this.getFactory().isBoolean(paramObject)) {
			outBooleanLiteral(this.getFactory().getBoolean(paramObject));
		} else if (this.getFactory().isObject(paramObject)) {
			outObject(paramObject, paramBoolean);
		} else if (this.getFactory().isArray(paramObject)) {
			outArrayLiteral(paramObject, paramBoolean);
		} else if (paramObject instanceof JsonReference) {
			outReference((JsonReference) paramObject);
		} else if (paramObject instanceof DateTime) {
			DateTime dt = (DateTime) paramObject;
			outDateLiteral_(dt.toJavaDate());
		} else if (paramObject instanceof Date) {
			outDateLiteral_((Date) paramObject);
		} else {
			outStringLiteral("JsonGenerator cannot process unknown type of "
					+ ((paramObject != null) ? paramObject.getClass().getName() : "<null>"));
		}
	}
}
