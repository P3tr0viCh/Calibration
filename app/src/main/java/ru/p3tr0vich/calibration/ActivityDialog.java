package ru.p3tr0vich.calibration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.factories.FragmentFactory;

public class ActivityDialog extends AppCompatActivity implements
        FragmentInterface.OnFragmentChangeListener {

    private static final String EXTRA_DIALOG = "EXTRA_DIALOG";
    private static final String EXTRA_ARGS = "EXTRA_ARGS";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DIALOG_SCALE_CHANGE})
    public @interface Dialog {
    }

    public static final int DIALOG_SCALE_CHANGE = 0;

    public interface ActivityDialogFragment {
        boolean onSaveClicked();
    }

    public static void start(@NonNull Activity parent, @Dialog int dialog, @Nullable Bundle args) {
        parent.startActivity(new Intent(parent, ActivityDialog.class)
                .putExtra(EXTRA_DIALOG, dialog)
                .putExtra(EXTRA_ARGS, args));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Toolbar toolbarDialog = (Toolbar) findViewById(R.id.toolbar_dialog);
        setSupportActionBar(toolbarDialog);

        assert toolbarDialog != null;

        toolbarDialog.setNavigationIcon(R.drawable.ic_close);

        toolbarDialog.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        if (savedInstanceState == null) {
            Fragment fragment;

            switch (getIntent().getIntExtra(EXTRA_DIALOG, -1)) {
                case DIALOG_SCALE_CHANGE:
                    fragment = FragmentFactory.getFragmentNewInstance(FragmentFactory.IDS.SCALE_CHANGE,
                            getIntent().getBundleExtra(EXTRA_ARGS));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown dialog type");
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment, null)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .commit();
        }
    }

    @Override
    public void onFragmentChange(FragmentInterface fragment) {
        setTitle(fragment.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityDialogFragment fragment =
                (ActivityDialogFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (fragment.onSaveClicked()) {
            setResult(Activity.RESULT_OK);
            finish();
        }

        return true;
    }
}