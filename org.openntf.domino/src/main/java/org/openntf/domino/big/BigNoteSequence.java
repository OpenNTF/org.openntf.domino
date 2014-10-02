package org.openntf.domino.big;

import java.io.Externalizable;
import java.util.List;

/**
 * @author Nathan T. Freeman The purpose of a BigNoteSequence is to represent Note references across multiple databases or even multiple
 *         servers. Unlike DocumentCollections or NoteCollections, NoteSequences are always ordered
 * 
 */
public interface BigNoteSequence extends Externalizable, List<NoteMeta> {

}
