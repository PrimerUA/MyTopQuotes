package top.quotes.pkg.util.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FilesController {

    private final static String TQ_DIRECTORY_NAME = "Top Quotes/";
    private final static String TQ_DIRECTORY_PATH = "/mnt/sdcard/" + TQ_DIRECTORY_NAME;

    public static void saveScore(Context context, String fileName, String content) {
	if (SDCardOK(context)) {

	    File sdcard = new File(Environment.getExternalStorageDirectory(), TQ_DIRECTORY_NAME);
	    if (!sdcard.exists()) {
		sdcard.mkdirs();
	    }
	    File scoresFile = Environment.getExternalStorageDirectory();
	    try {
		BufferedWriter fwd = new BufferedWriter(new FileWriter(new File(TQ_DIRECTORY_PATH + fileName), true));

		if (scoresFile.canWrite()) {
		    fwd.append(content + "\n");
		}
		fwd.close();
	    } catch (IOException e) {
		Log.e("One", "Could not write file " + e.getMessage());
	    }
	}
    }

    public static ArrayList<String> loadScoresFromFile(Context context) {
	ArrayList<String> result = null;
	if (SDCardOK(context)) {
	    File dir = new File(TQ_DIRECTORY_PATH);

	    // Get the text file
	    File file = new File(dir, "scores.txt");

	    // Read text from file
	    result = new ArrayList<String>();
	    try {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String buff = in.readLine();
		while (buff != null) {
		    result.add(buff);
		    buff = in.readLine();
		}
		in.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return result;
    }

    public static void saveRating(Context context, String fileName, String content) {
	if (SDCardOK(context)) {

	    File sdcard = new File(Environment.getExternalStorageDirectory(), TQ_DIRECTORY_NAME);
	    if (!sdcard.exists()) {
		sdcard.mkdirs();
	    }
	    File scoresFile = Environment.getExternalStorageDirectory();
	    try {
		BufferedWriter fwd = new BufferedWriter(new FileWriter(new File(TQ_DIRECTORY_PATH + fileName), true));

		if (scoresFile.canWrite()) {
		    fwd.append(content + "***");
		}
		fwd.close();
	    } catch (IOException e) {
		Log.e("One", "Could not write file " + e.getMessage());
	    }
	}
    }

    public static ArrayList<String> loadRatingsFromFile(Context context, String fileName) {
	ArrayList<String> result = new ArrayList<String>();
	ArrayList<String> ratedQuotesList = new ArrayList<String>();
	if (SDCardOK(context)) {
	    File dir = new File(TQ_DIRECTORY_PATH);
	    File file = new File(dir, fileName);
	    try {

		BufferedReader in = new BufferedReader(new FileReader(file));
		String buff = in.readLine();
		String line;
		while ((line = in.readLine()) != null) {
		    if (FilesController.process(line, "***")) {
			result.add(buff);
			buff = new String();
		    } else
			buff = buff + line + "\n";
		}

		in.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	for (int i = 0; i < result.size(); i++) {
	    ratedQuotesList.add(result.get(i));
	}
	return ratedQuotesList;
    }

    public static void clearFile(String fileName) {
	File dir = new File(TQ_DIRECTORY_PATH);
	File file = new File(dir, fileName);
	if (file.exists()) {
	    file.delete();
	}
    }

    private static Boolean SDCardOK(Context mContext) {
	String auxSDCardStatus = Environment.getExternalStorageState();

	if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED))
	    return true;
	else if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
	    Toast.makeText(mContext, "Warning, the SDCard it's only in read mode.\nthis does not result in malfunction" + " of the read aplication", Toast.LENGTH_LONG).show();
	    return true;
	} else if (auxSDCardStatus.equals(Environment.MEDIA_NOFS)) {
	    Toast.makeText(mContext, "Error, the SDCard can't be used, it has not a corret format or " + "is not formated.", Toast.LENGTH_LONG).show();
	    return false;
	} else if (auxSDCardStatus.equals(Environment.MEDIA_REMOVED)) {
	    Toast.makeText(mContext, "Error, the SDCard is not found, to use the reader you need " + "insert a SDCard on the device.", Toast.LENGTH_LONG).show();
	    return false;
	} else if (auxSDCardStatus.equals(Environment.MEDIA_SHARED)) {
	    Toast.makeText(mContext, "Error, the SDCard is not mounted beacuse is using " + "connected by USB. Plug out and try again.", Toast.LENGTH_LONG).show();
	    return false;
	} else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE)) {
	    Toast.makeText(mContext, "Error, the SDCard cant be mounted.\nThe may be happend when the SDCard is corrupted " + "or crashed.", Toast.LENGTH_LONG).show();
	    return false;
	} else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED)) {
	    Toast.makeText(mContext, "Error, the SDCArd is on the device but is not mounted." + "Mount it before use the app.", Toast.LENGTH_LONG).show();
	    return false;
	}

	return true;
    }

    public static boolean process(String str, String pattern) {
	return pattern.equals(str) ? true : false;
    }
}
