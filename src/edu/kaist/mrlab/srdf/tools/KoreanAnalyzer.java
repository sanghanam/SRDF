package edu.kaist.mrlab.srdf.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class KoreanAnalyzer {
	private Socket soc;

	public String getResult(String input) throws Exception {
		StringBuffer sb = new StringBuffer();

		InetAddress ia = null;

		String serverIp = "143.248.135.20";

		try {
			ia = InetAddress.getByName(serverIp);
			soc = new Socket(ia, 44417);

			OutputStream os = soc.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);

			bos.write((input).getBytes()); 
			bos.flush();
			soc.shutdownOutput();
			
			InputStream is = soc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				line = line.trim();
				if (line.equals(""))
					continue;

				sb.append(line);
				sb.append("\n");
			}
			bos.close();
			br.close();

		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		KoreanAnalyzer ex = new KoreanAnalyzer();
		
		try {
			String output = ex.getResult("Antoine-Laurent de Lavoisier는 새로운 연소 이론을 주장하여 플로지스톤설을 폐기하고 화학을 발전시켰다.");
			System.out.println(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}