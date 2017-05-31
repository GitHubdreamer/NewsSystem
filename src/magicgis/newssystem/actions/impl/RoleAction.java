package magicgis.newssystem.actions.impl;

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.stackmap.TypeData.ClassName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import magicgis.newssystem.actions.BaseAction;
import magicgis.newssystem.models.security.Right;
import magicgis.newssystem.models.security.Role;
import magicgis.newssystem.services.RightService;
import magicgis.newssystem.services.RoleService;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = 1L;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RightService rightService;

	private List<Right> noOwnRights = new ArrayList<Right>();
	private List<Role> allRoles = new ArrayList<Role>();
	private static Logger logger = Logger.getLogger(ClassName.class);
	// ��ɫӵ�е�Ȩ��id����
	private Integer[] ownRightIds;

	/**
	 * ɾ����ɫ
	 */
	public String deleteRole() {
		model = roleService.getEntity(model.getId());
		roleService.deleteEntity(model);
		logger.info("ɾ����ɫ��" + model.getRoleName());
		return "to_RoleAction_findAllRoles";
	}

	/**
	 * �༭��ɫ
	 */
	public String editRole() {
		model = roleService.getEntity(model.getId());
		this.noOwnRights = rightService.findRightsNotInRange(model.getRights());
		return "to_updateRolePage";
	}

	/**
	 * �������½�ɫ
	 */
	public String saveOrUpdateRole() {
		System.out.println(ownRightIds);
		roleService.saveOrUpdateRole(model, ownRightIds);
		logger.info("�������½�ɫ��" + model.getRoleName());
		return "to_RoleAction_findAllRoles";
	}

	/**
	 * ��ѯ�����еĽ�ɫ
	 */
	public String findAllRoles() {
		setAllRoles(roleService.findAllEntities());
		return "to_roleListPage";
	}

	/**
	 * ��ת�����ӽ�ɫ��ҳ��
	 */
	public String toAddRolePage() {
		setNoOwnRights(rightService.findAllEntities());
		return "to_addRolePage";
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}

	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}

	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}

	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}

}
