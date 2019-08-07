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
import java.text.DecimalFormat;
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

public class Deposit extends JFrame{

	JPanel panelDeposit =new JPanel();
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
	JButton btnDeposit = new JButton("Deposit");

	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop1.png"));

	DecimalFormat df=new DecimalFormat("#0.00");
	SessionBean sessionBean;

	public Deposit(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		btnAction();
	}
	private void btnAction() {
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkValidationDeposit()) {
					if(checkMinimumBalance()) {
						if(depositeMoney()) {
							if(DepositTransaction()) {
								sendEmail();
								JOptionPane.showMessageDialog(null, "Deposit Successfull");
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
			String subject = "Deposit Money from ATM";
			String messageText = "Successfully Deposit Money."+"\n"
								+"Amount of Deposit money is "+txtAmount.getText().trim();
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
	private void txtClear() {
		txtAmount.setText("");
	}
	private boolean checkValidationDeposit()
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
		if(amount>=Minamount) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "Minimum 500tk can you deposit");
		}
		return false;
	}
	protected boolean depositeMoney() {
		int NewBalance=0;
		try 
		{
			String query="select balance from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while (rs.next()) {
				NewBalance = rs.getInt("balance") + Integer.parseInt(txtAmount.getText());
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
	private boolean DepositTransaction()
	{
		try
		{
			String query="insert into tbtransactioninfo (accountno, balance,entryTime, status)"
					+ "values('"+sessionBean.getUserId().trim()+"','"+txtAmount.getText().trim()+"',now(),'Deposit')";
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
	private void frame() {
		setSize(830, 445);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Deposit Money");
	}
	private void cmp() {
		add(panelDeposit);
		panelDeposit.setLayout(new BorderLayout());
		panelDeposit.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelDeposit.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelDeposit.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelDeposit.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelDeposit.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(70, 0));
		panelEast.setBackground(Color.decode("#4C4177"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(390, 0));
		panelWest.setBackground(Color.decode("#4C4177"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 65));
		panelWestNorth.setBackground(Color.decode("#4C4177"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#4C4177"));
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 60));
		panelNorth.setBackground(Color.decode("#4C4177"));
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
		lblEnterAmount.setForeground(Color.decode("#4C4177"));
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
		panelCenterSouth.add(btnDeposit);
		btnDeposit.setPreferredSize(new Dimension(130, 45));
		btnDeposit.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnDeposit.setBackground(Color.decode("#4C4177"));
		btnDeposit.setForeground(Color.white);
		btnDeposit.setFocusable(false);
		flow.setVgap(30);
		flow.setHgap(20);
		flow.setAlignment(2);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 70));
		panelSouth.setBackground(Color.decode("#4C4177"));
	}
}
