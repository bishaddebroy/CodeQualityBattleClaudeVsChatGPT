import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserRegistrationForm extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField birthDateField; // Simulated date picker
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JRadioButton otherButton;
    private JButton submitButton;

    public UserRegistrationForm() {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(7, 2, 10, 10));

        // Username
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        // Email
        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        // Password
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Birth Date
        add(new JLabel("Birth Date (yyyy-MM-dd):"));
        birthDateField = new JTextField(); // Simulated date picker
        add(birthDateField);

        // Gender
        add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        ButtonGroup genderGroup = new ButtonGroup();
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        genderPanel.add(otherButton);
        add(genderPanel);

        // Submit Button
        submitButton = new JButton("Submit");
        add(new JLabel()); // Empty label for alignment
        add(submitButton);

        // Add action listener to Submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        setVisible(true);
    }

    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String birthDateStr = birthDateField.getText().trim();
        String gender = null;

        if (maleButton.isSelected()) {
            gender = "Male";
        } else if (femaleButton.isSelected()) {
            gender = "Female";
        } else if (otherButton.isSelected()) {
            gender = "Other";
        }

        // Validate inputs
        StringBuilder errors = new StringBuilder();

        if (username.isEmpty()) {
            errors.append("- Username is required.\n");
        }
        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,}$")) {
            errors.append("- Enter a valid email address.\n");
        }
        if (password.isEmpty()) {
            errors.append("- Password is required.\n");
        }
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
        } catch (ParseException e) {
            errors.append("- Enter a valid birth date in the format yyyy-MM-dd.\n");
        }
        if (gender == null) {
            errors.append("- Select a gender.\n");
        }

        // Show errors or success message
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new UserRegistrationForm();
    }
}
