package com.example.cameraapp.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cameraapp.models.Attachment;
import com.example.cameraapp.models.Misconduct;
import com.example.cameraapp.models.Report;
import com.example.cameraapp.models.Student;
import com.example.cameraapp.models.User;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE = "semms_iukl.db";
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_REPORT = "report";
    private static final String TABLE_ATTACHMENT = "attachment";
    private static final String TABLE_MISCONDUCT = "misconduct";
    private static final String TABLE_USER = "user";
    private Context context;
    private ContentValues contentValues;
    SQLiteDatabase db;

    public SQLiteHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateStudentTable = "CREATE TABLE " + TABLE_STUDENT + " ( "
                + "id INTEGER PRIMARY KEY, "
                + "student_id TEXT, "
                + "matric_id TEXT, "
                + "ic_or_passport TEXT, "
                + "name TEXT, "
                + "programme TEXT, "
                + "contact_no TEXT, "
                + "email TEXT)";

        String queryCreateReportTable = "CREATE TABLE " + TABLE_REPORT + " ( "
                + "id INTEGER PRIMARY KEY, "
                + "report_id TEXT, "
                + "student_id TEXT, "
                + "course_code TEXT, "
                + "course_name TEXT, "
                + "exam_venue TEXT, "
                + "exam_date TEXT, "
                + "exam_time TEXT, "
                + "misconduct_time TEXT, "
                + "misconduct_description TEXT, "
                + "action_taken TEXT, "
                + "reporter_id TEXT, "
                + "superior_id TEXT, "
                + "witness1_name TEXT, "
                + "witness1_contact_no TEXT, "
                + "witness1_email TEXT, "
                + "witness2_name TEXT, "
                + "witness2_contact_no TEXT, "
                + "witness2_email TEXT, "
                + "report_status TEXT, "
                + "case_status TEXT, "
                + "uploaded_by TEXT, "
                + "last_approval_date TEXT, "
                + "is_valid TEXT)";

        String queryCreateAttachmentTable = "CREATE TABLE " + TABLE_ATTACHMENT + " ( "
                + "id INTEGER PRIMARY KEY, "
                + "attachment_id TEXT, "
                + "path TEXT, "
                + "report_id TEXT)";

        String queryCreateMisconductTable = "CREATE TABLE " + TABLE_MISCONDUCT + " ( "
                + "id INTEGER PRIMARY KEY, "
                + "misconduct_id TEXT, "
                + "type TEXT, "
                + "report_id TEXT)";

        String queryCreateUserTable = "CREATE TABLE " + TABLE_USER + " ( "
                + "id INTEGER PRIMARY KEY, "
                + "user_id TEXT, "
                + "staff_id TEXT, "
                + "password TEXT, "
                + "name TEXT, "
                + "email TEXT, "
                + "contact_no TEXT, "
                + "user_type TEXT, "
                + "created_date TEXT, "
                + "modified_date TEXT, "
                + "last_login TEXT, "
                + "granted_access TEXT)";

        db.execSQL(queryCreateStudentTable);
        db.execSQL(queryCreateReportTable);
        db.execSQL(queryCreateAttachmentTable);
        db.execSQL(queryCreateMisconductTable);
        db.execSQL(queryCreateUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void emptyTables(){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUDENT);
        db.execSQL("DELETE FROM " + TABLE_REPORT);
        db.execSQL("DELETE FROM " + TABLE_ATTACHMENT);
        db.execSQL("DELETE FROM " + TABLE_MISCONDUCT);
        db.execSQL("DELETE FROM " + TABLE_USER);
    }

    public void addStudent(Student student){
        db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put("student_id", student.getStudentId());
        contentValues.put("matric_id", student.getMatricId());
        contentValues.put("ic_or_passport", student.getIcOrPassport());
        contentValues.put("name", student.getName());
        contentValues.put("programme", student.getProgramme());
        contentValues.put("contact_no", student.getContactNo());
        contentValues.put("email", student.getEmail());

        db.insert(TABLE_STUDENT, null, contentValues);
        db.close();
    }

    public void addReport(Report report){
        db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put("report_id", report.getReport_id());
        contentValues.put("student_id", report.getStudentId());
        contentValues.put("course_code", report.getCourseCode());
        contentValues.put("course_name", report.getCourseName());
        contentValues.put("exam_venue", report.getExamVenue());
        contentValues.put("exam_date", report.getExamDate());
        contentValues.put("exam_time", report.getExamTime());
        contentValues.put("misconduct_time", report.getMisconductTime());
        contentValues.put("misconduct_description", report.getMisconductDescription());
        contentValues.put("action_taken", report.getActionTaken());
        contentValues.put("reporter_id", report.getReporterUserId());
        contentValues.put("superior_id", report.getSuperiorUserId());
        contentValues.put("witness1_name", report.getWitness1Name());
        contentValues.put("witness1_contact_no", report.getWitness1ContactNo());
        contentValues.put("witness1_email", report.getWitness1Email());
        contentValues.put("witness2_name", report.getWitness2Name());
        contentValues.put("witness2_contact_no", report.getWitness2ContactNo());
        contentValues.put("witness2_email", report.getWitness2Email());
        contentValues.put("report_status", report.getReportStatus());
        contentValues.put("case_status", report.getCaseStatus());
        contentValues.put("uploaded_by", report.getUploadedBy());
        contentValues.put("last_approval_date", report.getLastApprovalDate());
        contentValues.put("is_valid", report.getIsValid());

        db.insert(TABLE_REPORT, null, contentValues);
        db.close();
    }

    public void addAttachment(Attachment attachment){
        db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put("attachment_id", attachment.getAttachment_id());
        contentValues.put("path", attachment.getPath());
        contentValues.put("report_id", attachment.getReportId());

        db.insert(TABLE_ATTACHMENT, null, contentValues);
        db.close();
    }

    public void addMisconduct(Misconduct misconduct){
        db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put("misconduct_id", misconduct.getMisconductId());
        contentValues.put("type", misconduct.getType());
        contentValues.put("report_id", misconduct.getReportId());

        db.insert(TABLE_MISCONDUCT, null, contentValues);
        db.close();
    }

    public void addUser(User user){
        db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put("user_id", user.getUserID());
        contentValues.put("staff_id", user.getStaffId());
        contentValues.put("password", user.getPassword());
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("contact_no", user.getContactNo());
        contentValues.put("user_type", user.getUserType());
        contentValues.put("created_date", user.getCreatedDate());
        contentValues.put("modified_date", user.getModifiedDate());
        contentValues.put("last_login", user.getLastLogin());
        contentValues.put("granted_access", user.getGrantedAccess());

        db.insert(TABLE_USER, null, contentValues);
        db.close();
    }

    public ArrayList<Report> getReport(){
        db = this.getReadableDatabase();
        ArrayList<Report> allReports = new ArrayList<>();
        Report report = new Report();
        String query = "SELECT * FROM " + TABLE_REPORT;
        Cursor result = db.rawQuery(query, null);
        if (result.moveToFirst()) {
            while (result.moveToNext()) {
                report.setReport_id(result.getString(1));
                System.out.println(result.getString(1));
                report.setStudentId(result.getString(2));
                report.setCourseCode(result.getString(3));
                report.setCourseName(result.getString(4));
                report.setExamVenue(result.getString(5));
                report.setExamDate(result.getString(6));
                report.setExamTime(result.getString(7));
                report.setMisconductTime(result.getString(8));
                report.setMisconductDescription(result.getString(9));
                report.setActionTaken(result.getString(10));
                report.setReporterUserId(result.getString(11));
                report.setSuperiorUserId(result.getString(12));
                report.setWitness1Name(result.getString(13));
                report.setWitness1ContactNo(result.getString(14));
                report.setWitness1Email(result.getString(15));
                report.setWitness2Name(result.getString(16));
                report.setWitness2ContactNo(result.getString(17));
                report.setWitness2Email(result.getString(18));
                report.setReportStatus(result.getString(19));
                report.setCaseStatus(result.getString(20));
                report.setUploadedBy(result.getString(21));
                report.setLastApprovalDate(result.getString(22));
                report.setIsValid(result.getString(23));
                allReports.add(report);
            }
        }
        db.close();
        return allReports;
    }

    public ArrayList<Student> getStudentDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Student> allStudents = new ArrayList<>();
        Student student = new Student();
        String query = "SELECT * FROM " + TABLE_STUDENT;
        Cursor result = db.rawQuery(query, null);
        if (result.moveToFirst()) {
            while (!result.isAfterLast()) {
                student.setStudentId(result.getString(1));
                student.setMatricId(result.getString(2));
                student.setIcOrPassport(result.getString(3));
                student.setName(result.getString(4));
                student.setProgramme(result.getString(5));
                student.setContactNo(result.getString(6));
                student.setEmail(result.getString(7));
                allStudents.add(student);
                result.moveToNext();
            }
        }
        db.close();

        return allStudents;
    }
}
