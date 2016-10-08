package com.blackbox.ids.core.model;

/** Enum to represent equality of application number, publication number and grant number*/
public enum CompareNumber {

    /** Represents no equality check*/
	NONE,
    /** Represents equality of application number, publication number and grant number */
    APG,
    /** Represents equality of application number and publication number*/
	AP,
    /** Represents equality of publication number and grant number*/
	PG,
    /** Represents equality of application number and grant number*/
	AG,
    /** Represents application number starts with PA*/
	START_PA,
    /** Represents application number starts with BR*/
	START_BR;

}
