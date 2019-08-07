package com.example.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class CheckBalance extends JFrame{
	
	JPanel panelBalance =new JPanel();
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
	
	JLabel lblBalanceAmount = new JLabel("Balance Amount");
	JTextField txtBalance = new JTextField(23);

	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop5.png"));
	
	SessionBean sessionBean;
	
	public CheckBalance(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		ShowBalance();
		btnAction();
	}
	private void btnAction() {
		txtBalance.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {
				txtBalance.setFont(new Font("Monaco", Font.PLAIN, 25));
			}
			public void mouseEntered(MouseEvent e) {
				txtBalance.setFont(new Font("Monaco", Font.BOLD, 35));
			}
			public void mouseClicked(MouseEvent e) {}
		});
	}
	private boolean ShowBalance() {
		int balance=0;
		try {
			String query="select balance from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs = dbConnection.sta.executeQuery(query);
			if(rs.next()) {
				balance = rs.getInt("balance");
				String s1 = Integer.toString(balance);
				txtBalance.setText(s1);
				return true;
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+" show");
		}
		return false;
	}
	
	private void frame() {
		setSize(830, 445);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Balance Check");
	}
	private void cmp() {
		add(panelBalance);
		panelBalance.setLayout(new BorderLayout());
		panelBalance.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelBalance.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelBalance.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelBalance.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelBalance.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(60, 0));
		panelEast.setBackground(Color.decode("#4CAF50"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(390, 0));
		panelWest.setBackground(Color.decode("#4CAF50"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 75));
		panelWestNorth.setBackground(Color.decode("#4CAF50"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#4CAF50"));
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 70));
		panelNorth.setBackground(Color.decode("#4CAF50"));
	}
	private void panelcenterwork() {
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelcentercenterwork();
		panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
		panelcenternorthwork();
	}
	private void panelcenternorthwork() {
		panelCenterNorth.setBackground(Color.white);
		panelCenterNorth.setPreferredSize(new Dimension(0, 80));
		FlowLayout flow = new FlowLayout();
		panelCenterNorth.setLayout(flow);
		panelCenterNorth.add(lblBalanceAmount);
		lblBalanceAmount.setFont(new Font("Monaco", Font.BOLD, 20));
		lblBalanceAmount.setForeground(Color.decode("#4CAF50"));
		flow.setVgap(50);
	}
	private void panelcentercenterwork() {
		panelCenterCenter.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterCenter.setLayout(flow);
		panelCenterCenter.add(txtBalance);
		txtBalance.setPreferredSize(new Dimension(140, 50));
		txtBalance.setFont(new Font("Monaco", Font.PLAIN, 25));
		flow.setVgap(40);
		txtBalance.setEditable(false);
		txtBalance.setHorizontalAlignment(JTextField.CENTER);
		txtBalance.setBorder(null);
		txtBalance.setBackground(Color.white);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 70));
		panelSouth.setBackground(Color.decode("#4CAF50"));
	}
}

