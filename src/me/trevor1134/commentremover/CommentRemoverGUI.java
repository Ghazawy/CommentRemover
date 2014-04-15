package me.trevor1134.commentremover;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/*
 * CommentRemover - Remove all comments from a java file.
 * Copyright (C) 2014 Trevor1134
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

public class CommentRemoverGUI {

	private JButton btnCopy;
	private JButton btnProccess;
	private JButton btnReset;
	private String extension = "java";
	private JFileChooser fc;
	private JFrame frame;
	private JTextArea jta;
	private JTextField textField;
	private String VERSION = "1.0";

	/**
	 * Create the application.
	 */
	public CommentRemoverGUI() {
		initialize();
	}

	/**
	 * Method getBrowseListener.
	 * 
	 * @return ActionListener
	 */
	private ActionListener getBrowseListener() {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileFilter(getFileFilter());

				int returnVal = fc.showOpenDialog((Component) e.getSource());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					textField.setText(file.getAbsolutePath());

				}
			}
		};
		return listener;

	}

	/**
	 * Method getFileFilter.
	 * 
	 * @return FileFilter
	 */
	private FileFilter getFileFilter() {

		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				String path = f.getAbsolutePath().toLowerCase();
				if (f.isDirectory())
					return true;

				if ((path.endsWith("java") && ((path.charAt(path.length() - extension.length() - 1)) == '.')))
					return true;
				return false;
			}

			@Override
			public String getDescription() {
				return "Java Files";
			}
		};
		return filter;
	}

	/**
	 * Method getProccessListener.
	 * 
	 * @return ActionListener
	 */
	private ActionListener getProccessListener() {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText() == null)
					return;
				File f = new File(textField.getText());
				if (!f.isFile()) {
					System.out.println("The selected path appears to be invalid.");
					return;
				}
				String n = readFile(textField.getText());

				btnProccess.setVisible(false);
				btnReset.setVisible(true);
				btnCopy.setVisible(true);

				jta.setText(n.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", ""));

			}
		};
		return listener;
	}

	/**
	 * Method getResetListener.
	 * 
	 * @return ActionListener
	 */
	private ActionListener getResetListener() {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnProccess.setVisible(true);
				btnReset.setVisible(false);
				btnCopy.setVisible(false);
				fc.setSelectedFile(null);

				jta.setText("Awaiting File...");
				textField.setText("");
			}
		};
		return listener;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setTitle("CommentRemover v" + VERSION);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 594, 50);
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		Font fon = new Font("Arial", 1, 12);
		JLabel lblCommentremover = new JLabel("CommentRemover");
		lblCommentremover.setBounds(10, 11, 106, 14);
		lblCommentremover.setFont(fon);
		panel.add(lblCommentremover);

		JLabel lblRemoveAllComments = new JLabel("Remove all comments from a .java file.");
		lblRemoveAllComments.setBounds(10, 25, 186, 14);
		panel.add(lblRemoveAllComments);

		JLabel lblVersionnAuthor = new JLabel("<html><body style='width: 75px'>" + "Version: " + VERSION
				+ "<br>Author: Trevor1134" + "</html>");
		lblVersionnAuthor.setBounds(487, 12, 97, 27);
		panel.add(lblVersionnAuthor);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 50, 594, 1);
		frame.getContentPane().add(separator);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 50, 594, 50);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(134, 17, 20, 14);
		panel_1.add(lblFile);

		textField = new JTextField();
		textField.setBounds(159, 14, 206, 20);
		panel_1.add(textField);
		textField.setColumns(40);
		textField.setEditable(false);

		final JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(this.getBrowseListener());

		btnBrowse.setBounds(370, 13, 79, 23);
		panel_1.add(btnBrowse);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 150, 594, 221);
		frame.getContentPane().add(panel_2);

		JScrollPane jsp;
		panel_2.add(jsp = new JScrollPane(jta = new JTextArea()));

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 99, 594, 50);
		frame.getContentPane().add(panel_3);

		btnProccess = new JButton("Proccess!");
		btnProccess.addActionListener(this.getProccessListener());
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnReset = new JButton("Reset");
		panel_3.add(btnReset);
		btnReset.setVisible(false);
		btnReset.addActionListener(getResetListener());

		Component horizontalStrut = Box.createHorizontalStrut(25);
		panel_3.add(horizontalStrut);
		panel_3.add(btnProccess);

		Component horizontalStrut_1 = Box.createHorizontalStrut(25);
		panel_3.add(horizontalStrut_1);

		btnCopy = new JButton("Copy To Clipboard");
		panel_3.add(btnCopy);
		btnCopy.setVisible(false);
		btnCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jta.selectAll();
				jta.copy();

				JOptionPane.showMessageDialog(frame, "Code copied to clipboard!");
				return;
			}
		});

		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setVisible(true);
		jta.setText("Awaiting File...");
		jta.setRows(10);
		jta.setColumns(65);
	}

	/**
	 * Method readFile.
	 * 
	 * @param fileName
	 *        String
	 * @return String
	 */
	private String readFile(String fileName) {

		File file = new File(fileName);

		char[] buffer = null;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

			buffer = new char[(int) file.length()];

			int i = 0;
			int c = bufferedReader.read();

			while (c != -1) {
				buffer[i++] = (char) c;
				c = bufferedReader.read();
			}
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buffer);
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *        String[]
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					CommentRemoverGUI window = new CommentRemoverGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}