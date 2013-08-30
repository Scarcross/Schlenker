package de.leichten.schlenkerapp.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.InputType;
import android.util.AttributeSet;

public class EditTextIntegerPreference extends EditTextPreference {

	public EditTextIntegerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
	}

	public EditTextIntegerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
	}

	public EditTextIntegerPreference(Context context) {
		super(context);
		getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
	}

	@Override
	protected String getPersistedString(String defaultReturnValue) {
		return String.valueOf(getPersistedInt(-1));
	}

	@Override
	protected boolean persistString(String value) {
		return persistInt(Integer.valueOf(value));
	}
}
