package View;    

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Model.UserChatData;
import Model.UserManagerData;   
/** 
 * @author Happut-WangFei
 * @version 
 * @time 2011-10-27 ����12:20:58
 */
public class ChatRecordWindow extends JFrame{
	private JPanel Panel1;
	private JScrollPane MainPanel;
	
	private JLabel txt;
	private JTable table;
	public ChatRecordWindow(String tablename){
		this.setTitle("�����¼");
		
		Panel1=new JPanel();

		txt=new JLabel("�����¼��");
		Panel1.add(txt);
		////////////////////////////////////
		String[] headers = { "IP��ַ", "����¼��", "����ʱ��" ,"��������"};

		DefaultTableModel model = new DefaultTableModel(null, headers){
		  public boolean isCellEditable(int row, int column) {
		    return false;
		  }
		};
		table = new JTable(model);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(500);

		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		LinkedList<UserChatData> ll = MainWindow.db.QueryChatData(tablename);
		
		// �������
		for (UserChatData tmp : ll) {
			String[] arr = new String[4];
			arr[0] = tmp.getIp();
			arr[1] = tmp.getId();
			arr[2] = tmp.getChattime();
			arr[3] = tmp.getMessage();

			// ������ݵ����
			tableModel.addRow(arr);
		}

		tableModel.setRowCount(10);
		// ���±��
		table.invalidate();
		MainPanel = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		MainPanel.setPreferredSize(new Dimension(852, 181));
		//////////////////////////////////////////////
		this.getContentPane().add(Panel1, "North");
		this.getContentPane().add(MainPanel, "Center");

		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] arg){
		new ChatRecordManagerWindow();
	}
}
  
  
