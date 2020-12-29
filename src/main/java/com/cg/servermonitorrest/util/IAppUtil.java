package com.cg.servermonitorrest.util;

import java.util.List;
import java.util.Map;

import com.cg.servermonitorrest.resources.ProcessDumpBean;

public interface IAppUtil {

	public String readFiles(String fileName);

	public List<ProcessDumpBean> buildProcessBean(String processDump);

	boolean writeFiles(String fileName, String content);
	
	public Map<String, String> findPropertiesWithPrefix(String fileName, String prefix);
}
