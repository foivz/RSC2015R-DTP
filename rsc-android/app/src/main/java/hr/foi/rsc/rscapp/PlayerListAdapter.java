package hr.foi.rsc.rscapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.rsc.model.Person;

/**
 * Created by tomo on 21.11.15..
 */
public class PlayerListAdapter extends ArrayAdapter<Person> {

    ArrayList<Person> persons;
    LayoutInflater inflater;

    public PlayerListAdapter(Context context, int resource, ArrayList<Person> persons) {
        super(context, resource);
        this.persons = persons;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Person getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView presence;
        public TextView personName;
        public TextView personSurname;
        public TextView personUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.item_player_list, null);
                holder = new ViewHolder();

                holder.presence = (ImageView) vi.findViewById(R.id.status);
                holder.personName = (TextView) vi.findViewById(R.id.personName);
                holder.personSurname = (TextView) vi.findViewById(R.id.personSurname);
                holder.personUsername = (TextView) vi.findViewById(R.id.personUsername);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if(persons.get(position).isReady()) {
                holder.presence.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_online));
            } else {
                holder.presence.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_busy));
            }
            holder.personName.setText(persons.get(position).getName() + " ");
            holder.personSurname.setText(persons.get(position).getSurname());
            holder.personUsername.setText(persons.get(position).getCredentials().getUsername());

        } catch (Exception e) {
        }
        return vi;
    }
}
