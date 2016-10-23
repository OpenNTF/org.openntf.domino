/**
 * 
 */
package org.openntf.domino.schema;

import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.schema.impl.DocumentDefinition;
import org.openntf.domino.schema.impl.ItemDefinition;

/**
 * @author Nathan T. Freeman
 * 
 */
public interface IDatabaseSchema {

	public Map<String, DocumentDefinition> getDocumentDefinitions();

	public Map<String, ItemDefinition> getItemDefinitions();

	public IItemDefinition createItemDefinition(String itemKey, Class<?> type);

	public Map<Class<? extends IDominoType>, IDominoType> getTypeDefinitions();

	public IDominoType getTypeDefinition(final Class<? extends IDominoType> type);

	public Document createDocument(final Database db, final String doctype);

	public boolean validateDocument(final Document doc);

}
