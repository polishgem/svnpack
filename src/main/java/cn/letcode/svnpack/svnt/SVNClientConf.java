package cn.letcode.svnpack.svnt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

import cn.letcode.svnpack.SvnPack;
import cn.letcode.svnpack.file.FileUtil;

/**
 * 初始化svn配置的属性值
 * 
 * @author 帅军
 * 
 */
public class SVNClientConf {
	public static String	PROTYPE;
	public static Logger	log	= Logger.getLogger(SvnPack.class.getName());
	public static String	SVNTYPE;
	public static String	PRONAME;
	public static String	SVNURL;
	public static String	USERNAME;
	public static String	PASSWORD;
	public static Long		STARTVISION;
	public static Long		ENDVISION;
	public static String	OPERTYPE;
	public static String	CLASSURL;
	public static String	zipPath;

	/**
	 * 初始化处理过程中使用的变量
	 */
	public static void init() {
		Properties props = new Properties();
		InputStream in = null;
		try {
			File file = new File("conf" + FileUtil.separator + "svnpack.properties");
			log.info("配置文件路径[" + file.getAbsolutePath() + "]");
			in = new FileInputStream(file);
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			props.load(bf);
			SVNTYPE = props.getProperty("svntype");
			PRONAME = props.getProperty("proName");
			SVNURL = props.getProperty("svnurl");
			USERNAME = props.getProperty("uname");
			PASSWORD = props.getProperty("passwd");
			STARTVISION = Long.valueOf(props.getProperty("startvision"));
			ENDVISION = Long.valueOf(props.getProperty("endvision"));
			OPERTYPE = props.getProperty("opertype");
			PROTYPE = props.getProperty("protype");
			CLASSURL = props.getProperty("classurl");
			zipPath = props.getProperty("zipPath");
			log.info("*********************************************");
			log.info("svntpe\t\t:" + SVNTYPE);
			log.info("proname\t\t:" + PRONAME);
			log.info("protype\t\t:" + PROTYPE);
			log.info("svnurl\t\t:" + SVNURL);
			log.info("uname\t\t:" + USERNAME);
			log.info("passwd\t\t:" + "******");
			log.info("startvision\t:" + STARTVISION);
			log.info("endvision\t:" + ENDVISION);
			log.info("opertype\t:" + OPERTYPE);
			log.info("classurl\t:" + CLASSURL);
			log.info("*********************************************");
			setupLibrary();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				System.err.println("读取配置文件异常！！！");
			}
		}
	}

	/**
	 * 根据加载svn请求的类型启动svn服务
	 * 
	 * @throws Exception
	 */
	static void setupLibrary() throws Exception {
		if (StringUtils.isBlank(SVNTYPE)) {
			throw new Exception("svn connect type is error !!! and svntype is:" + SVNTYPE);
		}
		if ("http".equals(SVNTYPE)) {
			DAVRepositoryFactory.setup();
			log.info("For using over http:// and https:// ");
		}
		if ("svn".equals(SVNTYPE)) {
			SVNRepositoryFactoryImpl.setup();
			log.info("For using over svn:// and svn+xxx://");
		}
		if ("file".equals(SVNTYPE)) {
			FSRepositoryFactory.setup();
			log.info("For using over file:///");
		}
	}
}
