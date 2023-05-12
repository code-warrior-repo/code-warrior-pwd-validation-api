package org.codewarrior.pwd.service;

import java.util.List;

import org.codewarrior.pwd.rules.RuleResult;

public interface ValidationService {
    /**
     * This method validates password using the rules configured in system
     *
     * @param password - password to be validated
     * @return collection of applied rule result
     */
    List<RuleResult> validate(String password);
}
