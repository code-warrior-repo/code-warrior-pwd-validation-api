package org.codewarrior.pwd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings(value = { "serial" })
public class ValidationControllerTest {
	  private static final String ALPHA_NUMERIC = "ALPHA_NUMERIC";
	    private static final String CHAR_SEQUENCE = "CHAR_SEQUENCE";
	    private static final String LENGTH = "LENGTH";
	    private static final String PASSWORD_VALIDATION_URI = "/password/validate";

	    @Autowired
	    private TestRestTemplate restTemplate;

	    @Test
	    public void testValidate() {
	        String password = "pass123";
	        ResponseEntity<String> responseEntity =
	                restTemplate.postForEntity(PASSWORD_VALIDATION_URI, password, String.class);
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    }


	    @Test
	    public void Should_FailAlphaNumericRule_ForLetters() throws JSONException {
	        String password = "passing";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, false);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailAlphaNumericRule_ForNumbers() throws JSONException {
	        String password = "123456";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, false);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailAlphaNumericRule_ForUppercaseLetters() throws JSONException {
	        String password = "passInG";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, false);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailAlphaNumericRule_ForSpecialSymbols() throws JSONException {
	        String password = "pass123$!@#%";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, false);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailLengthRule_ForInvalidLength() throws JSONException {
	        String password = "pas1";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, false);
	        }});
	    }

	    @Test
	    public void Should_PassLengthRule_ForMinimumLength() throws JSONException {
	        String password = "pass8";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailLengthRule_ForLessThanMinimumLength() throws JSONException {
	        String password = "pas1";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, false);
	        }});
	    }

	    @Test
	    public void Should_PassLengthRule_ForMaxLength() throws JSONException {
	        String password = "pass12345678";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailCharSequenceRule_ForImmediateSameSequence() throws JSONException {
	        String password = "passpass123";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, false);
	            put(LENGTH, true);
	        }});
	    }

	    @Test
	    public void Should_FailLengthRule_ForGreaterThanMinimumLength() throws JSONException {
	        String password = "pass1234567891";

	        verfiyRuleResult(password, new HashMap<String, Boolean>() {{
	            put(ALPHA_NUMERIC, true);
	            put(CHAR_SEQUENCE, true);
	            put(LENGTH, false);
	        }});
	    }

	    private void verfiyRuleResult(String password, HashMap<String, Boolean> expectedResult) throws JSONException {
	        ResponseEntity<String> responseEntity =
	                restTemplate.postForEntity(PASSWORD_VALIDATION_URI, password, String.class);
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	        JSONArray ruleArray = new JSONArray(responseEntity.getBody());
	        for (int i = 0; i < ruleArray.length(); ++i) {
	            JSONObject rule = ruleArray.getJSONObject(i);
	            String ruleName = rule.getString("name");
	            boolean ruleResult = rule.getBoolean("success");
	            if (ALPHA_NUMERIC.equals(ruleName)) {
	                assertEquals(expectedResult.get(ALPHA_NUMERIC), ruleResult);
	            } else if (CHAR_SEQUENCE.equals(ruleName)) {
	                assertEquals(expectedResult.get(CHAR_SEQUENCE), ruleResult);
	            } else if (LENGTH.equals(ruleName)) {
	                assertEquals(expectedResult.get(LENGTH), ruleResult);
	            }
	        }
	    }
}
