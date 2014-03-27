package de.foconis.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ValueHolder.DataType;
import org.openntf.domino.formula.ValueHolderBoolean;
import org.openntf.domino.formula.ValueHolderNumber;
import org.openntf.domino.formula.ValueHolderObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValueHolderNumberTest {

	ValueHolder vhi;
	ValueHolder vhd;
	ValueHolder vhI;
	ValueHolder vhL;
	ValueHolder vhD;
	ValueHolder vhErr;

	@Before
	public void setUp() throws Exception {
		vhi = ValueHolder.createValueHolder(int.class, 5);
		vhd = ValueHolder.createValueHolder(double.class, 5);
		vhI = ValueHolder.createValueHolder(Integer.class, 5);
		vhD = ValueHolder.createValueHolder(Long.class, 5);
		vhErr = ValueHolder.createValueHolder(Long.class, 5);
		vhErr.add(3);
		vhErr.add(5);
		//		vhErr.setError(new RuntimeException("This is a error"));
	}

	@Test
	public final void testDataTypeHandling() {
		assertTrue("Instance of ValueHolderNumber", vhi instanceof org.openntf.domino.formula.ValueHolderNumber);
		assertTrue("Instance of ValueHolderNumber", vhd instanceof org.openntf.domino.formula.ValueHolderNumber);
		assertTrue("Instance of ValueHolderNumber", vhI instanceof org.openntf.domino.formula.ValueHolderNumber);
		assertTrue("Instance of ValueHolderNumber", vhD instanceof org.openntf.domino.formula.ValueHolderNumber);

		assertEquals(DataType._UNSET, vhi.dataType);
		assertEquals(DataType._UNSET, vhd.dataType);
		assertEquals(DataType._UNSET, vhI.dataType);
		assertEquals(DataType._UNSET, vhD.dataType);

		vhi.add(42);
		assertEquals(DataType.INTEGER, vhi.dataType);
		vhi.add(37.0);
		assertEquals(DataType.INTEGER, vhi.dataType);

		vhI.add(42.0);
		assertEquals(DataType.INTEGER, vhI.dataType);
		vhI.add(37);
		assertEquals(DataType.INTEGER, vhI.dataType);

		//--- double
		vhd.add(42);
		assertEquals(DataType.INTEGER, vhd.dataType);
		vhd.add(37.00001);
		assertEquals(DataType.DOUBLE, vhd.dataType);

		vhD.add(42.00001);
		assertEquals(DataType.DOUBLE, vhD.dataType);
		vhD.add(37);
		assertEquals(DataType.DOUBLE, vhD.dataType);

	}

	@Test
	public final void testGetInt() {
		testDataTypeHandling();
		testGetIntHelper(vhi);
		testGetIntHelper(vhd);
		testGetIntHelper(vhI);
		testGetIntHelper(vhD);
	}

	private final void testGetIntHelper(final ValueHolder vh) {
		assertEquals(42, vh.getInt(0));
		for (int i = 1; i < 10; i++)
			assertEquals(37, vh.getInt(i));

		assertEquals(2, vh.size);
		vh.add(51);
		assertEquals(42, vh.getInt(0));
		assertEquals(37, vh.getInt(1));
		for (int i = 2; i < 10; i++)
			assertEquals(51, vh.getInt(i));
		assertEquals(3, vh.size);
	}

	@Test
	public final void testGetDouble() {
		testDataTypeHandling();
		testGetDoubleHelper(vhi);
		testGetDoubleHelper(vhd);
		testGetDoubleHelper(vhI);
		testGetDoubleHelper(vhD);
	}

	private final void testGetDoubleHelper(final ValueHolder vh) {
		assertEquals(42, vh.getInt(0));
		for (int i = 1; i < 10; i++)
			assertEquals(37, vh.getDouble(i), .001);

		assertEquals(2, vh.size);
		vh.add(51.6);
		assertEquals(42, vh.getDouble(0), .001);
		assertEquals(37, vh.getDouble(1), .001);
		for (int i = 2; i < 10; i++)
			assertEquals(51.6, vh.getDouble(i), .001);
		assertEquals(3, vh.size);
	}

	@Test
	public final void testAddAll() {
		testDataTypeHandling();
		vhi.addAll(vhI);
		assertEquals(DataType.INTEGER, vhi.dataType);
		assertEquals(4, vhi.size);
		assertEquals(2, vhI.size);

		assertEquals(42, vhi.getInt(0));
		assertEquals(37, vhi.getInt(1));
		assertEquals(42, vhi.getInt(2));
		assertEquals(37, vhi.getInt(3));
		assertEquals(37, vhi.getInt(4));
		assertEquals(37, vhi.getInt(5));

		vhI.addAll(vhd);
		assertEquals(DataType.DOUBLE, vhI.dataType);

		assertEquals(42, vhi.getInt(0));
		assertEquals(37, vhi.getInt(1));
		assertEquals(42, vhi.getInt(2));
		assertEquals(37, vhi.getInt(3));
		assertEquals(37, vhi.getInt(4));
		assertEquals(37, vhi.getInt(5));

	}

	@Test
	public final void testAddInt() {
		vhi.add(1);
		vhi.add(2);
		vhi.add(3);
		vhi.add(Integer.MIN_VALUE);
		vhi.add(Integer.MAX_VALUE);
		assertEquals(5, vhi.size);
		assertEquals(DataType.INTEGER, vhi.dataType);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public final void testAddIntE() {
		vhi.add(1);
		vhi.add(1);
		vhi.add(1);
		vhi.add(1);
		vhi.add(1);
		vhi.add(1);

	}

	@Test
	public final void testAddDouble() {
		vhi.add(100000000.0);
		assertEquals(DataType.INTEGER, vhi.dataType);
		vhi.add(Integer.MIN_VALUE);
		assertEquals(DataType.INTEGER, vhi.dataType);
		vhi.add((long) 100000);
		assertEquals(DataType.INTEGER, vhi.dataType);
		vhi.add((long) Integer.MIN_VALUE - 10);
		assertEquals(DataType.DOUBLE, vhi.dataType);
	}

	@Test
	public final void testToList() throws EvaluateException {
		testDataTypeHandling();
		List<Object> l = vhi.toList();
		assertEquals(l.get(0), 42);
		assertEquals(l.get(1), 37);
	}

	@Test
	public final void testNewInstance() {
		vhi.add(42);
		ValueHolder vh = vhi.newInstance(10);
		assertEquals(DataType._UNSET, vh.dataType);
	}

	@Test
	public final void testGetObjectInt() {
		vhi.add(42);
		assertTrue(vhi.getObject(0) instanceof Integer);
	}

	@Test
	public final void testValueHolder() {

	}

	@Test
	public final void testCreateValueHolder() {
		vhi = ValueHolder.createValueHolder(boolean.class, 10);
		assertTrue(vhi instanceof ValueHolderBoolean);

		vhi = ValueHolder.createValueHolder(char.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(byte.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(short.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(int.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(long.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(float.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(double.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Boolean.class, 10);
		assertTrue(vhi instanceof ValueHolderBoolean);

		vhi = ValueHolder.createValueHolder(Character.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Byte.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Short.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Integer.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Long.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Float.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

		vhi = ValueHolder.createValueHolder(Double.class, 10);
		assertTrue(vhi instanceof ValueHolderNumber);

	}

	@Test(expected = UnsupportedOperationException.class)
	public final void testCreateValueHolder2() {

		vhi = ValueHolder.createValueHolder(void.class, 10);
		assertTrue(vhi instanceof ValueHolderObject);

	}

	@Test
	public final void testGetError() {
		assertTrue(vhErr.getError() != null);
		assertEquals(DataType.ERROR, vhErr.dataType);
		assertEquals(1, vhErr.size);
		assertTrue(!vhErr.add(3));
		assertEquals(1, vhErr.size);
		assertTrue(!vhErr.add(4.5));
		assertEquals(1, vhErr.size);
	}

	@Test
	public final void testGetError2() {
		assertTrue(vhErr.getError() != null);
		assertEquals(DataType.ERROR, vhErr.dataType);
		assertEquals(1, vhErr.size);
	}

	@Test
	public final void testGet() {
		testDataTypeHandling();
		Object o;

		o = vhi.get(10);
		assertTrue(o instanceof Integer);
		assertEquals(o, 37);

		o = vhd.get(10);
		assertTrue(o instanceof Double);
		assertEquals(o, 37.00001);
	}

	@Test(expected = ClassCastException.class)
	public final void testGetString() {
		vhi.getString(0);
	}

	@Test(expected = ClassCastException.class)
	public final void testGetDateTime() {
		vhi.getDateTime(0);
	}

	@Test(expected = ClassCastException.class)
	public final void testGetBoolean() {
		vhi.getBoolean(0);
	}

	@Test
	public final void testIsTrue() {
		FormulaContext ctx = new FormulaContext(null, null);
		ctx.useBooleans(false);
		assertTrue(!vhi.isTrue(ctx));
		vhi.add(0);
		assertTrue(!vhi.isTrue(ctx));
		vhi.add(1);
		assertTrue(vhi.isTrue(ctx));
	}

	@Test(expected = ClassCastException.class)
	public final void testIsTrue2() {
		FormulaContext ctx = new FormulaContext(null, null);
		ctx.useBooleans(true);
		vhi.add(0);
		assertTrue(!vhi.isTrue(ctx));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testAddString() {
		vhi.add("Hello");
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testAddString2() {
		vhi.add(32);
		vhi.add("Hello");
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testAddBoolean() {
		vhi.add(true);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	public final void testAddDateTime() {
		vhi.add(new org.openntf.domino.impl.CalendarDateTime());
	}

	//	@SuppressWarnings("cast")
	//	@Test
	//	public final void testSetError() {
	//		vhi.setError(new RuntimeException("ex"));
	//		assertTrue(vhi.getError() instanceof RuntimeException);
	//	}

	@Test(expected = IllegalArgumentException.class)
	public final void testAddObject() {
		vhi.add(new Object());
	}

	@Test
	public final void testAddObject2() {
		vhi.add(BigInteger.valueOf(Long.MAX_VALUE));
		assertEquals(DataType.DOUBLE, vhi.dataType);
		assertEquals((double) Long.MAX_VALUE, vhi.getDouble(0), 0);
	}

}
