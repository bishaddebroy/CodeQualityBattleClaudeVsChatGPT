import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import java.util.Date;
import javax.swing.border.EmptyBorder;

public class RegistrationForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JDateChooser dateChooser;
    private ButtonGroup genderGroup;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JRadioButton otherButton;
    private JButton submitButton;
    private JLabel errorLabel;

    public RegistrationForm() {
        setTitle("User Registration");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Date picker
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Birth Date:"), gbc);
        dateChooser = new JDateChooser();
        gbc.gridx = 1;
        mainPanel.add(dateChooser, gbc);

        // Gender selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Gender:"), gbc);
        
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        genderGroup = new ButtonGroup();
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");
        
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        genderPanel.add(otherButton);
        
        gbc.gridx = 1;
        mainPanel.add(genderPanel, gbc);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(errorLabel, gbc);

        // Submit button
        submitButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        // Add action listener to submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    // Process the registration
                    JOptionPane.showMessageDialog(RegistrationForm.this,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        add(mainPanel);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        // Validate username
        if (usernameField.getText().trim().length() < 3) {
            errors.append("Username must be at least 3 characters long\n");
        }

        // Validate email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(emailField.getText().trim()).matches()) {
            errors.append("Invalid email address\n");
        }

        // Validate password
        if (new String(passwordField.getPassword()).length() < 6) {
            errors.append("Password must be at least 6 characters long\n");
        }

        // Validate birth date
        if (dateChooser.getDate() == null) {
            errors.append("Please select a birth date\n");
        } else {
            Date currentDate = new Date();
            if (dateChooser.getDate().after(currentDate)) {
                errors.append("Birth date cannot be in the future\n");
            }
        }

        // Validate gender selection
        if (!maleButton.isSelected() && !femaleButton.isSelected() && !otherButton.isSelected()) {
            errors.append("Please select a gender\n");
        }

        if (errors.length() > 0) {
            errorLabel.setText("<html>" + errors.toString().replace("\n", "<br>") + "</html>");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationForm().setVisible(true);
            }
        });
    }
}
