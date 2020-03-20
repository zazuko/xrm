package com.zazuko.rdfmapping.fanin.nq.nqfanin.domain;

public class PositionInformation {
	private final int start, end;

	public PositionInformation(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return "PositionInformation [start=" + start + ", end=" + end + "]";
	}

}
