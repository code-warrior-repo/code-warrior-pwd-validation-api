package org.codewarrior.pwd.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

public class LengthRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password length must be between 5 and 12 characters";
    private static final String LENGTH_VALIDATION = "length.validation";

    private static BaseRule validationRule;

    @Mock
    private static MessageSource messageSource = mock(MessageSource.class);

    @BeforeAll
    public static void setUp() throws Exception{
        int PASSWORD_MIN_LENGTH = 5;
        int PASSWORD_MAX_LENGTH = 12;
        validationRule = new LengthRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource);

        when(messageSource.getMessage(eq(LENGTH_VALIDATION),any(Object[].class),any())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        assertEquals(ruleResult.isSuccess(), true);
        assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_5chars(){
        RuleResult ruleResult = validationRule.validate("test5");
        assertEquals(ruleResult.isSuccess(), true);
        assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_lessThan5chars(){
        RuleResult ruleResult = validationRule.validate("testtest123");
        assertEquals(ruleResult.isSuccess(), true);
        assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_12chars(){
        RuleResult ruleResult = validationRule.validate("test12345678");
        assertEquals(ruleResult.isSuccess(), true);
        assertEquals(ruleResult.getErrorMessage(), null);
    }

    @Test
    public void testValidate_greaterThan12chars(){
        RuleResult ruleResult = validationRule.validate("testing123456");
        assertEquals(ruleResult.isSuccess(), false);
        assertEquals(ruleResult.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }
}
