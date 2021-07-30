package com.zazuko.rdfmapping.dsl.ide.contentassist;

import com.zazuko.rdfmapping.dsl.serializer.RdfMappingSyntacticSequencer;

public class SequencerAccess extends RdfMappingSyntacticSequencer {

	public String getBLOCK_BEGINToken() {
		return this.getBLOCK_BEGINToken(null, null, null);
	}

	public String getBLOCK_ENDToken() {
		return this.getBLOCK_ENDToken(null, null, null);
	}

	public String getLINE_ENDToken() {
		return this.getLINE_ENDToken(null, null, null);
	}
}
