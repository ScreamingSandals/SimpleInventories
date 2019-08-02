package misat11.lib.sgui.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.operations.conditions.AndCondition;
import misat11.lib.sgui.operations.conditions.BooleanCondition;
import misat11.lib.sgui.operations.conditions.Condition;
import misat11.lib.sgui.operations.conditions.EqualsCondition;
import misat11.lib.sgui.operations.conditions.GreaterThanCondition;
import misat11.lib.sgui.operations.conditions.GreaterThanOrEqualCondition;
import misat11.lib.sgui.operations.conditions.NegationCondition;
import misat11.lib.sgui.operations.conditions.NotEqualCondition;
import misat11.lib.sgui.operations.conditions.OrCondition;

public class OperationParser {

	public static final List<String> STRING_BEGIN_END = Arrays.asList("\"", "'");
	public static final List<String> DUAL_OPERATORS = Arrays.asList("==", "!=", ">", "<", ">=", "<=", "&&", "||");
	public static final List<String> SINGLE_OPERATORS = Arrays.asList("!");
	public static final List<String> ESCAPE_SYMBOLS = Arrays.asList("\\");
	public static final List<String> BRACKET_START_OR_END = Arrays.asList("(", ")");
	public static final String BRACKET_START = "(";
	public static final String BRACKET_END = ")";

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

	public static Operation getFinalOperation(SimpleGuiFormat format, String operationString) {
		// 1) parsing
		char[] chars = operationString.toCharArray();
		int lastIndexOfEscape = -2;
		List<String> firstResult = new ArrayList<>();
		String operand = "";
		boolean buildingString = false;
		String buildingStringWith = "";
		for (int i = 0; i < chars.length; i++) {
			String c = String.valueOf(chars[i]);
			String pair = c + ((i != (chars.length - 1)) ? chars[i + 1] : "");
			if (ESCAPE_SYMBOLS.contains(c) && lastIndexOfEscape != (i - 1)) {
				lastIndexOfEscape = i;
			} else if (buildingString) {
				// While building string
				if (lastIndexOfEscape != (i - 1) && c.equals(buildingStringWith)) {
					buildingString = false;
					firstResult.add(operand.trim());
					operand = ""; // Clear operand
				} else {
					operand += c;
				}
			} else if (BRACKET_START_OR_END.contains(c) && lastIndexOfEscape != (i - 1)) {
				if (!operand.equals("")) {
					firstResult.add(operand.trim());
					operand = ""; // Clear operand
				}
				firstResult.add(c);
			} else if (STRING_BEGIN_END.contains(c) && lastIndexOfEscape != (i - 1) && operand.equals("")) {
				// String beginning
				buildingString = true;
				buildingStringWith = c;
			} else if ((DUAL_OPERATORS.contains(c) || DUAL_OPERATORS.contains(pair)) && lastIndexOfEscape != (i - 1)) {
				// Dual operator
				if (!operand.equals("")) {
					firstResult.add(operand.trim());
					operand = ""; // Clear operand
				}
				if (DUAL_OPERATORS.contains(pair)) {
					i++; // Skip next character
					firstResult.add(pair);
				} else {
					firstResult.add(c);
				}
			} else if (SINGLE_OPERATORS.contains(c) && lastIndexOfEscape != (i - 1)) {
				// Single operator
				if (!operand.equals("")) {
					firstResult.add(operand.trim());
					operand = ""; // Clear operand
				}
				firstResult.add(c);
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
			firstResult.add(operand.trim());
		}

		return internalProcess(format, firstResult);
	}

	private static Operation internalProcess(SimpleGuiFormat format, List<String> firstResult) {
		// 2) Prepare objects
		List<Object> operations = new ArrayList<>();

		Object lastOperand = null;
		String lastOperation = "";
		for (int i = 0; i < firstResult.size(); i++) {
			String str = firstResult.get(i);
			if (DUAL_OPERATORS.contains(str)) {
				if (lastOperand == null) {
					throw new RuntimeException(
							"Invalid operation: There is dual operator but first object is missing!");
				}
				if (str.equals("&&") || str.equals("||")) {
					if (lastOperand != null) {
						if (operations.size() == 0) {
							operations.add(lastOperand);
						} else if (lastOperand instanceof String) {
							if (!operations.get(operations.size() - 1).equals(lastOperand)) {
								operations.add(lastOperand);
							}
						} else if (operations.get(operations.size() - 1) != lastOperand) {
							operations.add(lastOperand);
						}
						lastOperand = null;
					}
					operations.add(str); // Another priority
				} else {
					lastOperation = str;
				}
			} else if (SINGLE_OPERATORS.contains(str)) {
				if (lastOperand != null) {
					throw new RuntimeException(
							"Invalid operation: There are two operands but this operator is just for one operand!");
				}
				lastOperation = str;
			} else if (BRACKET_START.equals(str)) {
				int bracketEnd = firstResult.size();
				int openedBrackets = 1;
				List<String> internalFirstResult = new ArrayList<>();
				for (int k = i + 1; k < firstResult.size(); k++) {
					String kk = firstResult.get(k);
					if (BRACKET_START.equals(kk)) {
						openedBrackets++;
					} else if (BRACKET_END.equals(kk)) {
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
				System.out.println("Bracket " + operand + "; operation before " + lastOperation);

				if (lastOperand != null && lastOperation.equals("")) {
					throw new RuntimeException("Invalid operation: There are two operands without operator!");
				}
				if (!lastOperation.equals("")) {
					System.out.println("This");
					Operation op = null;
					switch (lastOperation) {
					case "==":
						op = new EqualsCondition(format, lastOperand, operand);
						break;
					case "!=":
						op = new NotEqualCondition(format, lastOperand, operand);
						break;
					case "<=":
						op = new GreaterThanOrEqualCondition(format, operand, lastOperand);
						break;
					case ">=":
						op = new GreaterThanOrEqualCondition(format, lastOperand, operand);
						break;
					case "<":
						op = new GreaterThanCondition(format, operand, lastOperand);
						break;
					case ">":
						op = new GreaterThanCondition(format, lastOperand, operand);
						break;
					case "!":
						op = new NegationCondition(format, operand);
						break;
					}
					lastOperand = op;
					lastOperation = "";
					if (op != null) {
						operations.add(op);
					}
				} else {
					System.out.println("This2");
					lastOperand = operand;
				}
			} else {
				if (lastOperand != null && lastOperation.equals("")) {
					throw new RuntimeException("Invalid operation: There are two operands without operator!");
				}
				if (!lastOperation.equals("")) {
					Operation op = null;
					switch (lastOperation) {
					case "==":
						op = new EqualsCondition(format, lastOperand, str);
						break;
					case "!=":
						op = new NotEqualCondition(format, lastOperand, str);
						break;
					case "<=":
						op = new GreaterThanOrEqualCondition(format, str, lastOperand);
						break;
					case ">=":
						op = new GreaterThanOrEqualCondition(format, lastOperand, str);
						break;
					case "<":
						op = new GreaterThanCondition(format, str, lastOperand);
						break;
					case ">":
						op = new GreaterThanCondition(format, lastOperand, str);
						break;
					case "!":
						op = new NegationCondition(format, str);
						break;
					}
					lastOperand = op;
					lastOperation = "";
					if (op != null) {
						operations.add(op);
					}
				} else {
					lastOperand = str;
				}
			}
			System.out.println("Last OPERAND: " + lastOperand);
		}

		if (lastOperand != null && !operations.contains(lastOperand)) {
			operations.add(lastOperand);
		}

		for (Object obj : operations) {
			System.out.println("OPERATION " + obj);
		}

		// 3) If there is just one operation, return it!
		if (operations.size() == 1) {
			if (operations.get(0) instanceof Operation) {
				return (Operation) operations.get(0);
			} else {
				return new BlankOperation(format, operations.get(0));
			}
		}

		// 4) Parsing && and || and brackets
		Object lastOp = null;
		String lap = "";

		for (int i = 0; i < operations.size(); i++) {
			Object op = operations.get(i);
			if (op instanceof String) {
				String str = (String) op;
				if (str.equals("&&") || str.equals("||")) {
					if (lastOp == null) {
						throw new RuntimeException(
								"Invalid operation: There is dual operator but first object is missing!");
					}
					lap = str;
					continue;
				}
			}
			Object prevOp = lastOp;
			lastOp = op;
			if (prevOp != null && !lap.equals("")) {
				Operation op2 = null;
				switch (lap) {
				case "&&":
					op2 = new AndCondition(format, prevOp, lastOp);
					break;
				case "||":
					op2 = new OrCondition(format, prevOp, lastOp);
					break;
				default:
					throw new RuntimeException("Invalid operator: " + lap + " !");
				}
				lastOp = op2;
				lap = ""; // Clear lap
			}
		}

		if (lastOp instanceof Operation) {
			return (Operation) lastOp;
		} else {
			return new BlankOperation(format, lastOp);
		}
	}
}
