package net.raysforge.vfs;

public class Record {
	
	public final long offset;
	public final int length;
	
	public Record(long offset, int length) {
		this.offset = offset;
		this.length = length;
	}

}
