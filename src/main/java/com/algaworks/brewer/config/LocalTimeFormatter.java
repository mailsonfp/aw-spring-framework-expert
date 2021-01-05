package com.algaworks.brewer.config;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.algaworks.brewer.config.format.TemporalFormatter;

@Component
public class LocalTimeFormatter extends TemporalFormatter<LocalTime> {
	
	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		return env.getProperty("localdate.format-" + locale, "HH:mm");
	}

	@Override
	public LocalTime parse(String text, DateTimeFormatter formatter) {
		return LocalTime.parse(text, formatter);
	}	
}
