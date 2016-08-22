package ru.p3tr0vich.calibration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.Utils;
import ru.p3tr0vich.calibration.utils.UtilsFormat;

public class FragmentActivityDialogScaleChange extends FragmentBase
        implements ActivityDialog.ActivityDialogFragment {

    private static final String KEY_RECORD_ID = "KEY_RECORD_ID";

    private long mRecordId;

    private EditText mEditName;
    private EditText mEditId;

    @Override
    public String getTitle() {
        return getContext().getString(mRecordId != 0 ?
                R.string.dialog_title_update :
                R.string.dialog_title_add);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_dialog_scale_change, container, false);

        mEditName = (EditText) view.findViewById(R.id.edit_name);
        mEditId = (EditText) view.findViewById(R.id.edit_id);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();

            ScaleRecord record;

            if (bundle != null && bundle.containsKey(ScaleRecord.NAME)) {
                record = new ScaleRecord(bundle);
            } else {
                record = new ScaleRecord();
            }

            mRecordId = record.getId();

            mEditName.setText(record.getName());
            UtilsFormat.longToEditText(mEditId, record.getId(), false);
        } else {
            mRecordId = savedInstanceState.getLong(KEY_RECORD_ID);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_RECORD_ID, mRecordId);
    }

    /**
     * Проверяет введённые данные перед сохранением.
     *
     * @return Истина, если данные валидны, ложь, если нет (например, существует запись с введённым айди).
     */
    private boolean checkRecord() {
        if (TextUtils.isEmpty(mEditId.getText())) {
            mEditId.requestFocus();
            Utils.toast(R.string.message_error_need_id);
            return false;
        }

        long id = UtilsFormat.editTextToLong(mEditId);
        if (id <= 0) {
            mEditId.requestFocus();
            Utils.toast(R.string.message_error_bad_id);
            return false;
        }

        return true;
    }

    /**
     * Выполняет добавление новой или изменение уже существующей записи.
     *
     * @return Истина, если сохранение успешно.
     */
    private boolean saveRecord() {
        Utils.hideKeyboard(getActivity());

        ScaleRecord record = new ScaleRecord(
                UtilsFormat.editTextToLong(mEditId),
                mEditName.getText().toString(),
                "", 0, 0);

        if (mRecordId != 0) {
            if (!ContentProviderHelper.updateRecord(getContext(), record)) {
                Utils.toast(R.string.message_error_update_record);

                return false;
            }
        } else {
            if (!ContentProviderHelper.insertRecord(getContext(), record)) {
                Utils.toast(R.string.message_error_insert_record);

                return false;
            }
        }

        return true;
    }

    @Override
    public boolean onSaveClicked() {
        return checkRecord() && saveRecord();
    }
}