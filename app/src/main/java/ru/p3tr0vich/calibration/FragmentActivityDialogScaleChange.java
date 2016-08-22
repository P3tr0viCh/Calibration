package ru.p3tr0vich.calibration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.Utils;
import ru.p3tr0vich.calibration.utils.UtilsFormat;

public class FragmentActivityDialogScaleChange extends FragmentBase
        implements ActivityDialog.ActivityDialogFragment {

    private static final String KEY_RECORD_ID = "KEY_RECORD_ID";

    /**
     * Номера позиций в спинере класса точности в статике.
     * См. strings/spinner_class_static
     */
    private interface SpinnerClassStatic {
        int NONE = 0;
        int CLASS_HIGH = 1;
        int CLASS_MEDIUM = 2;
        int CLASS_LOW = 3;
    }

    /**
     * Номера позиций в спинере класса точности в динамике.
     * См. strings/spinner_class_dynamic
     */
    private interface SpinnerClassDynamic {
        int NONE = 0;
        int CLASS_0_dot_5 = 1;
        int CLASS_1 = 2;
        int CLASS_2 = 3;
    }

    private long mRecordId;

    private EditText mEditName;
    private EditText mEditId;
    private EditText mEditType;
    private Spinner mSpinnerClassStatic;
    private Spinner mSpinnerClassDynamic;

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
        mEditType = (EditText) view.findViewById(R.id.edit_type);
        mSpinnerClassStatic = (Spinner) view.findViewById(R.id.spinner_class_static);
        mSpinnerClassDynamic = (Spinner) view.findViewById(R.id.spinner_class_dynamic);

        ArrayAdapter<CharSequence> adapterClassStatic = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_class_static, R.layout.spinner_item);
        adapterClassStatic.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinnerClassStatic.setAdapter(adapterClassStatic);

        ArrayAdapter<CharSequence> adapterClassDynamic = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_class_dynamic, R.layout.spinner_item);
        adapterClassDynamic.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinnerClassDynamic.setAdapter(adapterClassDynamic);

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
            mEditType.setText(record.getType());

            setSelectedClassStatic(record.getClassStatic());
            setSelectedClassDynamic(record.getClassDynamic());
        } else {
            mRecordId = savedInstanceState.getLong(KEY_RECORD_ID);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_RECORD_ID, mRecordId);
    }

    private void setSelectedClassStatic(@ScaleRecord.ClassStatic int classStatic) {
        int position;
        switch (classStatic) {
            case ScaleRecord.CLASS_STATIC_HIGH:
                position = SpinnerClassStatic.CLASS_HIGH;
                break;
            case ScaleRecord.CLASS_STATIC_LOW:
                position = SpinnerClassStatic.CLASS_LOW;
                break;
            case ScaleRecord.CLASS_STATIC_MEDIUM:
                position = SpinnerClassStatic.CLASS_MEDIUM;
                break;
            case ScaleRecord.CLASS_STATIC_NONE:
            default:
                position = SpinnerClassStatic.NONE;
        }
        mSpinnerClassStatic.setSelection(position);
    }

    @ScaleRecord.ClassStatic
    private int getSelectedClassStatic() {
        switch (mSpinnerClassStatic.getSelectedItemPosition()) {
            case SpinnerClassStatic.CLASS_HIGH:
                return ScaleRecord.CLASS_STATIC_HIGH;
            case SpinnerClassStatic.CLASS_LOW:
                return ScaleRecord.CLASS_STATIC_LOW;
            case SpinnerClassStatic.CLASS_MEDIUM:
                return ScaleRecord.CLASS_STATIC_MEDIUM;
            case SpinnerClassStatic.NONE:
            default:
                return ScaleRecord.CLASS_STATIC_NONE;
        }
    }

    private void setSelectedClassDynamic(@ScaleRecord.ClassDynamic int classDynamic) {
        int position;
        switch (classDynamic) {
            case ScaleRecord.CLASS_DYNAMIC_0_DOT_5:
                position = SpinnerClassDynamic.CLASS_0_dot_5;
                break;
            case ScaleRecord.CLASS_DYNAMIC_1:
                position = SpinnerClassDynamic.CLASS_1;
                break;
            case ScaleRecord.CLASS_DYNAMIC_2:
                position = SpinnerClassDynamic.CLASS_2;
                break;
            case ScaleRecord.CLASS_DYNAMIC_NONE:
            default:
                position = SpinnerClassDynamic.NONE;
        }
        mSpinnerClassDynamic.setSelection(position);
    }

    @ScaleRecord.ClassDynamic
    private int getSelectedClassDynamic() {
        switch (mSpinnerClassDynamic.getSelectedItemPosition()) {
            case SpinnerClassDynamic.CLASS_0_dot_5:
                return ScaleRecord.CLASS_DYNAMIC_0_DOT_5;
            case SpinnerClassDynamic.CLASS_2:
                return ScaleRecord.CLASS_DYNAMIC_2;
            case SpinnerClassDynamic.CLASS_1:
                return ScaleRecord.CLASS_DYNAMIC_1;
            case SpinnerClassDynamic.NONE:
            default:
                return ScaleRecord.CLASS_DYNAMIC_NONE;
        }
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
                mEditType.getText().toString(),
                getSelectedClassStatic(),
                getSelectedClassDynamic());

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