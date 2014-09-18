package SystemKernel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Model.UserMessage;
import View.ChatWindow;
import View.UserItem;

public class MainWindowClick implements MouseListener {
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == 1) {
			UserMessage um = new UserMessage(
					((UserItem) e.getSource()).getId(),
					((UserItem) e.getSource()).getIp(), 2, ""); // ´´½¨´°¿Ú code:2
			ChatWindow cw = new ChatWindow(um);

		} else if (e.getButton() == 3) {

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
}
