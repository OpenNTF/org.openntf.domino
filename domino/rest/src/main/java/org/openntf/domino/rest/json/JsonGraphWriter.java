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
import java.util.TimeZone;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.resources.frames.JsonFrameAdapter;
import org.openntf.domino.rest.service.Parameters.ParamMap;

public class JsonGraphWriter extends JsonWriter {
	protected DFramedTransactionalGraph<?> graph_;
	protected ParamMap parameters_;

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
			boolean forceLowerCaseKeys) {
		super(JsonGraphFactory.instance, arg1, arg2);
		forceLowerCaseKeys_ = forceLowerCaseKeys;
		parameters_ = parameters;
		graph_ = graph;
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
		try {
			return dateToString(paramDateTime.toJavaDate(), paramBoolean);
		} catch (NotesException localNotesException) {
			throw new AbstractIOException(localNotesException, "");
		}
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

	@Override
	public void outObject(Object paramObject) throws IOException, JsonException {
		if (paramObject == null) {
			super.outNull();
		} else if (paramObject instanceof EdgeFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (EdgeFrame) paramObject, parameters_);
			super.outObject(adapter);
		} else if (paramObject instanceof VertexFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (VertexFrame) paramObject, parameters_);
			super.outObject(adapter);
		} else if (paramObject instanceof Class<?>) {
			String className = ((Class<?>) paramObject).getName();
			super.outStringLiteral(className);
		} else if (paramObject instanceof Enum) {
			String className = ((Enum<?>) paramObject).getClass().getName();
			String enumName = ((Enum<?>) paramObject).name();
			super.outStringLiteral(className + " " + enumName);
		} else if (paramObject instanceof org.openntf.domino.impl.View.DominoColumnInfo) {
			String itemName = ((org.openntf.domino.impl.View.DominoColumnInfo) paramObject).getItemName();
			super.outStringLiteral(itemName);
		} else {
			// Class<?> clazz = paramObject.getClass();
			// String name = clazz.getName();
			// System.out.println("DEBUG: Attempting to jsonify a " + name);
			super.outObject(paramObject);
		}
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
				try {
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
				} catch (NotesException localNotesException) {
					throw new AbstractIOException(localNotesException, "");
				}
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
		if (this.getFactory().isNull(paramObject)) {
			outNull();
		} else if (paramObject instanceof EdgeFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (EdgeFrame) paramObject, parameters_);
			outObject(adapter);
		} else if (paramObject instanceof VertexFrame) {
			JsonFrameAdapter adapter = new JsonFrameAdapter(graph_, (VertexFrame) paramObject, parameters_);
			outObject(adapter);
		} else if (paramObject instanceof Class<?>) {
			String className = ((Class) paramObject).getName();
			outStringLiteral(className);
		} else if (paramObject instanceof Enum) {
			String className = ((Enum) paramObject).getClass().getName();
			String enumName = ((Enum) paramObject).name();
			outStringLiteral(className + " " + enumName);
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
		} else if (paramObject instanceof Date) {
			outDateLiteral_((Date) paramObject);
		} else {
			outStringLiteral("JsonGenerator cannot process unknown type of "
					+ ((paramObject != null) ? paramObject.getClass().getName() : "<null>"));
		}
	}
}
