package com.androyen.ribbit;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

public class MessageAdapter extends ArrayAdapter<ParseObject> {
	
	//
	protected Context mContext;
	protected List<ParseObject> mMessages;
	
	//constructor
	public MessageAdapter(Context context, List<ParseObject> messages) {
		
		super(context, R.layout.messsage_item, messages);
		mContext = context;
		mMessages = messages;
	}
	
	//Adapter getView() method. Create view, inflate and attach to listview
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Recycle views   Using ViewHolder pattern
		ViewHolder holder;
		
		//recycle existing views
		
		if (convertView == null) {
			//Inflate convertView
			convertView = LayoutInflater.from(mContext).inflate(R.layout.messsage_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
		}
		else {
			//view already exist   Gets Tag for the view
			holder = (ViewHolder) convertView.getTag();
		}
		
		//Set data in view in ListView
		ParseObject message = mMessages.get(position);
		
		if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
			holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
			holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
		}
		else if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_VIDEO)) {
			holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
			holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
		}
		return convertView;
	}
	
	//Viewholder pattern
	private static class ViewHolder {
		ImageView iconImageView;
		TextView nameLabel;
	}

}
