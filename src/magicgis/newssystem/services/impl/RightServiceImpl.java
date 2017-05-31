package magicgis.newssystem.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import magicgis.newssystem.daos.impl.RightDaoImpl;
import magicgis.newssystem.models.Page;
import magicgis.newssystem.models.security.Right;
import magicgis.newssystem.services.RightService;
import magicgis.newssystem.utils.ValidateUtils;

@Service("rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements
		RightService {

	@Autowired
	private RightDaoImpl rightDao;
	private Page<Right> page = null;
	/**
	 * ��������Ȩ��
	 */
	@Override
	public void saveOrUpdateRight(Right model) {
		long rightCode = 0L;
		int rightPos = 0;
		if (model.getId() == null) {
			Object[] arr = rightDao.findMaxRight();
			Integer topPos = (Integer) arr[0];
			Long topCode = (Long) arr[1];
			// û��Ȩ��
			if (topPos == null) {
				rightPos = 0;
				rightCode = 1L;
			} else {
				// Ȩ�����Ƿ񵽴����ֵ
				if (topCode >= (1L << 60)) {
					rightPos = topPos + 1;
					rightCode = 1L;
				} else {
					rightPos = topPos;
					rightCode = topCode << 1;
				}
			}
			model.setRightCode(rightCode);
			model.setRightPos(rightPos);
		}
		this.saveOrUpdateEntity(model);
	}

	/**
	 * ͨ��ExtractAllRightsUtil������Ȩ��
	 */
	public void appendRightByURL(String url) {
		Right r = (Right) rightDao.appendRightByURL(url);
		if (r == null) {
			Right t = new Right();
			t.setRightUrl(url);
			this.saveOrUpdateRight(t);
		}
	}

	/**
	 * ��������Ȩ��
	 */
	@Override
	public void batchSaveRight(List<Right> allRights) {
		for (Right r : allRights) {
			rightDao.batchSaveRight(r.getRightName(), r.isCommon(), r.getId());
		}
	}

	/**
	 * ��ѯ��ָ����Χ�ڵ�Ȩ��
	 */
	@Override
	public List<Right> findRightsInRange(Integer[] ownRightIds) {
		if (ValidateUtils.isValid(ownRightIds)) {
			return rightDao.findRightsInRange(ownRightIds);
		}
		return null;
	}

	/**
	 * ��ѯ����ָ����Χ�ڵ�Ȩ��
	 */
	@Override
	public List<Right> findRightsNotInRange(Set<Right> rights) {
		if (rights == null || rights.size() == 0) {
			return this.findAllEntities();
		} else {
			return rightDao.findRightsNotInRange(rights);
		}
	}

	/**
	 * �õ����Ȩ��λ
	 */
	@Override
	public int getMaxRightPos() {
		Integer pos = (Integer) rightDao.getMaxRightPos();
		return pos == null ? 0 : pos;
	}

	@Override
	public Page<Right> listAllRightPage(int pageNo, int pageSize) {
		page = rightDao.listAllNewsPage(pageNo, pageSize);
		return page;
	}

}
