package com.cc.utils;

import java.util.List;
import java.util.logging.Logger;

import org.tmatesoft.svn.core.wc.SVNDiffStatus;

import com.cc.utils.svnt.SVNChangedFiles;
import com.cc.utils.svnt.SVNClientConf;
import com.cc.utils.svnt.SVNPathToClassPath;

public class SvnPack {
	static Logger log = Logger.getLogger(SvnPack.class.getName());
	
	public static void main(String[] args) {
		SVNClientConf.init();
		try {
			SVNChangedFiles svnChangedFiles = new SVNChangedFiles();
			List<SVNDiffStatus> list = svnChangedFiles.getChargeStatusList();
			SVNPathToClassPath.getClassPack(list, SVNClientConf.PROTYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
