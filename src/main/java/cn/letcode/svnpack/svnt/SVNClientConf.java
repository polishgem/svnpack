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
			System.out.println("*********************************************");
			System.out.println("svntpe\t\t:" + SVNTYPE);
			System.out.println("proname\t\t:" + PRONAME);
			System.out.println("protype\t\t:" + PROTYPE);
			System.out.println("svnurl\t\t:" + SVNURL);
			System.out.println("uname\t\t:" + USERNAME);
			System.out.println("passwd\t\t:" + "******");
			System.out.println("startvision\t:" + STARTVISION);
			System.out.println("endvision\t:" + ENDVISION);
			System.out.println("opertype\t:" + OPERTYPE);
			System.out.println("classurl\t:" + CLASSURL);
			System.out.println("*********************************************");
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
