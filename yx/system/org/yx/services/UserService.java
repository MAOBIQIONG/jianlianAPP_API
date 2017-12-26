package org.yx.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.Page;
import org.yx.common.entity.PageData;
import org.yx.common.entity.User;


@Service("userService")
public class UserService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	
	public PageData findByUiId(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.findByUiId", pd);
	}

	
	public PageData findByUId(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.findByUId", pd);
	}

	
	public PageData findByUE(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.findByUE", pd);
	}

	
	public PageData findByUN(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.findByUN", pd);
	}

	
	public void saveU(PageData pd) throws Exception {
		this.dao.save("UserXMapper.saveU", pd);
	}

	
	public void editU(PageData pd) throws Exception {
		this.dao.update("UserXMapper.editU", pd);
	}

	
	public void setSKIN(PageData pd) throws Exception {
		this.dao.update("UserXMapper.setSKIN", pd);
	}

	
	public void deleteU(PageData pd) throws Exception {
		this.dao.delete("UserXMapper.deleteU", pd);
	}

	
	public void deleteAllU(String[] USER_IDS) throws Exception {
		this.dao.delete("UserXMapper.deleteAllU", USER_IDS);
	}

	
	public List<PageData> listPdPageUser(Page page) throws Exception {
		return (List) this.dao.findForList("UserXMapper.userlistPage", page);
	}

	public PageData findUserCount(Page page) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.findUserCount",page);
	} 
	
	public List<PageData> listAllUser(PageData pd) throws Exception {
		return (List) this.dao.findForList("UserXMapper.listAllUser", pd);
	}

	
	public List<PageData> listGPdPageUser(Page page) throws Exception {
		return (List) this.dao.findForList("UserXMapper.userGlistPage", page);
	}

	public List<PageData> listManager() throws Exception {
		return (List) this.dao.findForList("UserXMapper.findAllManager", null);
	} 
	
	public void saveIP(PageData pd) throws Exception {
		this.dao.update("UserXMapper.saveIP", pd);
	}

	
	public PageData getUserByNameAndPwd(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserXMapper.getUserInfo", pd);
	}

	
	public void updateLastLogin(PageData pd) throws Exception {
		this.dao.update("UserXMapper.updateLastLogin", pd);
	}

	
	public User getUserAndRoleById(String USER_ID) throws Exception {
		return (User) this.dao.findForObject("UserMapper.getUserAndRoleById",
				USER_ID);
	}

	
	public PageData isBindSchool(User user) throws Exception {
		return (PageData) this.dao.findForObject("SchoolMapper.findByUserId",
				user);
	}

	
	public PageData findInfoByUId(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserMapper.getUserInfoById",
				pd);
	}
	
	public PageData findCount(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("UserMapper.findCount",pd);
	}
	
	public Set<String> findRoles(String username) {
		try {
			List<PageData> roles = (List<PageData>) this.dao.findForList(
					"UserMapper.findRolesByUsername", username);
			if (roles != null && !roles.isEmpty()) {
				Set<String> set = new HashSet<String>();
				for (PageData pd : roles) {
					set.add(pd.getString("ROLE_NAME"));
				}
				return set;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}

	}

}
