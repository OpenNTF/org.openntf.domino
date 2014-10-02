/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.xml.internal.stream;

import java.util.Map;

import javolution.text.CharArray;
import javolution.util.FastTable;
import javolution.util.function.Function;
import javolution.xml.stream.XMLStreamException;

/**
 * Defines entities while parsing.
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.0, June 16, 2006
 */
public final class EntitiesImpl {

    /**
     * Holds maximum length.
     */
    private int _maxLength = 1;

    /**
     * Holds the user defined entities mapping.
     */
    private Map<String, String> _entitiesMapping;

    /**
     * Default constructor.
     */
    EntitiesImpl() {}

    /**
     * Returns the length of the largest entity defined (default {@code 1}).
     */
    public int getMaxLength() {
        return _maxLength;
    }

    /**
     * Replaces the entity at the specified position. 
     * The five predefined XML entities "&#38;lt;", "&#38;gt;", "&#38;apos;",
     * "&#38;quot;", "&#38;amp;" as well as character refererences 
     * (decimal or hexadecimal) are always recognized.
     * 
     * @param buffer the data buffer.
     * @param start the index of entity first character (index of '&')
     * @return the length of the replacement entity (including ';') 
     * @throws XMLStreamException if the entity is not recognized.
     */
    public int replaceEntity(char[] buffer, int start, int length)
            throws XMLStreamException {

        // Checks for character references.
        if (buffer[start + 1] == '#') {
            char c = buffer[start + 2];
            int base = (c == 'x') ? 16 : 10;
            int i = (c == 'x') ? 3 : 2;
            int charValue = 0;
            for (; i < length - 1; i++) {
                c = buffer[start + i];
                charValue *= base;
                charValue += (c <= '9') ? (c - '0') : (c <= 'Z') ? c - 'A' + 10
                        : c - 'a' + 10;
            }
            buffer[start] = (char) charValue;
            return 1;
        }

        if ((buffer[start + 1] == 'l') && (buffer[start + 2] == 't')
                && (buffer[start + 3] == ';')) {
            buffer[start] = '<';
            return 1;
        }

        if ((buffer[start + 1] == 'g') && (buffer[start + 2] == 't')
                && (buffer[start + 3] == ';')) {
            buffer[start] = '>';
            return 1;
        }

        if ((buffer[start + 1] == 'a') && (buffer[start + 2] == 'p')
                && (buffer[start + 3] == 'o') && (buffer[start + 4] == 's')
                && (buffer[start + 5] == ';')) {
            buffer[start] = '\'';
            return 1;
        }

        if ((buffer[start + 1] == 'q') && (buffer[start + 2] == 'u')
                && (buffer[start + 3] == 'o') && (buffer[start + 4] == 't')
                && (buffer[start + 5] == ';')) {
            buffer[start] = '"';
            return 1;
        }

        if ((buffer[start + 1] == 'a') && (buffer[start + 2] == 'm')
                && (buffer[start + 3] == 'p') && (buffer[start + 4] == ';')) {
            buffer[start] = '&';
            return 1;
        }

        // Searches user defined entities.
        _tmp.setArray(buffer, start + 1, length - 2);
        CharSequence replacementText = (_entitiesMapping != null) ?  _entitiesMapping
                .get(_tmp) : null;
        if (replacementText == null)
            throw new XMLStreamException("Entity " + _tmp + " not recognized");
        int replacementTextLength = replacementText.length();
        for (int i = 0; i < replacementTextLength; i++) {
            buffer[start + i] = replacementText.charAt(i);
        }
        return replacementTextLength;
    }

    private CharArray _tmp = new CharArray();

    /**
     * Sets the current custom entity mapping. For example: {@code 
     * new FastMap().put("copy", "©")} to define the copyright entity.
     */
    public void setEntitiesMapping(Map<String, String> entityToReplacementText) {
        FastTable<String> values = new FastTable<String>();
        values.addAll(entityToReplacementText.values());
        _maxLength = values.mapped(new Function<CharSequence, Integer>() {

            @Override
            public Integer apply(CharSequence csq) {
                return csq.length();
            }}).max();
        
          _entitiesMapping = entityToReplacementText;
    }

    /**
     * Returns the custom entity mapping or {@code null} if none.
     */
    public Map<String, String> getEntitiesMapping() {
        return _entitiesMapping;
    }

    // Implements Reusable.
    public void reset() {
        _maxLength = 1;
        _entitiesMapping = null;
    }

}
