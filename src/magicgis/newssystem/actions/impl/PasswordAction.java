package magicgis.newssystem.actions.impl;

import java.io.ByteArrayInputStream;

import javassist.bytecode.stackmap.TypeData.ClassName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.models.security.Admin;
import magicgis.newssystem.services.AdminService;
import magicgis.newssystem.utils.DataUtils;
import magicgis.newssystem.utils.ValidateUtils;

@Controller
@Scope("prototype")
public class PasswordAction extends BaseAction<Admin> {

	private static final long serialVersionUID = 6927353605936609243L;

	@Autowired
	private AdminService adminService;
	private static Logger logger = Logger.getLogger(ClassName.class);
	private String validationCode = null;
	private ByteArrayInputStream byteArrayInputStream = null;
	private String newpassword = null;
	private String newpassword2 = null;

	/**
	 * �õ���֤��
	 */
	public String getRandomPictrue() {
		this.setByteArrayInputStream(vcu.getImage());
		sessionMap.put("changePasswordRandom", vcu.getVerificationCodeValue());// ȡ������ַ�������HttpSession
		return "validationCode_success";
	}
	
	public String transfer(){
		return "to_successChangePage";
	}

	/**
	 * �޸�����
	 */
	public String changePassword() {
		return "to_PasswordAction_transfer";
	}

	/**
	 * �޸�����У��
	 */
	public void validateChangePassword() {
		Admin admin = (Admin) sessionMap.get("admin");
		String changePasswordRandom = ((String) sessionMap
				.get("changePasswordRandom")).toLowerCase();
		if (!ValidateUtils.isValid(model.getPassword())) {
			addActionError("ԭ���벻��Ϊ��");
			return;
		} else if (!(ValidateUtils.isValid(newpassword) && ValidateUtils
				.isValid(newpassword2))) {
			addActionError("�����벻��Ϊ��");
			return;
		} else if (!admin.getPassword().equals(
				DataUtils.md5(model.getPassword()))) {
			addActionError("ԭ�����������");
			return;
		} else if (!newpassword.equals(newpassword2)) {
			addActionError("�������������벻һ��");
			return;
		} else if (newpassword.length() < 6) {
			addActionError("�����볤�Ȳ������������ַ�");
			return;
		} else if (!changePasswordRandom.equals(validationCode.toLowerCase())) {
			addActionError("��֤�����");
			return;
		} else {
			admin.setPassword(DataUtils.md5(newpassword));
			adminService.updateEntity(admin);
			sessionMap.put("admin", admin);
			logger.info("����Ա�޸�����:" + admin.getUsername());
		}
	}

	/**
	 * ��ת���޸������ҳ��
	 */
	public String toChangePassword() {
		return "to_changePasswordPage";
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getNewpassword2() {
		return newpassword2;
	}

	public void setNewpassword2(String newpassword2) {
		this.newpassword2 = newpassword2;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public ByteArrayInputStream getByteArrayInputStream() {
		return byteArrayInputStream;
	}

	public void setByteArrayInputStream(
			ByteArrayInputStream byteArrayInputStream) {
		this.byteArrayInputStream = byteArrayInputStream;
	}

}
