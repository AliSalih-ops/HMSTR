import javax.swing.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class HospitalManagementSystem extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Data structure to store patient information
    private ArrayList<Patient> patients = new ArrayList<>();
    private int nextPatientNumber = 1; // Tracks the next available patient number
    // Data structure to store reservations
    private ArrayList<Reservation> reservations = new ArrayList<>();
    // GUI components
    private JTextField firstnameField, lastnameField, ageField, phoneField, illnessField, addressField, patientNoteField, weightField, heightField;
    private JComboBox<String> searchDoctorComboBox, bloodTypeComboBox, genderComboBox, emergencylevelComboBox;
    private JTextArea reservationNoteArea;
    private JButton addButton;
    
    // Constructor
    public HospitalManagementSystem() {
        super("Hospital Management System");
        // Create labels and text fields
        JLabel firstnameLabel = new JLabel("First Name:");
        JLabel lastnameLabel = new JLabel ("Last Name:");
        JLabel ageLabel = new JLabel ("Age:");
        JLabel genderLabel = new JLabel ("Gender:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel illnessLabel = new JLabel("Illness:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel bloodTypeLabel = new JLabel("Blood Type:");
        JLabel patientNoteLabel = new JLabel("Patient Note:");
        JLabel weightLabel = new JLabel("Weight (kg):");
        JLabel heightLabel = new JLabel("Height (cm):");
        firstnameField = new JTextField(20);
        lastnameField = new JTextField(20);
        phoneField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female",});
        ageField = new JTextField(20);
        illnessField = new JTextField(20);
        addressField = new JTextField(20);
        patientNoteField = new JTextField(20);
        weightField = new JTextField(20);
        heightField = new JTextField(20);
        bloodTypeComboBox = new JComboBox<>(new String[]{"A", "B", "AB", "O", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
       
        // Add document filters to restrict input
        ((AbstractDocument) firstnameField.getDocument()).setDocumentFilter(new LetterDocumentFilter());
        ((AbstractDocument) lastnameField.getDocument()).setDocumentFilter(new LetterDocumentFilter());
        ((AbstractDocument) ageField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        ((AbstractDocument) weightField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        ((AbstractDocument) heightField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        // Create buttons
        addButton = new JButton("Add Patient");
        JButton searchButton = new JButton("Search Patient");
        JButton reservationsButton = new JButton("Reservations");
        addButton.setEnabled(false); // Initially disable the button
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add patient details
                String firstname = firstnameField.getText();
                String lastname = lastnameField.getText();
                String age = ageField.getText();
                String gender = (String) genderComboBox.getSelectedItem();
                String phone = phoneField.getText();
                String illness = illnessField.getText();
                String address = addressField.getText();
                String bloodType = (String) bloodTypeComboBox.getSelectedItem();
                String patientNote = patientNoteField.getText();
                String weight = weightField.getText();
                String height = heightField.getText();
                int patientNumber = generatePatientNumber();
                patients.add(new Patient(patientNumber, firstname, lastname, age, gender, phone, illness, address, bloodType, patientNote, weight, height, height, height, height));
                JOptionPane.showMessageDialog(null, "Patient added successfully! Patient ID: " + patientNumber);
                // Clear input fields
                clearFields();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open search window
                openSearchWindow();
            }
        });
        reservationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open reservations window
                openReservationsWindow();
            }
        });
        // Create a panel to hold the input fields
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(firstnameLabel);
        inputPanel.add(firstnameField);
        inputPanel.add(lastnameLabel);
        inputPanel.add(lastnameField);
        inputPanel.add(ageLabel);
        inputPanel.add(ageField);
        inputPanel.add(genderLabel);
        inputPanel.add(genderComboBox);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(illnessLabel);
        inputPanel.add(illnessField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);
        inputPanel.add(bloodTypeLabel);
        inputPanel.add(bloodTypeComboBox);
        inputPanel.add(patientNoteLabel);
        inputPanel.add(patientNoteField);
        inputPanel.add(weightLabel);
        inputPanel.add(weightField);
        inputPanel.add(heightLabel);
        inputPanel.add(heightField);
        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(reservationsButton);
        // Add panels to the frame
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        // Set frame properties
        pack(); // Adjusts frame size based on components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
        // Add document listeners to text fields
        firstnameField.getDocument().addDocumentListener(new FieldDocumentListener());
        lastnameField.getDocument().addDocumentListener(new FieldDocumentListener());
        ageField.getDocument().addDocumentListener(new FieldDocumentListener());
        phoneField.getDocument().addDocumentListener(new FieldDocumentListener());
        illnessField.getDocument().addDocumentListener(new FieldDocumentListener());
        addressField.getDocument().addDocumentListener(new FieldDocumentListener());
        patientNoteField.getDocument().addDocumentListener(new FieldDocumentListener());
        weightField.getDocument().addDocumentListener(new FieldDocumentListener());
        heightField.getDocument().addDocumentListener(new FieldDocumentListener());
    }
    // Method to clear input fields
    private void clearFields() {
        firstnameField.setText("");
        lastnameField.setText("");
        ageField.setText("");
        genderComboBox.setSelectedIndex(0);
        phoneField.setText("");
        illnessField.setText("");
        addressField.setText("");
        bloodTypeComboBox.setSelectedIndex(0);
        patientNoteField.setText("");
        weightField.setText("");
        heightField.setText("");
        addButton.setEnabled(false); // Disable the button after clearing fields
    }
    // Method to generate patient number
    private int generatePatientNumber() {
        return nextPatientNumber++;
    }
    // Method to open search window
    private void openSearchWindow() {
        JFrame searchFrame = new JFrame("Search Patients");
        searchFrame.setSize(500, 500);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null); // Center the frame
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        // Add search panel to the frame
        searchFrame.getContentPane().add(searchPanel);
        // Make the frame visible
        searchFrame.setVisible(true);
    }
    // Method to create the search panel
    private JPanel createSearchPanel() {
        // Create labels and text fields
        JLabel patientNumberLabel = new JLabel("Patient Number:");
        JLabel firstnameLabel = new JLabel("First Name:");
        JLabel lastnameLabel = new JLabel("Last Name:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel illnessLabel = new JLabel("Illness:");
        JLabel addressLabel = new JLabel("Address:");
        JTextField searchPatientNumberField = new JTextField(20);
        JTextField searchFirstNameField = new JTextField(20);
        JTextField searchLastNameField = new JTextField(20);
        JTextField searchPhoneField = new JTextField(20);
        JTextField searchIllnessField = new JTextField(20);
        JTextField searchAddressField = new JTextField(20);
        // Add document filters to restrict input
        ((AbstractDocument) searchPatientNumberField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        ((AbstractDocument) searchFirstNameField.getDocument()).setDocumentFilter(new LetterDocumentFilter());
        ((AbstractDocument) searchLastNameField.getDocument()).setDocumentFilter(new LetterDocumentFilter());
        ((AbstractDocument) searchPhoneField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        // Create button
        JButton searchButton = new JButton("Search");
        // Create text area to display search results
        JTextArea resultTextArea = new JTextArea(10, 40);
        resultTextArea.setEditable(false);
        // Add action listener for search button
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve search criteria
                int patientNumber = -1;
                try {
                    patientNumber = Integer.parseInt(searchPatientNumberField.getText());
                } catch (NumberFormatException ex) {
                    // Handle invalid input
                }
                String firstname = searchFirstNameField.getText();
                String lastname = searchLastNameField.getText();
                String phone = searchPhoneField.getText();
                String illness = searchIllnessField.getText();
                String address = searchAddressField.getText();
                // Perform search
                ArrayList<Patient> searchResults = new ArrayList<>();
                for (Patient patient : patients) {
                    boolean match = true;
                    if (patientNumber != -1 && patient.getPatientNumber() != patientNumber) {
                        match = false;
                    }
                    if (!firstname.isEmpty() && !patient.getFirstName().equalsIgnoreCase(firstname)) {
                        match = false;
                    }
                    if (!lastname.isEmpty() && !patient.getLastName().equalsIgnoreCase(lastname)) {
                        match = false;
                    }
                    if (!phone.isEmpty() && !patient.getPhone().equals(phone)) {
                        match = false;
                    }
                    if (!illness.isEmpty() && !patient.getIllness().equalsIgnoreCase(illness)) {
                        match = false;
                    }
                    if (!address.isEmpty() && !patient.getAddress().equalsIgnoreCase(address)) {
                        match = false;
                    }
                    if (match) {
                        searchResults.add(patient);
                    }
                }
                // Display search results
                resultTextArea.setText("");
                if (searchResults.isEmpty()) {
                    resultTextArea.append("No patient found with these records.");
                } else {
                    for (Patient patient : searchResults) {
                        resultTextArea.append("Patient Number: " + patient.getPatientNumber() + "\n");
                        resultTextArea.append("First Name: " + patient.getFirstName() + "\n");
                        resultTextArea.append("Last Name: " + patient.getLastName() + "\n");
                        resultTextArea.append("Age: " + patient.getAge() + "\n");
                        resultTextArea.append("Gender: " + patient.getGender() + "\n");
                        resultTextArea.append("Phone: " + patient.getPhone() + "\n");
                        resultTextArea.append("Illness: " + patient.getIllness() + "\n");
                        resultTextArea.append("Address: " + patient.getAddress() + "\n");
                        resultTextArea.append("Patient Note: " + patient.getPatientNote() + "\n");
                        resultTextArea.append("Blood Type: " + patient.getBloodType() + "\n");
                        resultTextArea.append("Weight: " + patient.getWeight() + " kg\n");
                        resultTextArea.append("Height: " + patient.getHeight() + " cm\n\n");
                    }
                }
            }
        });
        // Create a panel to hold the search components
        JPanel searchPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(patientNumberLabel);
        searchPanel.add(searchPatientNumberField);
        searchPanel.add(firstnameLabel);
        searchPanel.add(searchFirstNameField);
        searchPanel.add(lastnameLabel);
        searchPanel.add(searchLastNameField);
        searchPanel.add(phoneLabel);
        searchPanel.add(searchPhoneField);
        searchPanel.add(illnessLabel);
        searchPanel.add(searchIllnessField);
        searchPanel.add(addressLabel);
        searchPanel.add(searchAddressField);
        // Add an empty label for alignment
        searchPanel.add(new JLabel());
        searchPanel.add(searchButton);
        // Create a panel to hold the result text area
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(new JScrollPane(resultTextArea));
        // Create a panel to hold both search components
        JPanel searchComponentsPanel = new JPanel(new BorderLayout());
        searchComponentsPanel.add(searchPanel, BorderLayout.NORTH);
        searchComponentsPanel.add(resultPanel, BorderLayout.CENTER);
        return searchComponentsPanel;
    }
    // Method to open reservations window
    private void openReservationsWindow() {
        JFrame reservationsFrame = new JFrame("Reservations");
        reservationsFrame.setSize(1000, 400);
        reservationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reservationsFrame.setLocationRelativeTo(null); // Center the frame
        // Create reservations panel
        JPanel reservationsPanel = createReservationsPanel();
        // Add reservations panel to the frame
        reservationsFrame.getContentPane().add(reservationsPanel);
        // Make the frame visible
        reservationsFrame.setVisible(true);
    }
    // Method to create the reservations panel
    private JPanel createReservationsPanel() {
        // Create labels and text fields
        JLabel patientNumberLabel = new JLabel("Patient Number:");
        JLabel doctorLabel = new JLabel("Doctor:");
        JLabel dateLabel = new JLabel("Date:");
        JLabel timeLabel = new JLabel("Time:");
        JLabel reservationNoteLabel = new JLabel("Reservation Note:");
        JLabel emergencylevelLabel = new JLabel("Emergency Level:");
        JTextField patientNumberField = new JTextField(20);
        JComboBox<String> doctorComboBox = new JComboBox<>(getDoctors()); //getEmergencyLevels
        JComboBox<String> emergencylevelComboBox = new JComboBox<>(getEmergencyLevels());
     // Get the current date
        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        int currentMonth = now.get(Calendar.MONTH) + 1; // Month is 0-based, so add 1
        int currentYear = now.get(Calendar.YEAR);
        // Create comboboxes for date
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        JComboBox<String> dayComboBox = new JComboBox<>(days);
        dayComboBox.setSelectedItem(String.valueOf(currentDay));
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1);
        }
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedItem(String.valueOf(currentMonth));
        int currentYear1 = now.get(Calendar.YEAR);
        Integer[] years = new Integer[81]; // Assuming the range from 2020 to 2100
        for (int i = 0; i < 81; i++) {
            years[i] = currentYear1 + i;
        }
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(currentYear1);
     // Get the current time
        Calendar now1 = Calendar.getInstance();
        int currentHour = now1.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int currentMinute = now1.get(Calendar.MINUTE);
        // Calculate the nearest 15-minute increment
        int nearestMinute = (currentMinute / 15) * 15;
        if (currentMinute % 15 > 7) {
            nearestMinute += 15; // Round up if closer to the next 15-minute mark
        }
        // Create comboboxes for time
        String[] hours = new String[24]; // Modified to include 24
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        hourComboBox.setSelectedItem(String.format("%02d", currentHour));
        String[] minutes = new String[]{"00", "15", "30", "45"}; // Modified to include 00, 15, 30, 45
        JComboBox<String> minuteComboBox = new JComboBox<>(minutes);
        minuteComboBox.setSelectedItem(String.format("%02d", nearestMinute));
        // Create text fields for notes
        JTextField reservationNoteField = new JTextField(20);
        // Create button
       
        JButton reserveButton = new JButton("Reserve");
        reserveButton.setPreferredSize(new Dimension(30, 20)); // Set preferred size
         getContentPane().add(reserveButton, BorderLayout.CENTER); 
    
        // Create a panel to hold the reservation components
        JPanel reservationsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        reservationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reservationsPanel.add(patientNumberLabel);
        reservationsPanel.add(patientNumberField);
        reservationsPanel.add(doctorLabel);
        reservationsPanel.add(doctorComboBox);
        reservationsPanel.add(dateLabel);
        // Create panel to hold date comboboxes
        JPanel datePanel = new JPanel(new GridLayout(1, 6)); // Adjust grid layout according to your preference
        datePanel.add(new JLabel("Day:"));
        datePanel.add(dayComboBox);
        datePanel.add(new JLabel ("     Month:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("     Year:"));
        datePanel.add(yearComboBox);
        reservationsPanel.add(datePanel);
        reservationsPanel.add(timeLabel);
        // Create panel to hold time comboboxes
        JPanel timePanel = new JPanel(new FlowLayout());
        timePanel.add(new JLabel("Hour:"));
        timePanel.add(hourComboBox);
        timePanel.add(new JLabel("Minute:"));
        timePanel.add(minuteComboBox);
        reservationsPanel.add(timePanel);
        
        // Add components for notes
        reservationsPanel.add(reservationNoteLabel);
        reservationsPanel.add(reservationNoteField);
        reservationsPanel.add(emergencylevelLabel);
        reservationsPanel.add(emergencylevelComboBox);
        
         
       // Create an empty label for alignment
        reservationsPanel.add(new JLabel());
        reservationsPanel.add(reserveButton);
        // Create a panel to display reservations
        JPanel displayPanel = new JPanel(new BorderLayout());
        JTextArea reservationsTextArea = new JTextArea(10, 40);
        reservationsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reservationsTextArea);
        displayPanel.add(scrollPane, BorderLayout.CENTER);
        // Create a tabbed pane to hold both components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Make Reservation", reservationsPanel);
        tabbedPane.addTab("View Reservations", displayPanel);
     // Add action listener for reserve button
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve reservation details
                int patientNumber;
                try {
                    patientNumber = Integer.parseInt(patientNumberField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid patient number!");
                    return;
                }
                // Check if patient number is valid
                if (!isPatientRegistered(patientNumber)) {
                    JOptionPane.showMessageDialog(null, "Patient with this ID does not exist!");
                    return;
                }
                String doctor = (String) doctorComboBox.getSelectedItem();
                String emergencylevel = (String) emergencylevelComboBox.getSelectedItem();
                // Retrieve date from comboboxes
                int day = Integer.parseInt((String) dayComboBox.getSelectedItem());
                int month = Integer.parseInt((String) monthComboBox.getSelectedItem());
                int year = yearComboBox.getItemAt(yearComboBox.getSelectedIndex());
                String date = String.format("%02d/%02d/%04d", day, month, year);
                // Retrieve time from comboboxes
                int hour = Integer.parseInt((String) hourComboBox.getSelectedItem());
                int minute = Integer.parseInt((String) minuteComboBox.getSelectedItem());
                String time = String.format("%02d:%02d", hour, minute);

                String reservationNote = reservationNoteField.getText(); // Retrieve reservation note
                // Add reservation
                reservations.add(new Reservation(patientNumber, doctor, date, time, reservationNote, emergencylevel));
                JOptionPane.showMessageDialog(null, "Reservation added successfully!");
                
                // Clear input fields
                patientNumberField.setText("");
                reservationNoteField.setText("");

                // Reset date combo boxes to the current date
                Calendar now = Calendar.getInstance();
                dayComboBox.setSelectedItem(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
                monthComboBox.setSelectedItem(String.valueOf(now.get(Calendar.MONTH) + 1)); // Calendar.MONTH is zero-based
                yearComboBox.setSelectedItem(now.get(Calendar.YEAR));

                // Reset time combo boxes to the current time
                int currentHour = now.get(Calendar.HOUR_OF_DAY);
                int currentMinute = now.get(Calendar.MINUTE);
                int nearestMinute = (currentMinute / 15) * 15;
                if (currentMinute % 15 > 7) {
                    nearestMinute += 15;
                }
                hourComboBox.setSelectedItem(String.format("%02d", currentHour));
                minuteComboBox.setSelectedItem(String.format("%02d", nearestMinute));

                // Update reservations text area
                updateReservationsTextArea(reservationsTextArea);
            }
        });

        // Update reservations text area
        updateReservationsTextArea(reservationsTextArea);
        // Create a panel to hold both components
        JPanel reservationsComponentsPanel = new JPanel(new BorderLayout());
        reservationsComponentsPanel.add(tabbedPane, BorderLayout.NORTH);
        return reservationsComponentsPanel;
    }
    // Method to check if a patient is registered
    private boolean isPatientRegistered(int patientNumber) {
        for (Patient patient : patients) {
            if (patient.getPatientNumber() == patientNumber) {
                return true;
            }
        }
        return false;
    }
 // Method to update reservations text area using a greedy algorithm
    private void updateReservationsTextArea(JTextArea reservationsTextArea) {
        // Clear the text area
        reservationsTextArea.setText("");

        // Create a copy of the reservations list
        LinkedList<Reservation> sortedReservations = new LinkedList<>(reservations);

        // Initialize a list to store sorted reservations
        List<Reservation> sortedList = new ArrayList<>();

        // Iterate through the list of reservations
        while (!sortedReservations.isEmpty()) {
            // Find the reservation with the highest emergency level
            Reservation highestEmergencyReservation = null;
            int highestEmergencyLevel = Integer.MIN_VALUE;
            for (Reservation reservation : sortedReservations) {
                int emergencyLevel = Integer.parseInt(reservation.getEmergencylevel().substring("Level ".length()));
                if (emergencyLevel > highestEmergencyLevel) {
                    highestEmergencyLevel = emergencyLevel;
                    highestEmergencyReservation = reservation;
                }
            }

            // Add the reservation with the highest emergency level to the sorted list
            sortedList.add(highestEmergencyReservation);

            // Remove the added reservation from the copy list
            sortedReservations.remove(highestEmergencyReservation);
        }

        // Append sorted reservations to the text area
        for (Reservation reservation : sortedList) {
            // Find patient name corresponding to the patient number
            String patientName = findPatientName(reservation.getPatientNumber());
            reservationsTextArea.append("Patient Number: " + reservation.getPatientNumber() + "\n");
            reservationsTextArea.append("Patient Name: " + patientName + "\n");
            reservationsTextArea.append("Doctor: " + reservation.getDoctor() + "\n");
            reservationsTextArea.append("Date: " + reservation.getDate() + "\n");
            reservationsTextArea.append("Time: " + reservation.getTime() + "\n");
            reservationsTextArea.append("Reservation Note: " + reservation.getReservationNote() + "\n");
            reservationsTextArea.append("Emergency Level: " + reservation.getEmergencylevel() + "\n\n");
            reservationsTextArea.append("---------------------------------------------");

        }
    }
            // Modified to include reservation note
       
   
    // Method to find patient name by patient number
    private String findPatientName(int patientNumber) {
        for (Patient patient : patients) {
            if (patient.getPatientNumber() == patientNumber) {
                return patient.getFirstName()+(" ") + patient.getLastName();
            }
        }
        return "Unknown"; // Return "Unknown" if patient not found (this should not occur if the patient is registered)
    }
    // Method to get doctors' names
    private String[] getDoctors() {
        return new String[]{"Dr. Salih", "Dr. Chafai", "Dr. Ozturk", "Dr. Who", "Dr. Strange", "Dr. Smith", "Dr. Davis", "Dr. Garcia", "Dr. Rodriguez", "Dr. Martinez"};
    }
        private String[] getEmergencyLevels() {
        	return new String[]{"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
        	
    }
    // Patient class
    private class Patient {
        private int patientNumber;
        private String firstname;
        private String lastname;
        private String age;
        private String gender;
        private String phone;
        private String illness;
        private String address;
        private String bloodType;
        private String patientNote;
        private String weight;
        private String height;
        public Patient(int patientNumber, String firstname, String lastname, String age, String gender,  String phone, String illness, String address, String bloodType, String patientNote, String weight, String height, String weight2, String height2, String height3) {
            this.patientNumber = patientNumber;
            this.firstname = firstname;
            this.lastname = lastname;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.illness = illness;
            this.address = address;
            this.bloodType = bloodType;
            this.patientNote = patientNote;
            this.weight = weight;
            this.height = height;
        }
		// Getters
        public int getPatientNumber() {
            return patientNumber;
        }
        public String getFirstName() {
          
			return firstname;
        }
        public String getLastName() {
            
			return lastname;
        }
		public String getAge() {
			return age;
		}
        public String getGender() {
			return gender;
		}
		
        public String getPhone() {
            return phone;
        }
        public String getIllness() {
            return illness;
        }
        public String getAddress() {
            return address;
        }
        public String getBloodType() {
            return bloodType;
        }
        public String getPatientNote() {
            return patientNote;
        }
        public String getWeight() {
            return weight;
        }
        public String getHeight() {
            return height;
        }
    }
    // Reservation class
    private class Reservation {
        private int patientNumber;
        private String doctor;
        private String date;
        private String time;
        private String reservationNote;
        private String emergencylevel;
        // Added reservation note
        public Reservation(int patientNumber, String doctor, String date, String time, String reservationNote, String emergencylevel) {
            this.patientNumber = patientNumber;
            this.doctor = doctor;
            this.date = date;
            this.time = time;
            this.reservationNote = reservationNote;
            this.emergencylevel = emergencylevel;// Added reservation note
        }
        // Getters
        public int getPatientNumber() {
            return patientNumber;
        }
        public String getDoctor() {
            return doctor;
        }
        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
        public String getReservationNote() {
            return reservationNote;
        }
        public String getEmergencylevel() {
            return emergencylevel;
            // Added reservation note
            
        }
    }
    // Document filter to allow only letters
    private class LetterDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (containsOnlyLetters(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (containsOnlyLetters(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        private boolean containsOnlyLetters(String text) {
            return text.chars().allMatch(Character::isLetter);
        }
    }
    // Document filter to allow only numbers
    private class NumberDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (containsOnlyNumbers(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (containsOnlyNumbers(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        private boolean containsOnlyNumbers(String text) {
            return text.chars().allMatch(Character::isDigit);
        }
    }
 // Document listener for input fields
    private class FieldDocumentListener implements DocumentListener {
      
        @Override
        public void removeUpdate(DocumentEvent e) {
            checkInput();
        }
     
        @Override
        public void insertUpdate(DocumentEvent e) {
            checkInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            checkInput();
        }

        private void checkInput() {
            // Enable the add button if all required fields are filled
            addButton.setEnabled(!firstnameField.getText().isEmpty() && 
                                 !lastnameField.getText().isEmpty() && 
                                 !phoneField.getText().isEmpty() &&
                                 !illnessField.getText().isEmpty() && 
                                 !addressField.getText().isEmpty() && 
                                 !ageField.getText().isEmpty() && 
                                 !weightField.getText().isEmpty() && 
                                 !heightField.getText().isEmpty() &&
                                 !patientNoteField.getText().isEmpty());
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HospitalManagementSystem();
            }
        });
    }
	public JComboBox<String> getEmergencylevelComboBox() {
		return emergencylevelComboBox;
	}
	public void setEmergencylevelComboBox(JComboBox<String> emergencylevelComboBox) {
		this.emergencylevelComboBox = emergencylevelComboBox;
	}
}
