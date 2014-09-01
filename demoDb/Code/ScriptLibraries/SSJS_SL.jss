function transactionTest(successOrFail:boolean) {
	var sb:java.lang.StringBuilder = new java.lang.StringBuilder();
	var txn:org.openntf.domino.transactions.DatabaseTransaction.DatabaseTransaction = database.startTransaction();
	try {
		var selVal = viewScope.get("selectedStateSSJS");
		var toggle = true;
		var count = 0;
		if ("".equals(selVal)) {
			viewScope.put("SSJSTest", "First select a value");
			return;
		}
		sb.append("Starting update with " + selVal);
		var stateView = database.getView("allStates");
		var state = stateView.getDocumentByKey(selVal, true);
		state.replaceItemValue("txnTest", new Date());
		sb.append("...Updated State pending committal, value is " + state.get("txnTest").toString());
		var contacts = database.getView("AllContactsByState");
		var dc = contacts.getAllDocumentsByKey(selVal, true);
		for (doc in dc) {
			if (toggle) {
				doc.replaceItemValue("txnTest", new Date());
				count += 1;
			}
			toggle = !toggle;
		}
		sb.append("...Updated " + @Text(count) + " Contacts pending committal.");
		if (successOrFail) {
			txn.commit();
			sb.append("...Committed");
			viewScope.put("SSJSTest", sb.toString());
		} else {
			throw new Exception("Now roll back");
		}
	} catch (e) {
		sb.append("Rolling back");
		txn.rollback();
		viewScope.put("SSJSTest", sb.toString());
	}
}