package SystemKernel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;

import Model.UserMessage;
import View.ChatRecordWindow;
import View.FileSendWindow;
import View.MainWindow;

public class MainWindowPopupMenu extends JPopupMenu implements ActionListener{

	private UserMessage um;
	
	public MainWindowPopupMenu(UserMessage um){
		this.um=um;
		add("发送文件").addActionListener(this);
		add("查看聊天记录").addActionListener(this);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("发送文件"))
		{
			//创建一个文件打开对话框
			JFileChooser jfc=new JFileChooser() ;	
			jfc.setDialogTitle("选择要发送的文件");
			jfc.setMultiSelectionEnabled(false);
			if(JFileChooser.APPROVE_OPTION==jfc.showOpenDialog(null)){
				//接收文件名以及相应的路径信息
				
				File f=jfc.getSelectedFile();
				
				UserMessage FileSend=new UserMessage(um.getId(),um.getIp(),5,f.getName()+" "+f.length());
				
				FileSendWindow fsw=new FileSendWindow(um,f);
			}

		
		}else if(e.getActionCommand().equals("查看聊天记录")){
			String ip=um.getIp();
			String table=ip.replace('.', '_');
			ChatRecordWindow crw=new ChatRecordWindow(table);
		}
		
	}

}
