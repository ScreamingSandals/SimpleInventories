package misat11.lib.sgui.generator;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

		JMenuItem save = new JMenuItem("Save");
		save.setEnabled(false);
		menu.add(save);
		
		menu.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			System.exit(0);
		});
		menu.add(exit);

		frame.setJMenuBar(bar);

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

				List<ItemInfo> prepared = sgui.getPreparedData();

				// TODO
				
				frame.remove(button);

				SwingUtilities.updateComponentTreeUI(frame);
			}
		};
		
		button.addActionListener(listener);
		open.addActionListener(listener);
		frame.add(button);

		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
