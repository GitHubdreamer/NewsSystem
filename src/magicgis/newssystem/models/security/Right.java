package magicgis.newssystem.models.security;

import magicgis.newssystem.models.BaseEntity;


public class Right extends BaseEntity{
	
	private static final long serialVersionUID = -7550772473227188714L;
	private String rightName = "δ����";
	private String rightUrl;
	private boolean common;
	private String rightDesc;
	private long rightCode;// Ȩ����,1<<n
	private int rightPos; // Ȩ��λ,�൱�ڶ�Ȩ�޷���,��0��ʼ
	
	public String getRightName() {
		return rightName;
	}
	public void setRightName(String rightName) {
		this.rightName = rightName;
	}
	public String getRightUrl() {
		return rightUrl;
	}
	public void setRightUrl(String rightUrl) {
		this.rightUrl = rightUrl;
	}
	public String getRightDesc() {
		return rightDesc;
	}
	public void setRightDesc(String rightDesc) {
		this.rightDesc = rightDesc;
	}
	public long getRightCode() {
		return rightCode;
	}
	public void setRightCode(long rightCode) {
		this.rightCode = rightCode;
	}
	public int getRightPos() {
		return rightPos;
	}
	public void setRightPos(int rightPos) {
		this.rightPos = rightPos;
	}
	public boolean isCommon() {
		return common;
	}
	public void setCommon(boolean common) {
		this.common = common;
	}

}
