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

public class CharSequenceRuleTest {

    private static final String regex = "(\\p{Alnum}{2,})\\1";
    private static final String CHAR_SEQ_VALIDATION = "charactersequence.validation";

    private static BaseRule validationRule;

    @Mock
    private static MessageSource messageSource = mock(MessageSource.class);

    @BeforeAll
    public static void setUp() throws Exception{
        validationRule = new CharSequenceRule(regex, messageSource);

        when(messageSource.getMessage(eq(CHAR_SEQ_VALIDATION),any(Object[].class),any())).thenReturn("Sample Message");
    }

    @Test
    public void testValidate(){
        RuleResult ruleResult = validationRule.validate("test1234");
        assertEquals(ruleResult.isSuccess(), true);
    }

    @Test
    public void testValidate_ImmediateCharSeq(){
        RuleResult ruleResult = validationRule.validate("testtest123");
        assertEquals(ruleResult.isSuccess(), false);
    }

    @Test
    public void testValidate_NotImmediateCharSeq(){
        RuleResult ruleResult = validationRule.validate("test123test");
        assertEquals(ruleResult.isSuccess(), true);
    }
}
