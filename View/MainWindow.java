package View;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.*;

import Model.UserMessage;
import SystemKernel.DBUtils;
import SystemKernel.MainServer;

public class MainWindow extends JFrame implements ActionListener {
	public static UserMessage OnInfo, OffInfo, Chat, SendFile;
	public static LinkedList Online;
	public static HashMap FileSend;
	public static HashMap FileRecv;
	public static HashMap ChattingList;
	public static JPanel RGKS_1_Panel;
	public static JPanel RGKS_2_Panel;
	public static DBUtils db;
	
	public static boolean isGroupSend;

	JPanel OtherPanel;

	JButton groupsend;
	JButton chatrecorder;
	JButton exit;

	JScrollPane scroll1;
	JScrollPane scroll2;

	public static MainServer ms;

	public MainWindow(String id, String team) throws UnknownHostException {

		this.setTitle(id);

		// 初始化在线人数组以及打开窗口数组
		Online = new LinkedList();
		FileSend = new HashMap();
		FileRecv = new HashMap();
		ChattingList = new HashMap();

		// 创建界面
		// 创建RGKS_1_Panel
		RGKS_1_Panel = new JPanel(new FlowLayout());
		RGKS_1_Panel.setPreferredSize(new Dimension(100, 200));
		scroll1 = new JScrollPane(RGKS_1_Panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setPreferredSize(new Dimension(125, 200));
		scroll1.setBorder(BorderFactory.createTitledBorder("RGKS_1组"));


		// 创建RGKS_2_Panel
		RGKS_2_Panel = new JPanel(new FlowLayout());
		RGKS_2_Panel.setPreferredSize(new Dimension(100, 200));
		scroll2 = new JScrollPane(RGKS_2_Panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2.setPreferredSize(new Dimension(125, 200));
		scroll2.setBorder(BorderFactory.createTitledBorder("RGKS_2组"));

		// 创建OtherPanel
		OtherPanel = new JPanel(new GridLayout(3, 1));
		groupsend = new JButton("群发消息");
		groupsend.addActionListener(this);
		chatrecorder = new JButton("聊天记录");
		chatrecorder.addActionListener(this);
		exit = new JButton("退出");
		exit.addActionListener(this);
		OtherPanel.add(groupsend);
		OtherPanel.add(chatrecorder);
		OtherPanel.add(exit);

		this.getContentPane().add(scroll1, "North");
		this.getContentPane().add(scroll2);
		this.getContentPane().add(OtherPanel, "South");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();

		// 获取本机IP "192.168.1.101"
		String ip = InetAddress.getLocalHost().getHostAddress();

		// 组装UserMessage
		OnInfo = new UserMessage(id, ip, 0, team);
		OffInfo = new UserMessage(id, ip, 1, team);
		Chat = new UserMessage(id, ip, 3, "");
		SendFile = new UserMessage(id,ip,5,"SendFile");

		System.out.println(ip.substring(0, ip.lastIndexOf('.') + 1));

		// 创建ip段数组
		LinkedList ipDuan = new LinkedList();
		String Preip = ip.substring(0, ip.lastIndexOf('.') + 1);
		for (int i = 1; i < 255; i++) {
			ipDuan.add(Preip + i);
		}
		ipDuan.remove(ip);

		System.out.println(ip);

		// 启动UDP服务器
		ms = new MainServer(8760);

		// 群发上线消息
		ms.broadcastUserMessage(OnInfo, ipDuan);
		
		db=new DBUtils();

		// 程序退出所执行操作
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ms.broadcastUserMessage(OffInfo, Online);
			}
		});
	}

	public static void main(String[] arg) {
		try {
			new MainWindow("wangfei", "RGKS-1");
			UserItem ui = new UserItem("172.211.371.411", "wangyirui");
			MainWindow.RGKS_1_Panel.add(ui);
			MainWindow.RGKS_1_Panel.validate();
			MainWindow.RGKS_1_Panel.updateUI();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == groupsend && isGroupSend == false) {
			new GroupSendWindow(this.Online);
			isGroupSend=true;
		} else if (e.getSource() == chatrecorder) {
			new ChatRecordManagerWindow();
		} else if (e.getSource() == exit) {
			ms.broadcastUserMessage(OffInfo, Online);
			this.dispose();
			System.exit(0);
		} else {

		}

	}
}