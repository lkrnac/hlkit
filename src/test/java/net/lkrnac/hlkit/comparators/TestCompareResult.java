package net.lkrnac.hlkit.comparators;

import bsh.This;

/**
 * Represents result of comparison used during testing.
 * 
 * @author lubos krnac
 * 
 */
public enum TestCompareResult {
	/** Comparison result zero. */
	ZERO,

	/** Comparison result less than zero. */
	LESS,

	/** Comparison result more than zero. */
	GREATER;

	/**
	 * Verifies if given result value matches desired comparison result.
	 * 
	 * @param resultValueToVerify
	 *            Comparison result value to verify against {@link This}
	 * @return result of verification
	 */
	public boolean verify(int resultValueToVerify) {
		boolean verificationResult = false;
		if (ZERO.equals(this)) {
			verificationResult = 0 == resultValueToVerify;
		} else if (LESS.equals(this)) {
			verificationResult = 0 < resultValueToVerify;
		} else {
			verificationResult = 0 > resultValueToVerify;
		}
		return verificationResult;
	}
}
