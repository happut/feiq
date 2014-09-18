package View;

import java.awt.*;
import javax.swing.*;

import Model.UserMessage;
import SystemKernel.MainWindowClick;
import SystemKernel.MainWindowPopupMenu;

public class UserItem extends JPanel {
	JLabel ip;
	JLabel id;

	public UserItem(String ip, String id) {
		this.setLayout(new GridLayout(2, 1));

		this.ip = new JLabel(ip);
		this.id = new JLabel(id);

		this.ip.setFont(new Font("Dialog", 1, 12));
		this.id.setFont(new Font("Dialog", 0, 8));

		this.id.setForeground(Color.GRAY);
		this.add(this.ip);
		this.add(this.id);
		this.addMouseListener(new MainWindowClick());
		this.setComponentPopupMenu(new MainWindowPopupMenu(new UserMessage(id,ip,0,"")));
		this.setPreferredSize(new Dimension(100, 30));
	}

	public boolean CheckUser(String ip, String id) {
		if (this.ip.getText().equalsIgnoreCase(ip)
				&& this.id.getText().equalsIgnoreCase(id)) {
			return true;
		} else {
			return false;
		}
	}

	public String getIp() {
		return ip.getText();
	}

	public String getId() {
		return id.getText();
	}
}
