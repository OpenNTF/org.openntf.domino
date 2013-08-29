/**
 * 
 */
package org.openntf.domino.types;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.ext.Database.Events;

/**
 * @author nfreeman
 * 
 */
public class DatabaseEvent implements IDatabaseEvent {
	private static final Logger log_ = Logger.getLogger(DatabaseEvent.class.getName());
	private static final long serialVersionUID = 1L;

	private final Database database_;
	private final Events event_;
	private final Object source_;
	private final Object target_;

	/**
	 * 
	 */
	public DatabaseEvent(final Database database, final Events event, final Object source, final Object target) {
		database_ = database;
		event_ = event;
		source_ = source;
		target_ = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.IDatabaseEvent#getDatabase()
	 */
	@Override
	public Database getDatabase() {
		return database_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.IDatabaseEvent#getSource()
	 */
	@Override
	public Object getSource() {
		return source_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.IDatabaseEvent#getEvent()
	 */
	@Override
	public Events getEvent() {
		return event_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.IDatabaseEvent#getTarget()
	 */
	@Override
	public Object getTarget() {
		return target_;
	}
}
