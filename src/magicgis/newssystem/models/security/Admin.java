package magicgis.newssystem.models.security;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import magicgis.newssystem.models.BaseEntity;

public class Admin extends BaseEntity {

	private static final long serialVersionUID = -2754043214810493011L;
	// �û���
	private String username;
	// ����
	private String password;
	// Email
	private String email;
	// �Ƿ�����
	private boolean enabled;
	// ������¼ʧ�ܴ���
	private Integer loginFailureCount;
	// ��������
	private Date lockedTime;
	// ����¼����
	private Date loginTime;
	// ����¼IP
	private String ipAddress;
	// ����ʱ��
	private Date createTime = new Date();
	// ӵ�е�Ȩ��
	private Set<Role> roles = new HashSet<Role>();
	// Ȩ���ܺ�
	private long[] rightSum;

	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long[] getRightSum() {
		return rightSum;
	}

	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}

	/**
	 * �ж��û��Ƿ����ָ��Ȩ��
	 */
	public boolean hasRight(Right r) {
		int pos = r.getRightPos();
		long code = r.getRightCode();
		return !((rightSum[pos] & code) == 0);//������Ӧλ����1������Ϊ1������Ϊ0
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * �����û�Ȩ���ܺ�
	 */
	public void calculateRightSum() {
		int pos = 0;
		long code = 0;
		for (Role role : roles) {
			for (Right r : role.getRights()) {
				pos = r.getRightPos();
				code = r.getRightCode();
				rightSum[pos] = rightSum[pos] | code;//λ�������������Ӧλ����0������Ϊ0������Ϊ1
			}
		}
		// �ͷ���Դ
		roles = null;
	}

}
