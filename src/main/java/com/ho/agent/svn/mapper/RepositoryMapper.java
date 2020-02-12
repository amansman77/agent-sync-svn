package com.ho.agent.svn.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ho.agent.svn.constant.Constant.PropertyKey;
import com.ho.agent.svn.property.AppProperty;

public class RepositoryMapper {
	
	private final static String ID_SEPERATOR = "-";
	
	private String propertyId;
	
	private String remoteUri;
	private String localPath;
	private Long checkedRevision;
	
	public static List<RepositoryMapper> getRepositorys() {
		AppProperty appProperty = AppProperty.getInstance();
				
		Map<String, RepositoryMapper> repositoryMap = new HashMap<String, RepositoryMapper>();
		
		Set<Entry<Object,Object>> propertyEntries = appProperty.getAppProps().entrySet();
		Iterator<Entry<Object, Object>> propertyIterator = propertyEntries.iterator();
		while (propertyIterator.hasNext()) {
			Entry<Object, Object> propertyEntry = propertyIterator.next();
			String propertyKey = (String) propertyEntry.getKey();
			
			if (propertyKey.contains(PropertyKey.SVN_URL)) {
				String repositoryKey = propertyKey.substring(propertyKey.lastIndexOf(ID_SEPERATOR) + 1);
						
				RepositoryMapper repositoryMapper = repositoryMap.get(repositoryKey);
				if (repositoryMapper == null) {
					repositoryMapper = new RepositoryMapper();
					repositoryMap.put(repositoryKey, repositoryMapper);
					
					repositoryMapper.setPropertyId(repositoryKey);
				}
				repositoryMapper.setRemoteUri((String) propertyEntry.getValue());
			} else if (propertyKey.contains(PropertyKey.SVN_TARGET_SOURCE_PATH)) {
				String repositoryKey = propertyKey.substring(propertyKey.lastIndexOf(ID_SEPERATOR) + 1);
				
				RepositoryMapper repositoryMapper = repositoryMap.get(repositoryKey);
				if (repositoryMapper == null) {
					repositoryMapper = new RepositoryMapper();
					repositoryMap.put(repositoryKey, repositoryMapper);
					
					repositoryMapper.setPropertyId(repositoryKey);
				}
				repositoryMapper.setLocalPath((String) propertyEntry.getValue());
			} else if (propertyKey.contains(PropertyKey.DATA_CHECKED_REVISION)) {
				String repositoryKey = propertyKey.substring(propertyKey.lastIndexOf(ID_SEPERATOR) + 1);
				
				RepositoryMapper repositoryMapper = repositoryMap.get(repositoryKey);
				if (repositoryMapper == null) {
					repositoryMapper = new RepositoryMapper();
					repositoryMap.put(repositoryKey, repositoryMapper);
					
					repositoryMapper.setPropertyId(repositoryKey);
				}
				repositoryMapper.setCheckedRevision(Long.valueOf((String) propertyEntry.getValue()));
			}
		}
		
		return new ArrayList<RepositoryMapper>(repositoryMap.values());
	}
	
	public String getPropertyKey(String propertyKey) {
		return propertyKey + ID_SEPERATOR + propertyId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getRemoteUri() {
		return remoteUri;
	}
	public void setRemoteUri(String remoteUri) {
		this.remoteUri = remoteUri;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public Long getCheckedRevision() {
		return checkedRevision;
	}
	public void setCheckedRevision(Long checkedRevision) {
		this.checkedRevision = checkedRevision;
	}
	
}
