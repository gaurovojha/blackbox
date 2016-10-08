package com.blackbox.ids.ui.form.mdm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.ui.controller.MdmController.ApplicationScreen;

public class ApplicationForm extends FamilySearchForm {

	public static final String MODEL_KEY = "applicationForm";

	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMMDDYYYY;

	public enum Action {
		CREATE_APPLICATION,
		UPDATE_APPLICATION,
		UPDATE_FAMILYLINKAGE;

		public static final Action fromString(final String strAction) {
			Action action = null;
			for (final Action e : Action.values()) {
				if (e.name().equalsIgnoreCase(strAction)) {
					action = e;
					break;
				}
			}
			return action;
		}
	}

	private String action;
	private String screen;
	private String parentFamily;
	private Long parentApplication;
	private Long correspondenceId;
	private Long notificationId;
	private List<SingleApplicationForm> apps = new ArrayList<>();

	public ApplicationForm(CreateApplicationRequestForm form) {
		super();
		SingleApplicationForm appForm = new SingleApplicationForm();
		appForm.setJurisdictionName(form.getJurisdiction());
		appForm.setApplicationNo(form.getApplicationNo());
		appForm.setFamilyId(form.getFamilyId());
		this.apps.add(appForm);
		this.notificationId = form.getNotificationId();
		this.setCorrespondenceId(form.getCorrespondenceId());
	}

	public ApplicationForm() {
		super();
	}

	/*- ---------------------------- Conversion to entity -- */
	public List<ApplicationBase> toEntity() {
		List<ApplicationBase> applications = new ArrayList<>(apps.size());
		this.apps.parallelStream().forEach(e -> applications.add(e.toEntity()));
		applications.forEach(e -> {
			e.setFamilyId(parentFamily);
		});
		return applications;
	}

	public List<ApplicationStage> toDrafts() {
		List<ApplicationStage> drafts = this.apps.parallelStream().map(SingleApplicationForm::toDraft)
				.collect(Collectors.toList());
		drafts.forEach(e -> e.setFamilyId(familyId));
		return drafts;
	}

	public ApplicationForm update(MdmRecordDTO application) {
		if(application.getNotificationId()!=null) {
			this.action = Action.UPDATE_FAMILYLINKAGE.name();
		}else {
			this.action = Action.UPDATE_APPLICATION.name();
		}
		this.screen = ApplicationScreen.DETAILS.name();
		this.notificationId = application.getNotificationId();
		this.apps.add(new SingleApplicationForm().update(application));
		return this;
	}

	public void setFamilyDetails(FamilyDetailDTO familyDetails) {
		this.jurisdiction = familyDetails.getJurisdiction();
		this.applicationNo = familyDetails.getApplicationNo();
		this.docketNo = familyDetails.getAttorneyDocket();
		this.parentFamily = familyDetails.getFamilyId();
	}

	/*- ---------------------------- getter-setters -- */
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getParentFamily() {
		return parentFamily;
	}

	public void setParentFamily(String parentFamily) {
		this.parentFamily = parentFamily;
	}

	public Long getParentApplication() {
		return parentApplication;
	}

	public void setParentApplication(Long parentApplication) {
		this.parentApplication = parentApplication;
	}

	public List<SingleApplicationForm> getApps() {
		return apps;
	}

	public void setApps(List<SingleApplicationForm> apps) {
		this.apps = apps;
	}

	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

}
