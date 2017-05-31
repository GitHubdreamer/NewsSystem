package magicgis.newssystem.daos.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import magicgis.newssystem.models.User;
import magicgis.newssystem.utils.DataUtils;

@SuppressWarnings("unchecked")
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> {

	/**
	 * ���User�е������Ƿ�ռ��
	 */
	public List<User> isTokenUp(String str) {
		String hql = "FROM User WHERE username = ?";
		List<User> list = getSession().createQuery(hql).setString(0, str)
				.list();
		return list;

	}

	/**
	 * ����¼ʱ�Ƿ�Ϊ�û�
	 */
	public User isUser(String username, String password) {
		String hql = "FROM User WHERE username = ? AND password= ?";
		User user = (User) getSession().createQuery(hql).setString(0, username)
				.setString(1, DataUtils.md5(password)).uniqueResult();
		return user;

	}

}