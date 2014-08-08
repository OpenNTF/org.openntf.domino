package de.foconis.lsext;

/*----------------------------------------------------------------------------*/
import java.util.ArrayList;

/*----------------------------------------------------------------------------*/
public class LSFocArrayList extends ArrayList<Object> {

	private static final long serialVersionUID = -2588419084979352342L;

	private int iModCount = 1;
	private int iCapacity = 128;
	private int iCapacityIncrement = 128;
	private String iFocIF = null;

	/*----------------------------------------------------------------------------*/
	LSFocArrayList(final int numElements, final int modCount, final int capacity, final int capacityIncrement, final String focIF) {
		this(numElements);
		iModCount = modCount;
		iCapacity = capacity;
		iCapacityIncrement = capacityIncrement;
		iFocIF = focIF;
	}

	public LSFocArrayList(final int capacity) {
		super(capacity);
	}

	/*----------------------------------------------------------------------------*/
	public int getModCount() {
		return iModCount;
	}

	public int getCapacity() {
		return iCapacity;
	}

	public int getCapacityIncrement() {
		return iCapacityIncrement;
	}

	public String getFocIF() {
		return iFocIF;
	}
	/*----------------------------------------------------------------------------*/
}
