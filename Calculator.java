import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator implements ActionListener {

    JFrame frame;
    JTextField textfield;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton, negButton;
    JPanel panel;

    Font myFont = new Font("Typewriter", Font.BOLD, 30);

    Calculator() {

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(390, 530);
        frame.setLayout(null);

        textfield = new JTextField();
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Delete");
        clrButton = new JButton("Clear");
        negButton = new JButton("(-)");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = negButton;

        for (int i = 0; i < 9; i++) {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }

        negButton.setBounds(50, 430, 100, 50);
        delButton.setBounds(150, 430, 100, 50);
        clrButton.setBounds(250, 430, 100, 50);

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(negButton);
        frame.add(textfield);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Calculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Number buttons
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                textfield.setText(textfield.getText() + i);
            }
        }

        // Decimal point
        if (e.getSource() == decButton) {
            String text = textfield.getText();
            if (!text.endsWith(".") && !text.contains(".")) {
                textfield.setText(text + ".");
            }
        }

        // Operators
        if (e.getSource() == addButton) addOperator('+');
        if (e.getSource() == subButton) addOperator('-');
        if (e.getSource() == mulButton) addOperator('*');
        if (e.getSource() == divButton) addOperator('/');

        // Equals
        if (e.getSource() == equButton) calculateResult();

        // Clear
        if (e.getSource() == clrButton) {
            textfield.setText("");
        }

        // Delete last character
        if (e.getSource() == delButton) {
            String text = textfield.getText();
            if (text.length() > 0) {
                textfield.setText(text.substring(0, text.length() - 1));
            }
        }

        // Negate
        if (e.getSource() == negButton) {
            String text = textfield.getText();
            if (text.isEmpty()) return;

            int opIndex = -1;
            for (int i = 1; i < text.length(); i++) {
                if ("+-*/".indexOf(text.charAt(i)) >= 0) {
                    opIndex = i;
                    break;
                }
            }

            try {
                if (opIndex == -1) {
                    // negate first number
                    double temp = Double.parseDouble(text);
                    temp *= -1;
                    textfield.setText(String.valueOf(temp));
                } else {
                    // negate second number
                    String beforeOp = text.substring(0, opIndex + 1);
                    String afterOp = text.substring(opIndex + 1);
                    if (afterOp.isEmpty()) {
                        // start second number as negative
                        textfield.setText(beforeOp + "-");
                    } else {
                        double temp = Double.parseDouble(afterOp);
                        temp *= -1;
                        textfield.setText(beforeOp + temp);
                    }
                }
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }
    }

    private void addOperator(char op) {
        String text = textfield.getText();
        if (text.isEmpty()) {
            if (op == '-') { // allow starting negative number
                textfield.setText("-");
            }
            return;
        }
        // prevent multiple operators
        for (char c : "+-*/".toCharArray()) {
            if (text.indexOf(c, 1) > 0) return; // operator already exists
        }
        textfield.setText(text + op);
    }

    private void calculateResult() {
        try {
            String text = textfield.getText().replaceAll("\\s+", "");
            char op = ' ';
            int opIndex = -1;

            // find operator
            for (int i = 1; i < text.length(); i++) {
                if ("+-*/".indexOf(text.charAt(i)) >= 0) {
                    op = text.charAt(i);
                    opIndex = i;
                    break;
                }
            }

            if (opIndex == -1) return;

            double n1 = Double.parseDouble(text.substring(0, opIndex));
            double n2 = Double.parseDouble(text.substring(opIndex + 1));
            double res = 0;

            switch (op) {
                case '+': res = n1 + n2; break;
                case '-': res = n1 - n2; break;
                case '*': res = n1 * n2; break;
                case '/': res = n1 / n2; break;
            }

            textfield.setText(String.valueOf(res));

        } catch (Exception e) {
            textfield.setText("Error");
        }
    }
}
