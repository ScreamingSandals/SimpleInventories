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

		// 2) Prepare objects
		List<Object> operations = new ArrayList<>();

		Object lastOperand = null;
		String lastOperation = "";
		for (String str : firstResult) {
			if (DUAL_OPERATORS.contains(str)) {
				if (lastOperand == null) {
					throw new RuntimeException(
							"Invalid operation: There is dual operator but first object is missing!");
				}
				if (str.equals("&&") || str.equals("||")) {
					operations.add(str); // Another priority
					lastOperand = null;
				} else {
					lastOperation = str;
				}
			} else if (SINGLE_OPERATORS.contains(str)) {
				if (lastOperand != null) {
					throw new RuntimeException(
							"Invalid operation: There are two operands but this operator is just for one operand!");
				}
				lastOperation = str;
			} else if (BRACKET_START_OR_END.contains(str)) {
				if (str.equals(")")) {
					if (lastOperand != null && !operations.contains(lastOperand)) {
						if (lastOperand instanceof Operation) {
							operations.add(lastOperand);
						} else {
							operations.add(new BooleanCondition(format, lastOperand));
						}
					}
				}
				operations.add(str);

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
		}

		if (lastOperand != null && !operations.contains(lastOperand)) {
			if (lastOperand instanceof Operation) {
				operations.add(lastOperand);
			} else {
				operations.add(new BooleanCondition(format, lastOperand));
			}
		}
		
		// 3) If there is just one operation, return it!
		if (operations.size() == 1) {
			return (Operation) operations.get(0);
		}
		
		return processAndOrAndBrackets(format, operations); 
	}
	
	private static Operation processAndOrAndBrackets(SimpleGuiFormat format, List<Object> operations) {
		// 4) Parsing && and || and brackets
		Operation lastOp = null;
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
				} else if (str.equals("(")) {
					List<Object> noperations = new ArrayList<>();
					int openedbrackets = 1;
					int bracketEndIndex = operations.size();
					for (int k = i + 1; k < operations.size(); k++) {
						Object op2 = operations.get(k);
						if (op2 instanceof String) {
							if (op2.equals("(")) {
								openedbrackets++;
							} else if (op2.equals(")")) {
								openedbrackets--;
								if (openedbrackets == 0) {
									bracketEndIndex = k;
									break; // This is end
								}
							}
						}
						noperations.add(op2);
					}
					i = bracketEndIndex;
					op = processAndOrAndBrackets(format, noperations);
				}
			}
			Operation prevOp = lastOp;
			if (op instanceof Operation) {
				lastOp = (Operation) op;
				if (prevOp != null && !lap.equals("")) {
					Operation op2 = null;
					switch (lap) {
					case "&&":
						op2 = new AndCondition(format, prevOp, lastOp);
						break;
					case "||":
						op2 = new OrCondition(format, prevOp, lastOp);
						break;
					}
					lastOp = op2;
				}
			}
		}
		
		return lastOp;
	}

	// For testing of parser
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter condition string: ");
		String condition = sc.nextLine();
		System.out.println(getFinalOperation(null, condition));
	}
}
