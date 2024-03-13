import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

class Book {
    //attribues of Book class object
    Long book_ID;
    String title;
    String author;
    String genre;
    boolean availability_status;
}//T

class User {
    //attributes of User class object:
    Long user_ID;
    String name;
    String contact_information;
    String[] borrowed_books = new String[10];//it gives no of books that can be borrowed by user
}

class Library {
    Book[] storedbook = new Book[100];

    User[] storedUsers = new User[100];
    public int bid;
    public int uid;

    //Library constructor with no arguments takes user id and book id from library file and set them in the variables
    Library() {
        File file = new File("lib.txt");

        try {
            Scanner inp = new Scanner(file);
            while (inp.hasNextLine()) {
                String line = inp.nextLine();
                if (line.contains("uid:")) {
                    uid = parseInt(inp.nextLine());
                } else if (line.contains("bid:")) {
                    bid = parseInt(inp.nextLine());
                }
            }
        } catch (Exception ex) {
            System.out.println("Sorry there is problem due to exception" + ex);
        }
    }


    void addingNewBooks() {
        try {
            FileWriter writerb = new FileWriter("book.txt", true);//opening file in append mode

            storedbook[bid] = new Book(); // creating instance of object
            storedbook[bid].book_ID = (long) bid;
            // asking user for details of book
            String author = JOptionPane.showInputDialog("Tell the book author:");
            storedbook[bid].author = author;

            String book = JOptionPane.showInputDialog("Tell the book name:");
            storedbook[bid].title = book;

            String genre = JOptionPane.showInputDialog("Tell the book genre:");
            storedbook[bid].genre = genre;

            String avail = JOptionPane.showInputDialog("Tell the book availability status (true/false):");
            storedbook[bid].availability_status = Boolean.parseBoolean(avail);
//writing in file the details of book
            writerb.write("Id: " + bid + " author: " + storedbook[bid].author + " genre: " + storedbook[bid].genre + " title: " + storedbook[bid].title + " availabilitystatus :" + storedbook[bid].availability_status + "\n");//writing in the file
            writerb.close();
            JOptionPane.showMessageDialog(null, "Book added successfully");
            bid++;
            //updating bookid and userid in the file
            FileWriter writer1 = new FileWriter("lib.txt");
            writer1.write("\nuid:\n" + uid + "\nbid:\n" + bid);
            writer1.close();
        } catch (IOException e) {
            System.out.println("Sorry cannot write in the file due to the exception" + e);
        }
    }

    void addingNewUsers() {
        try {
            FileWriter writer = new FileWriter("users.txt", true);//openingfile in append mode

            storedUsers[uid] = new User();//creating instance of object
            storedUsers[uid].user_ID = (long) uid;
//taking user's details
            String name = JOptionPane.showInputDialog("Tell the user name:");
            storedUsers[uid].name = name;

            String contact = (JOptionPane.showInputDialog("Tell the user contact's number:"));
            storedUsers[uid].contact_information = contact;
//writing details in the file
            writer.write(" name: " + storedUsers[uid].name + " ID: " + uid + " ContactInformation: " +
                    storedUsers[uid].contact_information + " books borrowed:" + "\n");

            writer.close();
            JOptionPane.showMessageDialog(null, "User added successfully!");
            //updating userid and bookid in the file
            uid++;
            FileWriter writer1 = new FileWriter("lib.txt");
            writer1.write("\nuid:\n" + uid + "\nbid:\n" + bid);
            writer1.close();
        } catch (Exception e) {
            System.out.println("Sorry cannot write in the file due to the exception" + e);
        }
    }


    void displayingBooks() {
        System.out.println("Books in the library are");
        File file = new File("book.txt");
        JTextArea textArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        try {
            Scanner sc = new Scanner(file);
            //diplaying books in the file with help of loop
            while (sc.hasNextLine()) {
                textArea.append((sc.nextLine() + "\n"));
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("sorry cannot read form file due to the exception " + e);
        }
        JOptionPane.showMessageDialog(null, scrollPane, "Books in the library", JOptionPane.PLAIN_MESSAGE);
    }


    void BooksToBeReturned() {
        System.out.println("Books in the library to be returned are:");
        File file = new File("book.txt");
        JTextArea textArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        try {
            Scanner sc = new Scanner(file);
            String s;
            while (sc.hasNextLine()) {
                s = sc.nextLine();
                if (s.contains("availabilitystatus :false")) { //checking availability status of book and returning them in GUI
                    textArea.append(s + "\n");
                }
            }
            JOptionPane.showMessageDialog(null, scrollPane, "Books in the library to be returned", JOptionPane.PLAIN_MESSAGE);
            //displayed books in the library
            sc.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Sorry, cannot read from file due to the exception: " + e,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    void searchingForBooks() {
        Scanner sc = new Scanner(System.in);
        String name = JOptionPane.showInputDialog("Tell the book name's or author's name to find the book:");//asks user for name
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        File file = new File("book.txt");
        boolean checker = false;
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String Name = scan.nextLine();

                if ((Name.contains(" author: " + name)) || (Name.contains(" title: " + name))) {
                    //if book is found it is displayed
                    textArea.append(Name + "\n");
                    checker = true;//checker is set to true
                }

            }
            JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
            if (!checker) {
                JOptionPane.showMessageDialog(null, "Sorry book of that author or title name do not exist in the library");
                //if book not found it tells that book could not be foound
            }
            scan.close();
        } catch (Exception e) {
            System.out.println("sorry cannot read form file due to the exception " + e);
        }


    }

    void borrowBook() {
        String bookNameToBorrow = JOptionPane.showInputDialog("Enter the name of the book to borrow:");//asks user the book name
        boolean bookchecker = false;
        try {
            // Read book information from the file
            File bookFile = new File("book.txt");
            Scanner bookScanner = new Scanner(bookFile);
            String line;
            String filestr = "";
            while (bookScanner.hasNextLine()) {//writes the whole file in a string
                line = bookScanner.nextLine();
                filestr = filestr + line + "\n";
            }
            if (filestr.contains(bookNameToBorrow)) {//checks if it contains book name
                if (filestr.contains(bookNameToBorrow + " availabilitystatus :true")) {//checks status of the book
                    bookchecker = true;

                    filestr = filestr.replace(bookNameToBorrow + " availabilitystatus :true",
                            bookNameToBorrow + " availabilitystatus :false");//replace status of book


                    FileWriter writer = new FileWriter("book.txt");
                    writer.write(filestr);//writes the whole file
                    writer.close();

                    String IDOfUserToBorrow = JOptionPane.showInputDialog("Enter the User ID who wants to borrow:");//asks user the ID
                    File UserFile = new File("users.txt");
                    String ID;
                    Scanner scanner = new Scanner(UserFile);

                    while (scanner.hasNextLine()) {
                        ID = scanner.nextLine();
                        if (ID.contains("ID: " + IDOfUserToBorrow) && bookchecker) {//if book is found and ID of user found it adds book int user line
                            if (ID.contains("books borrowed:")) {
                                FileWriter writer2 = new FileWriter(UserFile, true);
                                writer2.write(" " + bookNameToBorrow);
                                writer2.close();
                                JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry Person of that ID does not exists");
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Sorry availability status of the book is already not available(False)");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Book of that name do not exists Sorry");
            }
        } catch (Exception e) {
            System.out.println("Sorry we cannot write in the file due to exception" + e);
        }
    }

    void returnBook() {
        String userid = JOptionPane.showInputDialog("Tell the ID of user who wants to return the book:");//asks the ID of user to return book
        String bookname = JOptionPane.showInputDialog("Tell the book name which is to be returned:");//asks the book name to be returned
        File bookfile = new File("book.txt");

        try {
            String Line;
            Scanner sc = new Scanner(bookfile);
            String filestr = "";
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                filestr = filestr + line + "\n";//writes whole file in a string
            }
            if (filestr.contains(bookname)) {//checks bookname in the file
                if (filestr.contains(bookname + " availabilitystatus :false")) {//checks status of the book
                    filestr = filestr.replace(bookname + " availabilitystatus :false", bookname + " availabilitystatus :true");//replaces false with thrue
                    FileWriter bwrite = new FileWriter(bookfile);
                    bwrite.write(filestr);//write the whole file
                    bwrite.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry but book is already in the library");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Sorry book of that name do not exists");
            }
            try {
                File Userfile = new File("users.txt");
                Scanner scanneruser = new Scanner(Userfile);
                String filestr2 = "";
                String line2;
                while (scanneruser.hasNextLine()) {
                    line2 = scanneruser.nextLine();
                    filestr2 = filestr2 + line2 + "\n";
                }
                if (filestr2.contains("ID: " + userid)) {
                    if (filestr2.contains(" " + bookname)) {
                        filestr2 = filestr2.replace(" " + bookname, "");

                        FileWriter Uwrite = new FileWriter(Userfile);
                        Uwrite.write(filestr2);
                        Uwrite.close();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Sorry retry due to exception" + e);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Sorry cannot return the book due to exception " + e + "Please do it again ");
        }
    }

    void searchingForBooksUser() {

        String ID = JOptionPane.showInputDialog("Tell the ID of user to search the book:");//asks user for book name
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        File file = new File("users.txt");
        try {
            Scanner scann = new Scanner(file);
            while (scann.hasNextLine()) {
                String line = scann.nextLine();
                if (line.contains("ID: " + ID)) {//checks ID of user
                    textArea.append(line);//returns User
                }

            }
            scann.close();
            JOptionPane.showMessageDialog(null, scrollPane, "User Information", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Sorry cannot read from file due to Exception " + e + " Please do it again");
        }

    }
}// IP To <b>Run</b> code, press <shortcut actionId="Run"/> or


// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        //creating Scanner  object
        Scanner sc = new Scanner(System.in);

        Library library = new Library();
//creating frame for GUI
        JFrame Frame = new JFrame("lib Management program");
        Frame.setSize(700, 500);
        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setLayout(null);


//creating button and setting the properties
        JButton button1 = new JButton("Add book");
        Frame.add(button1);
        button1.setBounds(5, 50, 690, 30);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                library.addingNewBooks();
            }
        });
//creating button and setting the properties
        JButton button2 = new JButton("Add User");
        Frame.add(button2);
        button2.setBounds(5, 90, 690, 30);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                library.addingNewUsers();
            }
        });
        //creating button and setting the properties
        JButton button3 = new JButton("display books");
        Frame.add(button3);
        button3.setBounds(5, 130, 690, 30);
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                library.displayingBooks();
            }
        });
        //creating button and setting the properties
        JButton button4 = new JButton("Search book by Name");
        Frame.add(button4);
        button4.setBounds(5, 170, 690, 30);
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                library.searchingForBooks();
            }
        });
        //creating button and setting the properties
        JButton button5 = new JButton("Search book by UserID");
        Frame.add(button5);
        button5.setBounds(5, 210, 690, 30);
        button5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                library.searchingForBooksUser();
            }
        });
        //creating button and setting the properties
        JButton button6 = new JButton("Books to be returned");
        Frame.add(button6);
        button6.setBounds(5, 250, 690, 30);
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                library.BooksToBeReturned();
            }
        });
        //creating button and setting the properties
        JButton button9 = new JButton("Return book");
        Frame.add(button9);
        button9.setBounds(5, 290, 690, 30);
        button9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                library.returnBook();
            }
        });
        //creating button and setting the properties
        JButton button8 = new JButton("Borrow Book");
        Frame.add(button8);
        button8.setBounds(5, 330, 690, 30);
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                library.borrowBook();
            }
        });
        //creating button and setting the properties
        JButton button7 = new JButton("Exit");
        Frame.add(button7);
        button7.setBounds(5, 370, 690, 30);
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });
    }
}

