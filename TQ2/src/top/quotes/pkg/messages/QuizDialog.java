package top.quotes.pkg.messages;

import top.quotes.pkg.R;
import top.quotes.pkg.ScoresScreen;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.fragments.QuizFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

public class QuizDialog {

	public static AlertDialog showQuizDialog(final Context context,
			String userAnswer, String correctAnswer, final int scoreCount,
			final boolean isEnd) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (userAnswer.equals(correctAnswer)) {
			builder.setTitle(R.string.correct_answer_title);
			builder.setMessage(correctAnswer + " "
					+ context.getString(R.string.correct_answer_message));
		} else {
			builder.setTitle(R.string.incorrect_answer_title);
			builder.setMessage(userAnswer + " "
					+ context.getString(R.string.incorrect_answer_message)
					+ " " + correctAnswer);
		}
		builder.setIcon(R.drawable.ic_launcher);
		if (isEnd) {
			builder.setNeutralButton(R.string.score_title,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							showScoresScreen(context, scoreCount);
						}
					});
			builder.setCancelable(false);
		} else {
			builder.setPositiveButton(R.string.continue_quiz,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(R.string.finish_quiz,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							showScoresScreen(context, scoreCount);
							QuizFragment.setScoreAndAttempts(0,
									ConstantsFacade.QUIZ_MAX_ATTEMPTS_COUNT);
						}
					});
			builder.setCancelable(true);
		}
		return builder.show();
	}

	private static void showScoresScreen(Context context, int scoreCount) {
		Intent intent = new Intent(context, ScoresScreen.class);
		intent.putExtra(ConstantsFacade.CURRENT_SCORE, scoreCount);
		context.startActivity(intent);
	}
}
