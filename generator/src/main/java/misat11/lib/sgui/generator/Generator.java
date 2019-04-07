package misat11.lib.sgui.generator;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Generator {

	public static void main(String[] args) {
		JFrame frame = new JFrame("SimpleGuiFormat generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

		try {
			InputStream is = Generator.class.getResourceAsStream("/Minecraftia.ttf");
			f = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 13f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Font font = f;

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.yml, *.yaml", "yml", "yaml");
		fc.setFileFilter(filter);

		JMenuBar bar = new JMenuBar();

		JMenu menu = new JMenu("File");
		menu.setFont(font);
		bar.add(menu);

		JMenuItem open = new JMenuItem("Open");
		open.setFont(font);
		menu.add(open);

		final JMenuItem save = new JMenuItem("Save");
		save.setFont(font);
		save.setEnabled(false);
		menu.add(save);

		menu.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			System.exit(0);
		});
		exit.setFont(font);
		menu.add(exit);

		frame.setJMenuBar(bar);

		final JPanel content = new JPanel(null);
		content.setSize(800, 600);

		JButton button = new JButton("Load configuration");
		button.setFont(font);
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
				label.setFont(font);
				label.setBounds(44, 44, 300, 50);
				content.add(label);

				JButton toMainMenu = new JButton("Go to parent");
				toMainMenu.setEnabled(false);
				toMainMenu.setFont(font);
				toMainMenu.setBounds(44, 2, 200, 40);

				JButton manageParent = new JButton("Manage parent");
				manageParent.setEnabled(false);
				manageParent.setFont(font);
				manageParent.setBounds(556, 2, 200, 40);

				JButton addChild = new JButton("Add child");
				addChild.setFont(font);
				addChild.setBounds(556, 44, 200, 40);

				ImagePanel[] itemContainers = new ImagePanel[SimpleGuiFormat.ITEMS_ON_PAGE];

				final OpenModal onOpen = (parent, founded, page, ii) -> {
					try {
						JDialog dialog = new JDialog(frame, "", Dialog.ModalityType.DOCUMENT_MODAL);

						JLabel dialogLabel = new JLabel("Item settings", SwingConstants.CENTER);
						dialogLabel.setFont(font);
						dialogLabel.setBounds(200, 5, 200, 60);
						dialog.add(dialogLabel);

						ImagePanel panel = new ImagePanel(
								ImageIO.read(Generator.class.getResourceAsStream("/img/slot.png")));
						panel.setBounds(262, 70, 75, 75);

						dialog.add(panel);

						ImagePanel insidePanel = null;

						if (founded != null) {
							insidePanel = new ImagePanel(ImageIO.read(Generator.class.getResourceAsStream(
									founded.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
							insidePanel.setBounds(7, 7, 60, 60);
							panel.add(insidePanel);
						}

						JLabel par = new JLabel(
								"Parent: "
										+ (parent != null ? parent.getItem().obj.containsKey("meta")
												? ((Map) parent.getItem().obj.get("meta")).containsKey("display-name")
														? (String) ((Map) parent.getItem().obj.get("meta"))
																.get("display-name")
														: "Unknown Menu"
												: "Unknown Menu" : "Main menu"));
						par.setFont(font);
						par.setBounds(200, 150, 200, 60);
						dialog.add(par);

						JLabel absPos = new JLabel(
								"Absolute position: " + (ii + (information.page * SimpleGuiFormat.ITEMS_ON_PAGE)));
						absPos.setFont(font);
						absPos.setBounds(200, 175, 200, 60);
						dialog.add(absPos);

						JButton stack = new JButton("Item stack");
						if (founded == null) {
							stack.setEnabled(false);
						}
						stack.setFont(font);
						stack.setBounds(75, 235, 125, 60);
						dialog.add(stack);

						JButton variables = new JButton("Variables");
						if (founded == null) {
							variables.setEnabled(false);
						}
						variables.setFont(font);
						variables.setBounds(237, 235, 125, 60);
						dialog.add(variables);

						final ItemInfo in = founded;
						JButton childs = new JButton("Childs");
						if (founded == null) {
							childs.setEnabled(false);
						}
						childs.addActionListener(e3 -> {
							information.parent = in;
							information.page = 0;
							toMainMenu.setEnabled(true);
							manageParent.setEnabled(true);

							for (int j = 0; j < itemContainers.length; j++) {
								itemContainers[j].removeAll();
							}

							String text = "";
							if (information.parent == null) {
								text += "Main menu";
							} else {
								text += information.parent.getItem().obj.containsKey("meta")
										? ((Map) information.parent.getItem().obj.get("meta")).containsKey("display-name")
												? (String) ((Map) information.parent.getItem().obj.get("meta"))
														.get("display-name")
												: "Unknown Menu"
										: "Unknown Menu";
							}
							text += " - page " + (information.page + 1);
							label.setText(text);

							for (ItemInfo info : prepared) {
								if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
										&& info.getParent() == information.parent) {
									try {
										JPanel pp = new ImagePanel(ImageIO.read(Generator.class.getResourceAsStream(
												info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
										pp.setBounds(7, 7, 60, 60);
										itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(pp);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}

							SwingUtilities.updateComponentTreeUI(frame);

							dialog.setVisible(false);
						});
						childs.setFont(font);
						childs.setBounds(400, 235, 125, 60);
						dialog.add(childs);

						dialog.setSize(600, 500);
						dialog.setLayout(null);
						dialog.setLocationRelativeTo(null);
						dialog.setVisible(true);
					} catch (Exception ex) {

					}
				};

				for (int i = 0; i < itemContainers.length; i++) {
					try {
						itemContainers[i] = new ImagePanel(
								ImageIO.read(Generator.class.getResourceAsStream("/img/slot.png")));
						final int ii = i;
						itemContainers[i].setImagePanelMouseListener(e2 -> {
							ItemInfo founded = null;
							for (ItemInfo info : prepared) {
								if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
										&& info.getParent() == information.parent
										&& (info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE) == ii) {
									founded = info;
									break;
								}
							}

							onOpen.open(information.parent, founded, information.page, ii);
						});
						itemContainers[i].setBounds(44 + 80 * (i % 9), 100 + 80 * (i / 9), 75, 75);
						content.add(itemContainers[i]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				for (ItemInfo info : prepared) {
					if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
							&& info.getParent() == information.parent) {
						try {
							JPanel panel = new ImagePanel(ImageIO.read(Generator.class
									.getResourceAsStream(info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
							panel.setBounds(7, 7, 60, 60);
							itemContainers[info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE].add(panel);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				toMainMenu.addActionListener(e2 -> {
					information.page = information.parent.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE;
					information.parent = information.parent.getParent();

					if (information.parent == null) {
						toMainMenu.setEnabled(false);
						manageParent.setEnabled(false);
					}

					for (int i = 0; i < itemContainers.length; i++) {
						itemContainers[i].removeAll();
					}
					String text = "";
					if (information.parent == null) {
						text += "Main menu";
					} else {
						text += information.parent.getItem().obj.containsKey("meta")
								? ((Map) information.parent.getItem().obj.get("meta")).containsKey("display-name")
										? (String) ((Map) information.parent.getItem().obj.get("meta"))
												.get("display-name")
										: "Unknown Menu"
								: "Unknown Menu";
					}
					text += " - page " + (information.page + 1);
					label.setText(text);

					for (ItemInfo info : prepared) {
						if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
								&& info.getParent() == information.parent) {
							try {
								JPanel panel = new ImagePanel(ImageIO.read(Generator.class.getResourceAsStream(
										info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
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
				manageParent.addActionListener(e2 -> {
					if (information.parent != null) {
						onOpen.open(information.parent.getParent(), information.parent,
								information.parent.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE,
								information.parent.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE);
					}
				});
				content.add(manageParent);
				addChild.addActionListener(e2 -> {
					
				});
				content.add(addChild);

				JButton previous = new JButton("Previous page");
				previous.setBounds(170, 460, 200, 50);
				previous.setFont(font);
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
							text += information.parent.getItem().obj.containsKey("meta")
									? ((Map) information.parent.getItem().obj.get("meta")).containsKey("display-name")
											? (String) ((Map) information.parent.getItem().obj.get("meta"))
													.get("display-name")
											: "Unknown Menu"
									: "Unknown Menu";
						}
						text += " - page " + (information.page + 1);
						label.setText(text);

						for (ItemInfo info : prepared) {
							if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
									&& info.getParent() == information.parent) {
								try {
									JPanel panel = new ImagePanel(ImageIO.read(Generator.class.getResourceAsStream(
											info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
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
				next.setFont(font);
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
						text += information.parent.getItem().obj.containsKey("meta")
								? ((Map) information.parent.getItem().obj.get("meta")).containsKey("display-name")
										? (String) ((Map) information.parent.getItem().obj.get("meta"))
												.get("display-name")
										: "Unknown Menu"
								: "Unknown Menu";
					}
					text += " - page " + (information.page + 1);
					label.setText(text);

					for (ItemInfo info : prepared) {
						if ((info.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) == information.page
								&& info.getParent() == information.parent) {
							try {
								JPanel panel = new ImagePanel(ImageIO.read(Generator.class.getResourceAsStream(
										info.hasChilds() ? "/img/tree_item.png" : "/img/item.png")));
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

				for (ActionListener al : save.getActionListeners()) {
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
