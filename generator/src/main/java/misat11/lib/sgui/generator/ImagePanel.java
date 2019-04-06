package misat11.lib.sgui.generator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements MouseListener {
	
	private ImagePanelMouseListener listener;

	private Image img;

	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}
	
	public void setImagePanelMouseListener(ImagePanelMouseListener listener) {
		this.listener = listener;
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.listener != null) {
			this.listener.mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
