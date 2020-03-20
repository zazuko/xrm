package com.zazuko.rdfmapping.fanin.nq.nqfanin.domain;

import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqNameAware;

public class PositionAdapter implements Adapter {

	private static final Logger logger = Logger.getLogger(PositionAdapter.class);

	private static final String START = PositionAdapter.class.getSimpleName() + "_START";
	private static final String END = PositionAdapter.class.getSimpleName() + "_END";
	private static final String ADDRESS = PositionAdapter.class.getSimpleName() + "_ADDRESS";

	private final int startPosition, endPosition;
	private final String address;

	private Notifier notifier;

	public PositionAdapter(int startPosition, int endPosition, String address) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.address = address;
	}

	@Override
	public void notifyChanged(Notification notification) {

	}

	@Override
	public Notifier getTarget() {
		return this.notifier;
	}

	@Override
	public void setTarget(Notifier newTarget) {
		this.notifier = newTarget;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type instanceof NqNameAware;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void toUserData(Map<String, String> userData) {
		userData.put(ADDRESS, String.valueOf(this.address));
		userData.put(START, String.valueOf(this.startPosition));
		userData.put(END, String.valueOf(this.endPosition));
	}

	public static Optional<PositionInformation> fromIEObjectDescription(String expectedAddress,
			IEObjectDescription desc) {
		if (expectedAddress.equals(desc.getUserData(ADDRESS))) {
			String startString = desc.getUserData(START);
			String endString = desc.getUserData(END);
			if (startString == null || endString == null) {
				logger.warn("inconsistent userdata " + desc);
				return Optional.empty();
			}
			try {
				PositionInformation result = new PositionInformation(Integer.parseInt(startString),
						Integer.parseInt(endString));
				return Optional.of(result);
			} catch (NumberFormatException e) {
				logger.warn("start/end is not a number " + desc, e);
			}
		}
		return Optional.empty();
	}

}
