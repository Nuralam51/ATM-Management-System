package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.Menu.BalanceTransfer;
import com.example.Menu.ChangePassword;
import com.example.Menu.CheckBalance;
import com.example.Menu.Deposit;
import com.example.Menu.Transacion;
import com.example.Menu.WithDraw;

public class WorkingPanel extends JPanel{

	JPanel panelNorth, panelSouth, panelCenter, panelWest, panelEast, panelMain;
	JPanel panelNorthWest, panelNorthCenter;
	JPanel panelWestNorth, panelWestSouth;
	JPanel panelCenterWest, panelCenterCenter;
	JButton btnDepositeMoney, btnChangePassword, btnlogOut, 
	btnWithdrowMoney, btnBalanceTransfer, btnCheckBalance, btnTransaction;

	JLabel lblQuotesTop = new JLabel(new ImageIcon("image/WorkingQuotestop.png"));
	JLabel lblQuotesBottom = new JLabel(new ImageIcon("image/WorkingQuotesbottom.png"));
	
	JTextField txtSessionName = new JTextField(20);

	JFrame frame;
	SessionBean sessionBean;
	
	public WorkingPanel(JFrame frm, SessionBean bean){
		this.sessionBean=bean;
		this.frame=frm;
		cmp();
		btnBackgroundwork();
		btnAction();
		sessionName();
	}
	protected boolean ShowBalance() {
		try
		{
			String query="select balance from tbcustomerinfo where customerid='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			dbConnection.sta.executeUpdate(query);
			dbConnection.con.close();
			return true;
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp+ "insert");
		}
		return false;
	}
	public boolean sessionName() {
		try
		{
			String query="select name from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next()){
				txtSessionName.setText(rs.getString("name"));
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp+ "insert");
		}
		return false;
	}
	private void btnAction() {
		btnDepositeMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Deposit deposit = new Deposit(sessionBean);
				deposit.setVisible(true);
			}
		});
		btnWithdrowMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WithDraw withdraw = new WithDraw(sessionBean);
				withdraw.setVisible(true);
			}
		});
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePassword changepass = new ChangePassword(sessionBean);
				changepass.setVisible(true);
			}
		});
		btnBalanceTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BalanceTransfer transfer = new BalanceTransfer(sessionBean);
				transfer.setVisible(true);
			}
		});
		btnCheckBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckBalance checkbalance = new CheckBalance(sessionBean);
				checkbalance.setVisible(true);
			}
		});
		btnTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Transacion transaction = new Transacion(sessionBean);
				transaction.setVisible(true);
			}
		});
		btnlogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Sure to Log Out?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if(a == JOptionPane.YES_OPTION) {
					frame.setVisible(false);
					LogIn login = new LogIn();
					login.setVisible(true);
				}
			}
		});
	}
	private void cmp() {
		setLayout(new BorderLayout());
		panelNorth = new JPanel();
		add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelEast = new JPanel();
		add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelWest = new JPanel();
		add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void panelnorthwork() {
		panelNorth.setBackground(Color.decode("#00bcd4"));
		panelNorth.setPreferredSize(new Dimension(0, 90));
		panelNorth.setLayout(new GridLayout());
		panelNorthWest = new JPanel();
		panelNorth.add(panelNorthWest);
		panelNorthWestwork();
		panelNorthCenter = new JPanel();
		panelNorth.add(panelNorthCenter);
		panelNorthCenterwork();
	}
	private void panelNorthCenterwork() {
		panelNorthCenter.setBackground(Color.decode("#00bcd4"));
		FlowLayout flow = new FlowLayout();
		flow.setVgap(30);
		panelNorthCenter.setLayout(flow);
		panelNorthCenter.add(txtSessionName);
		txtSessionName.setFont(new Font("Monaco", Font.BOLD, 25));
		txtSessionName.setForeground(Color.white);
		txtSessionName.setBackground(Color.decode("#00bcd4"));
		txtSessionName.setBorder(null);
		txtSessionName.setEditable(false);
		flow.setAlignment(1);
	}
	private void panelNorthWestwork() {
		panelNorthWest.setBackground(Color.decode("#00bcd4"));
	}
	private void panelcenterwork() {
		panelCenter.setBackground(Color.white);
		panelCenter.setLayout(new GridLayout());
		panelCenterWest = new JPanel();
		panelCenter.add(panelCenterWest);
		panelCenterWestWork();
		panelCenterCenter = new JPanel();
		panelCenter.add(panelCenterCenter);
		panelCenterCenterWork();
	}
	private void panelCenterWestWork() {
		panelCenterWest.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterWest.setLayout(flow);
		flow.setVgap(75);
		/*GridBagConstraints grid = new GridBagConstraints();
		panelCenterWest.setLayout(new GridBagLayout());
		grid.fill = GridBagConstraints.BOTH;
		grid.insets = new Insets(5, 5, 5, 5);

		grid.gridx = 0;
		grid.gridy = 0;*/
		btnDepositeMoney = new JButton("Deposite Money"); 
		panelCenterWest.add(btnDepositeMoney);
		btnDepositeMoney.setPreferredSize(new Dimension(200, 65));
		btnDepositeMoney.setBackground(Color.decode("#39b5ff"));

		/*grid.gridx = 0;
		grid.gridy = 1;*/
		btnBalanceTransfer = new JButton("Balance Transfer"); 
		panelCenterWest.add(btnBalanceTransfer);
		btnBalanceTransfer.setPreferredSize(new Dimension(200, 65));

		/*grid.gridx = 0;
		grid.gridy = 2;*/
		btnChangePassword = new JButton("Change Password");
		panelCenterWest.add(btnChangePassword);
		btnChangePassword.setPreferredSize(new Dimension(200, 65));
	}
	private void panelCenterCenterWork() {
		panelCenterCenter.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterCenter.setLayout(flow);
		flow.setVgap(75);
		/*GridBagConstraints grid = new GridBagConstraints();
		panelCenterCenter.setLayout(new GridBagLayout());
		grid.fill = GridBagConstraints.BOTH;
		grid.insets = new Insets(5, 5, 5, 5);

		grid.gridx = 0;
		grid.gridy = 0;*/
		btnWithdrowMoney = new JButton("Withdrow Money");
		panelCenterCenter.add(btnWithdrowMoney);
		btnWithdrowMoney.setPreferredSize(new Dimension(200, 65));

		/*grid.gridx = 0;
		grid.gridy = 1*/;
		btnCheckBalance = new JButton("Check Balance"); 
		panelCenterCenter.add(btnCheckBalance);
		btnCheckBalance.setPreferredSize(new Dimension(200, 65));

		/*grid.gridx = 0;
		grid.gridy = 2;*/
		btnTransaction = new JButton("Transaction"); 
		panelCenterCenter.add(btnTransaction);
		btnTransaction.setPreferredSize(new Dimension(200, 65));
	}
	private void panelsouthwork() {
		panelSouth.setBackground(Color.decode("#00bcd4"));
		panelSouth.setPreferredSize(new Dimension(0, 90));
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(2);
		flow.setVgap(25);
		flow.setHgap(100);
		panelSouth.setLayout(flow);
		btnlogOut = new JButton("Log Out");
		panelSouth.add(btnlogOut);
	}
	private void paneleastwork() {
		panelEast.setBackground(Color.decode("#00bcd4"));
		panelEast.setPreferredSize(new Dimension(70, 0));
	}
	private void panelwestwork() {
		panelWest.setBackground(Color.decode("#00bcd4"));
		panelWest.setPreferredSize(new Dimension(500, 0));
		panelWest.setLayout(new BorderLayout());
		panelWestSouth = new JPanel();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelWestSouthwork();
		panelWestNorth = new JPanel();
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelWestNorthwork();
	}
	private void panelWestNorthwork() {
		panelWestNorth.setBackground(Color.decode("#00bcd4"));
		panelWestNorth.setPreferredSize(new Dimension(0, 90));
		panelWestNorth.setLayout(new FlowLayout());
		panelWestNorth.add(lblQuotesTop);
	}
	private void panelWestSouthwork() {
		panelWestSouth.setBackground(Color.decode("#00bcd4"));
		panelWestSouth.setPreferredSize(new Dimension(0, 200));
		panelWestSouth.setLayout(new FlowLayout());
		panelWestSouth.add(lblQuotesBottom);
	}
	private void btnBackgroundwork() {
		btnDepositeMoney.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnDepositeMoney.setForeground(Color.white);
		btnDepositeMoney.setFocusable(false);
		btnBalanceTransfer.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnBalanceTransfer.setBackground(Color.decode("#39b5ff"));
		btnBalanceTransfer.setForeground(Color.white);
		btnBalanceTransfer.setFocusable(false);
		btnChangePassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnChangePassword.setBackground(Color.decode("#39b5ff"));
		btnChangePassword.setForeground(Color.white);
		btnChangePassword.setFocusable(false);
		btnWithdrowMoney.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnWithdrowMoney.setBackground(Color.decode("#39b5ff"));
		btnWithdrowMoney.setForeground(Color.white);
		btnWithdrowMoney.setFocusable(false);
		btnTransaction.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnTransaction.setBackground(Color.decode("#39b5ff"));
		btnTransaction.setForeground(Color.white);
		btnTransaction.setFocusable(false);
		btnCheckBalance.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnCheckBalance.setBackground(Color.decode("#39b5ff"));
		btnCheckBalance.setForeground(Color.white);
		btnCheckBalance.setFocusable(false);
		btnlogOut.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnlogOut.setBackground(Color.white);
		btnlogOut.setForeground(Color.black);
		btnlogOut.setFocusable(false);
		btnlogOut.setPreferredSize(new Dimension(120, 40));
	}
}
