package cn.letcode.svnpack.svnt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;

import cn.letcode.svnpack.file.FileUtil;

/**
 * Path相互转换
 *
 * @author 帅军
 *
 */
public class SVNPathToClassPath {
	static Logger log = Logger.getLogger(SVNPathToClassPath.class.getName());

	/**
	 * 获取文件对应的class的url列表<br>
	 * 通过传入的proType处理不同的路径
	 *
	 * @param list
	 * @param proType
	 * @return
	 * @throws Exception
	 */
	public static void getClassPack(List<SVNDiffStatus> list, String proType) throws Exception {
		if (list == null || list.size() <= 0)
			throw new Exception("svn path list is null or this list.size is 0");
		// 处理有项目类型的项目
		if ("J2EE".equals(SVNClientConf.PROTYPE)) {
			getClassJavaEEPath(list);
		} else if ("MAVEN".equals(SVNClientConf.PROTYPE.toUpperCase())) {
			getClassMavenPath(list);
		}
	}

	/**
	 * 独立处理JAVAEE项目文件目录结构
	 *
	 * @param list
	 * @param proType
	 * @return
	 * @throws Exception
	 */
	public static void getClassJavaEEPath(List<SVNDiffStatus> list) throws Exception {

		OutputStream os = null;
		ZipOutputStream zos = null;
		os = new FileOutputStream(SVNClientConf.zipPath + SVNClientConf.PRONAME + ".zip");
		zos = new ZipOutputStream(os);
		zos.setLevel(FileUtil.compressLevel);
		for (SVNDiffStatus status : list) {
			String fp = status.getPath().replace("src", "WEB-INF" + FileUtil.separator + "classes");
			fp = StringUtils.remove(fp, "WebRoot/");
			fp = fp.replace(".java", ".class");
			File file = new File(
					SVNClientConf.CLASSURL + FileUtil.separator + fp);
			if (file.exists()) {
				String context = SVNClientConf.PRONAME + FileUtil.separator + fp;
				FileUtil.compress(zos, file, context);
			} else {
				log.warn("file is not exists!!!! the file name is:" + file.getAbsolutePath());
			}
		}
		zos.closeEntry();
		zos.close();

	}

	/**
	 * 独立处理 MAVEN 项目文件目录结构
	 *
	 * @param list
	 * @param proType
	 * @return
	 * @throws Exception
	 */
	public static void getClassMavenPath(List<SVNDiffStatus> list) throws Exception {
		log.info("###############start package###############");
		OutputStream os = null;
		ZipOutputStream zos = null;
		os = new FileOutputStream(SVNClientConf.zipPath);
		zos = new ZipOutputStream(os);
		zos.setLevel(FileUtil.compressLevel);
		for (SVNDiffStatus status : list) {

			if ("pom.xml".equals(status.getPath()) || ".classpath".equals(status.getPath()) ||
					".project".equals(status.getPath())
					|| status.getPath().contains(".settings")) {
				continue;
			}

			String fp = status.getPath().replace("src/main/java", FileUtil.separator + "WEB-INF/classes");
			fp = fp.replace("src/main/resources", FileUtil.separator + "WEB-INF/classes");

			fp = StringUtils.remove(fp, "src/main/webapp/");

			fp = fp.replace(".java", ".class");
			String pathname = SVNClientConf.CLASSURL + FileUtil.separator + fp;
			File file = new File(
					pathname);
			if (file.exists()) {
				String context = SVNClientConf.PRONAME + FileUtil.separator + fp;
				FileUtil.compress(zos, file, context);

				log.info("compress file:" + pathname);
			} else {
				log.error("file not found：" + status.getPath());
			}
		}

		zos.flush();
		zos.close();
		log.info("###############package success###############");
	}

}