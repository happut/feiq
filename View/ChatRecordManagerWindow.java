package View;    

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Model.UserManagerData;

/** 
 * @author Happut-WangFei
 * @version 
 * @time 2011-10-27 上午11:14:07
 */
public class ChatRecordManagerWindow extends JFrame{
	private JPanel Panel1;
	private JScrollPane MainPanel;
	
	private JLabel txt;
	private JTable table;
	public ChatRecordManagerWindow(){
		this.setTitle("与本机连接的其他客户端列表");
		
		Panel1=new JPanel();

		txt=new JLabel("最后与本机连接的其他客户端的信息如下：");
		Panel1.add(txt);
		////////////////////////////////////
		String[] headers = { "IP地址", "最后登录名", "最后聊天时间" };

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

		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		LinkedList<UserManagerData> ll = MainWindow.db.QueryManagerData();
		
		// 填充数据
		for (UserManagerData tmp : ll) {
			String[] arr = new String[3];
			arr[0] = tmp.getIp();
			arr[1] = tmp.getId();
			arr[2] = tmp.getChattime();

			// 添加数据到表格
			tableModel.addRow(arr);
		}

		tableModel.setRowCount(10);
		// 更新表格
		table.invalidate();
		MainPanel = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		MainPanel.setPreferredSize(new Dimension(352, 181));
		//////////////////////////////////////////////
		this.getContentPane().add(Panel1, "North");
		this.getContentPane().add(MainPanel, "Center");

		this.pack();
		this.setVisible(true);
		
		table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow(); 
				String ip=(String) tableModel.getValueAt(selectedRowIndex, 0);
				if(ip!=null){
					System.out.println(selectedRowIndex+"   "+ip);
					String table=ip.replace('.', '_');
					
					ChatRecordWindow crw=new ChatRecordWindow(table);
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}
	
	public static void main(String[] arg){
		new ChatRecordManagerWindow();
	}
}
  
