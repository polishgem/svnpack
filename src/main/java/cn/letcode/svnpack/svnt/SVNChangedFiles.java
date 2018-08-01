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
		log.info("初始化获取差异文件列表实体对象!!!!!!");
		try {
			branchURL = SVNURL.parseURIEncoded(SVNClientConf.SVNURL
			        + FileUtil.separator + SVNClientConf.PRONAME);
			startingRevision = SVNRevision.create(SVNClientConf.STARTVISION);
			endingRevision = SVNRevision.create(SVNClientConf.ENDVISION);
			// 仓库访问身份认证
			authManager = SVNWCUtil.createDefaultAuthenticationManager(
			        SVNClientConf.USERNAME, SVNClientConf.PASSWORD);
		} catch (SVNException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 获取文件的文件路径列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public List<String> getChargeFilePathList() throws Exception {

		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNDiffClient diffClient = new SVNDiffClient(authManager, options);

		ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler();
		diffClient.doDiffStatus(branchURL, startingRevision, branchURL,
		        endingRevision, true, false, handler);
		List<SVNDiffStatus> chargeFiles = handler.changeFileList;
		List<String> pathList = new ArrayList<String>();
		for (SVNDiffStatus sta : chargeFiles) {
			String op = sta.getModificationType().toString();
			if (!StringUtils.isBlank(op)
			        && SVNClientConf.OPERTYPE.contains(op + ",")) {
				pathList.add(sta.getPath());
			}
		}
		return pathList;
	}

	@SuppressWarnings("deprecation")
	public List<File> getChargeFileList() throws Exception {

		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNDiffClient diffClient = new SVNDiffClient(authManager, options);

		ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler();
		diffClient.doDiffStatus(branchURL, startingRevision, branchURL,
		        endingRevision, true, true, handler);
		List<SVNDiffStatus> chargeFiles = handler.changeFileList;
		List<File> fileList = new ArrayList<File>();
		for (SVNDiffStatus sta : chargeFiles) {
			String op = sta.getModificationType().toString();
			if (!StringUtils.isBlank(op)
			        && SVNClientConf.OPERTYPE.contains(op + ",")) {
				fileList.add(sta.getFile());
			}
		}
		return fileList;
	}

	@SuppressWarnings("deprecation")
	public List<SVNDiffStatus> getChargeStatusList() throws Exception {

		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNDiffClient diffClient = new SVNDiffClient(authManager, options);
		ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler();

		diffClient.doDiffStatus(branchURL, startingRevision, branchURL, endingRevision, SVNDepth.fromRecurse(true), true, handler);

		return handler.changeFileList;
	}

	public void chargeFiles() {
		SVNUpdateClient updateClient = new SVNUpdateClient(authManager,
		        SVNWCUtil.createDefaultOptions(true));
		try {
			List<SVNDiffStatus> changes = getChargeStatusList();
			for (int idx = 0; idx < changes.size(); idx++) {
				SVNDiffStatus change = (SVNDiffStatus) changes.get(idx);
				File destination = new File("ddd" + change.getPath());
				System.out.println(change.getPath() + ":"
				        + change.getModificationType());

				System.out.println(change.getURL());
				System.out.println(destination);
				updateClient.doExport(change.getURL(), destination,
				        startingRevision, endingRevision, null, true, SVNDepth
				                .getInfinityOrEmptyDepth(true));
			}
		} catch (SVNException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
