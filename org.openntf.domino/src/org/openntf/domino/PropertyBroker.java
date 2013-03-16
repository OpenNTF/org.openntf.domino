package org.openntf.domino;

import java.util.Vector;

public interface PropertyBroker extends Base<lotus.domino.PropertyBroker>, lotus.domino.PropertyBroker {
	@Override
	public void clearProperty(String propertyName);

	@Override
	public Vector<NotesProperty> getInputPropertyContext();

	@Override
	public NotesProperty getProperty(String propertyName);

	@Override
	public Vector<Object> getPropertyValue(String propertyName);

	@Override
	public String getPropertyValueString(String propertyName);

	@Override
	public boolean hasProperty(String propertyName);

	@Override
	public NotesProperty setPropertyValue(String propertyName, Object propertyValue);

	@Override
	public void publish();
}
