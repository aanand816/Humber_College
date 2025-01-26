import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
        if (instructor != null) {
            System.out.println("\nInstructor Details:");
            instructor.displayDetails();
        } else {
            System.out.println("\nNo instructor assigned yet.");
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
}

// Main Class
public class CourseManagementSystem {
    public static void main(String[] args) {
        // Create Course
        Course course = new Course("101", "Object-Oriented Programming", 4);

        // Create Instructor
        Instructor instructor = new Instructor("501", "Dr. Emily White", "emily.white@university.com", "Computer Science");

        // Create Students
        String filePath = "students.csv"; // Replace with your file path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true; // To skip the header row

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Parse each line
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                String email = fields[2];

                // Create student
                Student student = new Student(id,name,email);
                // Add Students
                course.addStudent(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assign Instructor
        course.assignInstructor(instructor);
        // Display Course Details
        course.displayCourseDetails();

    }
}
