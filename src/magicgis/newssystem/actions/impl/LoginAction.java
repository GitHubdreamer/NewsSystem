package magicgis.newssystem.actions.impl;

import java.io.ByteArrayInputStream;

import javassist.bytecode.stackmap.TypeData.ClassName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.models.User;
import magicgis.newssystem.services.RightService;
import magicgis.newssystem.services.UserService;
import magicgis.newssystem.utils.ValidateUtils;

@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> {

	private static final long serialVersionUID = 4706482563369736723L;
	@Autowired
	private UserService userService;
	@Autowired
	private RightService rightService;
	private ByteArrayInputStream byteArrayInputStream;
	private String validationCode = null;
	private static Logger logger = Logger.getLogger(ClassName.class);
	
	/**
	 * ��ת����¼ҳ��
	 */
	public String login() {
		return "to_loginPage";
	}

	/**
	 * �õ���֤��
	 */
	public String getRandomPictrue() {
		this.setByteArrayInputStream(vcu.getImage());
		sessionMap.put("random", vcu.getVerificationCodeValue());// ȡ������ַ�������HttpSession
		return "validationCode_success";
	}

	/**
	 * ��¼�ɹ�
	 */
	public String doneLogin() {
		return "to_NewsAction_getAllPassedNews";
	}

	/**
	 * ��¼У��
	 */
	public void validateDoneLogin() {
		User user = userService
				.isUser(model.getUsername(), model.getPassword());
		String random = ((String) sessionMap.get("random")).toLowerCase();
		if (!(ValidateUtils.isValid(model.getUsername()) && ValidateUtils
				.isValid(model.getPassword()))) {
			addActionError("�û��������벻��Ϊ��");
			return;
		} else if (user == null || user.getId() == null || user.getId() <= 0) {
			addActionError("�û������������");
			return;
		} else if (!random.equals(validationCode.toLowerCase())) {
			addActionError("��֤�����");
			return;
		} else if (user != null && user.getId() > 0) {
			sessionMap.put("user", user);
			logger.info("�û���¼:" + user.getUsername());
		}
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
