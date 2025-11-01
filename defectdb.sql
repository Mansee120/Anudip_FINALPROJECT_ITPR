CREATE DATABASE defectdb;
USE defectdb;

CREATE TABLE defect (
    defect_id INT PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    status VARCHAR(20),
    priority VARCHAR(20),
    assigned_to VARCHAR(50)
);
