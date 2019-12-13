package misat11.lib.sgui.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.operations.arithmetic.AdditionArithmetic;
import misat11.lib.sgui.operations.arithmetic.DivisionArithmetic;
import misat11.lib.sgui.operations.arithmetic.ModuloArithmetic;
import misat11.lib.sgui.operations.arithmetic.MultiplicationArithmetic;
import misat11.lib.sgui.operations.arithmetic.SubstractionArithmetic;
import misat11.lib.sgui.operations.bitwise.AndBitwise;
import misat11.lib.sgui.operations.bitwise.ComplimentBitwise;
import misat11.lib.sgui.operations.bitwise.LeftShiftBitwise;
import misat11.lib.sgui.operations.bitwise.OrBitwise;
import misat11.lib.sgui.operations.bitwise.RightShiftBitwise;
import misat11.lib.sgui.operations.bitwise.XorBitwise;
import misat11.lib.sgui.operations.bitwise.ZeroFillRightShiftBitwise;
import misat11.lib.sgui.operations.conditions.AndCondition;
import misat11.lib.sgui.operations.conditions.BooleanCondition;
import misat11.lib.sgui.operations.conditions.Condition;
import misat11.lib.sgui.operations.conditions.EqualsCondition;
import misat11.lib.sgui.operations.conditions.FullEqualsCondition;
import misat11.lib.sgui.operations.conditions.GreaterThanCondition;
import misat11.lib.sgui.operations.conditions.GreaterThanOrEqualCondition;
import misat11.lib.sgui.operations.conditions.NegationCondition;
import misat11.lib.sgui.operations.conditions.NotEqualCondition;
import misat11.lib.sgui.operations.conditions.NotFullEqualsCondition;
import misat11.lib.sgui.operations.conditions.OrCondition;

public class OperationParser {

	public static final List<String> STRING_BEGIN_END = Arrays.asList("\"", "'");
	// All possible binary operators
	public static final List<String> DUAL_OPERATORS = Arrays.asList("==", "===", "!=", "!==", "<>", ">", "<", ">=", "<=", "&&", "||", "+",
			"-", "*", "/", "%", "&", "|", "^", "<<", ">>", ">>>");
	// All possible unary operators
	public static final List<String> SINGLE_OPERATORS = Arrays.asList("!", "~");
	public static final List<String> ESCAPE_SYMBOLS = Arrays.asList("\\");
	public static final List<String> BRACKET_START_OR_END = Arrays.asList("(", ")");
	public static final String BRACKET_START = "(";
	public static final String BRACKET_END = ")";

	private static final List<List<String>> PRIORITIES = new ArrayList<>();

	static {
		// operator priorities are from http://www.cs.bilkent.edu.tr/~guvenir/courses/CS101/op_precedence.html
		
		// operators u+ and u- are used internally while parsing unary + and unary -
		PRIORITIES.add(Arrays.asList("!", "u+", "u-", "~")); // 13
		PRIORITIES.add(Arrays.asList("*", "/", "%")); // 12
		PRIORITIES.add(Arrays.asList("+", "-")); // 11
		PRIORITIES.add(Arrays.asList("<<", ">>", ">>>")); // 10
		PRIORITIES.add(Arrays.asList(">", "<", ">=", "<=")); // 9
		PRIORITIES.add(Arrays.asList("==", "!=", "<>", "===", "!==")); // 8
		PRIORITIES.add(Arrays.asList("&")); // 7
		PRIORITIES.add(Arrays.asList("^")); // 6
		PRIORITIES.add(Arrays.asList("|")); // 5
		PRIORITIES.add(Arrays.asList("&&")); // 4
		PRIORITIES.add(Arrays.asList("||")); // 3
	}

	public static Condition getFinalCondition(SimpleGuiFormat format, String operationString) {
		Operation op = getFinalOperation(format, operationString);
		if (op instanceof Condition) {
			return (Condition) op;
		} else if (op instanceof BlankOperation) {
			return new BooleanCondition(format, ((BlankOperation) op).getBlankObject());
		} else {
			return new BooleanCondition(format, op);
		}
	}

	public static Condition getFinalNegation(SimpleGuiFormat format, String operationString) {
		Operation op = getFinalOperation(format, operationString);
		if (op instanceof BlankOperation) {
			return new NegationCondition(format, ((BlankOperation) op).getBlankObject());
		} else {
			return new NegationCondition(format, op);
		}
	}

	public static Operation getFinalOperation(SimpleGuiFormat format, String operationString) {
		// 1) parsing
		char[] chars = operationString.toCharArray();
		int lastIndexOfEscape = -2;
		List<Object> firstResult = new ArrayList<>();
		String operand = "";
		boolean buildingString = false;
		boolean includeBuildingStringWith = false;
		boolean finalizeStringAfterBuild = true;
		String buildingStringWith = "";
		for (int i = 0; i < chars.length; i++) {
			String c = String.valueOf(chars[i]);
			String pair = c + ((i < (chars.length - 1)) ? chars[i + 1] : "");
			String triplet = pair + ((i < (chars.length - 2)) ? chars[i + 2] : "");
			if (ESCAPE_SYMBOLS.contains(c) && lastIndexOfEscape != (i - 1)) {
				lastIndexOfEscape = i;
			} else if (buildingString) {
				// While building string
				if (lastIndexOfEscape != (i - 1) && c.equals(buildingStringWith)) {
					buildingString = false;
					if (includeBuildingStringWith) {
						operand = buildingStringWith + operand + buildingStringWith;
					}
					if (finalizeStringAfterBuild) {
						firstResult.add(new Operand(operand.trim()));
						operand = ""; // Clear operand
					}
					includeBuildingStringWith = false;
					finalizeStringAfterBuild = true;
				} else {
					operand += c;
				}
			} else if (BRACKET_START_OR_END.contains(c) && lastIndexOfEscape != (i - 1)) {
				if (!operand.equals("")) {
					firstResult.add(new Operand(operand.trim()));
					operand = ""; // Clear operand
				}
				firstResult.add(BRACKET_START.equals(c) ? new OpenBracket() : new CloseBracket());
			} else if (STRING_BEGIN_END.contains(c) && lastIndexOfEscape != (i - 1) && operand.equals("")) {
				// String beginning
				buildingString = true;
				buildingStringWith = c;
			} else if ((DUAL_OPERATORS.contains(c) || DUAL_OPERATORS.contains(pair) || DUAL_OPERATORS.contains(triplet)) && lastIndexOfEscape != (i - 1)) {
				// Dual operator
				if (!operand.equals("")) {
					firstResult.add(new Operand(operand.trim()));
					operand = ""; // Clear operand
				} else if (c.equals("%")) {
					// This is placeholder
					buildingString = true;
					buildingStringWith = "%";
					includeBuildingStringWith = true;
					finalizeStringAfterBuild = false;
					continue;
				}
				if (DUAL_OPERATORS.contains(triplet)) {
					i += 2;
					firstResult.add(new Operator(triplet, true));
				} else if (DUAL_OPERATORS.contains(pair)) {
					i++; // Skip next character
					firstResult.add(new Operator(pair, true));
				} else {
					if (c.equals("+") || c.equals("-")) {
						if (firstResult.size() == 0 || !(firstResult.get(firstResult.size() - 1) instanceof Operand)) {
							firstResult.add(new Operator("u" + c, false));
							continue;
						}
					}
					firstResult.add(new Operator(c, true));
				}
			} else if (SINGLE_OPERATORS.contains(c) && lastIndexOfEscape != (i - 1)) {
				// Single operator
				if (!operand.equals("")) {
					firstResult.add(new Operand(operand.trim()));
					operand = ""; // Clear operand
				}
				firstResult.add(new Operator(c, false));
			} else {
				// Regular letter
				if (operand.equals("")) {
					if (c.trim().equals("")) {
						continue; // Skip whitespace
					}
				}
				operand += c;
			}
		}

		if (!operand.equals("")) {
			firstResult.add(new Operand(operand.trim()));
		}

		return internalProcess(format, firstResult);
	}

	private static Operation internalProcess(SimpleGuiFormat format, List<Object> firstResult) {
		// 2) Prepare objects
		List<Object> operations = new ArrayList<>();
		for (int priority = 0; priority < PRIORITIES.size(); priority++) {
			List<Object> results = new ArrayList<>(priority == 0 ? firstResult : operations);
			List<String> operatorForThisPriority = PRIORITIES.get(priority);
			operations.clear();
			Object lastOperand = null;
			Operator lastOperation = null;
			for (int i = 0; i < results.size(); i++) {
				Object member = results.get(i);
				if (member instanceof Operator) {
					Operator operator = (Operator) member;
					if (operator.isBinary) {
						if (lastOperand == null) {
							throw new RuntimeException(
									"Invalid operation: There is dual operator but first object is missing!");
						}
						if (operatorForThisPriority.contains(operator.string)) {
							lastOperation = operator;
						} else {
							if (lastOperand != null) {
								if (operations.size() == 0) {
									operations.add(lastOperand);
								} else if (lastOperand instanceof Operand) {
									if (!operations.get(operations.size() - 1).equals(lastOperand)) {
										operations.add(((Operand) lastOperand).string);
									}
								} else if (operations.get(operations.size() - 1) != lastOperand) {
									operations.add(lastOperand);
								}
								lastOperand = null;
							}
							operations.add(operator); // Another priority
						}
					} else {
						if (lastOperand != null) {
							throw new RuntimeException(
									"Invalid operation: There are two operands but this operator is just for one operand!");
						}
						lastOperation = operator;
					}
				} else if (member instanceof OpenBracket) {
					int bracketEnd = results.size();
					int openedBrackets = 1;
					List<Object> internalFirstResult = new ArrayList<>();
					for (int k = i + 1; k < results.size(); k++) {
						Object kk = results.get(k);
						if (kk instanceof OpenBracket) {
							openedBrackets++;
						} else if (kk instanceof CloseBracket) {
							openedBrackets--;
							if (openedBrackets == 0) {
								bracketEnd = k;
								break;
							}
						}
						internalFirstResult.add(kk);
					}
					i = bracketEnd; // jump out of bracket
					Operation operand = internalProcess(format, internalFirstResult); // process bracket
					if (lastOperand != null && lastOperation == null) {
						throw new RuntimeException("Invalid operation: There are two operands without operator!");
					}
					if (lastOperation != null) {
						Operation op = getOperation(format, lastOperation, lastOperand, operand);
						lastOperand = op;
						lastOperation = null;
						if (op != null) {
							operations.add(op);
						}
					} else {
						lastOperand = operand;
					}
				} else {
					Object operand = member;
					if (lastOperand != null && lastOperation == null) {
						throw new RuntimeException("Invalid operation: There are two operands without operator!");
					}
					if (lastOperation != null) {
						Operation op = getOperation(format, lastOperation, lastOperand, operand);
						if (operations.size() != 0 && operations.get(operations.size() - 1).equals(lastOperand)) {
							operations.remove(operations.size() - 1);
						}
						lastOperand = op;
						lastOperation = null;
						if (op != null) {
							operations.add(op);
						}
					} else {
						lastOperand = operand;
					}
				}
			}

			if (lastOperand != null && !operations.contains(lastOperand)) {
				if (lastOperand instanceof Operand) {
					lastOperand = ((Operand) lastOperand).string;
				}
				operations.add(lastOperand);
			}

			// 3) If there is just one operation, return it!
			if (operations.size() == 1) {
				if (operations.get(0) instanceof Operation) {
					return (Operation) operations.get(0);
				} else {
					return new BlankOperation(format, operations.get(0));
				}
			}
		}

		throw new RuntimeException("Parsing error!");
	}

	private static Operation getOperation(SimpleGuiFormat format, Operator lastOperation, Object lastOperand,
			Object operand) {
		if (lastOperand instanceof Operand) {
			lastOperand = ((Operand) lastOperand).string;
		}
		if (operand instanceof Operand) {
			operand = ((Operand) operand).string;
		}
		switch (lastOperation.string) {
		case "==":
			return new EqualsCondition(format, lastOperand, operand);
		case "===":
			return new FullEqualsCondition(format, lastOperand, operand);
		case "!=":
		case "<>":
			return new NotEqualCondition(format, lastOperand, operand);
		case "!==":
			return new NotFullEqualsCondition(format, lastOperand, operand);
		case "<=":
			return new GreaterThanOrEqualCondition(format, operand, lastOperand);
		case ">=":
			return new GreaterThanOrEqualCondition(format, lastOperand, operand);
		case "<":
			return new GreaterThanCondition(format, operand, lastOperand);
		case ">":
			return new GreaterThanCondition(format, lastOperand, operand);
		case "!":
			return new NegationCondition(format, operand);
		case "&&":
			return new AndCondition(format, lastOperand, operand);
		case "||":
			return new OrCondition(format, lastOperand, operand);
		case "+":
			return new AdditionArithmetic(format, lastOperand, operand);
		case "-":
			return new SubstractionArithmetic(format, lastOperand, operand);
		case "*":
			return new MultiplicationArithmetic(format, lastOperand, operand);
		case "/":
			return new DivisionArithmetic(format, lastOperand, operand);
		case "%":
			return new ModuloArithmetic(format, lastOperand, operand);
		case "u+":
			return new AdditionArithmetic(format, 0, operand);
		case "u-":
			return new SubstractionArithmetic(format, 0, operand);
		case "&":
			return new AndBitwise(format, lastOperand, operand);
		case "|":
			return new OrBitwise(format, lastOperand, operand);
		case "^":
			return new XorBitwise(format, lastOperand, operand);
		case "~":
			return new ComplimentBitwise(format, operand);
		case "<<":
			return new LeftShiftBitwise(format, lastOperand, operand);
		case ">>":
			return new RightShiftBitwise(format, lastOperand, operand);
		case ">>>":
			return new ZeroFillRightShiftBitwise(format, lastOperand, operand);
		default:
			return null;
		}
	}

	private static interface OperationMember {

	}

	private static class Operand implements OperationMember {
		public String string;

		public Operand(String string) {
			this.string = string;
		}

		@Override
		public boolean equals(Object operand) {
			if (!(operand instanceof Operand)) {
				return false;
			}
			return ((Operand) operand).string.equals(string);
		}

		public String toString() {
			return string;
		}
	}

	private static class Operator implements OperationMember {
		public String string;
		public boolean isBinary;

		public Operator(String string, boolean isBinary) {
			this.string = string;
			this.isBinary = isBinary;
		}

		public String toString() {
			return string;
		}
	}

	private static abstract class Bracket implements OperationMember {

	}

	private static class OpenBracket extends Bracket {

	}

	private static class CloseBracket extends Bracket {

	}
}
