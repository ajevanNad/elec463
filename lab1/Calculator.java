//Ajevan Nadarajah

package lab1;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Calculator {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(600, 200, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ELEC 463 Calculator");
		frame.setLayout(null);
		frame.setVisible(true);
		
		JLabel firstNumLabel = new JLabel("First Number");
		firstNumLabel.setBounds(20, 20, 200, 20);
		firstNumLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(firstNumLabel);
		
		JTextField firstNumTextfield = new JTextField("");
		firstNumTextfield.setBounds(150, 20, 200, 20);
		firstNumTextfield.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(firstNumTextfield);
		
		JLabel secNumLabel = new JLabel("Second Number");
		secNumLabel.setBounds(20, 50, 200, 20);
		secNumLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(secNumLabel);
		
		JTextField secNumTextfield = new JTextField("");
		secNumTextfield.setBounds(150, 50, 200, 20);
		secNumTextfield.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(secNumTextfield);
		
		JLabel ansLabel = new JLabel("");
		ansLabel.setBounds(20, 100, 560, 20);
		ansLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(ansLabel);
		
		JButton plusButton = new JButton("+");
		plusButton.setBounds(20, 150, 50, 50);
		plusButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(plusButton);
		
		JButton minusButton = new JButton("-");
		minusButton.setBounds(80, 150, 50, 50);
		minusButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(minusButton);
		
		JButton multiButton = new JButton("*");
		multiButton.setBounds(140, 150, 50, 50);
		multiButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(multiButton);
		
		JButton divButton = new JButton("/");
		divButton.setBounds(200, 150, 50, 50);
		divButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(divButton);
		
		JButton maxButton = new JButton("Max");
		maxButton.setBounds(260, 150, 100, 50);
		maxButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(maxButton);
		
		JButton minButton = new JButton("Min");
		minButton.setBounds(370, 150, 100, 50);
		minButton.setFont(new Font("Times", Font.BOLD, 26));
		frame.getContentPane().add(minButton);
		
		plusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = add(num1, num2);
				ansLabel.setText("Answer: " + num1 + " + " + num2 + " = " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});
		
		minusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = subtract(num1, num2);
				ansLabel.setText("Answer: " + num1 + " - " + num2 + " = " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});
		
		multiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = multiply(num1, num2);
				ansLabel.setText("Answer: " + num1 + " * " + num2 + " = " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});
		
		divButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = divide(num1, num2);
				ansLabel.setText("Answer: " + num1 + " / " + num2 + " = " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});
		
		maxButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = max(num1, num2);
				ansLabel.setText("Answer: Maximum is: " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});
		
		minButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				double num1 = Double.parseDouble(firstNumTextfield.getText());
				double num2 = Double.parseDouble(secNumTextfield.getText());
				
				double ans = min(num1, num2);
				ansLabel.setText("Answer: Minimum is: " + Double.toString(ans));
				}
				catch(Exception error) {
					ansLabel.setText("Invalid Input!");
				}
			}
		});

	}
	
	public static double add(double x, double y) {
		return x + y;
	}
	
	public static double subtract(double x, double y) {
		return x - y;
	}
	
	public static double multiply(double x, double y) {
		return x * y;
	}
	
	public static double divide(double x, double y) {
		return x / y;
	}
	
	public static double max(double x, double y) {
		return (x > y) ? x : y;
	}
	
	public static double min(double x, double y) {
		return (x < y) ? x : y; 
	}

}
