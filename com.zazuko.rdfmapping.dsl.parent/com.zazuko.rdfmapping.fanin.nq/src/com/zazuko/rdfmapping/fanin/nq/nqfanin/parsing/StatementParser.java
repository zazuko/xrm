package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StatementParser {

	// https://www.w3.org/TR/n-quads/#grammar-production-BLANK_NODE_LABEL
	// [1] nquadsDoc ::= statement? (EOL statement)* EOL?
	// [2] statement ::= subject predicate object graphLabel? '.'
	// [3] subject ::= IRIREF | BLANK_NODE_LABEL
	// [4] predicate ::= IRIREF
	// [5] object ::= IRIREF | BLANK_NODE_LABEL | literal
	// [6] graphLabel ::= IRIREF | BLANK_NODE_LABEL
	// [7] literal ::= STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?

	// [144s]	LANGTAG	::=	'@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
	// [8]	EOL	::=	[#xD#xA]+
	// [10]	IRIREF	::=	'<' ([^#x00-#x20<>"{}|^`\] | UCHAR)* '>'
	// [11]	STRING_LITERAL_QUOTE	::=	'"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"'
	// [141s]	BLANK_NODE_LABEL	::=	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
	// [12]	UCHAR	::=	'<backslash>u' HEX HEX HEX HEX | '\U' HEX HEX HEX HEX HEX HEX HEX HEX
	// [153s]	ECHAR	::=	'\' [tbnrf"'\]
	// [157s]	PN_CHARS_BASE	::=	[A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
	// [158s]	PN_CHARS_U	::=	PN_CHARS_BASE | '_' | ':'
	// [160s]	PN_CHARS	::=	PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
	// [162s]	HEX	::=	[0-9] | [A-F] | [a-f]

	public Statement parse(int lineNumber, int position, String line) {
		LineContext ctx = new LineContext(line, lineNumber, position);
		Statement result = new Statement(ctx);

		result.setSubject(parse(ctx, ValueType.IRI_REF, ValueType.BLANK_NODE_LABEL));
		ctx.skipWhitespaces();

		result.setPredicate(parse(ctx, ValueType.IRI_REF, ValueType.BLANK_NODE_LABEL));
		ctx.skipWhitespaces();

		result.setObject(parse(ctx, ValueType.IRI_REF, ValueType.BLANK_NODE_LABEL, ValueType.LITERAL));
		result.setLanguageTag(parseOptional(ctx, ValueType.LANGUAGE_TAG));
		ctx.skipWhitespaces();

		result.setGraphLabel(parseOptional(ctx, ValueType.IRI_REF, ValueType.BLANK_NODE_LABEL));
		ctx.skipWhitespaces();

		ctx.consume('.');

		return result;
	}

	private Value parse(LineContext ctx, ValueType... types) {
		Value result = parseOptional(ctx, types);
		if (result == null) {
			throw new ParseException(ctx, "no suitable parser found. available parsers at this location are "
					+ Arrays.stream(types).map(String::valueOf).collect(Collectors.joining(", ", "[ ", " ]")));
		}
		return result;
	}

	private Value parseOptional(LineContext ctx, ValueType... types) {

		for (ValueType valueType : types) {
			if (valueType.getParser().canParse(ctx)) {
				return new Value(valueType, valueType.getParser().parse(ctx));
			}
		}
		return null;
	}

}
