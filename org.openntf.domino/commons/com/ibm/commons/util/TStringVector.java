/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;

import java.util.ArrayList;

/**
 * This class is a vector of string.
 * @ibm-non-published
 */
public class TStringVector extends ArrayList {

    /**
     *
     */
    public TStringVector( int initialCapacity ) {
        super(initialCapacity);
    }

    /**
     *
     */
    public TStringVector(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    /**
     *
     */
    public TStringVector() {
    }

    /**
     *
     */
//    public void setSize(int newSize, boolean initialize) {
//        if( initialize ) {
//            if( newSize>size ) {
//                ensureCapacity(newSize);
//                for(int i=size ; i<newSize; i++) {
//                    data[i] = null;
//                }
//            } else {
//                for(int i=newSize ; i<size; i++) {
//                    data[i] = null;
//                }
//            }
//        } else {
//            if( data==null || data.length<newSize ) {
//                data = new String[ TMath.max(delta,newSize) ];
//            }
//        }
//        size = newSize;
//    }
//    public final void setSize(int newSize) {
//        setSize( newSize, true );
//    }

    /**
     * Check if the string vector is case insensitive.
     * @return true if case insensitive
     */
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    /**
     * Set the case insensitive mode.
     * @param caseInsensitive
     */
    public void setCaseInsensitive( boolean caseInsensitive ) {
        this.caseInsensitive = caseInsensitive;
    }

    /**
     * Get an indexed string.
     * @param the string index
     * @return the desired string
     */
    public final String elementAt( int index ) {
        return (String)get(index);
    }

    /**
     * Get an indexed string.
     * @param the string index
     * @return the desired string
     */
    public final String getString( int index ) {
        return (String)get(index);
    }

    /**
     * Check if the string is contained in the vector.
     * The string are compared by value and not by the object pointer.
     * @return true if the string exists within the vector
     */
    public final boolean contains( Object item ) {
        return indexOf(item)>=0;
    }

    /**
     * Get the index of a specific string.
     * The string are compared by value and not by the object pointer.
     * @param item the string to look for
     * @return the index of that string (-1 if it does'nt esist)
     */
    public final int indexOf( Object item ) {
        return indexOf( item, 0 );
    }

    /**
     * Get the index of a specific string begining at a specific index.
     * The string are compared by value and not by the object pointer.
     * @param item the string to look for
     * @param index the index where to start the search
     * @return the index of that string (-1 if it does'nt esist)
     */
    public final int indexOf( Object item, int index ) {
        int size = size();
        String s = (String)item;
        if( caseInsensitive ) {
            for( int i=index; i<size; i++ ) {
                if( StringUtil.equalsIgnoreCase(getString(i),s) ) {
                    return i;
                }
            }
            return -1;
        } else {
            for( int i=index; i<size; i++ ) {
                if( StringUtil.equals(getString(i),s) ) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * Set the value of a specific string.
     * @param string the string value
     * @param index the index of the string to modify
     */
    public final void setElementAt( String item, int index ) {
        set( index, item ) ;
    }

    /**
     * Add a new string to the vector.
     * @param item the new string to add
     */
    public final void add( String item ) {
        super.add(item);
    }
    public final void addElement( String item ) {
        super.add(item);
    }

    public final void addUnique( String item ) {
        if( !contains(item) ) {
            super.add(item);
        }
    }
    public final void addUniqueElement( String item ) {
        addUnique(item);
    }

    /**
     * Add a list of elements.
     * @param items the new strings to add
     */
    public final void add( String[] items ) {
        if(items!=null) {
            for( int i=0; i<items.length; i++ ) {
                add(items[i]);
            }
        }
    }
    public final void addElements( String[] items ) {
        add(items);
    }

    public final void addUnique( String[] items ) {
        if(items!=null) {
            for( int i=0; i<items.length; i++ ) {
                addUniqueElement(items[i]);
            }
        }
    }
    public final void addUniqueElements( String[] items ) {
        addUnique(items);
    }

    /**
     *
     */
    public final void insertAt( String item, int index ) {
        super.add(index,item);
    }
    public final void insertElementAt( String item, int index ) {
        super.add(index,item);
    }

    /**
     *
     */
    public final void removeAll() {
        clear();
    }
    public final void removeAllElements() {
        clear();
    }

    /**
     *
     */
    public final void remove( String item ) {
        int idx = indexOf(item); // Take care of the case sensitivity
        if( idx>=0 ) {
            super.remove(idx);
        }
    }
    public final void removeElement( String item ) {
        remove(item);
    }

    /**
     *
     */
    public final void removeAt( int index ) {
        super.remove(index);
    }
    public final void removeElementAt( int index ) {
        super.remove(index);
    }

    /**
     *
     */
    public final void removeItems( int index, int length ) {
        if( length>=0 && index>=0 && index+length<=size() ) {
            removeRange(index,index+length);
        }
    }

    /**
     *
     */
    public final synchronized void sort() {
        QuickSort qSort = new QuickSort.JavaList( this ) {
            public int compare(Object o1, Object o2) {
                return caseInsensitive ? o1.toString().compareToIgnoreCase(o2.toString())
                                       : o1.toString().compareTo(o2.toString());
            }
        };
        qSort.sort();
    }

    public final synchronized void sortIgnoreCase() {
        QuickSort qSort = new QuickSort.JavaList( this ) {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        };
        qSort.sort();
    }

    /**
     *
     */
    public String getAsString() {
        int size = size();
        if(size>0) {
            StringBuffer buffer = new StringBuffer();

            for( int i=0; i<size; i++ ) {
                if( i>0 ) {
                    buffer.append( ", " ); //$NON-NLS-1$
                }
                buffer.append( '\"' );
                String s = getString(i);
                for( int j=0; j<s.length(); j++ ) {
                    char c = s.charAt(j);
                    switch(c) {
                        case '&':   buffer.append( "&amp;" );   break; //$NON-NLS-1$
                        case '"':   buffer.append( "&quot;" );  break; //$NON-NLS-1$
                        case '<':   buffer.append( "&lt;" );    break; //$NON-NLS-1$
                        case '>':   buffer.append( "&gt;" );    break; //$NON-NLS-1$
                        default:    buffer.append( c );         break;
                    }
                }
                buffer.append( '\"' );
            }

            return buffer.toString();

        }
        return null;
    }

    public String toString() {
        return getAsString();
    }

    public Object[] toArray() {
        return super.toArray( new String[size()] );
    }

    public String[] getElementArray() {
        return (String[])toArray();
    }


    /*
     *
     */
    private boolean     caseInsensitive;
}
