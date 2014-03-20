/**
 * © Copyright Foconis AG, 2014
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
package de.foconis.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ValueHolder.DataType;

/**
 * @author Roland Praml, Foconis AG
 * 
 */
public class ValueHolderTest {

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#ValueHolder()}.
	 */
	@Test
	public final void testValueHolder() {
		ValueHolder vh = new ValueHolder();
		assertEquals(DataType._UNSET, vh.dataType);
		assertEquals(0, vh.size);
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(java.lang.Object)}.
	 */
	@Test
	@Ignore
	public final void testValueOfObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(java.lang.RuntimeException)}.
	 */
	@Test
	@Ignore
	public final void testValueOfRuntimeException() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(int)}.
	 */
	@Test
	@Ignore
	public final void testValueOfInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(double)}.
	 */
	@Test
	public final void testValueOfDouble() {
		ValueHolder vh1 = ValueHolder.valueOf(3.1);
		assertEquals(3.1, vh1.getDouble(0), 0);
		assertEquals(3.1, vh1.getDouble(1), 0);
		assertEquals(3, vh1.getInt(0));
		assertEquals(3, vh1.getInt(1));
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(java.lang.String)}.
	 */
	@Test
	@Ignore
	public final void testValueOfString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(org.openntf.domino.DateTime)}.
	 */
	@Test
	@Ignore
	public final void testValueOfDateTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueOf(boolean)}.
	 */
	@Test
	public final void testValueOfBoolean() {
		FormulaContext ctx = new FormulaContext(null, null);
		ctx.useBooleans(true);

		ValueHolder vh1 = ValueHolder.valueOf(true);
		assertEquals(true, vh1.isTrue(ctx));
		assertEquals(Boolean.TRUE, vh1.getObject(0));

		ValueHolder vh2 = ValueHolder.valueOf(false);
		assertEquals(Boolean.FALSE, vh2.getObject(0));
		assertEquals(false, vh2.isTrue(ctx));

	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#valueDefault()}.
	 */
	@Test
	@Ignore
	public final void testValueDefault() {
		ValueHolder vh = ValueHolder.valueDefault();
		assertEquals("", vh.getString(0));
		assertEquals("", vh.getString(1));

	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#grow(int)}.
	 */
	@Test
	@Ignore
	public final void testGrow() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getError()}.
	 */
	@Test
	@Ignore
	public final void testGetError() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#get(int)}.
	 */
	@Test
	@Ignore
	public final void testGet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getObject(int)}.
	 */
	@Test
	@Ignore
	public final void testGetObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getString(int)}.
	 */
	@Test
	@Ignore
	public final void testGetString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getDateTime(int)}.
	 */
	@Test
	@Ignore
	public final void testGetDateTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getInt(int)}.
	 */
	@Test
	@Ignore
	public final void testGetInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#getDouble(int)}.
	 */
	@Test
	@Ignore
	public final void testGetDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#isTrue(org.openntf.domino.formula.FormulaContext)}.
	 */
	@Test
	@Ignore
	public final void testIsTrue() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#addAll(org.openntf.domino.formula.ValueHolder)}.
	 */
	@Test
	@Ignore
	public final void testAddAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#toString()}.
	 */
	@Test
	@Ignore
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(java.lang.String)}.
	 */
	@Test
	@Ignore
	public final void testAddString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(int)}.
	 */
	@Test
	public final void testAddInt() {
		ValueHolder vh = new ValueHolder();
		assertEquals(DataType._UNSET, vh.dataType);
		assertEquals(0, vh.size);

		vh.add(3); // Add 3
		assertEquals(DataType.INTEGER, vh.dataType);
		assertEquals(3, vh.getInt(0));
		assertEquals(3, vh.getInt(1));

		assertEquals(3.0D, vh.getDouble(0), 0);
		assertEquals(3.0D, vh.getDouble(1), 0);

		assertEquals(Integer.valueOf(3), vh.get(0));
		assertEquals(Integer.valueOf(3), vh.get(1));

		vh.add(4.0); // Add 4.0
		assertEquals(DataType.NUMBER, vh.dataType);
		vh.add(5); // Add 5

		assertEquals(DataType.NUMBER, vh.dataType);
		assertEquals(3, vh.size);

		assertEquals(3, vh.getInt(0));
		assertEquals(4, vh.getInt(1));
		assertEquals(5, vh.getInt(2));

		assertEquals(3.0D, vh.getDouble(0), 0);
		assertEquals(4.0D, vh.getDouble(1), 0);

		assertEquals(Integer.valueOf(3), vh.get(0));
		assertEquals(Double.valueOf(4.0D), vh.get(1));

	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(double)}.
	 */
	@Test
	public final void testAddDouble() {
		ValueHolder vh = new ValueHolder();
		assertEquals(DataType._UNSET, vh.dataType);
		assertEquals(0, vh.size);

		vh.add(3.0); // Add 3.0
		assertEquals(DataType.DOUBLE, vh.dataType);
		assertEquals(3, vh.getInt(0));
		assertEquals(3, vh.getInt(1));

		assertEquals(3.0D, vh.getDouble(0), 0);
		assertEquals(3.0D, vh.getDouble(1), 0);

		assertEquals(Double.valueOf(3), vh.get(0));
		assertEquals(Double.valueOf(3), vh.get(1));

		vh.add(4); // Add 4
		assertEquals(DataType.NUMBER, vh.dataType);
		vh.add(5.0); // Add 5.0

		assertEquals(DataType.NUMBER, vh.dataType);
		assertEquals(3, vh.size);

		assertEquals(3, vh.getInt(0));
		assertEquals(4, vh.getInt(1));
		assertEquals(5, vh.getInt(2));

		assertEquals(3.0D, vh.getDouble(0), 0);
		assertEquals(4.0D, vh.getDouble(1), 0);

		assertEquals(Double.valueOf(3), vh.get(0));
		assertEquals(Integer.valueOf(4), vh.get(1));
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(java.lang.Boolean)}.
	 */
	@Test
	public final void testAddBoolean() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(org.openntf.domino.DateTime)}.
	 */
	@Test
	public final void testAddDateTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#setError(java.lang.RuntimeException)}.
	 */
	@Test
	public final void testSetError() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#add(java.lang.Object)}.
	 */
	@Test
	public final void testAddObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#hasMultiValues(org.openntf.domino.formula.ValueHolder[])}.
	 */
	@Test
	public final void testHasMultiValues() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openntf.domino.formula.ValueHolder#toList()}.
	 */
	@Test
	public final void testToList() {
		fail("Not yet implemented"); // TODO
	}

}
