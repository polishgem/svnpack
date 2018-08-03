package cn.letcode.svnpack.svnt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.letcode.svnpack.file.FileUtil;

public class SVNChangedFiles {
	static Logger                    log = Logger
	                                             .getLogger(SVNChangedFiles.class
	                                                     .getName());
	static SVNURL                    branchURL;
	static SVNRevision               startingRevision;
	static SVNRevision               endingRevision;
	static ISVNAuthenticationManager authManager;

	public SVNChangedFiles() throws Exception {
		log.info("loading change files !!!!!!");
		try {
			branchURL = SVNURL.parseURIEncoded(SVNClientConf.SVNURL);
			startingRevision = SVNRevision.create(SVNClientConf.STARTVISION);
			endingRevision = SVNRevision.create(SVNClientConf.ENDVISION);
			// 仓库访问身份认证
			authManager = SVNWCUtil.createDefaultAuthenticationManager(
			        SVNClientConf.USERNAME, SVNClientConf.PASSWORD);
		} catch (SVNException e) {
			throw new Exception(e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public List<SVNDiffStatus> getChargeStatusList() throws Exception {

		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNDiffClient diffClient = new SVNDiffClient(authManager, options);
		ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler();

		diffClient.doDiffStatus(branchURL, startingRevision, branchURL, endingRevision, SVNDepth.fromRecurse(true), true, handler);

		return handler.changeFileList;
	}

}
