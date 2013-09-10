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

import java.util.Vector;

/**
 * Easy to use QuickSort set of classes for sorting data.
 * <p>
 * The QuickSort class is an abstract class that requires some of its methods to be
 * overridden to give actual access to the collection. 
 * </p>
 * @ibm-api
 */
public abstract class QuickSort {

	/**
	 * Sort the entire collection.
	 * @ibm-api
	 */
    public void sort() {
        qSortHelp( 0, getCount() );
    }
    
	/**
	 * Sort a subpart of the collection.
	 * @ibm-api
	 */
    public void sort( int first, int length ) {
        qSortHelp( first, length );
    }

	/**
	 * Returns the number of elements in the collection.
	 * @ibm-api
	 */
    public abstract int getCount();
    
	/**
	 * Compare 2 elements in the collection, based on their index.
	 * @ibm-api
	 */
    public abstract int compare(int idx1, int idx2);
    
	/**
	 * Exchange 2 elements in the collection, based on their index.
	 * @ibm-api
	 */
    public abstract void exchange(int idx1, int idx2);

    private final void qSortHelp( int pivotP, int nElem ) {
        int    leftP, rightP, pivotEnd, pivotTemp, leftTemp;
        int    lNum;
        int    retval;

        tailRecursion:
        do { // Tail recursion

            // Shell sort on smallest array
            if( nElem<=7 ) {
                // Specific case of 2 or less items
                if(nElem == 2) {
                    if(compare(pivotP, rightP = 1 + pivotP) > 0) {
                        exchange (pivotP, rightP);
                    }
                    return;
                }

                // Shell sort
                for( int i=0; i<nElem; i++ ) {
                    for( int j=i+1; j<nElem; j++ ) {
                        if( compare(i+pivotP, j+pivotP)>0 ) {
                            exchange(i+pivotP,j+pivotP);
                        }
                    }
                }
                return;
            }

            rightP = (nElem - 1) + pivotP;
            leftP  = (nElem >> 1) + pivotP;

            // sort the pivot, left, and right elements for "median of 3"
            if(compare(leftP, rightP) > 0) {
                exchange (leftP, rightP);
            }
            if(compare(leftP, pivotP) > 0) {
                exchange (leftP, pivotP);
            } else {
                if(compare(pivotP, rightP) > 0) {
                    exchange (pivotP, rightP);
                }
            }

            if(nElem == 3) {
                exchange (pivotP, leftP);
                return;
            }

            // now for the classic Hoare algorithm
            leftP = pivotEnd = pivotP + 1;

            mainloop:
            do {
                while((retval = compare(leftP, pivotP)) <= 0) {
                    if(retval == 0) {
                        exchange(leftP, pivotEnd);
                        pivotEnd += 1;
                    }
                    if( leftP<rightP )
                        leftP += 1;
                    else
                        break mainloop;
                }

                while(leftP<rightP) {
                    if((retval = compare(pivotP, rightP)) < 0)
                        rightP -= 1;
                    else {
                        exchange(leftP, rightP);
                        if(retval != 0) {
                            leftP += 1;
                            rightP -= 1;
                        }
                        break;
                    }
                }
            } while(leftP<rightP);

            if(compare(leftP, pivotP) <= 0) {
                leftP = leftP + 1;
            }
            leftTemp = leftP - 1;

            pivotTemp = pivotP;

            while((pivotTemp < pivotEnd) && (leftTemp >= pivotEnd)) {
                exchange(pivotTemp, leftTemp);
                pivotTemp += 1;
                leftTemp -= 1;
            }

            lNum = leftP - pivotEnd;
            nElem = (nElem + pivotP) - leftP;

            // Sort smaller partition first to reduce stack usage
            if(nElem < lNum) {
                qSortHelp(leftP, nElem);
                nElem = lNum;
            } else {
                qSortHelp(pivotP, lNum);
                pivotP = leftP;
            }
        } while(true); //goto tailRecursion;
    }

    /**
     * Quicksort class used to sort an array of integers.
     * @ibm-api
     */
    public static class IntArray extends QuickSort {
        public IntArray( int[] array, int first, int count ) {
            this.array = array;
            this.first = first;
            this.count = count;
        }
        public IntArray( int[] array ) {
            this( array, 0, array.length );
        }
        public int getCount() {
            return count;
        }
        public int compare(int idx1, int idx2) {
            return array[first+idx1]-array[first+idx2];
        }
        public void exchange(int idx1, int idx2) {
            int tmp = array[first+idx1];
            array[first+idx1] = array[first+idx2];
            array[first+idx2] = tmp;
        }
        private int[] array;
        private int first;
        private int count;
    }

    /**
     * Quicksort class used to sort an array of objects.
     * @ibm-api
     */
    public static class ObjectArray extends QuickSort {
        public ObjectArray( Object[] array, int first, int count ) {
            this.array = array;
            this.first = first;
            this.count = count;
        }
        public ObjectArray( Object[] array ) {
            this( array, 0, array!=null ? array.length : 0 );
        }
        public int getCount() {
            return count;
        }
        public void exchange(int idx1, int idx2) {
            Object tmp = array[first+idx1];
            array[first+idx1] = array[first+idx2];
            array[first+idx2] = tmp;
        }
        public Object getObject(int index) {
            return array[index];
        }
        public int compare(int idx1, int idx2) {
            return compare( array[idx1], array[idx2] );
        }
        public int compare(Object o1, Object o2) {
            if( o1==null && o2==null ) {
                return 0;
            }
            if( o2==null ) {
                return 1;
            }
            if( o1==null ) {
                return -1;
            }
            return o1.toString().compareTo(o2.toString());
        }
        private Object[] array;
        private int first;
        private int count;
    }

    /**
     * Quicksort class used to sort an array of Strings.
     * @ibm-api
     */
    public static class StringArray extends QuickSort {
        public StringArray( String[] array, int first, int count, boolean ignoreCase ) {
            this.array = array;
            this.first = first;
            this.count = count;
            this.ignoreCase = ignoreCase;
        }
        public StringArray( String[] array, boolean ignoreCase ) {
            this( array, 0, array!=null ? array.length : 0, ignoreCase );
        }
        public StringArray( String[] array) {
            this( array, false );
        }
        public int getCount() {
            return count;
        }
        public int compare(int idx1, int idx2) {
            return compare( array[first+idx1], array[first+idx2] );
        }
        public int compare(String s1, String s2) {
            if( s1==null && s2==null ) {
                return 0;
            }
            if( s2==null ) {
                return 1;
            }
            if( s1==null ) {
                return -1;
            }
            if( ignoreCase ) {
                return s1.compareToIgnoreCase(s2);
            } else {
                return s1.compareTo(s2);
            }
        }
        public void exchange(int idx1, int idx2) {
            String tmp = array[first+idx1];
            array[first+idx1] = array[first+idx2];
            array[first+idx2] = tmp;
        }
        private String[] array;
        private int first;
        private int count;
        private boolean ignoreCase;
    }

    /**
     * Quicksort class used to sort a Java List.
     * @ibm-api
     */
    public static class JavaList extends QuickSort {
        public JavaList( java.util.List list ) {
            this.list = list;
        }
        public int getCount() {
            return list.size();
        }
        public int compare(int idx1, int idx2) {
            Object o1 = list.get(idx1);
            Object o2 = list.get(idx2);
            return compare( o1, o2 );
        }
        public int compare(Object o1, Object o2) {
            String s1 = o1!=null ? o1.toString() : ""; 
            String s2 = o2!=null ? o2.toString() : ""; 
            return s1.compareToIgnoreCase(s2.toString());
        }
        public void exchange(int idx1, int idx2) {
            Object tmp = list.get(idx1);
            list.set(idx1,list.get(idx2));
            list.set(idx2,tmp);
        }
        private java.util.List list;
    }

    /**
     * Quicksort class used to sort a Java Vector.
     * @ibm-api
     */
    public static class JavaVector extends QuickSort {
        public JavaVector( Vector vector ) {
            this.vector = vector;
        }
        public int getCount() {
            return vector.size();
        }
        public int compare(int idx1, int idx2) {
            Object o1 = vector.elementAt(idx1);
            Object o2 = vector.elementAt(idx2);
            return compare( o1, o2 );
        }
        public int compare(Object o1, Object o2) {
            return o1.toString().compareToIgnoreCase(o2.toString());
        }
        public void exchange(int idx1, int idx2) {
            Object tmp = vector.elementAt(idx1);
            vector.setElementAt(vector.elementAt(idx2),idx1);
            vector.setElementAt(tmp,idx2);
        }
        private Vector vector;
    }

/*
    public static void main( String[] args ) {
        Debug.showGUI(true);

        checkString();
    }
    private static void checkInt() {
        int LOOP = 3;
        int COUNT = 500000;
        int[] items = new int[COUNT];
        for( int l=0; l<LOOP; l++ ) {
            for( int i=0; i<COUNT; i++ ) {
                items[i] = (int)(rnd.nextDouble()*50000);
            }
            TDiag.trace( "Before sorting={0}", items ); // $NON-NLS-1$
            long begin = System.currentTimeMillis();
            (new TQuickSort.IntArray(items)).sort();
            long end = System.currentTimeMillis();
            TDiag.trace( "Sorting {0} items in {1}ms ({2})", TString.toString(COUNT), TString.toString(end-begin), items ); // $NON-NLS-1$
            checkSorted(items);
        }
    }
    private static void checkSorted( int[] items ) {
        for( int i=0; i<items.length-1; i++ ) {
            if( items[i]>items[i+1] ) {
                TDiag.trace( "Invalid sort at {0} for array {1}", TString.toString(i), TString.toString(items) ); // $NON-NLS-1$
                return;
            }
        }
    }
    private static void checkString() {
        int LOOP = 3;
        int COUNT = 10000;
        String[] items = new String[COUNT];
        for( int l=0; l<LOOP; l++ ) {
            TStringBuffer buffer = new TStringBuffer();
            for( int i=0; i<COUNT; i++ ) {
                // The string is between 10 & 30 chrs
                int length = (int)(rnd.nextDouble()*20)+10;
                buffer.clear();
                for( int j=0; j<length; j++ ) {
                    buffer.append( chars.charAt((int)(rnd.nextDouble()*chars.length())) );
                }
                items[i] = buffer.toString();
            }
            TDiag.trace( "Before sorting={0}", items ); // $NON-NLS-1$
            long begin = System.currentTimeMillis();
            (new TQuickSort.StringArray(items,false)).sort();
            long end = System.currentTimeMillis();
            TDiag.trace( "Sorting {0} items in {1}ms ({2})", TString.toString(COUNT), TString.toString(end-begin), items ); // $NON-NLS-1$
            checkSorted(items);
        }
    }
    private static void checkSorted( String[] items ) {
        for( int i=0; i<items.length-1; i++ ) {
            if( items[i].compareTo(items[i+1])>0 ) {
                TDiag.trace( "Invalid sort at {0} for array {1}", TString.toString(i), TString.toString(items) ); // $NON-NLS-1$
                return;
            }
        }
    }
    private static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // $NON-NLS-1$
    private static Random rnd = new Random(System.currentTimeMillis());
*/
}
