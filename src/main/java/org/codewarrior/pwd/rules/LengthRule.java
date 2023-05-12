package org.codewarrior.pwd.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Validation rule related to length
 */
@Component
public class LengthRule extends BaseAbstractRule {

    private int minLength;
    private int maxLength;

    private static final String MESSAGE_ID = "length.validation";

    @Autowired
    public LengthRule(@Value("${length.validation.min:5}") int min,
                                @Value("${length.validation.max:12}") int max,
                                MessageSource messageSource) {
        super("LENGTH", messageSource, MESSAGE_ID, new Object[]{min, max});
        this.minLength = min;
        this.maxLength = max;
    }

    /**
     * This method checks whether provided string length is between min and max length
     *
     * @param str - string to be validated
     * @retun false if the input string length is not between min and max length
     */
    @Override
    public boolean doValidate(String str) {
        int strLength = str.length();
        return strLength >= minLength && strLength <= maxLength;
    }
}
