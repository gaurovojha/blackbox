package com.blackbox.ids.core.model;

/** Enum to represent Coversion Strategy of converting numbers into different format*/
public enum ConversionStrategy {

	/** Represents removal of special characters*/
	REMOVE_CHARACTER,
	/** Represents addition of filling year into number*/
	ADD_YEAR,
	/** Represents converted format is same as input format*/
	CORRECT_FORMAT,
	/** Represents removing check digit*/
	REMOVE_DIGIT,
	/** Represents converting number into specific year*/
	NUMBER_TO_YEAR,
	/** Represents comapring number with a specific year*/
	VALIDATE_YEAR,
	/** Represents addition of WO into given number*/
	ADD_WO,
	
	REMOVE_CC;

}
