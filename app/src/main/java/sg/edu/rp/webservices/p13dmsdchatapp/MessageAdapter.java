package sg.edu.rp.webservices.p13dmsdchatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> message;
    private Context context;
    private ImageView ivStar;

    TextView tvName, tvDateTime, tvMsgText;


    public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
        // Store the food that is passed to this adapter
        message = objects;
        // Store Context object as we would need to use it later
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_listview_row, parent, false);

        tvDateTime = (TextView) rowView.findViewById(R.id.tvDateTime);
        tvMsgText = (TextView) rowView.findViewById(R.id.tvMsgText);
        tvName = (TextView) rowView.findViewById(R.id.tvName);

        Message currentMessage = message.get(position);

        tvMsgText.setText(currentMessage.getMessageText());
        tvName.setText(currentMessage.getUserName());
        tvDateTime.setText(currentMessage.getMessageTime());

        return rowView;
    }
}
