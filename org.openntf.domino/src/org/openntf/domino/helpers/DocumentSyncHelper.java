/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentSyncHelper.
 */
public class DocumentSyncHelper {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DocumentSyncHelper.class.getName());

	/**
	 * The Enum Strategy.
	 */
	public static enum Strategy {

		/** The replace if newer. */
		REPLACE_IF_NEWER,
		/** The create and replace. */
		CREATE_AND_REPLACE,
		/** The replace only. */
		REPLACE_ONLY
	}

	public static enum TransactionRule {
		NO_TRANSACTION, COMMIT_EVERY_SOURCE, COMMIT_AT_END
	}

	/**
	 * The Enum Controls.
	 */
	public static enum Controls {

		/** The target server. */
		TARGET_SERVER,
		/** The target filepath. */
		TARGET_FILEPATH,
		/** The target lookup view. */
		TARGET_LOOKUP_VIEW,
		/** The source key formula. */
		SOURCE_KEY_FORMULA,
		/** The strategy. */
		STRATEGY,
		/** The sync source field. */
		SYNC_SOURCE_FIELD,
		/** The sync target field. */
		SYNC_TARGET_FIELD;
	}

	/** The target server_. */
	private String targetServer_;

	/** The target filepath_. */
	private String targetFilepath_;

	/** The target lookup view_. */
	private String targetLookupView_;

	/** The source key formula_. */
	// private String sourceKeyFormula_;

	private Formula sourceKeyFormula_;

	/** The strategy_. */
	private Strategy strategy_;

	private TransactionRule transactionRule_;

	/** The sync map_. */
	private Map<Formula, String> syncMap_;

	/**
	 * Instantiates a new document sync helper.
	 */
	public DocumentSyncHelper() {
		// TODO allow for constructor arguments to configure
	}

	/**
	 * Instantiates a new document sync helper.
	 * 
	 * @param controlDoc
	 *            the control doc
	 * @param controlMap
	 *            the control map
	 */
	public DocumentSyncHelper(final Document controlDoc, Map<Controls, String> controlMap) {
		if (controlMap == null)
			controlMap = new HashMap<Controls, String>();
		if (controlMap.containsKey(Controls.TARGET_SERVER)) {
			setTargetServer(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_SERVER)));
		} else if (controlDoc.hasItem(Controls.TARGET_SERVER.toString())) {
			setTargetServer(controlDoc.getItemValueString(Controls.TARGET_SERVER.toString()));
		} else {
			setTargetServer(Factory.getParentDatabase(controlDoc).getServer());
		}
		if (controlMap.containsKey(Controls.TARGET_FILEPATH)) {
			setTargetFilepath(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_FILEPATH)));
		} else if (controlDoc.hasItem(Controls.TARGET_FILEPATH.toString())) {
			setTargetFilepath(controlDoc.getItemValueString(Controls.TARGET_FILEPATH.toString()));
		} else {
			setTargetFilepath(Factory.getParentDatabase(controlDoc).getFilePath());
		}
		if (controlMap.containsKey(Controls.TARGET_LOOKUP_VIEW)) {
			setTargetLookupView(controlDoc.getItemValueString(controlMap.get(Controls.TARGET_LOOKUP_VIEW)));
		} else if (controlDoc.hasItem(Controls.TARGET_LOOKUP_VIEW.toString())) {
			setTargetLookupView(controlDoc.getItemValueString(Controls.TARGET_LOOKUP_VIEW.toString()));
		} else {
			setTargetLookupView("TARGET_LOOKUP_VIEW");
		}
		if (controlMap.containsKey(Controls.SOURCE_KEY_FORMULA)) {
			setSourceKeyFormula(controlDoc.getItemValueString(controlMap.get(Controls.SOURCE_KEY_FORMULA)));
		} else if (controlDoc.hasItem(Controls.SOURCE_KEY_FORMULA.toString())) {
			setSourceKeyFormula(controlDoc.getItemValueString(Controls.SOURCE_KEY_FORMULA.toString()));
		} else {
			setSourceKeyFormula("SOURCE_KEY_FORMULA"); // this would be the name of the field on the source document used for the lookup
		}
		if (controlMap.containsKey(Controls.STRATEGY)) {
			setStrategy(Strategy.valueOf(controlDoc.getItemValueString(controlMap.get(Controls.STRATEGY))));
		} else if (controlDoc.hasItem(Controls.STRATEGY.toString())) {
			setStrategy(Strategy.valueOf(controlDoc.getItemValueString(Controls.STRATEGY.toString())));
		} else {
			setStrategy(Strategy.CREATE_AND_REPLACE);
		}
		Map<Object, String> syncMap = new HashMap<Object, String>();
		if (controlMap.containsKey(Controls.SYNC_SOURCE_FIELD) && controlMap.containsKey(Controls.SYNC_TARGET_FIELD)) {
			java.util.Vector<Object> keyVec = controlDoc.getItemValue(controlMap.get(Controls.SYNC_SOURCE_FIELD));
			java.util.Vector<Object> valueVec = controlDoc.getItemValue(controlMap.get(Controls.SYNC_TARGET_FIELD));
			int i = 0;
			for (Object key : keyVec) {
				Formula Fkey = new Formula();
				Fkey.setExpression((String) key);
				syncMap.put(Fkey, (String) valueVec.get(i));
				i++;
			}
		} else if (controlDoc.hasItem(Controls.SYNC_SOURCE_FIELD.toString()) && controlDoc.hasItem(Controls.SYNC_TARGET_FIELD.toString())) {
			java.util.Vector<Object> keyVec = controlDoc.getItemValue(Controls.SYNC_SOURCE_FIELD.toString());
			java.util.Vector<Object> valueVec = controlDoc.getItemValue(Controls.SYNC_TARGET_FIELD.toString());
			int i = 0;
			for (Object key : keyVec) {
				Formula Fkey = new Formula();
				Fkey.setExpression((String) key);
				syncMap.put(Fkey, (String) valueVec.get(i));
				i++;
			}
		} else {
			// TODO some default sync mappping, perhaps dynamic
		}
		setSyncMap(syncMap);
	}

	/**
	 * Instantiates a new document sync helper.
	 * 
	 * @param strategy
	 *            the strategy
	 * @param syncMap
	 *            the sync map
	 * @param args
	 *            the args, up to four, each in order is:
	 *            <ol>
	 *            <li>target server</li>
	 *            <li>target filepath</li>
	 *            <li>target lookup view</li>
	 *            <li>formula to apply to each source document to get key for target documents. E.g. "ContactID" = use ContactID field of
	 *            source document as key value to apply to target lookup view to get the collection to update
	 *            </ol>
	 */
	public DocumentSyncHelper(final Strategy strategy, final Map<Object, String> syncMap, final String... args) {
		setStrategy(strategy);
		setSyncMap(syncMap);
		if (args.length >= 1) {
			setTargetServer(args[0]);
		}
		if (args.length >= 2) {
			setTargetFilepath(args[1]);
		}
		if (args.length >= 3) {
			setTargetLookupView(args[2]);
		}
		if (args.length >= 4) {
			setSourceKeyFormula(args[3]);
		}

	}

	public void processSince(final Database sourceDb, final Date sinceDate) {
		DateTime dt = sourceDb.getAncestorSession().createDateTime(sinceDate);
		DocumentCollection sourceCollection = sourceDb.getModifiedDocuments(dt);
		process(sourceCollection);
	}

	/**
	 * Process.
	 * 
	 * @param coll
	 *            the coll
	 */
	public void process(final DocumentCollection coll) {
		// TODO Check to make sure properties are all set up before running
		Session session = coll.getAncestorSession();
		Database targetDb = session.getDatabase(getTargetServer(), getTargetFilepath());
		View targetView = targetDb.getView(getTargetLookupView());
		Strategy strategy = getStrategy();
		DatabaseTransaction txn = null;
		if (getTransactionRule() == TransactionRule.COMMIT_AT_END) {
			txn = targetDb.startTransaction();
		}
		for (Document source : coll) {
			if (getTransactionRule() == TransactionRule.COMMIT_EVERY_SOURCE) {
				txn = targetDb.startTransaction();
			}
			DateTime sourceLastMod = source.getLastModified();
			// Object lookupKey = Factory.wrappedEvaluate(session, getSourceKeyFormula(), source);
			Object lookupKey = getSourceKeyFormula().getValue(source);
			DocumentCollection targetColl = targetView.getAllDocumentsByKey(lookupKey, true);
			for (Document target : targetColl) {
				// boolean targetDirty = false;
				for (Map.Entry<Formula, String> entry : getSyncMap().entrySet()) {
					String targetItemName = entry.getValue();
					java.util.Vector<?> sourceValue = entry.getKey().getValue(source);
					// Factory.wrappedEvaluate(session, entry.getKey(), source);
					if (strategy == Strategy.CREATE_AND_REPLACE) {
						target.replaceItemValue(targetItemName, sourceValue);
						// targetDirty = true;
					} else {
						Item targetItem = target.getFirstItem(targetItemName);
						if (strategy == Strategy.REPLACE_IF_NEWER) {
							DateTime itemLastMod = targetItem.getLastModified();
							if (sourceLastMod.isAfter(itemLastMod)) {
								targetItem.setValues(sourceValue);
								// targetDirty = true;
							}
						} else if (strategy == Strategy.REPLACE_ONLY) {
							if (targetItem != null) {
								targetItem.setValues(sourceValue);
								// targetDirty = true;
							}
						}
					}
				}
				if (getTransactionRule() == TransactionRule.NO_TRANSACTION || txn == null) {
					target.save();
				}
			}
			if (getTransactionRule() == TransactionRule.COMMIT_EVERY_SOURCE && txn != null) {
				txn.commit();
				txn = null;
			}
		}
		if (getTransactionRule() == TransactionRule.COMMIT_AT_END && txn != null) {
			txn.commit();
			txn = null;
		}
	}

	public void setTargetView(final org.openntf.domino.View view) {
		setTargetLookupView(view.getName());
		setTargetDatabase(view.getAncestorDatabase());
	}

	public void setTargetDatabase(final Database db) {
		setTargetServer(db.getServer());
		setTargetFilepath(db.getFilePath());
	}

	public void setTargetDatabase(final Database db, final String viewName) {
		setTargetServer(db.getServer());
		setTargetFilepath(db.getFilePath());
		setTargetLookupView(viewName);
	}

	/**
	 * Gets the target server.
	 * 
	 * @return the target server
	 */
	public String getTargetServer() {
		return targetServer_;
	}

	/**
	 * Sets the target server.
	 * 
	 * @param targetServer
	 *            the new target server
	 */
	public void setTargetServer(final String targetServer) {
		targetServer_ = targetServer;
	}

	/**
	 * Gets the target filepath.
	 * 
	 * @return the target filepath
	 */
	public String getTargetFilepath() {
		return targetFilepath_;
	}

	/**
	 * Sets the target filepath.
	 * 
	 * @param targetFilepath
	 *            the new target filepath
	 */
	public void setTargetFilepath(final String targetFilepath) {
		targetFilepath_ = targetFilepath;
	}

	/**
	 * Gets the target lookup view.
	 * 
	 * @return the target lookup view
	 */
	public String getTargetLookupView() {
		return targetLookupView_;
	}

	/**
	 * Sets the target lookup view.
	 * 
	 * @param targetLookupView
	 *            the new target lookup view
	 */
	public void setTargetLookupView(final String targetLookupView) {
		targetLookupView_ = targetLookupView;
	}

	/**
	 * Gets the source key formula.
	 * 
	 * @return the source key formula
	 */
	public Formula getSourceKeyFormula() {
		if (sourceKeyFormula_ == null) {
			sourceKeyFormula_ = new Formula();
		}
		return sourceKeyFormula_;
	}

	/**
	 * Sets the source key formula.
	 * 
	 * @param sourceKeyFormula
	 *            the new source key formula
	 */
	public void setSourceKeyFormula(final String sourceKeyFormula) {
		if (sourceKeyFormula_ == null) {
			sourceKeyFormula_ = new Formula();
		}
		sourceKeyFormula_.setExpression(sourceKeyFormula);
	}

	/**
	 * Gets the strategy.
	 * 
	 * @return the strategy
	 */
	public Strategy getStrategy() {
		return strategy_;
	}

	public TransactionRule getTransactionRule() {
		if (transactionRule_ == null) {
			transactionRule_ = TransactionRule.NO_TRANSACTION;
		}
		return transactionRule_;
	}

	/**
	 * Sets the strategy.
	 * 
	 * @param strategy
	 *            the new strategy
	 */
	public void setStrategy(final Strategy strategy) {
		strategy_ = strategy;
	}

	public void setTransactionRule(final TransactionRule rule) {
		transactionRule_ = rule;
	}

	/**
	 * Gets the sync map.
	 * 
	 * @return the sync map
	 */
	public Map<Formula, String> getSyncMap() {
		return syncMap_;
	}

	/**
	 * Sets the sync map.
	 * 
	 * @param syncMap
	 *            the sync map
	 */
	public void setSyncMap(final Map<java.lang.Object, String> syncMap) {
		Map<Formula, String> formulaMap = new HashMap<Formula, String>();
		for (Map.Entry<Object, String> entry : syncMap.entrySet()) {
			if (entry.getKey() instanceof Formula) {
				formulaMap.put((Formula) entry.getKey(), entry.getValue());
			}
			Formula Fkey = new Formula();
			Fkey.setExpression(String.valueOf(entry.getKey()));
			formulaMap.put(Fkey, entry.getValue());
		}
		syncMap_ = formulaMap;
	}

}
