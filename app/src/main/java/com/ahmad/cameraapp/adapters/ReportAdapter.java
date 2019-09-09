package com.ahmad.cameraapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.models.Report;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ListViewHolder> {

    private ArrayList<Report> reportList;
    private Context context;
    private OnItemClickListener listener;
    private static View list;

    public ReportAdapter(Context context, ArrayList<Report> reportList, OnItemClickListener listener) {
        this.reportList = reportList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Report report);
    }

    public void refreshAdapter(ArrayList<Report> reportList){
        this.reportList.clear();
        this.reportList = reportList;
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_list_item, viewGroup, false);
        list = itemView;
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int i) {
        Report report = reportList.get(i);
        if (report.getReportStatus().equals("Pending")){
            holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
        }
        else if (report.getReportStatus().equals("Approved")){
            holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        else if (report.getReportStatus().equals("Denied")){
            holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        holder.textStatus.setText(report.getReportStatus());
        holder.textReportId.setText(report.getReport_id());
        holder.textUploadedDate.setText(report.getUploadedBy());
        holder.bind(reportList.get(i), listener);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textReportId, textUploadedDate;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.text_status);
            textReportId = itemView.findViewById(R.id.text_report_id);
            textUploadedDate = itemView.findViewById(R.id.text_uploaded_date);
        }

        public void bind(final Report report, final OnItemClickListener listener) {
            list.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(report);
                }
            });
        }
    }
}
