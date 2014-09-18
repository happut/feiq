package View;

import java.awt.event.*;
import java.net.*;

import javax.swing.*;

public class IdSetWindow extends JFrame implements ActionListener {
	JPanel panel;
	JTextField id;
	JButton confirm;
	JComboBox choose;

	public IdSetWindow() {
		id = new JTextField(10);
		confirm = new JButton("确认");
		confirm.addActionListener(this);

		String[] a = { "RGKS-1", "RGKS-2" };
		choose = new JComboBox(a);

		panel = new JPanel();

		panel.add(new JLabel("ID:"));
		panel.add(id);
		panel.add(new JLabel("选择小组"));
		panel.add(choose);

		panel.add(confirm);

		this.add(panel);

		this.setVisible(true);
		this.setSize(400, 65);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("" + id.getText() + " " + choose.getSelectedItem());
		try {
			new MainWindow(id.getText(), (String) (choose.getSelectedItem()));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.dispose();
	}

	public static void main(String[] args) {
		new IdSetWindow();
	}
}
