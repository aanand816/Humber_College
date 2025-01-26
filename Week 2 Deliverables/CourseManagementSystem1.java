import java.io.*;
import java.util.*;

// Custom Exception for Invalid CSV Format
class InvalidCSVFormatException extends Exception {
    public InvalidCSVFormatException(String message) {
        super(message);
    }
}

// Superclass Person
class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}

// Subclass Student
class Student extends Person {
    private String studentId;

    public Student(int studentId, String name, String email) {
        super(name, email);
        this.studentId = String.valueOf(studentId);
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public void displayDetails() {
        System.out.println("Student ID: " + studentId);
        super.displayDetails();
    }
}

// Subclass Instructor
class Instructor extends Person {
    private String instructorId;
    private String department;

    public Instructor(String instructorId, String name, String email, String department) {
        super(name, email);
        this.instructorId = instructorId;
        this.department = department;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public void displayDetails() {
        System.out.println("Instructor ID: " + instructorId);
        System.out.println("Department: " + department);
        super.displayDetails();
    }
}

// Class Course
class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private Instructor instructor;
    private List<Student> students;

    public Course(String courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.students = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void assignInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void displayCourseDetails() {
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Credits: " + credits);
        System.out.println("\nInstructor Details:");
        if (instructor != null) {
            instructor.displayDetails();
        } else {
            System.out.println("No instructor assigned.");
        }

        System.out.println("\nEnrolled Students:");
        if (students.isEmpty()) {
            System.out.println("No students enrolled yet.");
        } else {
            for (Student student : students) {
                student.displayDetails();
                System.out.println();
            }
        }
    }

    // Save all data back to CSV
    public void saveToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.csv"))) {
            bw.write("StudentID,Name,Email\n");
            for (Student student : students) {
                bw.write(student.getStudentId() + "," + student.getName() + "," + student.getEmail() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving students to CSV: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("instructor.csv"))) {
            bw.write("InstructorID,Name,Email,Department\n");
            if (instructor != null) {
                bw.write(instructor.getInstructorId() + "," + instructor.getName() + "," + instructor.getEmail() + "," + instructor.getDepartment() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving instructor to CSV: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("course.csv"))) {
            bw.write("CourseID,CourseName,Credits\n");
            bw.write(courseId + "," + courseName + "," + credits + "\n");
        } catch (IOException e) {
            System.err.println("Error saving course to CSV: " + e.getMessage());
        }
    }
}

// Main Class
public class CourseManagementSystem1 {

    private static Course readCourse(String filePath) throws IOException, InvalidCSVFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line = br.readLine();
            if (line != null) {
                return parseCourse(line);
            }
        }
        return null;
    }

    private static Instructor readInstructor(String filePath) throws IOException, InvalidCSVFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line = br.readLine();
            if (line != null) {
                return parseInstructor(line);
            }
        }
        return null;
    }

    private static Student parseStudent(String line) throws InvalidCSVFormatException {
        String[] data = line.split(",");
        if (data.length != 3) {
            throw new InvalidCSVFormatException("Invalid student format. Each line must have 3 fields.");
        }
        return new Student(Integer.parseInt(data[0]), data[1], data[2]);
    }

    private static Course parseCourse(String line) throws InvalidCSVFormatException {
        String[] data = line.split(",");
        if (data.length != 3) {
            throw new InvalidCSVFormatException("Invalid course format. Each line must have 3 fields.");
        }
        return new Course(data[0], data[1], Integer.parseInt(data[2]));
    }

    private static Instructor parseInstructor(String line) throws InvalidCSVFormatException {
        String[] data = line.split(",");
        if (data.length != 4) {
            throw new InvalidCSVFormatException("Invalid instructor format. Each line must have 4 fields.");
        }
        return new Instructor(data[0], data[1], data[2], data[3]);
    }


    //Main Class
    public static void main(String[] args) {
        Course course = null;
        Instructor instructor = null;

        try {
            course = readCourse("course.csv");

            instructor = readInstructor("instructor.csv");

            // Read and assign students
            try (BufferedReader br = new BufferedReader(new FileReader("students.csv"))) {
                br.readLine(); // Skip header
                String line;
                while ((line = br.readLine()) != null) {
                    Student student = parseStudent(line);
                    course.addStudent(student);
                }
            } catch (IOException | InvalidCSVFormatException e) {
                System.err.println("Error reading students: " + e.getMessage());
            }

            // Assign Instructor and Display Details
            if (course != null && instructor != null) {
                course.assignInstructor(instructor);
                course.displayCourseDetails();
                course.saveToCSV(); // Save updated data
            }
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

