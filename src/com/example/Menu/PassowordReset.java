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
import java.io.File;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

import com.example.Admin.dbConnection;

public class PassowordReset extends JFrame{

	JPanel panelNorth = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelCenterNorth = new JPanel();
	JPanel panelCenterCenter = new JPanel();
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelWest = new JPanel();

	JLabel lblAccountNo = new JLabel("Account No");
	JTextArea txtShowPassword = new JTextArea(6, 50);
	JScrollPane scrollShowPassword = new JScrollPane(txtShowPassword,JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	public JTextField txtAccountNo = new JTextField(30);

	JButton btnReset = new JButton("Reset");

	Random random = new Random(); 
	int rand_int1 = random.nextInt(10000); 
	String s1 = Integer.toString(rand_int1);

	public PassowordReset() {
		frame();
		cmp();
		btnBackgroundwork();
		btnAction();
	}
	private void btnAction() {
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(insertNewPassword()) {
					if(sendEmail()) {

					}
				}
			}
		});
	}
	private boolean insertNewPassword() 
	{
		try
		{
			String query="update tbcustomerinfo set password='"+s1+"' where accountno='"+txtAccountNo.getText().trim()+"'";
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
	private boolean CollectCustomerMail() 
	{
		try 
		{
			String query="select email from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next())
			{
				String email = rs.getString("email");
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return true;
	}
	protected boolean sendEmail() {
		String email="";
		try 
		{
			String query="select email from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'";
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
			String subject = "ATM Password Reset";
			String messageText = "New Password id: "+s1;
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
			txtShowPassword.setText("Your Password Reset Successfull.");
			//System.out.println(s1 + email);
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return false;
	}
	private void frame() {
		setSize(670, 470);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Edit Customer");
	}
	private void cmp() {
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		add(panelEast, BorderLayout.EAST);
		paneleastwork();
		add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 50));
		panelNorth.setBackground(Color.decode("#4CAF50"));
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 50));
		panelSouth.setBackground(Color.decode("#4CAF50"));
	}
	private void panelcenterwork() {
		panelCenter.setBackground(Color.white);
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
		panelcenternorthwork();
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelcentercenterwork();
	}
	private void panelcenternorthwork() {
		panelCenterNorth.setPreferredSize(new Dimension(0, 160));
		panelCenterNorth.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		flow.setVgap(70);
		flow.setHgap(10);
		panelCenterNorth.setLayout(flow);
		panelCenterNorth.add(lblAccountNo);
		lblAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		panelCenterNorth.add(txtAccountNo);
		txtAccountNo.setPreferredSize(new Dimension(300, 30));
		txtAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAccountNo.setEditable(false);
		panelCenterNorth.add(btnReset);
		btnReset.setPreferredSize(new Dimension(130, 35));
	}
	private void panelcentercenterwork() {
		FlowLayout flow = new FlowLayout();
		flow.setVgap(30);
		panelCenterCenter.setLayout(flow);
		panelCenterCenter.add(scrollShowPassword);
		txtShowPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtShowPassword.setEditable(false);
		txtShowPassword.setForeground(Color.red);
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(55, 0));
		panelEast.setBackground(Color.decode("#4CAF50"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(55, 0));
		panelWest.setBackground(Color.decode("#4CAF50"));
	}
	private void btnBackgroundwork() {
		btnReset.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnReset.setBackground(Color.decode("#4CAF50"));
		btnReset.setForeground(Color.white);
		btnReset.setFocusable(false);	
	}
}
