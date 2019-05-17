package com.ramyhelow.qrreader.Adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramyhelow.qrreader.Model.Code;
import com.ramyhelow.qrreader.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Code> data;
    private Activity activity;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public RecyclerAdapter(ArrayList<Code> data, Activity activity) {
        this.data = data;
        this.activity = activity;

        myClipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.code_list_item, viewGroup, false);

        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.code_text.setText(String.valueOf(data.get(i).getText()));
        myViewHolder.code_time.setText(data.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView code_text;
        public TextView code_time;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            code_text = itemView.findViewById(R.id.code_text);

            code_text.setAutoLinkMask(Linkify.WEB_URLS);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClip = ClipData.newPlainText("text", code_text.getText().toString());
                    myClipboard.setPrimaryClip(myClip);

                    Toast.makeText(activity, "' "+code_text.getText().toString()+" ' Copied",
                            Toast.LENGTH_SHORT).show();
                }
            });



            code_time = itemView.findViewById(R.id.code_time);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String escapedQuery = null;
//                    Uri uri = null;
//                    try {
//                        escapedQuery = URLEncoder.encode(code_text.getText().toString(), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    if(escapedQuery.contains("www")){
//                        uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
//                    }else{
//                        uri = Uri.parse("http://www.barcodelookup.com/" + escapedQuery);
//                    }
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    itemView.getContext().startActivity(intent);
//                }
//            });

        }
    }
}