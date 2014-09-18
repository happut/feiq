package SystemKernel;

import java.net.*;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Component;
import java.io.*;

import javax.swing.JOptionPane;

import Model.*;
import View.ChatWindow;
import View.FileReceiveWindow;
import View.FileSendWindow;
import View.MainWindow;
import View.UserItem;

public class MainServer extends Thread {
	int port;
	int FileSendPort;
	DatagramSocket ds;
	UserMessage On, Off;

	// 构造函数
	public MainServer(int port) {
		this.port = port; // 设置端口号
		this.On = MainWindow.OnInfo;
		this.Off = MainWindow.OffInfo;
		
		FileSendPort=10000;
		start(); // 启动线程 调用run方法
	}

	public void run() {

		try {
			ds = new DatagramSocket(port); // 建立服务器套接字
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				byte[] data = new byte[1024];
				DatagramPacket receiveDp = new DatagramPacket(data, data.length);
				ds.receive(receiveDp);

				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bais);
				UserMessage um = (UserMessage) ois.readObject();

				ois.close();
				bais.close();

				dealMessage(um);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	public void dealMessage(UserMessage um) {
		System.out.println(um);
		switch (um.getCode()) {
		case 0: // 上线消息
			if (!(MainWindow.Online.contains(um.getIp()))) {
				// 改变界面
				UserItem ui = new UserItem(um.getIp(), um.getId());
				MainWindow.Online.add(um.getIp());
				if (um.getMessage().equalsIgnoreCase("RGKS-1")) {
					MainWindow.RGKS_1_Panel.add(ui);
				} else if (um.getMessage().equalsIgnoreCase("RGKS-2")) {
					MainWindow.RGKS_2_Panel.add(ui);
				} else {

				}
				MainWindow.RGKS_1_Panel.validate();
				MainWindow.RGKS_1_Panel.updateUI();
				MainWindow.RGKS_2_Panel.validate();
				MainWindow.RGKS_2_Panel.updateUI();
				// 回发上线信息
				this.sendUserMessage(this.On, um.getIp());
			}

			break;
		case 1: // 下线消息
			if (MainWindow.Online.contains(um.getIp())) {
				MainWindow.Online.remove(um.getIp());
				if (um.getMessage().equalsIgnoreCase("RGKS-1")) {
					Component[] cp = MainWindow.RGKS_1_Panel.getComponents();
					for (int i = 0; i < cp.length; i++) {
						if (((UserItem) cp[i])
								.CheckUser(um.getIp(), um.getId())) {
							MainWindow.RGKS_1_Panel.remove(cp[i]);
						}
					}
				} else if (um.getMessage().equalsIgnoreCase("RGKS-2")) {
					Component[] cp = MainWindow.RGKS_2_Panel.getComponents();
					for (int i = 0; i < cp.length; i++) {
						if (((UserItem) cp[i])
								.CheckUser(um.getIp(), um.getId())) {
							MainWindow.RGKS_2_Panel.remove(cp[i]);
						}
					}
				} else {

				}
				MainWindow.RGKS_1_Panel.validate();
				MainWindow.RGKS_1_Panel.updateUI();
				MainWindow.RGKS_2_Panel.validate();
				MainWindow.RGKS_2_Panel.updateUI();
			}
			break;
		case 3:  //普通聊天信息
			if (MainWindow.ChattingList.containsKey(um.getIp())) {
				((ChatWindow) (MainWindow.ChattingList.get(um.getIp())))
						.addMessage(um);
			} else {
				JOptionPane.showMessageDialog(null, "收到来自" + um.getIp() + "("
						+ um.getId() + ")的消息，请查收", "收到消息",
						JOptionPane.INFORMATION_MESSAGE);
				ChatWindow cw = new ChatWindow(um);
			}
			break;
		case 4:  //群发消息
			int Instruction=JOptionPane.showConfirmDialog(null, 
					"收到来自" + um.getIp() + "(" + um.getId() + ")的群发消息：\n"+um.getMessage(),"收到群发消息",
					JOptionPane.YES_NO_OPTION);
			if(Instruction==JOptionPane.YES_OPTION){
				um.setCode(2);
				um.setMessage("");
				ChatWindow cw = new ChatWindow(um);
			}else if(Instruction==JOptionPane.NO_OPTION){
				
			}else{
				
			}
			break;
		case 5:  //收到发送文件消息		
			
			
			int Instruction1=JOptionPane.showConfirmDialog(null, 
					"收到来自" + um.getIp() + "(" + um.getId() + ")的发送文件请求：\n"+um.getMessage().substring(0,um.getMessage().indexOf('#'))+"\n" +
							um.getMessage().substring(um.getMessage().indexOf('#')+1,um.getMessage().length())+"字节\n是否接收？","收到发送文件请求",
					JOptionPane.YES_NO_OPTION);
			if(Instruction1==JOptionPane.YES_OPTION){
				//同意接收 回发确认消息
				UserMessage AgreeFileSend=new UserMessage(MainWindow.SendFile.getId(),MainWindow.SendFile.getIp(),6,"Agree#"+um.getMessage()+"#"+FileSendPort);
				this.sendUserMessage(AgreeFileSend, um.getIp());
				
				//创建文件接收窗口
				FileReceiveWindow frw=new FileReceiveWindow(um,FileSendPort++);
				
			}else if(Instruction1==JOptionPane.NO_OPTION){
				UserMessage DisagreeFileSend=new UserMessage(MainWindow.SendFile.getId(),MainWindow.SendFile.getIp(),6,"Disagree#"+um.getMessage());
				this.sendUserMessage(DisagreeFileSend, um.getIp());				
			}else{
				
			}	
			break;
		
		case 6://收到对方回发的文件消息
			if(um.getMessage().startsWith("Agree")){//收到同意消息
				String[] tmp=um.getMessage().split("#");
				FileSendWindow fsw = (FileSendWindow) MainWindow.FileSend.get(tmp[1]);
				fsw.ModifyInfo("对方已同意，正在准备TCP连接...");
				fsw.setPort(Integer.parseInt(tmp[3]));
				fsw.suspend.setEnabled(true);
				fsw.exit.setEnabled(true);
				Thread t = new Thread(fsw);
				t.start();
				
			}
			else if(um.getMessage().startsWith("Disagree")){//收到不同意
				String[] tmp = um.getMessage().split("#");
				FileSendWindow fsw = (FileSendWindow) MainWindow.FileSend.get(tmp[1]);
				if (fsw.getFileName().equalsIgnoreCase(tmp[1])) {
					fsw.ModifyInfo("对方拒绝了你的发送文件申请");
					fsw.send.setEnabled(true);
				}
			}				
			break;
		}
			

	}

	public static void main(String[] arg) {
//		new MainServer(8760);
//		UserMessage um=new UserMessage("wangfei","192.168.1.1",5,"Database.mdb");
//		int Instruction1=JOptionPane.showConfirmDialog(null, 
//				"收到来自" + um.getIp() + "(" + um.getId() + ")的发送文件请求：\n"+um.getMessage()+"\n是否接收？","收到发送文件请求",
//				JOptionPane.YES_NO_OPTION);
//		if(Instruction1==JOptionPane.YES_OPTION){
//			
//		}else if(Instruction1==JOptionPane.NO_OPTION){
//			
//		}else{
//			
//		}	
//		String s="Database.mdb 20";
//		int SpaceIndex=s.indexOf(' ');
//		String Filename=s.substring(0,SpaceIndex);
//		
//		int size=Integer.parseInt(s.substring(SpaceIndex+1,s.length()));
//		System.out.print(Filename+" "+size);
//		System.out.print(""+(int)(Math.random()*10));
	}

	public void sendUserMessage(UserMessage um, String ip) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(um);
			oos.flush();

			InetAddress server = InetAddress.getByName(ip);

			byte arr[] = baos.toByteArray();

			DatagramPacket sendDp = new DatagramPacket(arr, arr.length, server,
					8760);
			DatagramSocket ds = new DatagramSocket();

			ds.send(sendDp);
			oos.close();
			baos.close();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void broadcastUserMessage(UserMessage um, LinkedList ll) {
		for (int i = 0; i < ll.size(); i++) {
			sendUserMessage(um, (String) ll.get(i));
		}
	}
}
