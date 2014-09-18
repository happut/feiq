package View;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Model.UserMessage;

public class GroupSendWindow extends JFrame implements ActionListener {
	private JScrollPane panel1;
	private JPanel panel2;
	private JPanel OnlineIP;
	private JTextField send;
	private JButton confirm, exit,allselect;

	private String ip;

	public GroupSendWindow(LinkedList online) {
		// 设定标题
		this.setTitle("群发信息");

		// 创建各个控件
		OnlineIP = new JPanel();
		for (int i = 0; i < online.size(); i++) {
			JCheckBox tmp = new JCheckBox((String) online.get(i));
			OnlineIP.add(tmp);
		}
		send = new JTextField(20);

		panel1 = new JScrollPane(OnlineIP,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel1.setPreferredSize(new Dimension(200, 200));
		panel1.setBorder(BorderFactory.createTitledBorder("在线ip"));

		panel2 = new JPanel();

		confirm = new JButton("发送");
		confirm.addActionListener(this);
		exit = new JButton("退出");
		exit.addActionListener(this);
		allselect = new JButton("全选");
		allselect.addActionListener(this);


		panel2.add(send);
		panel2.add(confirm);
		panel2.add(allselect);
		panel2.add(exit);

		this.getContentPane().add(panel1, "Center");
		this.getContentPane().add(panel2, "South");

		this.setVisible(true);
		this.pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.isGroupSend=false;
			}
		});

	}

	public static void main(String[] arg) {
		LinkedList tmp = new LinkedList();
		tmp.add("192.168.1.1");
		tmp.add("192.168.1.2");
		new GroupSendWindow(tmp);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			Component[] tmp = OnlineIP.getComponents();
			LinkedList ips = new LinkedList();
			for (int i = 0; i < tmp.length; i++) {
				if (((AbstractButton) tmp[i]).isSelected() == true) {
					ips.add((String) (((JCheckBox) tmp[i]).getText()));
				}
			}
			UserMessage um = new UserMessage(MainWindow.OnInfo.getId(),
					MainWindow.OnInfo.getIp(), 4, send.getText());

			MainWindow.ms.broadcastUserMessage(um, ips);
		}else if(e.getSource()==allselect){
			Component[] tmp = OnlineIP.getComponents();
			for (int i = 0; i < tmp.length; i++) {
				if (((AbstractButton) tmp[i]).isSelected() == false) {
					((JCheckBox) tmp[i]).setSelected(true);
				}
			}			
		}else if(e.getSource()==exit){
			MainWindow.isGroupSend=false;
			this.dispose();
		}else{
		
		}
	}
}
