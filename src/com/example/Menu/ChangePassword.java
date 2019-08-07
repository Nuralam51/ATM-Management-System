package com.example.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class ChangePassword extends JFrame{
	
	JPanel panelChangePassword = new JPanel();
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

	JLabel lblAccountNo=new JLabel("Account No");
	JLabel lblOldPassword=new JLabel("Old Password");
	JLabel lblNewPassword=new JLabel("New Password");
	JLabel lblConfirmPassword=new JLabel("Confirm Password");

	JTextField txtAccountNo=new JTextField(25);
	JPasswordField txtOldPassword=new JPasswordField();
	JPasswordField txtNewPassword=new JPasswordField();
	JPasswordField txtConfirmPassword=new JPasswordField();

	JButton btnChange=new JButton("Change");
	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop3.png"));
	
	SessionBean sessionBean;
	
	public ChangePassword(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		btnAction();
	}
	private void btnAction() {
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(CheckMatchPassword()) {
					if(checkConfirmation("Sure to Change Password?")) {
						JOptionPane.showMessageDialog(null, "Password is Changed");
						changePassword();
						txtClear();
					}
				}
			}
		});
	}
	private void txtClear() {
		txtAccountNo.setText("");
		txtAccountNo.setText(sessionBean.getUserId());
		txtOldPassword.setText("");
		//txtOldPassword.setBorder(null);
		txtNewPassword.setText("");
		//txtNewPassword.setBorder(null);
		txtConfirmPassword.setText("");
		//txtConfirmPassword.setBorder(null);
	}
	private boolean checkOldPassword() {
		try {
			String query="select password from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'"
					+ "and password='"+txtOldPassword.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs = dbConnection.sta.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	private boolean changePassword() {
		try {
			String query="update tbcustomerinfo set password='"+txtConfirmPassword.getText().trim()+"' "
					+ "where accountno ='"+sessionBean.getUserId()+"' and password='"+txtOldPassword.getText().trim()+"'";
			dbConnection.connect();
			dbConnection.sta.executeUpdate(query);
			dbConnection.con.close();
			return true;
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	private boolean CheckMatchPassword() 
	{
		if(txtNewPassword.getText().trim().equals(txtConfirmPassword.getText().trim())) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "Password don't match.");
			txtNewPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
			txtConfirmPassword.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
		}
		return false;
	}
	private boolean checkConfirmation(String caption)
	{ 
		int a=JOptionPane.showConfirmDialog(null, caption, "Confirmation......" ,JOptionPane.YES_NO_OPTION);
		if(a==JOptionPane.YES_OPTION)
		{
			return true;
		}
		return false;
	}
	private void frame() {
		setSize(1000, 530);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Change Password");
	}
	private void cmp() {
		add(panelChangePassword);
		panelChangePassword.setLayout(new BorderLayout());
		panelChangePassword.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelChangePassword.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelChangePassword.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelChangePassword.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelChangePassword.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(90, 0));
		panelEast.setBackground(Color.decode("#3B5998"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(420, 0));
		panelWest.setBackground(Color.decode("#3B5998"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 65));
		panelWestNorth.setBackground(Color.decode("#3B5998"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#3B5998"));
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 70));
		panelNorth.setBackground(Color.decode("#3B5998"));
	}
	private void panelcenterwork() {
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelcentercenterwork();
		panelCenter.add(panelCenterSouth, BorderLayout.SOUTH);
		panelcentersouthwork();
	}
	private void panelcentercenterwork() {
		panelCenterCenter.setBackground(Color.white);
		GridBagConstraints c=new GridBagConstraints();
		GridBagLayout grid=new GridBagLayout();
		panelCenterCenter.setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.insets=new Insets(5, 5, 5, 5);

		c.gridx=0;
		c.gridy=0;
		panelCenterCenter.add(lblAccountNo,c);
		lblAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		
		c.gridx=1;
		c.gridy=0;
		panelCenterCenter.add(txtAccountNo,c);
		txtAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAccountNo.setText(sessionBean.getUserId());
		txtAccountNo.setEnabled(false);
		txtAccountNo.setPreferredSize(new Dimension(280, 35));
		
		c.gridx=0;
		c.gridy=1;
		panelCenterCenter.add(lblOldPassword,c);
		lblOldPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		
		c.gridx=1;
		c.gridy=1;
		panelCenterCenter.add(txtOldPassword,c);
		txtOldPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtOldPassword.setPreferredSize(new Dimension(250, 32));
		
		c.gridx=0;
		c.gridy=2;
		panelCenterCenter.add(lblNewPassword,c);
		lblNewPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		
		c.gridx=1;
		c.gridy=2;
		panelCenterCenter.add(txtNewPassword,c);
		txtNewPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtNewPassword.setPreferredSize(new Dimension(250, 32));
		
		c.gridx=0;
		c.gridy=3;
		panelCenterCenter.add(lblConfirmPassword,c);
		lblConfirmPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		
		c.gridx=1;
		c.gridy=3;
		panelCenterCenter.add(txtConfirmPassword,c);
		txtConfirmPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtConfirmPassword.setPreferredSize(new Dimension(250, 32));
	}
	private void panelcentersouthwork() {
		panelCenterSouth.setBackground(Color.white);
		panelCenterSouth.setPreferredSize(new Dimension(0, 100));
		FlowLayout flow = new FlowLayout();
		panelCenterSouth.setLayout(flow);
		panelCenterSouth.add(btnChange);
		btnChange.setPreferredSize(new Dimension(130, 45));
		btnChange.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnChange.setBackground(Color.decode("#3B5998"));
		btnChange.setForeground(Color.white);
		btnChange.setFocusable(false);
		flow.setVgap(30);
		flow.setHgap(60);
		flow.setAlignment(2);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 90));
		panelSouth.setBackground(Color.decode("#3B5998"));
	}
}
