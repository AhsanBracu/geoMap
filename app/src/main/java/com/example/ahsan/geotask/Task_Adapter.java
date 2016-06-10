package com.example.ahsan.geotask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.ahsan.geotask.model.Task;

/**
 * Created by ahsan on 11/19/15.
 */
public class Task_Adapter extends ArrayAdapter<Task>{

   ArrayList<Task> arraytask=new ArrayList<Task>();
   ViewHolder viewHolder;

    public Task_Adapter(Context context,ArrayList<Task>arraytask) {
        super(context,R.layout.task_details,arraytask);
        this.arraytask=arraytask;
    }

    @Override
    public Task getItem(int position) {
        return arraytask.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater Inflator = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=Inflator.inflate(R.layout.task_details,null,false);
            viewHolder=new ViewHolder();
            viewHolder.tilte=(TextView)convertView.findViewById(R.id.title);
            viewHolder.detail=(TextView)convertView.findViewById(R.id.details);
           // viewHolder.start_time=(TextView)convertView.findViewById(R.id.time_start);
          //  viewHolder.end_time=(TextView)convertView.findViewById(R.id.time_end);
            //viewHolder.map=(SupportMapFragment) get
            convertView.setTag(viewHolder);


        }else{

            viewHolder=(ViewHolder)convertView.getTag();
        }

        Task task=arraytask.get(position);

        viewHolder.tilte.setText(task.title);

        viewHolder.detail.setText(task.detail);
     //   viewHolder.start_time.setText(Double.toString(task.start_time));
    //    viewHolder.end_time.setText(Double.toString(task.finish_time));

        return convertView;
    }

    static class ViewHolder

    {
        TextView tilte,detail,start_time,end_time;


    }
}
