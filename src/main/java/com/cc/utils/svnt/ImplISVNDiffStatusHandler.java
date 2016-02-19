package com.cc.utils.svnt;

import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * 实现接口ISVNDiffStatusHandler回调句柄，获取变动的文件列表<br>
 * 方法handleDiffStatus获取变动的文件列表<br>
 * <strong>导出的列表支持添加，删除，修改的文件列表</strong>
 * 
 * @author 帅军
 * 
 */
public class ImplISVNDiffStatusHandler implements ISVNDiffStatusHandler {
	List<SVNDiffStatus> changeFileList = new ArrayList<SVNDiffStatus>();
	
	@Override
	public void handleDiffStatus(SVNDiffStatus status) throws SVNException {
		// 可以判定文件的操作类型判断SVNStatusType.STATUS_ADDED
		if (status.getKind() == SVNNodeKind.FILE
		        && (status.getModificationType() == SVNStatusType.STATUS_ADDED || status
		                .getModificationType() == SVNStatusType.STATUS_MODIFIED)) {
			changeFileList.add(status);
		}
	}
	
	public ImplISVNDiffStatusHandler() {
	}
	
	/**
	 * 构造函数，可以添加其他的list对象并添加值
	 * 
	 * @param changeFileList
	 */
	public ImplISVNDiffStatusHandler(List<SVNDiffStatus> changeFileList) {
		this.changeFileList = changeFileList;
	}
}
