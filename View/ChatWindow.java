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
		// �趨����
		this.setTitle(um.getIp() + "(" + um.getId() + ")");

		// ���������ؼ�
		receive = new JTextArea(10, 30);
		receive.setEditable(false);
		receive.setLineWrap(true);
		send = new JTextField(20);

		panel1 = new JScrollPane(receive,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel1.setPreferredSize(new Dimension(200, 200));
		panel2 = new JPanel();

		confirm = new JButton("����");
		confirm.addActionListener(this);
		exit = new JButton("�˳�");
		exit.addActionListener(this);

		// panel1.add(receive);

		panel2.add(send);
		panel2.add(confirm);
		panel2.add(exit);

		this.getContentPane().add(panel1, "Center");
		this.getContentPane().add(panel2, "South");

		// �������м�����ô��ڹ���������
		MainWindow.ChattingList.put(um.getIp(), this);
		ip = um.getIp();

		addMessage(um);
		this.setVisible(true);
		this.pack();
		
		//���ɱ���
		table=um.getIp().replace('.', '_');	
		
		//�жϸñ��Ӧ��Ŀ�Ƿ����
		if(!MainWindow.db.CheckTableExist(table)){
			//�����ڣ��ͽ����±��Ӧ��Ŀ
			UserManagerData umd=new UserManagerData();
			umd.setIp(um.getIp());
			umd.setId(um.getId());
			umd.setChattime("");
			umd.setChattable(table);
			
			//�����±��Ӧ��Ŀ
			MainWindow.db.InsertManagerData(umd);
			//������Ӧ��
			MainWindow.db.CreateMessageTable(table);
		}		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.ChattingList.remove(ip);
				// ���������¼
			}
		});

	}

	public static void main(String[] arg) {
		JOptionPane.showMessageDialog(null, "�յ�����" + "192.168.1.1" + "("
				+ "wangfei" + ")����Ϣ�������", "�յ���Ϣ",
				JOptionPane.INFORMATION_MESSAGE);
		new ChatWindow(new UserMessage("wangfei", "192.168.1.1", 3, "ɵ��"));
	}

	public void addMessage(UserMessage um) {
		if (um.getCode() == 2) {
			//�ֵ�UserItem����ִ��addMessage����
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
				//���¹�����е����ʱ��
				MainWindow.db.updateManagerTime(ucd.getIp(), ucd.getId(),ucd.getChattime());
				
				//����¼д����Ӧ��
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
