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

public class AlphaNumericRuleTest {

    private static final String regex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
    private static final String ALPHANUMERIC_VALIDATION = "alphanumeric.validation";

    @Mock
    private static MessageSource messageSource = mock(MessageSource.class);

    private static BaseRule validationRule;

    @BeforeAll
    public static void setUp() throws Exception{
        validationRule = new AlphaNumericRule(regex, messageSource);

        when(messageSource.getMessage(eq(ALPHANUMERIC_VALIDATION),any(Object[].class),any())).thenReturn("Sammple message");
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        assertEquals(ruleResult.isSuccess(), true);
    }

    @Test
    public void testValidate_upperCase(){
        RuleResult ruleResult = validationRule.validate("TEST");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_digits(){
        RuleResult ruleResult = validationRule.validate("123");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_chars(){
        RuleResult ruleResult = validationRule.validate("test");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_specialChars(){
        RuleResult ruleResult = validationRule.validate("test123$!@#%");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_upperLowerCase(){
        RuleResult ruleResult = validationRule.validate("teST");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_upperLowerCase_Digits(){
        RuleResult ruleResult = validationRule.validate("teST123");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_upperCase_Digits(){
        RuleResult ruleResult = validationRule.validate("TEST123");
        assertEquals(ruleResult.isSuccess(), false);
    }
}
