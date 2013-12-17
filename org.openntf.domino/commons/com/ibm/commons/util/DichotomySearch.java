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


/**
 * Search using the dichotomy algorithm.
 * should use Java Array Binary Search
 * @ibm-not-published
 */
public abstract class DichotomySearch {

    protected abstract int getCount();
    protected abstract int compareIndexWithValue(int idx);

    /**
     * @ibm-api
     */
    public int search(boolean exactSearch) {
        int low = 0;
        int high = getCount()-1;
        while (low <= high) {
            int mid =(low + high)/2;
            int cmp = compareIndexWithValue(mid);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return exactSearch
                    ? -1
                    : (low<getCount() ? low : getCount());
    }

    /**
     * Abstract String search.
     * @ibm-api
     */
    public static abstract class StringSearch extends DichotomySearch {
        public StringSearch( boolean ignoreCase ) {
            this.ignoreCase = ignoreCase;
        }
        protected int compareIndexWithValue(int idx) {
            if( ignoreCase ) {
                return getString(idx).compareToIgnoreCase(value);
            } else {
                return getString(idx).compareTo(value);
            }
        }
        public int search(String value, boolean exactSearch) {
            this.value = value;
            return search(exactSearch);
        }
        public boolean contains(String value) {
            return search(value,true)>=0;
        }

        protected abstract String getString( int idx );
        private String value;
        private boolean ignoreCase;
    }

    /**
     * Search a String in an array of Strings.
     * @ibm-api
     */
    public static class StringArraySearch extends StringSearch {
        public StringArraySearch( String[] array, boolean ignoreCase ) {
            super(ignoreCase);
            this.array = array;
        }
        public int getCount() {
            return array.length;
        }
        protected String getString( int idx ) {
            return array[idx];
        }
        public String[] getArray() {
        	return array;
        }
        private String array[];
    }
}
