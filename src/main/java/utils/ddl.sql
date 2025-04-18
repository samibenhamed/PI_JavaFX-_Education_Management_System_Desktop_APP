-- Classes Tabel
CREATE TABLE IF NOT EXISTS  classes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE ,
    type ENUM('bachelor s degree' ,'master degree','engineering degree' ) NOT NULL ,
    level VARCHAR(255) NOT NULL ,
    field VARCHAR(255) NOT NULL ,
    speciality varchar(255) NOT NULL ,
    academic_year VARCHAR(20) ,
    description VARCHAR(255)
) ;
-- USERS TABLE
CREATE TABLE IF NOT EXISTS  users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL ,
    lastName VARCHAR(255) NOT NULL ,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL  ,
    birthdate DATE  NOT NULL ,
    gender ENUM('MALE', 'FEMALE') NOT NULL ,
    address VARCHAR(255) NOT NULL ,
    phone VARCHAR(255) NOT NULL ,
    national_id VARCHAR(255) NOT NULL UNIQUE,
    type ENUM('ADMIN','STUDENT', 'TEACHER') NOT NULL ,
    -- Teacher-specific
    teacher_specialty VARCHAR(255) ,
    -- Student-specific
    class_id INT   ,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET null
) ;
-- SUBJECTS TABLE
CREATE TABLE  IF NOT EXISTS subjects(
    id INT PRIMARY KEY AUTO_INCREMENT ,
    name VARCHAR(255) NOT NULL UNIQUE ,
    nb_hours INT NOT NULL ,
    coefficient DECIMAL(3,2)  NOT NULL ,
    description VARCHAR(255)
) ;
-- teaching_assignments table
-- This table defines the teaching assignments:
-- it links a teacher to a specific subject and class,
-- along with the time period during which the subject is taught.
CREATE TABLE IF NOT EXISTS  teaching_assignments (
    class_id INT NOT NULL ,
    subject_id INT NOT NULL ,
    teacher_id INT NOT NULL ,
    startDate  DATE NOT NULL ,
    endDate    DATE   ,
    status ENUM('ACTIVE', 'INACTIVE' , 'DONE') NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (class_id, subject_id),
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE  CASCADE ,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE  CASCADE ,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE  CASCADE

) ;

--  evaluations table
-- Stores information about evaluations:
-- the type (test, DS, exam), the subject, class, teacher, date, and percentage.
-- This table links evaluations to subjects and classes, and optionally to teachers.

CREATE TABLE IF NOT EXISTS evaluations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('TEST', 'DS', 'EXAM') NOT NULL,
    subject_id INT NOT NULL,
    class_id INT NOT NULL,
    teacher_id INT ,
    evaluation_date DATE NOT NULL,
    percentage DECIMAL(5,2) NOT NULL, -- e.g., 20 for 20%
    FOREIGN KEY (subject_id) REFERENCES subjects(id)  ON DELETE cascade ,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE  cascade  ,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE  set null
);
-- grades table
-- Links students to their evaluation results.
-- Stores the grade each student received in a given evaluation.
CREATE TABLE IF NOT EXISTS grades (
    student_id INT NOT NULL,
    evaluation_id INT NOT NULL,
    grade DECIMAL(5,2) NOT NULL,
    PRIMARY KEY (student_id, evaluation_id),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE ,
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE
);


-- sessions table
-- Represents a scheduled teaching session for a class & subject.
-- Helps track individual class dates for attendance.
CREATE TABLE IF NOT EXISTS sessions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);
-- attendances table
-- Stores whether a student was present or absent for a specific session.
CREATE TABLE IF NOT EXISTS attendances (
    session_id INT NOT NULL,
    student_id INT NOT NULL,
    status ENUM('PRESENT', 'ABSENT') NOT NULL DEFAULT 'ABSENT',
    PRIMARY KEY (session_id, student_id),
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
