package com.example.cameraapp.miscellanous;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.cameraapp.config.SQLiteHelper;
import com.example.cameraapp.models.Attachment;
import com.example.cameraapp.models.Misconduct;
import com.example.cameraapp.models.Report;
import com.example.cameraapp.models.Student;
import com.example.cameraapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;

public class InsertSQLiteAsync extends AsyncTask<JSONArray, Void, Void> {

    private SQLiteHelper db;
    private Context context;
    private JSONArray users, students, reports, attachments, misconducts;

    public InsertSQLiteAsync(Context context) {
        this.context = context;
        db = new SQLiteHelper(this.context);
    }

    @Override
    protected Void doInBackground(JSONArray... jsonArrays) {

        users = jsonArrays[0];
        students = jsonArrays[1];
        reports = jsonArrays[2];
        attachments = jsonArrays[3];
        misconducts = jsonArrays[4];

        db.emptyTables();

        try {
            User user = new User();
            for (int i = 0; i < users.length(); i++){

                user.setUserID(users.getJSONObject(i).getString("user_id"));
                user.setStaffId(users.getJSONObject(i).getString("staff_id"));
                user.setName(users.getJSONObject(i).getString("name"));
                user.setPassword(users.getJSONObject(i).getString("password"));
                user.setContactNo(users.getJSONObject(i).getString("contact_no"));
                user.setEmail(users.getJSONObject(i).getString("email"));
                user.setUserType(users.getJSONObject(i).getString("user_type"));
                user.setCreatedDate(users.getJSONObject(i).getString("created_date"));
                user.setLastLogin(users.getJSONObject(i).getString("last_login"));
                user.setModifiedDate(users.getJSONObject(i).getString("modified_date"));
                user.setGrantedAccess(users.getJSONObject(i).getString("granted_access"));
                //insert into local db
                db.addUser(user);
            }

            Student student = new Student();
            for (int i = 0; i < students.length(); i++){
                student.setStudentId(students.getJSONObject(i).getString("student_id"));
                student.setMatricId(students.getJSONObject(i).getString("matric_id"));
                student.setName(students.getJSONObject(i).getString("name"));
                student.setIcOrPassport(students.getJSONObject(i).getString("ic_or_passport"));
                student.setProgramme(students.getJSONObject(i).getString("programme"));
                student.setContactNo(students.getJSONObject(i).getString("contact_no"));
                student.setEmail(students.getJSONObject(i).getString("email"));
                //insert into local db
                db.addStudent(student);
            }

            Report report = new Report();
            for (int i = 0; i < reports.length(); i++){
                report.setReport_id(reports.getJSONObject(i).getString("report_id"));
                report.setStudentId(reports.getJSONObject(i).getString("student_id"));
                report.setReporterUserId(reports.getJSONObject(i).getString("reporter_id"));
                report.setSuperiorUserId(reports.getJSONObject(i).getString("superior_id"));
                report.setCourseCode(reports.getJSONObject(i).getString("course_code"));
                report.setCourseName(reports.getJSONObject(i).getString("course_name"));
                report.setExamDate(reports.getJSONObject(i).getString("exam_date"));
                report.setExamTime(reports.getJSONObject(i).getString("exam_time"));
                report.setExamVenue(reports.getJSONObject(i).getString("exam_venue"));
                report.setMisconductTime(reports.getJSONObject(i).getString("misconduct_time"));
                report.setMisconductDescription(reports.getJSONObject(i).getString("misconduct_description"));
                report.setActionTaken(reports.getJSONObject(i).getString("action_taken"));
                report.setWitness1Name(reports.getJSONObject(i).getString("witness1_name"));
                report.setWitness1ContactNo(reports.getJSONObject(i).getString("witness1_contact_no"));
                report.setWitness1Email(reports.getJSONObject(i).getString("witness1_email"));
                report.setWitness2Name(reports.getJSONObject(i).getString("witness2_name"));
                report.setWitness2ContactNo(reports.getJSONObject(i).getString("witness2_contact_no"));
                report.setWitness2Email(reports.getJSONObject(i).getString("witness2_email"));
                report.setCaseStatus(reports.getJSONObject(i).getString("case_status"));
                report.setReportStatus(reports.getJSONObject(i).getString("report_status"));
                report.setUploadedBy(reports.getJSONObject(i).getString("uploaded_by"));
                report.setLastApprovalDate(reports.getJSONObject(i).getString("last_approval_date"));
                report.setIsValid(reports.getJSONObject(i).getString("is_valid"));
                //insert into local db
                db.addReport(report);
            }

            Attachment attachment = new Attachment();
            for (int i = 0; i < attachments.length(); i++){
                attachment.setAttachment_id(attachments.getJSONObject(i).getString("attachment_id"));
                attachment.setPath(attachments.getJSONObject(i).getString("path"));
                attachment.setReportId(attachments.getJSONObject(i).getString("report_id"));
                //insert into local db
                db.addAttachment(attachment);
            }

            Misconduct misconduct = new Misconduct();
            for (int i = 0; i < misconducts.length(); i++){
                misconduct.setMisconductId(misconducts.getJSONObject(i).getString("misconduct_id"));
                misconduct.setType(misconducts.getJSONObject(i).getString("type"));
                misconduct.setReportId(misconducts.getJSONObject(i).getString("report_id"));
                //insert into local db
                db.addMisconduct(misconduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context,"Data Loaded Successfully.", Toast.LENGTH_SHORT).show();
        System.out.println("Data Loaded Successfully.");
    }
}
