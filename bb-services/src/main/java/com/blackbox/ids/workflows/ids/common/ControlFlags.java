/**
 *
 */
package com.blackbox.ids.workflows.ids.common;

/**
 * The class <code>ControlFlags</code> enumerates the control flags which decides the IDS build process along with the
 * possible cases.
 * <p/>
 * Key and values defined herein must exactly match with ones defined in <code>ids.bpnm</code>. Any update must be
 * performed at both places.
 *
 * @author ajay2258
 */
public class ControlFlags {

	public enum ParallegalAction {
		FILE_IDS, SUBMIT_FOR_APPROVAL, DISCARD_IDS;
		public static final String KEY = "parallegalAction";
	}

	public enum AttorneyResponse {
		REQUEST_CHANGE, APPROVE, DISCARD_IDS;
		public static final String KEY = "attorneyResponse";

		public static AttorneyResponse fromString(String strName) {
			AttorneyResponse response = null;
			for (AttorneyResponse e : AttorneyResponse.values()) {
				if (e.name().equalsIgnoreCase(strName)) {
					response = e;
					break;
				}
			}
			return response;
		}
	}

	public enum FilingDecision {
		AUTOMATIC, MANUAL;
		public static final String KEY = "filingDecision";
	}

	public enum UploadStatus {
		SUCCESS, FAILED;
		public static final String KEY = "uploadStatus";
	}

}
