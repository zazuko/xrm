package com.zazuko.rdfmapping.dsl.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public interface RdfMappingConstants {
	
	public static final String TOKEN_BLOCK_BEGIN = "{";
	public static final String TOKEN_BLOCK_END = "}";
	public static final String TOKEN_LINE_END = ";";

	public static final String TOKEN_QNAME_SEPARATOR_DEFAULT = ".";
	public static final char TOKEN_QNAME_SEPARATOR_DEFAULT_CHAR = TOKEN_QNAME_SEPARATOR_DEFAULT.charAt(0);
	
	// public static final String TOKEN_QNAME_SEPARATOR_RDFPREFIX = ":"; // #71 defuse this functionality for now
	public static final String TOKEN_QNAME_SEPARATOR_RDFPREFIX = TOKEN_QNAME_SEPARATOR_DEFAULT;
	public static final char TOKEN_QNAME_SEPARATOR_RDFPREFIX_CHAR = TOKEN_QNAME_SEPARATOR_RDFPREFIX.charAt(0);
	
	public static final String PREFIX_LABEL_SEPARATOR_CHARACTER = ":";
	public static final String PREFIX_LABEL_SEPARATOR_CHARACTER_REGEX = "\\" + PREFIX_LABEL_SEPARATOR_CHARACTER;

	public static final Set<OutputType> RMLISH_OUTPUTTYPES = Collections
			.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(new OutputType[] { OutputType.RML, OutputType.R2RML, OutputType.CARML })));

	public static final String OMNIMAP_KEY_LABEL = "label";
	public static final String OMNIMAP_KEY_DESCRIPTION = "description";
}
