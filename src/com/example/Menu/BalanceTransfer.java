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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class BalanceTransfer extends JFrame{

	JPanel panelBalanceTransfer = new JPanel();
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

	JLabel lblReceiverId = new JLabel("Receiver ID");
	JLabel lblAmount = new JLabel("Amount");
	JLabel lblRetypePassword = new JLabel("Retype Password");

	JTextField txtReceiverId = new JTextField();
	JTextField txtAmount = new JTextField();
	JPasswordField txtRetypePassword = new JPasswordField();

	JButton btnTransfer = new JButton("Transfer");
	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop4.png"));

	SessionBean sessionBean;

	public BalanceTransfer(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		btnAction();
	}
	private void btnAction() {
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkValidationBalanceTransfer()) {
					if(checkExistAccount()) {
						if(checkBalance()) {
							if(checkMinimumBalanceTransfer()) {
								if(checkReTyperPassword()) {
									if(TransferBalanceRemove()) {
										if(TransferBalanceAdd()) {
											sendEmail();
											BalanceTransferTransaction();
											JOptionPane.showMessageDialog(null, "Transfer Balance Successfull");
											txtClear();
										}
									}
								}
							}
						}
					}
				}
			}
		});
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
			String subject = "Balance Transfer from ATM";
			String messageText = "Successfully Balance Transfer."+"\n"
					+"Amount of Transfer balance is "+txtAmount.getText().trim();
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
	private boolean BalanceTransferTransaction()
	{
		try
		{
			String query="insert into tbtransactioninfo (accountno,receiveraccountno, balance,entryTime, status)"
					+ "values('"+sessionBean.getUserId().trim()+"','"+txtReceiverId.getText().trim()+"',"
					+ "'"+txtAmount.getText().trim()+"',now(),'Transfer')";
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
	private boolean checkReTyperPassword() {
		try {
			String query="select password from tbcustomerinfo where accountno='"+sessionBean.getUserId()+"'"
					+ "and password='"+txtRetypePassword.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs = dbConnection.sta.executeQuery(query);
			if(rs.next()) {
				return true;
			}
			JOptionPane.showMessageDialog(null, "Your Password is Wrong");
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	private boolean checkExistAccount() {
		try {
			String query="select accountNo from tbcustomerinfo";
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
	private void txtClear() {
		txtAmount.setText("");
		txtReceiverId.setText("");
		txtRetypePassword.setText("");
	}
	private boolean checkValidationBalanceTransfer()
	{
		if(!txtReceiverId.getText().trim().isEmpty()) {
			if(!txtAmount.getText().trim().isEmpty()) {
				if(!txtRetypePassword.getText().trim().isEmpty()) {
					return true;
				}
				else {
					JOptionPane.showMessageDialog(null, "please Re-Type your Password");	
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "please insert money amount");	
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "please insert Receiver AccountNo");	
		}
		return false;
	}
	protected boolean checkMinimumBalanceTransfer() {
		int Minamount = 500, amount;
		amount = Integer.parseInt(txtAmount.getText().trim());
		if(amount>=Minamount) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "Minimum 500tk can you Transfer");
		}
		return false;
	}
	protected boolean TransferBalanceRemove() {
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
				String query1="update tbcustomerinfo set balance='"+NewBalance+"' "
						+ "where accountno ='"+sessionBean.getUserId()+"'";
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
	protected boolean TransferBalanceAdd() {
		int NewBalance=0;
		try 
		{
			String query="select balance from tbcustomerinfo where accountno='"+txtReceiverId.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while (rs.next()) {
				NewBalance = rs.getInt("balance") + Integer.parseInt(txtAmount.getText());
			}
			dbConnection.con.close();
			try {
				String query1="update tbcustomerinfo set balance='"+NewBalance+"' "
						+ "where accountno ='"+txtReceiverId.getText().trim()+"'";
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
		setSize(1000, 530);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Transfer Balance");
	}
	private void cmp() {
		add(panelBalanceTransfer);
		panelBalanceTransfer.setLayout(new BorderLayout());
		panelBalanceTransfer.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelBalanceTransfer.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelBalanceTransfer.add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		panelBalanceTransfer.add(panelEast, BorderLayout.EAST);
		paneleastwork();
		panelBalanceTransfer.add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(90, 0));
		panelEast.setBackground(Color.decode("#E65100"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(420, 0));
		panelWest.setBackground(Color.decode("#E65100"));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelwestnorthwork();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelwestsouthwork();
	}
	private void panelwestnorthwork() {
		panelWestNorth.setPreferredSize(new Dimension(0, 65));
		panelWestNorth.setBackground(Color.decode("#E65100"));
		panelWestNorth.add(lblQuotestopIcon);
	}
	private void panelwestsouthwork() {
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		panelWestSouth.setBackground(Color.decode("#E65100"));
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 70));
		panelNorth.setBackground(Color.decode("#E65100"));
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
		panelCenterCenter.add(lblReceiverId,c);
		lblReceiverId.setFont(new Font("Monaco", Font.PLAIN, 13));

		c.gridx=1;
		c.gridy=0;
		panelCenterCenter.add(txtReceiverId,c);
		txtReceiverId.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtReceiverId.setPreferredSize(new Dimension(280, 35));

		c.gridx=0;
		c.gridy=1;
		panelCenterCenter.add(lblAmount,c);
		lblAmount.setFont(new Font("Monaco", Font.PLAIN, 13));

		c.gridx=1;
		c.gridy=1;
		panelCenterCenter.add(txtAmount,c);
		txtAmount.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAmount.setPreferredSize(new Dimension(250, 32));

		c.gridx=0;
		c.gridy=2;
		panelCenterCenter.add(lblRetypePassword,c);
		lblRetypePassword.setFont(new Font("Monaco", Font.PLAIN, 13));

		c.gridx=1;
		c.gridy=2;
		panelCenterCenter.add(txtRetypePassword,c);
		txtRetypePassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtRetypePassword.setPreferredSize(new Dimension(250, 32));
	}
	private void panelcentersouthwork() {
		panelCenterSouth.setBackground(Color.white);
		panelCenterSouth.setPreferredSize(new Dimension(0, 100));
		FlowLayout flow = new FlowLayout();
		panelCenterSouth.setLayout(flow);
		panelCenterSouth.add(btnTransfer);
		btnTransfer.setPreferredSize(new Dimension(130, 45));
		btnTransfer.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnTransfer.setBackground(Color.decode("#E65100"));
		btnTransfer.setForeground(Color.white);
		btnTransfer.setFocusable(false);
		flow.setVgap(30);
		flow.setHgap(60);
		flow.setAlignment(2);
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 90));
		panelSouth.setBackground(Color.decode("#E65100"));
	}
}
