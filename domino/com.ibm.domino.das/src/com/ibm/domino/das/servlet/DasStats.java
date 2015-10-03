/*
 * © Copyright IBM Corp. 2014
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

package com.ibm.domino.das.servlet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Encapsulates a map of the statistics for all DAS services
 * on this server.
 */
public class DasStats {

    private static DasStats s_stats = new DasStats();
    private Map<String, Object> statsMap = new ConcurrentHashMap<String, Object>();
    
    /**
     * Mutable version of the Integer class.
     * 
     * <p>The DasStats class uses MutableInteger to store int values to avoid
     * creating a new instance of Integer everytime the stat is incremented.
     */
    public class MutableInteger {
        
        private int _value = 0;
        
        public MutableInteger(int value) {
            _value = value;
        }
        
        /**
         * @return the value
         */
        public int getValue() {
            return _value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(int value) {
            _value = value;
        }
    }
    
    /**
     * Mutable version of Double class.
     * 
     * <p>The DasStats class uses MutableDouble to store double values to avoid
     * creating a new instance of Double everytime the stat is changed.
     */
    public class MutableDouble {
        
        private double _value = 0;
        
        public MutableDouble(double value) {
            _value = value;
        }
        
        /**
         * @return the value
         */
        public double getValue() {
            return _value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(double value) {
            _value = value;
        }
    }
    
    private DasStats() {
        // Private because this is a singleton
    }
    
    public static DasStats get() {
        return s_stats;
    }
    
    /**
     * Creates a new integer stat.  If the stat already exists, this
     * method replaces it.
     * 
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        Object current = statsMap.get(key);
        if ( current instanceof MutableInteger ) {
            // Synchronize because an assigment isn't
            // guaranteed to be atomic
            synchronized(current) {
                ((MutableInteger)current).setValue(value);
            }
        }
        else {
            statsMap.put(key, new MutableInteger(value));
        }
    }
    
    /**
     * Adds to an existing integer stat.  If the stat doesn't exist,
     * this method creates it.
     * 
     * @param key
     * @param value
     */
    public int addInteger(String key, int value) {
        
        int newValue = value;
        
        // TODO: Handle the race condition where two threads
        // are creating the same new stat at the same time.
        
        Object current = statsMap.get(key);
        if ( current instanceof MutableInteger ) {
            synchronized(current) {
                newValue = ((MutableInteger)current).getValue() + value;
                ((MutableInteger)current).setValue(newValue);
            }
        }
        else {
            setInteger(key, value);
        }
        
        return newValue;
    }
    
    /**
     * Reads an integer stat.
     * 
     * @param key
     * @return
     */
    public int getInteger(String key) {
        int value = 0;
        Object current = statsMap.get(key);
        if ( current instanceof MutableInteger ) {
            value = ((MutableInteger)current).getValue();
        }
        
        return value;
    }
    
    /**
     * Creates a new text stat.
     * 
     * @param key
     * @param value
     */
    public void setText(String key, String value) {
        
    }
    
    /**
     * Creates a new number stat.
     * 
     * @param key
     * @param value
     */
    public void setNumber(String key, double value) {
        Object current = statsMap.get(key);
        if ( current instanceof MutableDouble ) {
            // Synchronize because a double assigment isn't
            // guaranteed to be atomic
            synchronized(current) {
                ((MutableDouble)current).setValue(value);
            }
        }
        else {
            statsMap.put(key, new MutableDouble(value));
        }
    }
    
    /**
     * Adds to an existing number stat.  If the stat doesn't exist,
     * this method creates it.
     * 
     * @param key
     * @param value
     */
    public double addNumber(String key, double value) {
        
        double newValue = value;
        
        // TODO: Handle the race condition where two threads
        // are creating the same new stat at the same time.
        
        Object current = statsMap.get(key);
        if ( current instanceof MutableDouble ) {
            synchronized(current) {
                newValue = ((MutableDouble)current).getValue() + value;
                ((MutableDouble)current).setValue(newValue);
            }
        }
        else {
            setNumber(key, value);
        }
        
        return newValue;
    }
    
    /**
     * Reads a number stat.
     * 
     * @param key
     * @return
     */
    public double getNumber(String key) {
        double value = 0;
        Object current = statsMap.get(key);
        if ( current instanceof MutableDouble ) {
            value = ((MutableDouble)current).getValue();
        }
        
        return value;
    }
    
    /**
     * Gets a set containing all the stats.
     * 
     * @return
     */
    public Set<Map.Entry<String, Object>> getEntries() {
        return statsMap.entrySet();
    }
}
