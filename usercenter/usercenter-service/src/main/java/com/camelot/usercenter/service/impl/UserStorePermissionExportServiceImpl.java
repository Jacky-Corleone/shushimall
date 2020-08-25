package com.camelot.usercenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserMallResourceMybatisDAO;
import com.camelot.usercenter.dao.UserMybatisDAO;
import com.camelot.usercenter.dao.UserPermissionDAO;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.UserPermissionDTO;
import com.camelot.usercenter.dto.indto.StoreUserResourceInDTO;
import com.camelot.usercenter.service.UserStorePermissionExportService;
@Service("userStorePermissionExportService")
public class UserStorePermissionExportServiceImpl implements UserStorePermissionExportService {
	private final static Logger logger = LoggerFactory.getLogger(UserStorePermissionExportServiceImpl.class);

	@Resource
	private UserMallResourceMybatisDAO userMallResourceMybatisDAO;
	@Resource
	private UserMybatisDAO userMybatisDAO;
	@Resource
	private UserPermissionDAO userPermissionDAO;
	
	@Override
	public ExecuteResult<List<UserMallResourceDTO>> queryParentResourceList(Integer type,Integer modularType) {
		ExecuteResult<List<UserMallResourceDTO>> result=new ExecuteResult<List<UserMallResourceDTO>>();
		
		try {
			List<UserMallResourceDTO>  list=userMallResourceMybatisDAO.queryParentResourceList(type,modularType);
			UserMallResourceDTO umrDTO=new UserMallResourceDTO();
			for (UserMallResourceDTO userMallResourceDTO : list) {
				umrDTO.setParentId(userMallResourceDTO.getId());
				List<UserMallResourceDTO> list1 = userMallResourceMybatisDAO.selectList(umrDTO, null);
				userMallResourceDTO.setUserMallResourceList(list1);
			}
			result.setResult(list);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> addStoreUserResource(StoreUserResourceInDTO storeUserResourceInDTO) {
		 ExecuteResult<String> result=new  ExecuteResult<String>();
		try {
			//创建子账户
            String userId = userMybatisDAO.getUserIdFun();
			RegisterInfoDTO registerInfoDTO=new RegisterInfoDTO();
            registerInfoDTO.setUid(Long.parseLong(userId));
            registerInfoDTO.setParentId(storeUserResourceInDTO.getParentId());
			registerInfoDTO.setLoginpwd(storeUserResourceInDTO.getPassword());
			registerInfoDTO.setLoginname(storeUserResourceInDTO.getUsername());
			registerInfoDTO.setShopId(storeUserResourceInDTO.getShopId());
			registerInfoDTO.setNickName(storeUserResourceInDTO.getNickName());
			registerInfoDTO.setUserType(storeUserResourceInDTO.getUserDTO().getUsertype());
			registerInfoDTO.setPlatformId(storeUserResourceInDTO.getUserDTO().getPlatformId());
			
			registerInfoDTO.setStatus(storeUserResourceInDTO.getUserDTO().getUserstatus());
			registerInfoDTO.setQuickType(storeUserResourceInDTO.getUserDTO().getQuickType());
			registerInfoDTO.setPaymentStatus(storeUserResourceInDTO.getUserDTO().getPaymentStatus());
			
			userMybatisDAO.registerChildUser(registerInfoDTO);
			//添加子账户资源权限
			Integer[] resourceIds = storeUserResourceInDTO.getResourceIds();
			for (int i = 0; i < resourceIds.length; i++) {
				UserPermissionDTO userPermissionDTO=new UserPermissionDTO();
				userPermissionDTO.setResourceId(resourceIds[i]);
				userPermissionDTO.setShopId(storeUserResourceInDTO.getShopId());
				userPermissionDTO.setUserId(registerInfoDTO.getUid());
				userPermissionDTO.setUserName(storeUserResourceInDTO.getUsername());
				userPermissionDAO.insert(userPermissionDTO);
			}
			result.setResult("");
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ChildUserDTO>> queryChildUserList(Long parentId,Integer modularType,Pager page) {
		
		ExecuteResult<DataGrid<ChildUserDTO>> result=new ExecuteResult<DataGrid<ChildUserDTO>>();
		try {
			DataGrid<ChildUserDTO> dataGrid=new DataGrid<ChildUserDTO>();
			List<UserDTO> user=userMybatisDAO.queryChildUserList(parentId,page);
			Long count=userMybatisDAO.queryChildUserCount(parentId);
			ArrayList<ChildUserDTO> list = new ArrayList<ChildUserDTO>();
			
			for (UserDTO userDTO:user) {
				ChildUserDTO childUserDTO=new ChildUserDTO();
				childUserDTO.setShopId(userDTO.getShopId());
				childUserDTO.setUpdateTime(userDTO.getUpdated());
				childUserDTO.setUserId(userDTO.getUid());
				childUserDTO.setUsername(userDTO.getUname());
				childUserDTO.setNickName(userDTO.getNickname());
				System.out.println("uid-----------------"+userDTO.getUid());
				//查询用户资源
				List<UserMallResourceDTO>   userResourceList=userMallResourceMybatisDAO.selectMallResourceById(userDTO.getUid(),Long.valueOf(modularType));
				childUserDTO.setUserMallResourceList(userResourceList);
				list.add(childUserDTO);
			}
			
			
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<ChildUserDTO> getChildUser(Long userId,Integer modularType) {
	   ExecuteResult<ChildUserDTO>  result=new  ExecuteResult<ChildUserDTO> ();
		try {
			ChildUserDTO childUserDTO=new ChildUserDTO();
			UserDTO user = userMybatisDAO.queryUserById(userId);
			childUserDTO.setShopId(user.getShopId());
			childUserDTO.setUpdateTime(user.getUpdated());
			childUserDTO.setUserId(user.getUid());
			childUserDTO.setUsername(user.getUname());
			childUserDTO.setNickName(user.getNickname());
			//查询用户资源
			List<UserMallResourceDTO>   userResourceList=userMallResourceMybatisDAO.selectMallResourceById(userId,Long.valueOf(modularType));
			childUserDTO.setUserMallResourceList(userResourceList);
			result.setResult(childUserDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<List<UserMallResourceDTO>> queryUserMallResourceById(Long userId,Integer modularType) {
		ExecuteResult<List<UserMallResourceDTO>> result=new ExecuteResult<List<UserMallResourceDTO>>();
		try {
			//根据用户ID查询用户资源
			List<UserMallResourceDTO>   userResourceList=userMallResourceMybatisDAO.selectMallResourceById(userId,Long.valueOf(modularType));
			UserMallResourceDTO umrDTO=new UserMallResourceDTO();
			//查询资源下的二级资源
			for (UserMallResourceDTO userMallResourceDTO : userResourceList) {
				umrDTO.setParentId(userMallResourceDTO.getId());
				List<UserMallResourceDTO> list = userMallResourceMybatisDAO.selectList(umrDTO, null);
				userMallResourceDTO.setUserMallResourceList(list);
			}
			result.setResult(userResourceList);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyUserResourceById(UserPermissionDTO userPermissionDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
		userPermissionDAO.deleteByType(userPermissionDTO.getUserId(),Long.valueOf(userPermissionDTO.getModularType()));
		for (int i = 0; i < userPermissionDTO.getResourceIds().length; i++) {
			userPermissionDTO.setResourceId(userPermissionDTO.getResourceIds()[i]);
			userPermissionDAO.insert(userPermissionDTO);
		}
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteUserById(Integer modularType,Long... userId) {
		ExecuteResult<String> result=new ExecuteResult<String>() ;
			try {
				for (int i = 0; i < userId.length; i++) {
					userMybatisDAO.delete(userId[i]);
					userPermissionDAO.deleteByType(userId[i],Long.valueOf(modularType));
				}
				result.setResultMessage("success");
			} catch (Exception e) {
				result.getErrorMessages().add(e.getMessage());
				result.setResultMessage("error");
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			
			return result;

	}

}
