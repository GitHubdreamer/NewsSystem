package magicgis.newssystem.utils;

public final class StringUtils {
	
	private StringUtils(){
		
	}

	/**
	 * ���ַ���ת��Ϊ�ַ�������
	 */
	public static String[] string2Arr(String str, String tag) {
		if (ValidateUtils.isValid(str)) {
			String[] arr = str.split(tag);
			return arr;
		}
		return null;
	}

	/**
	 * ��ѯһ���ַ����������Ƿ����ĳ�ַ���
	 */
	public static boolean contains(String[] values, String value) {
		if (ValidateUtils.isValid(value)) {
			for (String str : values) {
				if (str.equals(value))
					return true;
			}
		}
		return false;
	}

	/**
	 * ���ַ�������ת��Ϊ�ַ���
	 */
	public static String arr2String(Object[] str) {
		if (ValidateUtils.isValid(str)) {
			StringBuffer stringBuffer = new StringBuffer();
			for (Object s : str) {
				stringBuffer.append(s + ",");
			}
			return stringBuffer.substring(0, stringBuffer.length() - 1);
		}
		return null;
	}

	/**
	 * ���ַ������ȳ�����Χ�����ȡһ����
	 */
	public static String getDescString(String str, int length) {
		if (str != null && str.trim().length() > length) {
			return str.substring(0, length);
		}
		return str;
	}
}
