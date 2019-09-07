package com.example.cameraapp.miscellanous;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cameraapp.R;
import com.example.cameraapp.models.Report;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ListViewHolder> {

    private ArrayList<Report> reportList;

    public ReportAdapter(ArrayList<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_list_item, viewGroup, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int i) {
        Report report = reportList.get(i);
        holder.textStatus.setText(report.getReportStatus());
        holder.textReportId.setText(report.getReport_id());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textReportId;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.text_status);
            textReportId = itemView.findViewById(R.id.text_report_id);
        }
    }
}
