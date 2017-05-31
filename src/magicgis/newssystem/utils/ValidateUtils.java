package magicgis.newssystem.utils;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.aware.AdminAware;
import magicgis.newssystem.models.security.Admin;
import magicgis.newssystem.models.security.Right;
import magicgis.newssystem.utils.ValidateUtils;

public final class ValidateUtils {
	
	private ValidateUtils(){
		
	}

	/**
	 * �ж��ַ�������Ч��
	 */
	public static boolean isValid(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * �жϼ��ϵ���Ч��
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * �ж������Ƿ���Ч
	 */
	public static boolean isValid(Object[] arr) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean hasRight(String nameSpace, String actionName,
			HttpServletRequest req, BaseAction baseAction) {
		if (!ValidateUtils.isValid(nameSpace) || "/".equals(nameSpace)) {
			nameSpace = "";
		}
		// �������ӵĲ��������˵� ?xxxx
		if (actionName != null && actionName.contains("?")) {
			actionName = actionName.substring(0, actionName.indexOf("?"));
		}
		String url = nameSpace + "/" + actionName;
		HttpSession session = req.getSession();

		ServletContext sc = session.getServletContext();
		Map<String, Right> map = (Map<String, Right>) sc
				.getAttribute("all_rights_map");
		Right r = map.get(url);
		// ������Դ?
		if (r == null || r.isCommon()) {
			return true;
		} else {
			Admin admin = (Admin) session.getAttribute("admin");
			// ��½?
			if (admin == null) {
				return false;
			} else {
				// userAware����
				if (baseAction != null && baseAction instanceof AdminAware) {
					((AdminAware) baseAction).setAdmin(admin);
				}
				// ��Ȩ��?
				if (admin.hasRight(r)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
