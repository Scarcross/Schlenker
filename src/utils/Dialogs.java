package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogs {

	public static AlertDialog getConnectSuccesDialog(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Verbindung");
		alertDialog.setMessage("Verbindungstest erfolgreich!");
		alertDialog.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog.setCancelable(false);
		alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();
	}

	public static AlertDialog getWIFIConnectFailedDialog(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("WIFI");
		alertDialog.setMessage("Verbindungstest nicht erfolgreich! \nPrüfen Sie die WIFI Einstellungen!");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setCancelable(false);
		alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();
	}
	public static AlertDialog getFTPConnectFailedDialog(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("FTP");
		alertDialog.setMessage("Verbindungstest nicht erfolgreich! \nPrüfen Sie die FTP Einstellungen!");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setCancelable(false);
		alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();
	}

	public static AlertDialog getNoFileToDisplay(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("History");
		alertDialog.setMessage("Keine Bilddaten in der History gefunden!");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setCancelable(false);
		alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();		
	}
	public static AlertDialog getOutOfMemory(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Speicher");
		alertDialog.setMessage("Aktion kann nich ausgeführt werden Speicher voll! Starten Sie die Applikation neu!");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setCancelable(false);
		alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alertDialog.create();		
	}
}
