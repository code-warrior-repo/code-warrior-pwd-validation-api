package org.codewarrior.pwd.rules;

/**
 * Implement this interface to provide a new validation rule
 */
public interface BaseRule {
    /**
     * This method validates input string
     *
     * @param str - string to be validated
     * @return RuleResult if the validation fails
     */
	RuleResult validate(String str);
}
