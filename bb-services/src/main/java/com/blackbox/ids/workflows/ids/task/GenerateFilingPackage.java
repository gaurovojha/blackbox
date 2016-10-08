/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.blackbox.ids.core.util.file.JaxbXmlUtil;
import com.blackbox.ids.dto.ids.sb08.US_IDS;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @author ajay2258
 *
 */
public class GenerateFilingPackage implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		/*-
		 * 1. Populate US_IDS object
		 * 2. Generate xml data for SB 08
		 * 3. Fill IDS Form
		 * 4. Run uspto.joboptions to attachments
		 * 5. Package all files
		 */

		long idsID = (long) execution.getVariable(IDSBuildConst.IDS_dbID);
		US_IDS idsPayload = fetchIDSPayload(idsID);
		JaxbXmlUtil.toXML(idsPayload, null);
	}

	public static void main(String[] args) {
		final String xmlPath = "E:\\project\\blackbox\\svn\\Misc\\IDS\\1.xml";
		JaxbXmlUtil.toXML(new US_IDS(), xmlPath);
	}

	private US_IDS fetchIDSPayload(long idsID) {
		US_IDS ids = new US_IDS();
		return ids;
	}

}
