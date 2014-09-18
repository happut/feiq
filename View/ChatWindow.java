package View;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import Model.UserChatData;
import Model.UserManagerData;
import Model.UserMessage;

public class ChatWindow extends JFrame implements ActionListener {
	private JScrollPane panel1;
	private JPanel panel2;
	private JTextArea receive;
	private JTextField send;
	private JButton confirm, exit;

	private String ip;
	private String table;
	
	public ChatWindow(UserMessage um) {
		// 设定标题
		this.setTitle(um.getIp() + "(" + um.getId() + ")");

		// 创建各个控件
		receive = new JTextArea(10, 30);
		receive.setEditable(false);
		receive.setLineWrap(true);
		send = new JTextField(20);

		panel1 = new JScrollPane(receive,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel1.setPreferredSize(new Dimension(200, 200));
		panel2 = new JPanel();

		confirm = new JButton("发送");
		confirm.addActionListener(this);
		exit = new JButton("退出");
		exit.addActionListener(this);

		// panel1.add(receive);

		panel2.add(send);
		panel2.add(confirm);
		panel2.add(exit);

		this.getContentPane().add(panel1, "Center");
		this.getContentPane().add(panel2, "South");

		// 主界面中加入与该窗口关联的数组
		MainWindow.ChattingList.put(um.getIp(), this);
		ip = um.getIp();

		addMessage(um);
		this.setVisible(true);
		this.pack();
		
		//生成表名
		table=um.getIp().replace('.', '_');	
		
		//判断该表对应项目是否存在
		if(!MainWindow.db.CheckTableExist(table)){
			//不存在，就建立新表对应条目
			UserManagerData umd=new UserManagerData();
			umd.setIp(um.getIp());
			umd.setId(um.getId());
			umd.setChattime("");
			umd.setChattable(table);
			
			//插入新表对应项目
			MainWindow.db.InsertManagerData(umd);
			//创建对应表
			MainWindow.db.CreateMessageTable(table);
		}		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.ChattingList.remove(ip);
				// 保存聊天记录
			}
		});

	}

	public static void main(String[] arg) {
		JOptionPane.showMessageDialog(null, "收到来自" + "192.168.1.1" + "("
				+ "wangfei" + ")的消息，请查收", "收到消息",
				JOptionPane.INFORMATION_MESSAGE);
		new ChatWindow(new UserMessage("wangfei", "192.168.1.1", 3, "傻子"));
	}

	public void addMessage(UserMessage um) {
		if (um.getCode() == 2) {
			//手点UserItem，不执行addMessage函数
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			UserChatData ucd=new UserChatData();
			ucd.setIp(um.getIp());
			ucd.setId(um.getId());
			ucd.setChattime(df.format(new Date()));
			ucd.setMessage(um.getMessage());
			this.receive.append(ucd.getIp() + "(" + ucd.getId() + ")"
					+ ucd.getChattime()+ ":" + "\n" + "   "
					+ ucd.getMessage() + "\n");
				//更新管理表中的最后时间
				MainWindow.db.updateManagerTime(ucd.getIp(), ucd.getId(),ucd.getChattime());
				
				//将记录写到相应表
				MainWindow.db.InsertChatData(table, ucd);			
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			UserMessage um = new UserMessage(MainWindow.Chat.getId(),
					MainWindow.Chat.getIp(), 3, send.getText());
			MainWindow.ms.sendUserMessage(um, this.ip);
			addMessage(um);
			this.send.setText("");

		} else if (e.getSource() == exit) {
			MainWindow.ChattingList.remove(ip);
			this.dispose();
		}

	}
}
