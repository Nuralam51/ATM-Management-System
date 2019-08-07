package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jaco.mp3.player.MP3Player;

public class LogIn extends JFrame{

	JPanel panelLogin =new JPanel();
	JPanel panelNorth = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelCenterSouth = new JPanel();
	JPanel panelCenterCenter = new JPanel();
	JPanel panelCenterNorth = new JPanel();
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelWest = new JPanel();
	JPanel panelWestNorth = new JPanel();
	JPanel panelWestSouth = new JPanel();

	JLabel lblUserNameIcon = new JLabel(new ImageIcon("image/username.png"));
	JLabel lblPasswordIcon = new JLabel(new ImageIcon("image/password.png"));
	JLabel lblUserTypeIcon = new JLabel(new ImageIcon("image/usertype.png"));
	JLabel lblimageuserIcon = new JLabel(new ImageIcon("image/imageuser.png"));
	JLabel lblAccountNo = new JLabel("Account No");
	JLabel lblPassword = new JLabel("password");
	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop.png"));
	JLabel lblQuotesbottomIcon = new JLabel(new ImageIcon("image/Quotesbottom.png"));

	JTextField txtAccountNo = new JTextField(30);
	JPasswordField txtPassword = new JPasswordField();
	SuggestText cmbUserType = new SuggestText();

	JButton btnLogin = new JButton("Log In", new ImageIcon("image/login.png"));

	SessionBean sessionBean = new SessionBean();
	
	public LogIn() {
		frame();
		cmp();
		btnAction();
	}
	private void frame() {
		setSize(845, 535);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Log In");
	}
	private boolean checkValidation() {
		if(!txtAccountNo.getText().trim().isEmpty()) {
			if(!txtPassword.getText().trim().isEmpty()) {
				return true;
			}
			else {
				JOptionPane.showMessageDialog(null,"Insert password");
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "insert account no");
		}
		return false;
	}
	private boolean checkAccountNo() {
		if(cmbUserType.cmbSuggest.getSelectedItem()=="Admin") {
			try {
				String query="select * from tbadmininfo where accountno='"+txtAccountNo.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					txtAccountNo.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid AccountNo");
					txtAccountNo.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
				}
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		else {
			try {
				String query="select * from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					txtAccountNo.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid AccountNo");
					txtAccountNo.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
				}
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		return false;
	}
	private boolean checkPassword() {
		if(cmbUserType.cmbSuggest.getSelectedItem()=="Admin") {
			try {
				String query="select * from tbadmininfo where accountno = '"+txtAccountNo.getText().trim()+"' and password='"+txtPassword.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					txtPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid Password");
					txtPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
					JOptionPane.showMessageDialog(null, "Insert Right Password or Reset your Password. "
							+ "For reset Password click Forget Password");
				}
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		else {
			try {
				String query="select * from tbcustomerinfo where accountno = '"+txtAccountNo.getText().trim()+"' and password='"+txtPassword.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					txtPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid Password");
					txtPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
					JOptionPane.showMessageDialog(null, "Insert Right Password or Reset your Password. "
							+ "For reset Password click Forget Password");
				}
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		return false;
	}
	private boolean checkUserType() {
		if(cmbUserType.cmbSuggest.getSelectedItem()=="Admin") {
			try {
				String query="select * from tbadmininfo where accountno='"+txtAccountNo.getText().trim()+"'"
						+ "and password='"+txtPassword.getText().trim()+"'"
						+ "and designation='"+cmbUserType.txtSuggest.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					cmbUserType.cmbSuggest.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid UserType");
					cmbUserType.cmbSuggest.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
				}
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		else {
			try {
				String query="select * from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'"
						+ "and password='"+txtPassword.getText().trim()+"'"
						+ "and designation='"+cmbUserType.txtSuggest.getText().trim()+"'";
				dbConnection.connect();
				ResultSet rs = dbConnection.sta.executeQuery(query);
				if(rs.next()) {
					cmbUserType.cmbSuggest.setBorder(BorderFactory.createEtchedBorder(0, Color.decode("#2ecc71"), Color.decode("#2ecc71")));
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid UserType");
					cmbUserType.cmbSuggest.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
				}
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		return false;
	}
	private void UserloginAction() {
		sessionBean.setUserId(txtAccountNo.getText().trim());
		panelLogin.setVisible(false);
		WorkingPanel workingpanel = new WorkingPanel(this, sessionBean);
		add(workingpanel);
		setTitle("User");
		workingpanel.setVisible(true);
		setSize(1150, 720);
		setLocationRelativeTo(null);
		String song = "audio/UserLogin.mp3";
		MP3Player audio = new MP3Player(new File(song));
		audio.play();
	}
	private void AdminloginAction() {
		sessionBean.setUserId(txtAccountNo.getText().trim());
		panelLogin.setVisible(false);
		AdminPanel adminpanel = new AdminPanel(this, sessionBean);
		add(adminpanel);
		adminpanel.setVisible(true);
		setSize(1150, 720);
		setTitle("Admin Portal");
		setLocationRelativeTo(null);
		String song = "audio/AdminLogin.mp3";
		MP3Player audio = new MP3Player(new File(song));
		audio.play();
	}
	private void btnAction() {
		btnLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(checkValidation()) {
					if(checkAccountNo()) {
						if(checkPassword()) {
							if(checkUserType()) {
								if(cmbUserType.cmbSuggest.getSelectedItem()=="User") {
									UserloginAction();
								}
								else{
									AdminloginAction();
								}
							}
						}
					}
				}
			}
		});
		txtAccountNo.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if(txtAccountNo.getText().trim().isEmpty()) {
					lblAccountNo.setVisible(true);
				}
				else {
					lblAccountNo.setVisible(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {}
		});
		txtPassword.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if(txtPassword.getText().trim().isEmpty()) {
					lblPassword.setVisible(true);
				}
				else {
					lblPassword.setVisible(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {}
		});
		txtAccountNo.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				//lblAccountNo.setVisible(true);
				if(txtAccountNo.getText().trim().isEmpty()) {
					lblAccountNo.setVisible(true);
				}
				else {
					lblAccountNo.setVisible(false);
				}
			}
			public void focusGained(FocusEvent arg0) {
				lblAccountNo.setVisible(false);
			}
		});
		txtPassword.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				//lblPassword.setVisible(true);
				if(txtPassword.getText().trim().isEmpty()) {
					lblPassword.setVisible(true);
				}
				else {
					lblPassword.setVisible(false);
				}
			}
			public void focusGained(FocusEvent arg0) {
				lblPassword.setVisible(false);
			}
		});
		
		/*if(txtAccountNo.getText().trim().isEmpty()) {
			lblAccountNo.setVisible(true);
		}
		else {
			lblAccountNo.setVisible(false);
		}
		
		if(txtPassword.getText().trim().isEmpty()) {
			lblPassword.setVisible(true);
		}
		else {
			lblPassword.setVisible(false);
		}*/
	}
	private void cmp() {
		add(panelLogin);
		panelLogin.setLayout(new BorderLayout());
		panelLogin.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelLogin.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelLogin.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelLogin.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelLogin.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(60, 0));
		panelEast.setBackground(Color.decode("#39b5ff"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(390, 0));
		panelWest.setBackground(Color.decode("#39b5ff"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 65));
		panelWestNorth.setBackground(Color.decode("#39b5ff"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#39b5ff"));
		panelWestSouth.add(lblQuotesbottomIcon);
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 50));
		panelNorth.setBackground(Color.decode("#39b5ff"));
	}
	private void panelcenterwork() {
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelcentercenterwork();
		panelCenter.add(panelCenterSouth, BorderLayout.SOUTH);
		panelcentersouthwork();
		panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
		panelcenternorthwork();
	}
	private void panelcenternorthwork() {
		panelCenterNorth.setPreferredSize(new Dimension(0, 100));
		panelCenterNorth.setBackground(Color.white);
	}
	private void panelcentercenterwork() {
		panelCenterCenter.setBackground(Color.white);
		GridBagConstraints grid = new GridBagConstraints();
		panelCenterCenter.setLayout(new GridBagLayout());
		grid.fill = GridBagConstraints.BOTH;
		grid.insets = new Insets(5, 5, 5, 5);

		grid.gridx = 0;
		grid.gridy = 0;
		panelCenterCenter.add(lblUserNameIcon, grid);

		grid.gridx = 1;
		grid.gridy = 0;
		panelCenterCenter.add(txtAccountNo, grid);
		txtAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		FlowLayout flow = new FlowLayout();
		txtAccountNo.setLayout(flow);
		flow.setAlignment(0);
		flow.setVgap(5);
		txtAccountNo.add(lblAccountNo);
		lblAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		lblAccountNo.setForeground(Color.decode("#979a9a"));

		grid.gridx = 0;
		grid.gridy = 1;
		panelCenterCenter.add(lblPasswordIcon, grid);

		grid.gridx = 1;
		grid.gridy = 1;
		panelCenterCenter.add(txtPassword, grid);
		txtPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtPassword.setLayout(flow);
		flow.setVgap(5);
		txtPassword.add(lblPassword);
		lblPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		lblPassword.setForeground(Color.decode("#979a9a"));

		grid.gridx = 0;
		grid.gridy = 2;
		panelCenterCenter.add(lblUserTypeIcon, grid);

		grid.gridx = 1;
		grid.gridy = 2;
		panelCenterCenter.add(cmbUserType.cmbSuggest, grid);
		cmbUserType.cmbSuggest.setBackground(Color.white);
		cmbUserType.cmbSuggest.setFont(new Font("Monaco", Font.PLAIN, 13));
		cmbUserType.txtSuggest.setText("User");
		cmbUserType.cmbSuggest.addItem("User");
		cmbUserType.cmbSuggest.addItem("Admin");
	}
	private void panelcentersouthwork() {
		panelCenterSouth.setPreferredSize(new Dimension(0, 140));
		FlowLayout flow = new FlowLayout();
		panelCenterSouth.setLayout(flow);
		flow.setHgap(5);
		panelCenterSouth.setBackground(Color.white);
		panelCenterSouth.add(btnLogin);
		btnLogin.setPreferredSize(new Dimension(130, 35));
		btnLogin.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnLogin.setFocusable(false);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 50));
		panelSouth.setBackground(Color.decode("#39b5ff"));
	}
}
