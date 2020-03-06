package com.zazuko.rdfmapping.dsl.validation;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

public class TemplateFormatAnalyzer {

	public TemplateFormatAnalysis analyzeFormats(String input) throws TemplateFormatAnalyzerException {
		int maxNrOfArguments = (int) Math.ceil((double) input.length() / 3.0); // a parameter needs at least 3
																				// characters
		boolean[] readAttributes = new boolean[maxNrOfArguments];
		class DidReadMeListener {
			private final int index;

			public DidReadMeListener(int index) {
				this.index = index;
			}

			@Override
			public String toString() {
				readAttributes[this.index] = true;
				return null;
			}
		}

		Object[] fakedArgs = new Object[readAttributes.length];
		for (int a = 0; a < readAttributes.length; a++) {
			fakedArgs[a] = new DidReadMeListener(a);
		}

		MessageFormat f;
		try {
			f = new MessageFormat(input);
		} catch (IllegalArgumentException e) {
			// user input is rubbish
			throw new TemplateFormatAnalyzerException(e.getMessage(), e);
		}
		f.format(fakedArgs);

		// analyse what args have been used
		Set<Integer> usedKeys = new TreeSet<>();
		int maxKey = -1;
		for (int a = 0; a < readAttributes.length; a++) {
			if (readAttributes[a]) {
				maxKey = a;
				usedKeys.add(Integer.valueOf(a));
			}
		}
		Set<Integer> unusedKeys = new TreeSet<>();
		for (int a = 0; a <= maxKey; a++) {
			if (!usedKeys.contains(a)) {
				unusedKeys.add(a);
			}
		}

		return new TemplateFormatAnalysis(usedKeys, unusedKeys);
	}

	static class TemplateFormatAnalyzerException extends Exception {
		private static final long serialVersionUID = -7091028290621288352L;

		public TemplateFormatAnalyzerException(String message, Exception cause) {
			super(message, cause);
		}
	}

}
