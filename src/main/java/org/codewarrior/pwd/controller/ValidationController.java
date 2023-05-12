package org.codewarrior.pwd.controller;

import java.util.List;

import org.codewarrior.pwd.rules.RuleResult;
import org.codewarrior.pwd.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Rest requests related to password validation
 */
@RestController
@RequestMapping(path = "/password")
public class ValidationController {

	@Autowired
    private ValidationService validationService;

    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     * @return  represents the entire HTTP response contains each rule result
     *          Status Code 200 - Ok.
     */
	@PostMapping(value = "/validate")
    public ResponseEntity<List<RuleResult>> validate(@RequestBody String password) {
        List<RuleResult> ruleResults = validationService.validate(password);
        return new ResponseEntity<List<RuleResult>>(ruleResults, HttpStatus.OK);
    }
}
