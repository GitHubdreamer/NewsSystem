package magicgis.newssystem.actions.impl;

import java.io.ByteArrayInputStream;

import javassist.bytecode.stackmap.TypeData.ClassName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.models.User;
import magicgis.newssystem.services.UserService;
import magicgis.newssystem.utils.DataUtils;
import magicgis.newssystem.utils.ValidateUtils;

@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {

	private static final long serialVersionUID = 9150110980384938836L;
	private String confirmPassword;
	private static Logger logger = Logger.getLogger(ClassName.class);
	private ByteArrayInputStream byteArrayInputStream;
	@Autowired
	private UserService userService;
	private String validationCode = null;
	
	/**
	 * ��ת����¼ҳ��
	 */
	public String reg() {
		return "to_regPage";
	}

	/**
	 * �õ���֤��
	 */
	public String getRandomPictrue() {
		this.setByteArrayInputStream(vcu.getImage());
		sessionMap.put("regRandom", vcu.getVerificationCodeValue());// ȡ������ַ�������HttpSession
		return "validationCode_success";
	}

	public String doneReg() {
		model.setPassword(DataUtils.md5(model.getPassword()));
		userService.saveEntity(model);
		sessionMap.put("user", model);
		logger.info("�û�ע��ɹ���" + model.getUsername());
		return "to_NewsAction_getAllPassedNews";
	}

	public void validateDoneReg() {
		String regRandom = ((String) sessionMap.get("regRandom")).toLowerCase();
		if (!ValidateUtils.isValid(model.getUsername())) {
			addFieldError("username", "�û�������Ϊ��");
		}
		if (!ValidateUtils.isValid(model.getPassword())) {
			addFieldError("password", "���벻��Ϊ��");
		}
		if (!ValidateUtils.isValid(model.getEmail())) {
			addFieldError("email", "E-mail����Ϊ��");
		}
		if (model.getUsername().length() < 4) {
			addFieldError("username", "�û������������ĸ��ַ�");
		}
		if (model.getPassword().length() < 6) {
			addFieldError("password", "���벻�����������ַ�");
		}
		if (hasErrors()) {
			return;
		}
		if (!model.getPassword().equals(confirmPassword)) {
			addFieldError("password", "�����������벻һ��");
			return;
		}
		if (userService.isTokenUp(model.getUsername())) {
			addFieldError("email", "�û����Ѿ���ռ��");
		}
		if (!regRandom.equals(validationCode.toLowerCase())) {
			addFieldError("validationCode","��֤�����");
			return;
		} else {
			System.out.println("ע��ɹ�");
		}
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public ByteArrayInputStream getByteArrayInputStream() {
		return byteArrayInputStream;
	}

	public void setByteArrayInputStream(
			ByteArrayInputStream byteArrayInputStream) {
		this.byteArrayInputStream = byteArrayInputStream;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

}
