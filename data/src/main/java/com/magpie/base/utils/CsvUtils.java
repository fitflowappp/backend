package com.magpie.base.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletResponse;

public class CsvUtils {

	public static void download(String fileName, String data, HttpServletResponse response) {
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
			osw.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}