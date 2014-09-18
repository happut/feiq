package View;    

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

import Model.UserMessage;

/** 
 * @author Happut-WangFei
 * @version 
 * @time 2011-10-28 ����9:04:58
 */
public class FileReceiveWindow extends JFrame implements Runnable,ActionListener{
	private JLabel info;
	private JProgressBar progress;
	
	private JPanel infoPanel;
	private JPanel proPanel;
	private JPanel buttonPanel;
	
	private JButton suspend;
	private JButton exit;
	
	private boolean isSuspend;
	private boolean isExit;
	
	private int port;
	
	private String FileName;
	private int FileSize;
	
	
	public FileReceiveWindow(UserMessage um,int port){
		//��ʼ���׶�
		this.port=port;

		int SpaceIndex=um.getMessage().indexOf('#');
		FileName=um.getMessage().substring(0,SpaceIndex);
		
		FileSize=Integer.parseInt(um.getMessage().substring(SpaceIndex+1, um.getMessage().length()));
				
		this.setTitle("����"+FileName+"�ļ����ļ���СΪ"+FileSize+"�ֽ�");
		
		info=new JLabel("��ʼ����...");
		
		progress=new JProgressBar(0,FileSize);
		
		suspend=new JButton("��ͣ");
		suspend.addActionListener(this);
		exit=new JButton("�жϽ���");
		exit.addActionListener(this);
		
		infoPanel=new JPanel();
		proPanel=new JPanel();
		buttonPanel=new JPanel();
		
		isSuspend=false;
		isExit=false;
		
		//���Ԫ�ص��������
		infoPanel.add(info);
		
		proPanel.add(progress);
		
		buttonPanel.add(suspend);
		buttonPanel.add(exit);
		
		//���ڹ���
		this.getContentPane().add(infoPanel,"North");
		this.getContentPane().add(proPanel,"Center");
		this.getContentPane().add(buttonPanel,"South");
		
		this.setSize(400, 150);
		this.setVisible(true);
		
		Thread t=new Thread(this);
		t.start();
		
		MainWindow.FileRecv.put(FileName, this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.FileRecv.remove(FileName);
			}
		});	
	}
	
	public void run(){
		try {
			ServerSocket sSock = new ServerSocket(port); // �����������׽���
			ModifyInfo("TCP���ӳ�ʼ����ϣ��˿�Ϊ��" + port + "���ȴ�����...");

			Socket sock = sSock.accept(); // ������Ϣ
			ModifyInfo("�յ����ӣ�׼������...");

			BufferedInputStream netIn=new BufferedInputStream(sock.getInputStream());// 
        	
        	File f=new File(".\\FileReceive\\"+FileName);
        	f.createNewFile();
        	
            FileOutputStream fileOut = new FileOutputStream(f);
            
			int ch;
			while ((ch = netIn.read()) != -1) {
				while (isSuspend) {

				}
				fileOut.write(ch);
				ModifyProgressBar();
				ModifyInfo("�Ѵ���"+progress.getValue()+"�ֽ�...");
			}	
			fileOut.close();
			netIn.close();
			sock.close();
        	
        	ModifyInfo("������ϣ�");


		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public static void main(String[] arg){
//		try {
//			ServerSocket sSock = new ServerSocket(10000); // �����������׽���
//
//			Socket sock = sSock.accept(); // ������Ϣ
//
//			BufferedInputStream netIn=new BufferedInputStream(sock.getInputStream());// 
//        	
//        	File f=new File(".\\FileReceive\\GetBeta.exe");
//        	f.createNewFile();
//        	
//            FileOutputStream fileOut = new FileOutputStream(f);
//            
//			int ch;
//			while ((ch = netIn.read()) != -1) {
//				fileOut.write(ch);
//			}	
//
//			fileOut.close();
//			netIn.close();
//			sock.close();
//
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==suspend){
			if(isSuspend){
				isSuspend=false;
				suspend.setText("��ͣ");
				
			}else{
				isSuspend=true;
				ModifyInfo("����ͣ");
				suspend.setText("����");
			}
			
		}else if(e.getSource()==exit){
			isExit=true;
		}
		
	}

	public void ModifyInfo(String info){
		this.info.setText(info);
		this.infoPanel.validate();
		this.infoPanel.updateUI();
	}
	
	public void ModifyProgressBar(){
		progress.setValue(progress.getValue()+1);
	}
}
  
