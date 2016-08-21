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

import static ru.p3tr0vich.calibration.ActivityDialog.EXTRA_ARGS;

public class FragmentActivityDialogScaleChange extends FragmentBase
        implements ActivityDialog.ActivityDialogFragment {

    private ScaleRecord mRecord;

    private EditText mEditName;
    private EditText mEditId;

    @Override
    public String getTitle() {
        return getContext().getString(mRecord.getId() != 0 ?
                R.string.dialog_title_update :
                R.string.dialog_title_add);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        bundle = bundle.getBundle(EXTRA_ARGS);

        if (bundle != null && bundle.containsKey(ScaleRecord.NAME)) {
            mRecord = new ScaleRecord(bundle);
        } else {
            mRecord = new ScaleRecord();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_dialog_scale_change, container, false);

        mEditName = (EditText) view.findViewById(R.id.edit_name);
        mEditId = (EditText) view.findViewById(R.id.edit_id);

        if (savedInstanceState == null) {
            mEditName.setText(mRecord.getName());
            UtilsFormat.longToEditText(mEditId, mRecord.getId(), false);
        }

        return view;
    }

    /**
     * Проверяет введённые данные перед сохранением.
     *
     * @return Истина, если данные валидны, ложь, если нет (например, существуте запись с введённым айди).
     */
    private boolean checkRecord() {
        if (TextUtils.isEmpty(mEditId.getText())) {
            Utils.toast(R.string.message_error_need_id);
            return false;
        }

        long id = UtilsFormat.editTextToLong(mEditId);
        if (id <= 0) {
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
        mRecord.setId(UtilsFormat.editTextToLong(mEditId));
        mRecord.setName(mEditName.getText().toString());

        if (mRecord.getId() != 0) {
            if (!ContentProviderHelper.updateRecord(getContext(), mRecord)) {
                Utils.toast(R.string.message_error_update_record);

                return false;
            }
        } else {
            if (!ContentProviderHelper.insertRecord(getContext(), mRecord)) {
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