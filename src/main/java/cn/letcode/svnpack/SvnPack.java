package cn.letcode.svnpack;

import java.util.List;
import java.util.logging.Logger;

import org.tmatesoft.svn.core.wc.SVNDiffStatus;

import cn.letcode.svnpack.svnt.SVNChangedFiles;
import cn.letcode.svnpack.svnt.SVNClientConf;
import cn.letcode.svnpack.svnt.SVNPathToClassPath;

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
