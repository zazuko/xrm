package com.zazuko.rdfmapping.fanin.nq.nqfanin;

public class NqFaninSupportStandaloneSetup {

	public static void setup() {
		new NqFaninSupportStandaloneSetup();
	}

	public NqFaninSupportStandaloneSetup() {
		new NqFaninSupport().createInjectorAndDoEMFRegistration();
	}

}
