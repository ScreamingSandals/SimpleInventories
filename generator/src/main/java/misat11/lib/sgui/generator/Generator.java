package misat11.lib.sgui.generator;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Generator {

	public static void main(String[] args) {
		JFrame frame = new JFrame("SimpleGuiFormat generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.yml, *.yaml", "yml", "yaml");
		fc.setFileFilter(filter);

		JMenuBar bar = new JMenuBar();

		JMenu menu = new JMenu("File");
		bar.add(menu);

		JMenuItem open = new JMenuItem("Open");
		menu.add(open);

		final JMenuItem save = new JMenuItem("Save");
		save.setEnabled(false);
		menu.add(save);

		menu.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			System.exit(0);
		});
		menu.add(exit);

		frame.setJMenuBar(bar);

		final JPanel content = new JPanel(null);
		content.setSize(800, 600);

		JButton button = new JButton("Load configuration");
		button.setBounds(300, 250, 200, 50);

		ActionListener listener = e -> {
			int returnVal = fc.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				Map<String, Object> obj = YamlUtils.readYamlFile(file);

				if (obj == null) {
					JOptionPane.showMessageDialog(frame, "File " + file.getAbsolutePath() + " is not valid yaml!");
					return;
				}

				if (!obj.containsKey("data")) {
					JOptionPane.showMessageDialog(frame,
							"File " + file.getAbsolutePath() + " is not SimpleGuiFormat file!");
					return;
				}

				List<Map<String, Object>> data = (List<Map<String, Object>>) obj.get("data");

				SimpleGuiFormat sgui = new SimpleGuiFormat(data);
				sgui.generateData();

				final List<ItemInfo> prepared = sgui.getPreparedData();

				content.setBackground(Color.decode("#c6c6c6"));

				content.removeAll();

				save.setEnabled(true);
				
				CurrentInformation information = new CurrentInformation();

				JLabel label = new JLabel("Main menu - page 1");
				label.setBounds(44, 44, 300, 50);
				content.add(label);
				
				ImagePanel[] itemContainers = new ImagePanel[SimpleGuiFormat.ITEMS_ON_PAGE];

				for (int i = 0; i < itemContainers.length; i++) {
					try {
						itemContainers[i] = new ImagePanel(
								ImageIO.read(Generator.class.getResourceAsStream("/img/slot.png")));
						final int ii = i;
						itemContainers[i].setImagePanelMouseListener(e2 -> {
							ItemInfo founded = null;
							for (ItemInfo info : prepared) {
								if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page && info.getParent() == information.parent && (info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE) == ii) {
									founded = info;
									break;
								}
							}
							JOptionPane.showMessageDialog(frame, "TODO!!!");
						});
						itemContainers[i].setBounds(44 + 80 * (i % 9), 100 + 80 * (i / 9), 75, 75);
						itemContainers[i].setBackground(Color.decode("#8b8b8b"));
						content.add(itemContainers[i]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				for (ItemInfo info : prepared) {
					if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page && info.getParent() == information.parent) {
						try {
							JPanel panel = new ImagePanel(
									ImageIO.read(Generator.class.getResourceAsStream(info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
							panel.setBounds(7, 7, 60, 60);
							itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(panel);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
				JButton toMainMenu = new JButton("Main menu");
				toMainMenu.setEnabled(false);
				toMainMenu.setBounds(44, 2, 200, 40);
				toMainMenu.addActionListener(e2 -> {
					information.page = 0;
					information.parent = null;
					label.setText("Main menu - page 1");
					toMainMenu.setEnabled(false);

					for (int i = 0; i < itemContainers.length; i++) {
						itemContainers[i].removeAll();
					}

					for (ItemInfo info : prepared) {
						if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page && info.getParent() == information.parent) {
							try {
								JPanel panel = new ImagePanel(
										ImageIO.read(Generator.class.getResourceAsStream(info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
								panel.setBounds(7, 7, 60, 60);
								itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(panel);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}

					SwingUtilities.updateComponentTreeUI(frame);
				});
				content.add(toMainMenu);
				
				JButton previous = new JButton("Previous page");
				previous.setBounds(170, 460, 200, 50);
				previous.setEnabled(false);
				previous.addActionListener(e2 -> {
					if (information.page > 0) {
						information.page--;
						if (information.page <= 0) {
							previous.setEnabled(false);
						}
						for (int i = 0; i < itemContainers.length; i++) {
							itemContainers[i].removeAll();
						}
						String text = "";
						if (information.parent == null) {
							text += "Main menu";
						} else {
							text += information.parent.getItem().obj.containsKey("display-name") ? (String) information.parent.getItem().obj.get("display-name") : "Unknown";
						}
						text += " - page " + (information.page + 1);
						label.setText(text);

						for (ItemInfo info : prepared) {
							if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page && info.getParent() == information.parent) {
								try {
									JPanel panel = new ImagePanel(
											ImageIO.read(Generator.class.getResourceAsStream(info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
									panel.setBounds(7, 7, 60, 60);
									itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(panel);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}

						SwingUtilities.updateComponentTreeUI(frame);
					}
				});
				content.add(previous);
				
				JButton next = new JButton("Next page");
				next.setBounds(430, 460, 200, 50);
				next.addActionListener(e2 -> {
					    if (!previous.isEnabled()) {
					    	previous.setEnabled(true);
					    }
						information.page++;
						for (int i = 0; i < itemContainers.length; i++) {
							itemContainers[i].removeAll();
						}
						String text = "";
						if (information.parent == null) {
							text += "Main menu";
						} else {
							text += information.parent.getItem().obj.containsKey("display-name") ? (String) information.parent.getItem().obj.get("display-name") : "Unknown";
						}
						text += " - page " + (information.page + 1);
						label.setText(text);

						for (ItemInfo info : prepared) {
							if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page && info.getParent() == information.parent) {
								try {
									JPanel panel = new ImagePanel(
											ImageIO.read(Generator.class.getResourceAsStream(info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
									panel.setBounds(7, 7, 60, 60);
									itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(panel);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}

						SwingUtilities.updateComponentTreeUI(frame);
				});
				content.add(next);

				SwingUtilities.updateComponentTreeUI(frame);
				
			    for( ActionListener al : save.getActionListeners() ) {
			    	save.removeActionListener(al);
			    }
			    
			    save.addActionListener(e2 -> {
					int returnVal2 = fc.showSaveDialog(frame);

					if (returnVal2 == JFileChooser.APPROVE_OPTION) {
						File saveFile = fc.getSelectedFile();
						// TODO: save
					}
			    });
			}
		};

		button.addActionListener(listener);
		open.addActionListener(listener);
		content.add(button);

		frame.add(content);
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
