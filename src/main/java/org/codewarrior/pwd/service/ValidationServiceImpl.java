package org.codewarrior.pwd.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.codewarrior.pwd.exception.RuleConfigurationException;
import org.codewarrior.pwd.rules.RuleResult;
import org.codewarrior.pwd.rules.BaseRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * {@inheritDoc}
 */
@Service
public class ValidationServiceImpl implements ValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationServiceImpl.class);

	List<BaseRule> configuredValidationRules = new ArrayList<>();

	private String invalidRuleMessage;
	private String passwordNullOrEmpty;
	
	/**
	 * @param passwordValidationRules - All available rules
	 * @param configuredRules         - Rules defined in configuration file
	 */
	@Autowired
	public ValidationServiceImpl(Map<String, BaseRule> passwordValidationRules,
			@Value("#{'${password.validation.rules:}'.split(',')}") List<String> configuredRules,
			@Value("${no.rules.configured:}") String noRuleMessage,
			@Value("${invalid.rule:}") String invalidRuleMessage,
			@Value("${password.required:}") String passwordNullOrEmpty) {
		this.invalidRuleMessage = invalidRuleMessage;
		this.passwordNullOrEmpty = passwordNullOrEmpty;
		
		if (!CollectionUtils.isEmpty(configuredRules)) {
			LOGGER.info("Password rules configured - {}", Arrays.toString(configuredRules.toArray()));
			configuredRules.stream().filter(configuredRule -> StringUtils.hasLength(configuredRule))
					.filter(configuredRule -> isValidRule(configuredRule, passwordValidationRules))
					.map(configuredRule -> getBaseRule(configuredRule, passwordValidationRules))
					.forEach(configuredValidationRules::add);
		} else
			LOGGER.warn("No password rules configured");

		if (CollectionUtils.isEmpty(configuredValidationRules)) {
			LOGGER.info(noRuleMessage);
			configuredValidationRules.addAll(passwordValidationRules.values());
		}

		LOGGER.info("Configured validation rules - {}", Arrays.toString(configuredValidationRules.toArray()));

	}

	private boolean isValidRule(String enabledRule, Map<String, BaseRule> allPasswordValidationRules) {

		if (null == getBaseRule(enabledRule, allPasswordValidationRules)) {
			String invalidMessage = MessageFormat.format(invalidRuleMessage, enabledRule);
			LOGGER.error(invalidMessage);
			throw new RuleConfigurationException(invalidMessage);
		}

		return true;
	}

	private BaseRule getBaseRule(String enabledRule, Map<String, BaseRule> allPasswordValidationRules) {
		String ruleShortName = enabledRule.replaceAll("_", "");
		Optional<Entry<String, BaseRule>> optionalBaseRule = allPasswordValidationRules.entrySet().stream()
				.filter(k -> k.getKey().toUpperCase().startsWith(ruleShortName.toUpperCase())).findFirst();
		return optionalBaseRule.isPresent() ? optionalBaseRule.get().getValue() : null;
	}

	@Override
	public List<RuleResult> validate(String password) {
		if (isBlank(password)) {
			LOGGER.warn(passwordNullOrEmpty);
			throw new RuleConfigurationException(passwordNullOrEmpty);
		}
		return configuredValidationRules.stream()
				.map(passwordValidationRule -> passwordValidationRule.validate(password)).collect(Collectors.toList());
	}

	static boolean isBlank(String value) {
		return !StringUtils.hasLength(StringUtils.trimAllWhitespace(value));
	}
}