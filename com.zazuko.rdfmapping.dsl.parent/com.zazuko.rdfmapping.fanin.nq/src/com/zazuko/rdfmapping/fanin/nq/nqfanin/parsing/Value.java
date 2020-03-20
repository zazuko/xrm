package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class Value {
	private final ValueType type;
	private final String value;

	private String rawFragment;

	public Value(ValueType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	public String getRawFragment() {
		return rawFragment;
	}

	public void setRawFragment(String rawFragment) {
		this.rawFragment = rawFragment;
	}

	public ValueType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Value [type=" + type + ", value=" + value + "]";
	}

}
