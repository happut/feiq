package View;    

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import javax.swing.*;

import Model.UserMessage;

/** 
 * @author Happut-WangFei
 * @version 
 * @time 2011-10-28 ����9:04:58
 */
public class FileSendWindow extends JFrame implements Runnable,ActionListener{
	private JLabel info;
	private JProgressBar progress;
	
	private JPanel infoPanel;
	private JPanel proPanel;
	private JPanel buttonPanel;
	
	public JButton send;
	public JButton suspend;
	public JButton exit;
	
	private boolean isSuspend;
	private boolean isExit;
		
	private String FileName;
	private int FileSize;
	
	private String ip;
	private String id;
	
	private int port;
	
	private File file;
	
	public void setPort(int port) {
		this.port = port;
	}

	public FileSendWindow(UserMessage um, File file){
		//��ʼ���׶�
		ip=um.getIp();
		id=um.getId();
		
		this.file=file;
		
		FileName=file.getName();		
		FileSize=(int) file.length();
				
		this.setTitle("����"+FileName+"�ļ����ļ���СΪ"+FileSize+"�ֽ�");		
		info=new JLabel("��������Ա㿪ʼ...");
		
		progress=new JProgressBar(0,FileSize);
		
		send=new JButton("����");
		send.addActionListener(this);
		suspend=new JButton("��ͣ");
		suspend.setEnabled(false);
		suspend.addActionListener(this);
		exit=new JButton("�жϽ���");
		exit.setEnabled(false);
		exit.addActionListener(this);
		
		infoPanel=new JPanel();
		proPanel=new JPanel();
		buttonPanel=new JPanel();
		
		isSuspend=false;
		isExit=false;
		
		//���Ԫ�ص��������
		infoPanel.add(info);
		
		proPanel.add(progress);
		
		buttonPanel.add(send);
		buttonPanel.add(suspend);
		buttonPanel.add(exit);
		
		//���ڹ���
		this.getContentPane().add(infoPanel,"North");
		this.getContentPane().add(proPanel,"Center");
		this.getContentPane().add(buttonPanel,"South");
		
		this.setResizable(false);

		
		this.setSize(400, 150);
		this.setVisible(true);
		
		
		MainWindow.FileSend.put(FileName, this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.FileSend.remove(FileName);
			}
		});		

	}
	
	public void run(){
		try {
			Socket sock = new Socket(ip, port);       //�����׽���
			ModifyInfo("TCP���ӳ�ʼ����ϣ��˿�Ϊ��"+port+"��׼������...");

			BufferedOutputStream netOut = new BufferedOutputStream(sock.getOutputStream());			
			FileInputStream fileIn = new FileInputStream(file);
			
			int ch;
			while ((ch = fileIn.read()) != -1) {
				while (isSuspend) {

				}
				netOut.write(ch);
				ModifyProgressBar();
				ModifyInfo("�Ѵ���"+progress.getValue()+"�ֽ�...");
			}
			fileIn.close();
	        netOut.close();
	        sock.close();
	        ModifyInfo("�������");
	        send.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public static void main(String[] arg){
//		JFileChooser sfc=new JFileChooser() ;	
//		sfc.setDialogTitle("ѡ��Ҫ���͵��ļ�");
//		sfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		if(JFileChooser.APPROVE_OPTION==sfc.showOpenDialog(null))
//		{
//			File f=sfc.getSelectedFile();
//			System.out.println(f.getName());
//			UserMessage um=new UserMessage("wangfei","192.168.1.1",5,"");
//			new FileSendWindow(um,8760);
//			File f=new File(".\\db\\Database.mdb");
//			FileSendWindow fsw=new FileSendWindow(um,f);
//		}

//		System.out.print(System.getProperty(".separator"));
		
//		
//		try{
//		File f=new File(".\\db\\GetBeta.exe");
//		FileInputStream fileIn = new FileInputStream(f);
//	
//		
//    	File f2=new File(".\\FileReceive\\GetBeta.exe");
//    	f2.createNewFile();
//    	
//        FileOutputStream fo = new FileOutputStream(f2);	
//		int ch;
//        while((ch=fileIn.read())!=-1){
//        	fo.write(ch);
//        }
//        fo.close();
//        fileIn.close();	
//		}catch (Exception e) {
//			e.printStackTrace();
//
//		}

//		try {
//			Socket sock = new Socket("127.0.0.1", 10000);       //�����׽���
//
//			File file=new File(".\\db\\GetBeta.exe");
//			BufferedOutputStream netOut = new BufferedOutputStream(sock.getOutputStream());			
//			FileInputStream fileIn = new FileInputStream(file);
//			
//			int ch;
//			while ((ch = fileIn.read()) != -1) {
//				netOut.write(ch);
//			}
//			fileIn.close();
//	        netOut.close();
//	        sock.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==send){
			ModifyInfo("���ڵȴ��Է���Ӧ...");
			UserMessage um=new UserMessage(MainWindow.SendFile.getId(),MainWindow.SendFile.getIp(),5,this.FileName+"#"+this.FileSize);
			MainWindow.ms.sendUserMessage(um, ip);
			send.setEnabled(false);

		}else if(e.getSource()==suspend){
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

	public String getFileName() {
		return FileName;
	}

	public int getFileSize() {
		return FileSize;
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
  
