package com.example.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class WithDraw extends JFrame{

	JPanel panelWithdraw =new JPanel();
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

	JLabel lblEnterAmount = new JLabel("Enter an Amount");
	JTextField txtAmount = new JTextField(30);
	JButton btnWithdraw = new JButton("Withdraw");

	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop2.png"));

	SessionBean sessionBean;

	public WithDraw(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		btnAction();
	}
	private void btnAction() {
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkValidationWithdraw()) {
					if(checkMinimumBalance()) {
						if(checkBalance()) {
							if(WithdrawMoney()) {
								sendEmail();
								WithdrawTransaction();
								JOptionPane.showMessageDialog(null, "Withdraw Successfull");
								txtClear();
							}
						}
					}
				}
			}
		});
	}
	protected boolean sendEmail() {
		String email="";
		try 
		{
			String query="select email from tbcustomerinfo where accountno='"+sessionBean.getUserId().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next()) {
				email = rs.getString("email");
			}
			dbConnection.con.close();

			String host ="smtp.gmail.com";
			String user = "thinkingwholeworld2@gmail.com";
			String pass = "nuralambabu";
			String to = email;
			String from = "thinkingwholeworld2@gmail.com";
			String subject = "WithDraw Money from ATM";
			String messageText = "Successfully Withdraw Money."+"\n"
					+"Amount of Withdraw money is "+txtAmount.getText().trim();
			boolean sessionDebug = false;

			Properties props = System.getProperties();

			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");

			//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject); msg.setSentDate(new Date());
			msg.setText(messageText);

			Transport transport=mailSession.getTransport("smtp");
			transport.connect(host, user, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			//txtShowPassword.setText("Your Password Reset Successfull.");
			//System.out.println(s1 + email);
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return false;
	}
	private boolean WithdrawTransaction()
	{
		try
		{
			String query="insert into tbtransactioninfo (accountno, balance,entryTime, status)"
					+ "values('"+sessionBean.getUserId().trim()+"','"+txtAmount.getText().trim()+"',now(),'Withdraw')";
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
	private void txtClear() {
		txtAmount.setText("");
	}
	private boolean checkValidationWithdraw()
	{
		if(!txtAmount.getText().trim().isEmpty()) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "please insert money amount");
			txtAmount.setBorder(BorderFactory.createEtchedBorder(0, Color.red, Color.red));
		}
		return false;
	}
	protected boolean checkMinimumBalance() {
		int Minamount = 500, amount;
		amount = Integer.parseInt(txtAmount.getText().trim());
		if(Minamount<amount) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "Minimum 500tk can you Withdraw");
		}
		return false;
	}
	protected boolean checkBalance() {
		int amount = Integer.parseInt(txtAmount.getText().trim());
		int balance=0;
		try {
			String query="select balance from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			
			if(rs.next()) {
				balance = rs.getInt("balance");
				if(balance>=amount) {
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Insufficiant Balance!!");
				}
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insufficiant Balance!!");
		}
		return false;
	}
	protected boolean WithdrawMoney() {
		int NewBalance=0;
		try 
		{
			String query="select balance from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while (rs.next()) {
				NewBalance = rs.getInt("balance") - Integer.parseInt(txtAmount.getText());
			}
			dbConnection.con.close();
			try {
				String query1="update tbcustomerinfo set balance='"+NewBalance+"' where accountno ='"+sessionBean.getUserId()+"'";
				dbConnection.connect();
				dbConnection.sta.executeUpdate(query1);
				dbConnection.con.close();
				return true;
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp + " money");
		}
		return false;
	}
	private void frame() {
		setSize(830, 445);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Withdraw Money");
	}
	private void cmp() {
		add(panelWithdraw);
		panelWithdraw.setLayout(new BorderLayout());
		panelWithdraw.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelWithdraw.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelWithdraw.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelWithdraw.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelWithdraw.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(60, 0));
		panelEast.setBackground(Color.decode("#FF5252"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(390, 0));
		panelWest.setBackground(Color.decode("#FF5252"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 65));
		panelWestNorth.setBackground(Color.decode("#FF5252"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#FF5252"));
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 70));
		panelNorth.setBackground(Color.decode("#FF5252"));
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
		panelCenterNorth.setBackground(Color.white);
		panelCenterNorth.setPreferredSize(new Dimension(0, 50));
		FlowLayout flow = new FlowLayout();
		panelCenterNorth.setLayout(flow);
		panelCenterNorth.add(lblEnterAmount);
		lblEnterAmount.setFont(new Font("Monaco", Font.BOLD, 18));
		lblEnterAmount.setForeground(Color.decode("#FF5252"));
		flow.setVgap(30);
		flow.setHgap(20);
		flow.setAlignment(0);
	}
	private void panelcentercenterwork() {
		panelCenterCenter.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterCenter.setLayout(flow);
		panelCenterCenter.add(txtAmount);
		txtAmount.setPreferredSize(new Dimension(380, 35));
		txtAmount.setFont(new Font("Monaco", Font.BOLD, 15));
		flow.setVgap(30);
	}
	private void panelcentersouthwork() {
		panelCenterSouth.setBackground(Color.white);
		panelCenterSouth.setPreferredSize(new Dimension(0, 120));
		FlowLayout flow = new FlowLayout();
		panelCenterSouth.setLayout(flow);
		panelCenterSouth.add(btnWithdraw);
		btnWithdraw.setPreferredSize(new Dimension(130, 45));
		btnWithdraw.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnWithdraw.setBackground(Color.decode("#FF5252"));
		btnWithdraw.setForeground(Color.white);
		btnWithdraw.setFocusable(false);
		flow.setVgap(30);
		flow.setHgap(20);
		flow.setAlignment(2);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 70));
		panelSouth.setBackground(Color.decode("#FF5252"));
	}
}
