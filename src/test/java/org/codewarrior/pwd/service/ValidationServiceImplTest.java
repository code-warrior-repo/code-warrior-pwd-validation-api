package org.codewarrior.pwd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codewarrior.pwd.exception.RuleConfigurationException;
import org.codewarrior.pwd.rules.AlphaNumericRule;
import org.codewarrior.pwd.rules.BaseRule;
import org.codewarrior.pwd.rules.CharSequenceRule;
import org.codewarrior.pwd.rules.LengthRule;
import org.codewarrior.pwd.rules.RuleResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;


public class ValidationServiceImplTest {

	  private static final int PASSWORD_MIN_LENGTH = 5;
	    private static final int PASSWORD_MAX_LENGTH = 12;

	    private static final String alphaNumericRegex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
	    private static final String charSequenceRegex = "(\\p{Alnum}{2,})\\1";

	    
	    private static MessageSource messageSource = mock(MessageSource.class);

	    private static BaseRule alphaNumericRule;

	    private static BaseRule lengthRule;

	    private static BaseRule charSequenceRule;

	    private static ValidationService validationService;


	    @BeforeAll
	    public static void setUp() throws Exception{
	        when(messageSource.getMessage(anyString(),any(Object[].class),any())).thenReturn("Sample Message");

	        alphaNumericRule = spy(new AlphaNumericRule(alphaNumericRegex, messageSource));
	        lengthRule = spy(new LengthRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource));
	        charSequenceRule = spy(new CharSequenceRule(charSequenceRegex, messageSource));
	    }

	    @Test
	    public void testValidate() {
	        Map<String, BaseRule> passwordValidationRules = getPasswordValidationRules();
	        validationService = new ValidationServiceImpl(passwordValidationRules, Collections.emptyList(), null, null, null);

	        List<RuleResult> ruleResults = validationService.validate("pass1234");
	       
	        ruleResults.forEach(ruleResult -> {
	                    assertEquals(ruleResult.isSuccess(), true);
	                }
	        );
	    }

	    @Test
	    public void testValidate_null() {
	    	
	        Map<String, BaseRule> passwordValidationRules = getPasswordValidationRules();
	        validationService = new ValidationServiceImpl(passwordValidationRules, Collections.emptyList(), null, null, null);
	        ReflectionTestUtils.setField(validationService, "invalidRuleMessage", "Invaid Rule - {0}");

	        assertThrows(RuleConfigurationException.class, () -> validationService.validate(null));

	        
	    }

	    @Test
	    public void testValidate_multipleRules_oneRuleFailed() {
	        Map<String, BaseRule> passwordValidationRules = getPasswordValidationRules();
	        List<String> configuredRules = Arrays.asList("LENGTH", "ALPHA_NUMERIC");

	        validationService = new ValidationServiceImpl(passwordValidationRules, configuredRules, null, null, null);
	        ReflectionTestUtils.setField(validationService, "invalidRuleMessage", "Invaid Rule - {0}");

	        List<RuleResult> ruleResults = validationService.validate("passing");
	      
	        ruleResults.forEach(ruleResult -> {
	                    if ("ALPHA_NUMERIC".equals(ruleResult.getName())) {
	                        assertEquals(ruleResult.isSuccess(), false);
	                    } else {
	                        assertEquals(ruleResult.isSuccess(), true);
	                    }
	                }
	        );
	    }

	    @Test
	    public void testValidate_multipleRules_allRulesFailed() {
	        Map<String, BaseRule> passwordValidationRules = getPasswordValidationRules();
	        List<String> configuredRules = Arrays.asList("CHAR_SEQUENCE", "ALPHA_NUMERIC");

	        validationService = new ValidationServiceImpl(passwordValidationRules, configuredRules, null, null, null);

	        List<RuleResult> ruleResults = validationService.validate("passpass");
	       
	        ruleResults.forEach(ruleResult -> {
	                    assertEquals(ruleResult.isSuccess(), false);
	                }
	        );
	    }

	    @Test
	    public void testValidate_invalidRuleName() {

	        Map<String, BaseRule> passwordValidationRules = getPasswordValidationRules();
	        List<String> configuredRules = Arrays.asList("INVALID");
	        ReflectionTestUtils.setField(validationService, "invalidRuleMessage", "Invaid Rule - {0}");

	        assertThrows(NullPointerException.class, () ->   validationService = new ValidationServiceImpl(passwordValidationRules, configuredRules, null, null, null));
	       
	    }

	    private Map<String, BaseRule> getPasswordValidationRules() {
	        Map<String, BaseRule> passwordValidationRules = new HashMap<>();
	        passwordValidationRules.put("alphaNumericRule", alphaNumericRule);
	        passwordValidationRules.put("lengthRule", lengthRule);
	        passwordValidationRules.put("charSequenceRule", charSequenceRule);
	        return passwordValidationRules;
	    }
}
