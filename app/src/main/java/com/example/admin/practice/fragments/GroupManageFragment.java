package com.example.admin.practice.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.practice.DB.CIDBHandler;
import com.example.admin.practice.ListViewItem;
import com.example.admin.practice.R;
import com.example.admin.practice.activites.MainActivity;
import com.example.admin.practice.adapters.ContactsAdapter;
import com.example.admin.practice.adapters.GroupAdapter;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class GroupManageFragment extends DialogFragment {
    public static final String TAG = "GroupManageFragment";
    private static final String Key = "GroupManage";
    private ImageButton cB;
    private Button nG;
    private ListView mListView;
    private GroupAdapter mAdapter;
    private String selectedgroup;

    private CIDBHandler Cdh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_fragment, container, false);
        Cdh = new CIDBHandler(getActivity());
        Cdh.open();
        mListView = (ListView) rootView.findViewById(R.id.grouplist);

        mAdapter = new GroupAdapter();
        setmAdapter();

        cB = (ImageButton) rootView.findViewById(R.id.cancel);
        cB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        nG = (Button) rootView.findViewById(R.id.bt_newgroup);
        nG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addBuilder = new AlertDialog.Builder(getActivity(),R.style.MyAlterDialogStyle);
                final EditText et = new EditText(getActivity());
                et.setHint("그룹 이름을 입력해 주세요.");
                addBuilder.setTitle("그룹 추가하기");
                addBuilder.setView(et);
                addBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newgroup = et.getText().toString();
                        MainActivity.groups.add(newgroup);
                        MainActivity.setStringArrayPref(getActivity(),"groups",MainActivity.groups);

                        mAdapter.clear();
                        setmAdapter();
                        mAdapter.notifyDataSetChanged();

                    }
                });
                addBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                addBuilder.show();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedgroup = MainActivity.groups.get(position);
                final List<String> listitems = new ArrayList<>();
                listitems.add("멤버 추가");
                listitems.add("그룹 제거");
                final CharSequence[] items = listitems.toArray(new String[listitems.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(selectedgroup);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = items[which].toString();
                        switch(selectedText){
                            case "멤버 추가":
                                AddPeople Adialog = AddPeople.newInstance("add",selectedgroup);
                                Adialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme );
                                Adialog.show(getActivity().getFragmentManager(),"AddPeopleFragment");

                                mAdapter.clear();
                                setmAdapter();
                                mAdapter.notifyDataSetChanged();
                                break;
                            case "그룹 제거":
                                AlertDialog.Builder removeBuilder = new AlertDialog.Builder(getActivity(),R.style.MyAlterDialogStyle);
                                removeBuilder.setTitle("그룹 제거하기");
                                removeBuilder.setMessage("OK 버튼을 누르면 그룹이 제거됩니다.");
                                removeBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Cdh.deleteGroup(selectedgroup);
                                        MainActivity.groups.remove(position);
                                        MainActivity.setStringArrayPref(getActivity(),"groups",MainActivity.groups);
                                        mAdapter.clear();
                                        setmAdapter();
                                        mAdapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(),"Deleted Data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                removeBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(),"Canceled remove", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                removeBuilder.show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedgroup = MainActivity.groups.get(position);
                AddPeople Adialog = AddPeople.newInstance("minus",selectedgroup);
                Adialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme );
                Adialog.show(getActivity().getFragmentManager(),"AddPeopleFragment");
                mAdapter.clear();
                setmAdapter();
                mAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    public void setmAdapter(){

        mListView.setAdapter(mAdapter);
        for(int i = 0 ; i < MainActivity.groups.size();i++){
            mAdapter.addItem(1,Cdh.sizeofData(MainActivity.groups.get(i),0),MainActivity.groups.get(i),null);
        }
    }

}
