
/**
 * Class Infix2PostfixConverter
 *
 * accept infix expressions from the user, covert the infix expression to postfix expression
 * 
 * @author Ahmad Chaudhry
 * @version 2018.02.03
 */

import java.util.*;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Infix2PostfixConverter {

	/**
	 * Check whether or not c is an operator
	 * 
	 * @param c
	 *            the character being analyzed
	 * @return true if c is a operator, else returns false
	 */
	private boolean isOperator(char c) {

		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')';

	}

	/**
	 * Checks whether or not c is a space
	 * 
	 * @param the
	 *            character being analyzed
	 * @return true if c is a space, else returns false
	 */
	private boolean isSpace(char c) {

		return (c == ' ');

	}

	/**
	 * Checks lower precedence for two operators, one on the left and one on the
	 * right
	 * 
	 * @param op1
	 *            the left hand operator
	 * @param op2
	 *            the right hand operator
	 * 
	 * @return true if op1 has a lower precedence than op2, else returns false
	 */
	private boolean lowerPrecedence(char op1, char op2) {

		// different cases for op1 vs op2 precedence
		switch (op1) {

		case '+':
		case '-':
			return !(op2 == '+' || op2 == '-');

		case '*':
		case '/':
			return op2 == '^' || op2 == '(';

		case '^':
			return op2 == '(';

		case '(':
			return true;
		// this case should not happen
		default:
			return false;
		}

	}

	/**
	 * Method to convert infix string to postfix string
	 * 
	 * @param infix
	 *            the infix string being converted
	 * 
	 * @return the postfix string
	 * 
	 */

	public String convertPostfix(String infix) {

		// the stack of operators
		Stack<String> oprStack = new Stack<String>();

		// the first character
		char a;

		// StringTokenizer for the input string
		StringTokenizer parser = new StringTokenizer(infix, "+-*/^() ", true);

		// the postfix result
		StringBuffer postfix = new StringBuffer(infix.length());

		// Process the character
		while (parser.hasMoreTokens()) {

			// get the next character
			String character = parser.nextToken();
			// let c be the first character
			a = character.charAt(0);

			// if the character is an operator and of length 1
			if ((character.length() == 1) && isOperator(a)) {

				// the operator on the stack doesnt have a lower precedence so it goes before
				while (!oprStack.empty() && !lowerPrecedence(((String) oprStack.peek()).charAt(0), a))

					postfix.append(" ").append((String) oprStack.pop());

				// output the remaining operators in the stack
				if (a == ')') {
					String operator = (String) oprStack.pop();
					while (operator.charAt(0) != '(') {
						postfix.append(" ").append(operator);
						operator = (String) oprStack.pop();
					}
				} else
					// pushes the operator onto the stack
					oprStack.push(character);
				// the character is a space
			} else if ((character.length() == 1) && isSpace(a)) {
				// ignore it

				// the character is an operand
			} else {
				postfix.append(" ").append(character);
			}
		}
		// output the remaining operators into the postfix
		while (!oprStack.empty())
			postfix.append(" ").append((String) oprStack.pop());
		// return the postfix result
		return postfix.toString();
	}

	/**
	 * Main method used to call other methods for testing
	 * 
	 * @param args
	 *            contains the command-line arguments passed to the Java program
	 *            upon invocation
	 */
	public static void main(String[] args) throws ScriptException {

		// create array of strings to test
		String[] testString = { "(10 + 3 * 4 / 6)", "12*3 - 4 + (18 / 6)", "35 - 42* 17 /2 + 10", "3 * (4 + 5)",
				"3 * ( 17 - (5+2))/(2+3)" };
		// setup a converter
		Infix2PostfixConverter converter = new Infix2PostfixConverter();

		// test each string by outputting the infix, postfix and result
		for (int i = 0; i < testString.length; i++) {
			System.out.println("infix: " + testString[i]);
			System.out.println("postfix: " + converter.convertPostfix(testString[i]));
			System.out
					.println("result: " + new ScriptEngineManager().getEngineByName("JavaScript").eval(testString[i]));
			// spacing between each string
			System.out.println();
		}
	}
}