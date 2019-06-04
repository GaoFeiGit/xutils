package org.xdemo.app.xutils.j2se.hardware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xdemo.app.xutils.ext.GsonTools;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class HardwareUtils {

	/**
	 * 获取磁盘用量
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<HardDisk> getHardDisk() throws IOException {
		List<HardDisk> ret = new ArrayList<HardDisk>();
		for (Path root : FileSystems.getDefault().getRootDirectories()) {
			HardDisk disk = new HardDisk();
			FileStore store = Files.getFileStore(root);
			disk.setTotal(store.getTotalSpace());
			disk.setAvailable(store.getUsableSpace());
			disk.setName(root.toString());
			ret.add(disk);
		}
		return ret;
	}
	
	/**
	 * 获取系统内存
	 * @return
	 */
	public static Memory getMemory() {
		SystemInfo sys = new SystemInfo();
		HardwareAbstractionLayer hal = sys.getHardware();
		CentralProcessor cpu = hal.getProcessor();
		GlobalMemory gm = hal.getMemory();
		return new Memory(gm.getAvailable(), gm.getTotal());
	}
	
	/**
	 * 获取主板序列号
	 * 
	 * @return
	 */
	public static String getMotherboardSN() {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n" + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n" + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * 获取硬盘序列号
	 * 
	 * @param drive
	 *            盘符
	 * @return
	 */
	public static String getHardDiskSN(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n" + "Set colDrives = objFSO.Drives\n" + "Set objDrive = colDrives.item(\"" + drive + "\")\n" + "Wscript.Echo objDrive.SerialNumber"; // see
																																																							// note
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * 获取CPU序列号
	 * 
	 * @return
	 */
	public static String getCPUSerial() {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n" + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_Processor\") \n" + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";

			// + "    exit for  \r\n" + "Next";
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (result.trim().length() < 1 || result == null) {
			result = "无CPU_ID被读取";
		}
		return result.trim();
	}

	/**
	 * 获取MAC地址,默认第一块网卡的MAC地址
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getMac() throws UnknownHostException, SocketException {
		InetAddress ia = InetAddress.getLocalHost();
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 获取所有网卡的IP V4地址
	 * @param ipv4
	 * @return
	 * @throws UnknownHostException
	 */
	public static Set<String> ip(boolean ipv4) throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
		InetAddress[] ips = InetAddress.getAllByName(addr.getHostName());
		Set<String> v4 = new HashSet<String>();
		Set<String> v6 = new HashSet<String>();
		for (InetAddress _addr : ips) {
			if(_addr.getHostAddress().indexOf(":")==-1){
				v4.add(_addr.getHostAddress());
			}else{
				v6.add(_addr.getHostAddress());
			}
		}
		return ipv4?v4:v6;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		List<HardDisk> list = getHardDisk();
		System.out.println(GsonTools.toJson(list));
	}
}
