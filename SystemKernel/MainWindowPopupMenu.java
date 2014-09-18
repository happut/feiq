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
		add("�����ļ�").addActionListener(this);
		add("�鿴�����¼").addActionListener(this);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("�����ļ�"))
		{
			//����һ���ļ��򿪶Ի���
			JFileChooser jfc=new JFileChooser() ;	
			jfc.setDialogTitle("ѡ��Ҫ���͵��ļ�");
			jfc.setMultiSelectionEnabled(false);
			if(JFileChooser.APPROVE_OPTION==jfc.showOpenDialog(null)){
				//�����ļ����Լ���Ӧ��·����Ϣ
				
				File f=jfc.getSelectedFile();
				
				UserMessage FileSend=new UserMessage(um.getId(),um.getIp(),5,f.getName()+" "+f.length());
				
				FileSendWindow fsw=new FileSendWindow(um,f);
			}

		
		}else if(e.getActionCommand().equals("�鿴�����¼")){
			String ip=um.getIp();
			String table=ip.replace('.', '_');
			ChatRecordWindow crw=new ChatRecordWindow(table);
		}
		
	}

}
