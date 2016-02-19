package com.cc.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipOutputStream;

public class FileUtil {
	public static String	separator		= "/";
	public static int		compressLevel	= 6;

	/**
	 * 打包所有生产的文件
	 * 
	 * @param zos
	 * @param file
	 * @param context
	 * @throws IOException
	 */
	public static void compress(final ZipOutputStream zos, final File file,
			final String context) throws IOException {
		if (file.isFile()) {
			final FileInputStream is = new FileInputStream(file);
			final BufferedInputStream bin = new BufferedInputStream(is);
			zos.putNextEntry(new org.apache.tools.zip.ZipEntry(context));
			final byte buffer[] = new byte[4096];
			int count = 0;
			while ((count = bin.read(buffer)) != -1) {
				zos.write(buffer, 0, count);
			}
			bin.close();
			is.close();
			zos.closeEntry();
		} else if (file.isDirectory()) {
			final File files[] = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					String filePath = "";
					if (context != null && !context.equals("")) {
						filePath = context;
					}
					filePath = filePath + file.getName() + separator;
					compress(zos, files[i], filePath);
				}
			}
		}
	}

	/**
	 * 压缩文件，生成zip文件
	 * 
	 * @param sourceFile
	 *            需要压缩的文件列表,若是目录则跳过
	 * @param targetFile
	 *            目的文件名
	 * @throws IOException
	 */
	public static void compress(final File[] sourceFile, final File targetFile)
			throws IOException {
		final FileOutputStream fos = new FileOutputStream(targetFile);
		final ZipOutputStream zos = new ZipOutputStream(fos);

		for (int i = 0; i < sourceFile.length; i++) {
			final File item = sourceFile[i];
			if (item.isDirectory()) {
				continue;
			}
			final FileInputStream in = new FileInputStream(item);
			final BufferedInputStream bin = new BufferedInputStream(in);
			zos.putNextEntry(new org.apache.tools.zip.ZipEntry(item.getName()));
			final byte buffer[] = new byte[4096];
			int count = 0;
			while ((count = bin.read(buffer)) != -1) {
				zos.write(buffer, 0, count);
			}
			bin.close();
			zos.closeEntry();
		}
		zos.flush();
		zos.close();
	}
}
