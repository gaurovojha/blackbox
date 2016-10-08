/**
 *
 */
package com.blackbox.ids.core.model.mstr;

/**
 * @author ajay2258
 *
 */
public enum ProsecutionStatus {
	PUBLISHED,
	PENDING_PUBLICATION,
	GRANTED,
	ABANDONED;
	
	
	public static boolean contains(String s) {
        try {
        	ProsecutionStatus.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }
   }
}
