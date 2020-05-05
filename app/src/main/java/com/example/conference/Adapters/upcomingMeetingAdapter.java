package com.example.conference.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conference.Model.upcomingMeetings;
import com.example.conference.R;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class upcomingMeetingAdapter extends RecyclerView.Adapter<upcomingMeetingAdapter.MyHolder> {

    Context context;
    List<upcomingMeetings> upcomingMeetingList;

    public upcomingMeetingAdapter(Context context, List<upcomingMeetings> upcomingMeetingList) {
        this.context = context;
        this.upcomingMeetingList = upcomingMeetingList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scheduled_mettings_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String meetingId = upcomingMeetingList.get(position).getId();
        final String name = upcomingMeetingList.get(position).getHost();
        final String start = upcomingMeetingList.get(position).getStartdatetime();
        final  String end = upcomingMeetingList.get(position).getEndtime();

        holder.host.setText(name);
        holder.id.setText(meetingId);
        holder.startdate.setText(start);
        holder.enddate.setText(end);

    }

    @Override
    public int getItemCount() {
        return upcomingMeetingList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView host, id, startdate, enddate;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            host = itemView.findViewById(R.id.HostName);
            id = itemView.findViewById(R.id.meetingId);
            startdate = itemView.findViewById(R.id.startTime);
            enddate = itemView.findViewById(R.id.endTime);
        }
    }
}
